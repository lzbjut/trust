package trust.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trust.entity.CountHelper;
import trust.entity.Result;
import trust.entity.User;
import trust.mapper.CountHelperMapper;
import trust.mapper.RecordMapper;
import trust.mapper.ServiceMapper;
import trust.mapper.UserMapper;
import trust.role.ConsumerService;
import trust.role.PlatformService;
import trust.role.ProviderService;
import trust.role.Serve;
import trust.service.TestService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by inst1 on 2017/6/21.
 */
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
    private Integer id;
    private  void Test(int gen,int choice) throws BrokenBarrierException, InterruptedException {
        //从数据库中取出用户
        final List<User> users=userMapper.selectAll();
        long start=System.currentTimeMillis();
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
            Serve s=new Serve(user,platformService,consumerService,providerService,countDownLatch,test,beforeBarrier,afterBarrier,choice,gen);
            s.start();
        }
        //对应多个代用来在最终统计图中显示出平台在渐渐加强服务评估的精度，每次使用第一个栅栏等待所有的用户调用完当前迭代的量，第二个栅栏卡住用户等自己统计完插入进数据库再放行
        for(int i=0;i<gen;i++){
            beforeBarrier.await();
            //统计数据库反馈记录中符合可信阈值要求的服务调用次数
            int countSuccess=userMapper.countSuccess();
            //统计原子变量得到总共调用次数（无论服务可信还是非可信）
            int currentTotal=test.get();
            //将统计的数据录入到数据库中
            CountHelper countHelper=new CountHelper();
            countHelper.setGen(i+1);
            countHelper.setSuccessCount(countSuccess);
            countHelper.setTotalCount(currentTotal);
            countHelperMapper.insert(countHelper);
            //放开第二个栅栏，让用户继续调用服务，开始下一次迭代
            afterBarrier.await();

        }
        long end=new Date().getTime();
        System.out.print(end-start);
        System.out.print("完成");
    }

    //获取最终模拟完的结果，以折线图的情形展示
    public List<Result> GetResult() {
        //获取全部迭代中的数据
        List<CountHelper> countHelpers=countHelperMapper.selectAll();
        List<Result> results=new ArrayList<>();
        int j=countHelpers.size();
        for(int i=0;i<j;i++){
            Result result=new Result();
            result.setRound(i+1);
            if (countHelpers.get(i).getGen()==1) {
                CountHelper countHelper=countHelpers.get(i);
                //获取每次迭代中服务推荐最终结果的“成功推荐”所占比例
                result.setTrust(countHelper.getSuccessCount()/countHelper.getTotalCount());


            }

            else{
                CountHelper pre=countHelpers.get(i-1);
                CountHelper cur=countHelpers.get(i);
                result.setTrust((float)(cur.getSuccessCount()-pre.getSuccessCount())/(cur.getTotalCount()-pre.getTotalCount()));
            }
            results.add(result);

        }
        return results;

    }

    @Override
    public void Run() throws BrokenBarrierException, InterruptedException {
        Test(10,0);
        ClearPart();
        Test(10,1);
    }

    @Override
    public void Clear() {
        recordMapper.clearAll();
    }

    private void ClearPart() {
        recordMapper.clearPart();
    }
}
