package com.lz.role;

import com.lz.entity.Ser;
import com.lz.entity.User;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by inst1 on 2017/6/21.
 */


public class Serve extends Thread {
    int laps;
    int gen;
    AtomicInteger count;
     User user;
    PlatformService platformService;
    ConsumerService consumerService;
    ProviderService providerService;
    CountDownLatch countDownLatch;
    CyclicBarrier beforeBarrier;
    CyclicBarrier afterBarrier;

    //初始化多线程，将需要用到的mapper和service加入到类中
    public Serve(User user, PlatformService platformService, ConsumerService consumerService, ProviderService providerService, CountDownLatch countDownLatch, AtomicInteger count, CyclicBarrier beforeBarrier, CyclicBarrier afterBarrie, int gen,int laps){
        //将客户端需要的东西注入到线程中，由于mapper等需要连接的东西是通过threadlocal存储的所以不存在线程安全问题
        this.user=user;
        this.platformService=platformService;
        this.consumerService=consumerService;
        this.providerService=providerService;
        this.countDownLatch=countDownLatch;
        this.count=count;
        this.beforeBarrier=beforeBarrier;
        this.afterBarrier=afterBarrie;
        this.gen=gen;
        this.laps=laps;
    }



    @Override
    public void run() {
        //客户端初始化完，等待其他用户的客户端
        countDownLatch.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //定义代，根据代别来统计准确率
        for(int i=0;i<gen;i++) {

            //每代进行5发循环调用并反馈
            for (int j = 0; j < laps; j++) {
                //增加总调用次数
                count.incrementAndGet();
                //随机需求，（待定：根据不同种类用户产生不同质量需求）
                int ranLow = 1;
                int ranHigh =10;
                List<Ser> services=platformService.getTrustedService(ranLow,ranHigh,user,i);
                consumerService.UseService(services,user,i);



            }
            try {
                //根据每个迭代的统计操作控制
                beforeBarrier.await();
                afterBarrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
