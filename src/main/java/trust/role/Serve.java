package trust.role;

import trust.entity.Ser;
import trust.entity.User;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by inst1 on 2017/6/21.
 */


public class Serve extends Thread {
    int gen;
    AtomicInteger count;
     User user;
    PlatformService platformService;
    ConsumerService consumerService;
    ProviderService providerService;
    CountDownLatch countDownLatch;
    CyclicBarrier beforeBarrier;
    CyclicBarrier afterBarrier;
    int choice;

    //初始化多线程，将需要用到的mapper和service加入到类中
    public Serve(User user, PlatformService platformService, ConsumerService consumerService, ProviderService providerService, CountDownLatch countDownLatch, AtomicInteger count, CyclicBarrier beforeBarrier, CyclicBarrier afterBarrie, int choice,int gen){
        //将客户端需要的东西注入到线程中，由于mapper等需要连接的东西是通过threadlocal存储的所以不存在线程安全问题
        this.user=user;
        this.platformService=platformService;
        this.consumerService=consumerService;
        this.providerService=providerService;
        this.countDownLatch=countDownLatch;
        this.count=count;
        this.beforeBarrier=beforeBarrier;
        this.afterBarrier=afterBarrie;
        this.choice=choice;
        this.gen=gen;
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

            //每代进行50发循环调用并反馈
            for (int j = 0; j < 10; j++) {
                //增加总调用次数
                count.incrementAndGet();
                //随机需求，（待定：根据不同种类用户产生不同质量需求）
                int ranLow = 1;
                int ranHigh =10;
                List<Ser> services=platformService.getTrustedService(ranLow,ranHigh,user,choice);
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
