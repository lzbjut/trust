package trust.role.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trust.entity.Record;
import trust.entity.Ser;
import trust.entity.Usage;
import trust.entity.User;
import trust.mapper.RecordMapper;
import trust.mapper.ServiceMapper;
import trust.mapper.UsageMapper;
import trust.mapper.UserMapper;
import trust.role.ConsumerService;
import trust.role.ProviderService;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by inst1 on 2017/7/16.
 */
@Service("ConsumerService")
public class ConsumerServiceImpl implements ConsumerService {
   @Autowired
   private ProviderService providerService;

   @Autowired
   private UserMapper userMapper;

   @Autowired
   private ServiceMapper serviceMapper;

   @Autowired
   private RecordMapper recordMapper;

    @Autowired
    private UsageMapper usageMapper;

   private int SelectService(List<Ser> services){
        //根据被评估为可信的服务列表随机选择服务
        Random random=new Random();
        return random.nextInt(services.size());
    }
    @Override
    public void UseService(List<Ser> services ,User user,int gen){
       int len=services.size();
        //如果有可选的服务则选择服务
        if(len>0){
            int ran =SelectService(services);
            Record r = new Record();
            //调用服务器service模拟服务提供过程获取实际qos值
            Ser actual=providerService.ProduceServiceActualQuality(services.get(ran),gen);
            //计算可信值
            float trust=actual.getReliability()*user.getReliability()+actual.getThroughPut()*user.getThroughPut()+actual.getResponseTime()*user.getResponseTime()+actual.getUsability()*user.getUsability();
            //录入反馈记录
            r.setSe(services.get(ran).getId());
            r.setTime(new Date());
            r.setUsr(user.getId());
            r.setTrust(trust );
            //如果服务最终评估为实际可信，则证明推荐和评估过程成功了。
            if(trust>=0.9){
                userMapper.count(user.getId());
            }

            //插入反馈记录
            recordMapper.insert(r);
            //记录服务被调用次数
            serviceMapper.count(services.get(ran).getId());
            //记录用户调用的服务
            Usage usage=new Usage();
            usage.setSe(services.get(ran).getId());
            usage.setUsr(user.getId());
            usageMapper.insertUsage(usage);
        }
    }



}
