package com.lz.service.impl;

import com.lz.entity.CountHelper;
import com.lz.entity.Result;
import com.lz.entity.User;
import com.lz.mapper.CountHelperMapper;
import com.lz.mapper.RecordMapper;
import com.lz.mapper.ServiceMapper;
import com.lz.mapper.UserMapper;
import com.lz.role.ConsumerService;
import com.lz.role.PlatformService;
import com.lz.role.ProviderService;
import com.lz.role.Serve;
import com.lz.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


@Service("TestService")
public class TestServiceImpl implements TestService {
    public volatile AtomicInteger k=new AtomicInteger();
    @Autowired
    UserMapper userMapper;

    @Autowired
    ServiceMapper serviceMapper;

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    ConsumerService consumerService;

    @Autowired
    ProviderService providerService;

    @Autowired
    CountHelperMapper countHelperMapper;
    @Autowired
    PlatformService platformService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private List<CountHelper> countHelpers=new ArrayList<>();
    private  void Test(int gen,int choice,int laps,int countCase) throws BrokenBarrierException, InterruptedException {

        //初始化case2中的偏向服务对比试验统计用数据
        int sum=0;
        //从数据库中取出用户
        final List<User> users=userMapper.selectAll();
        long start= System.currentTimeMillis();
        //记录总共调用服务次数的数值，为之后统计图服务。
        AtomicInteger test=new AtomicInteger(0);
        //第一个栅栏用来拦住全部正在调用服务的用户，让服务统计。确保全部用户完成了调用量再统计
        CyclicBarrier beforeBarrier=new CyclicBarrier(users.size()+1);
        //第二个栅栏用来确保在统计时用户不会在继续调用，等到统计完成了再放用户进行下一轮使用，确保一致性。
        CyclicBarrier afterBarrier=new CyclicBarrier(users.size()+1);
        //确保全部用户线程同时开启（可有可无）
        final CountDownLatch countDownLatch=new CountDownLatch(users.size());
        //创建客户端，开始调用服务并模拟平台自动评估服务可信值，模拟用户在可选列表的服务列表中随机选择服务，服务提供商模拟提供特定范围内的服务质量，根据对比值自动产生反馈值并录入数据库中，将上面步骤中加入的栅栏加入到每个客户端中协助控制
        for(User user:users){
            Serve s=new Serve(user,platformService,consumerService,providerService,countDownLatch,test,beforeBarrier,afterBarrier,gen,laps);
            s.start();
        }
        //对应多个代用来在最终统计图中显示出平台在渐渐加强服务评估的精度，每次使用第一个栅栏等待所有的用户调用完当前迭代的量，第二个栅栏卡住用户等自己统计完插入进数据库再放行
        for(int i=0;i<gen;i++){
            beforeBarrier.await();
            CountHelper countHelper=new CountHelper();
            if(i==16){
                int a=1;
            }
            //根据不同的情况使用不同的统计策略
            switch (countCase){
                //统计整个过程中可信的服务次数和全部服务次数之间的比例
                case 0:
                    //统计数据库反馈记录中符合可信阈值要求的服务调用次数
                    int countSuccess=userMapper.countSuccess();
                    //统计原子变量得到总共调用次数（无论服务可信还是非可信）
                    int currentTotal=test.get();
                    //将统计的数据录入到数据库中
                    countHelper.setGen(i+1);
                    countHelper.setSuccessCount(countSuccess);
                    countHelper.setTotalCount(currentTotal);
                    countHelperMapper.insert(countHelper);
                    break;
                //统计好的服务提供商和坏服务提供商每轮被调用的次数
                case 1:
                    int countChoiceGood=serviceMapper.selectTotalGood();
                    int countChoiceBad=serviceMapper.selectTotalBad();
                    countHelper.setGen(i+1);
                    countHelper.setSuccessCount(countChoiceGood);
                    countHelper.setTotalCount(countChoiceBad);
                    countHelperMapper.insert(countHelper);
                    break;
                //统计带偏向相似度和不带偏向相似度的时候偏向服务器被调用次数
                case 2:

                        CountHelper countHelper1=new CountHelper();
                        countHelper1.setGen(i+1);
                        //储存总共的偏向服务调用总数
                        int totalRes=serviceMapper.selectTotalRes();
                        countHelper1.setTotalCount(totalRes-sum);
                        countHelpers.add(countHelper1);
                        sum=totalRes;

                         countHelper1=countHelpers.get(i);
                        //储存总共的偏向服务调用总数
                         totalRes=serviceMapper.selectTotalRes();
                        countHelper1.setSuccessCount(totalRes-sum);
                        sum=totalRes;

                    //当两遍都进行完了以后将统计数据录入数据库
                    if(i==gen-1&&choice==1){
                        countHelperMapper.insertCountHelpers(countHelpers);
                    }
                    break;
                default:
                    break;

            }

            //放开第二个栅栏，让用户继续调用服务，开始下一次迭代
            afterBarrier.await();


        }
        long end=System.currentTimeMillis();
        System.out.print(end-start);
        System.out.print("完成");
    }



    //获取最终模拟完的结果，以折线图的情形展示
    @Override
    public List<Result> GetResult(int countCase) {
        //获取全部迭代中的数据
        //初始化需要的数值
        List<CountHelper> countHelpers=countHelperMapper.selectAll();
        List<Result> results=new ArrayList<>();
        List<Result> resultsa=new ArrayList<>();
        int j=countHelpers.size();
        //根据不同的需求使用不同的统计方法
        switch(countCase) {
            //最普通的计数方式，单纯对比两次试验时成功服务的比率
            case 0:
                for (int i = 0; i < j; i++) {
                    Result result = new Result();
                    result.setRound(i + 1);
                    if (countHelpers.get(i).getGen() == 1) {
                        CountHelper countHelper = countHelpers.get(i);
                        //获取每次迭代中服务推荐最终结果的“成功推荐”所占比例
                        result.setTrust(countHelper.getSuccessCount() / countHelper.getTotalCount());


                    } else {
                        CountHelper pre = countHelpers.get(i - 1);
                        CountHelper cur = countHelpers.get(i);
                        result.setTrust((float) (cur.getSuccessCount() - pre.getSuccessCount()) / (cur.getTotalCount() - pre.getTotalCount()));
                    }
                    results.add(result);

                }
                break;
            //对比好和坏服务商服务次数差别
            case 1:
                for (int i=0;i<j;i++){
                    Result resultA=new Result();
                    resultA.setRound(i+1);
                    results.add(resultA);
                    Result resultB=new Result();
                    resultB.setRound(i+1);
                    resultsa.add(resultB);
                    if (i == 0) {
                        resultA.setTrust(countHelpers.get(0).getSuccessCount());
                        resultB.setTrust(countHelpers.get(0).getTotalCount());
                    }
                    else{
                        resultA.setTrust(countHelpers.get(i).getSuccessCount()-countHelpers.get(i-1).getSuccessCount());
                        resultB.setTrust(countHelpers.get(i).getTotalCount()-countHelpers.get(i-1).getTotalCount());
                    }
                }
                results.addAll(resultsa);
                break;
            //对比带偏向和不带偏向下偏向服务的调用次数
            case 2:
                for (int i=0;i<j;i++){
                    Result resultA=new Result();
                    resultA.setRound(i+1);
                    results.add(resultA);
                    Result resultB=new Result();
                    resultB.setRound(i+1);
                    resultsa.add(resultB);
                    resultA.setTrust(countHelpers.get(i).getSuccessCount());
                    resultB.setTrust(countHelpers.get(i).getTotalCount());
                }
                results.addAll(resultsa);
                break;
            default:
                break;
        }
        return results;

    }

    @Override
    public void Run(int gen,int laps,int choice) throws BrokenBarrierException, InterruptedException {
        switch (choice){
            //比较时间窗口
            case 0:
                platformService.init(false,false,false);
                Test(gen,0,laps,0);
                ClearPart();
                platformService.init(true,false,false);
                Test(gen,0,laps,0);
                ClearPart();
                break;
            //比较用户偏向
            case 1:
                platformService.init(true,false,false);
                Test(gen,0,laps,2);
                ClearPart();
                platformService.init(true,true,false);
                Test(gen,0,laps,2);
                ClearPart();
                break;
            //比较用户评价相似度
            case 2:
                platformService.init(true,true,false);
                Test(gen,0,laps,0);
                ClearPart();
                platformService.init(true,true,true);
                Test(gen,0,laps,0);
                ClearPart();
                break;
            default:
                break;
        }

//        switch (countCase){
//            //对比服务成功率
//            case 0:
//                Test(gen,0,laps,countCase);
//                ClearPart();
//                Test(gen,1,laps,countCase);
//                ClearPart();
//                break;
//            //对比好坏服务商的选中次数
//            case 1:
//                ClearPart();
//                Test(gen,0,laps,countCase);
//                break;
//            //对比对比带不带偏向下偏向服务的调用次数
//            case 2:
//                Clear();
//                Test(gen,0,laps,countCase);
//                ClearPart();
//                Test(gen,1,laps,countCase);
//                ClearPart();
//                break;
//            default:
//                break;
//        }

    }

    @Override
    public void Clear() {
        recordMapper.clearAll();
        stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
    }

    private void ClearPart() {
        recordMapper.clearPart();
        stringRedisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}

