package com.lz.role.impl;

import com.google.gson.Gson;
import com.lz.entity.Record;
import com.lz.entity.Ser;
import com.lz.entity.User;
import com.lz.mapper.ServiceMapper;
import com.lz.mapper.UserMapper;
import com.lz.role.ConsumerService;
import com.lz.role.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
   Gson gson;

   @Autowired
   private ServiceMapper serviceMapper;






   @Autowired
   private StringRedisTemplate stringRedisTemplate;

   private int SelectService(List<Ser> services){
        //根据被评估为可信的服务列表随机选择服务
        Random random=new Random();
        return random.nextInt(services.size());
    }
    @Override
    public void UseService(List<Ser> services , User user, int gen){

       int len=services.size();
       float trust;
        //如果有可选的服务则选择服务
        if(len>0){
            int ran =SelectService(services);
            Record r = new Record();
            if(services.get(ran).getRole()=="bad"){
                int i=1;
            }
            //调用服务器service模拟服务提供过程获取实际qos值
            Ser actual=providerService.ProduceServiceActualQuality(services.get(ran),gen);
            //恶意用户反馈相反的值，诋毁对手
//            if(user.getRole()=="bad"){
//                if(services.get(ran).getRole()=="bad"){
//                    trust=0.95F;
//                }
//                else{
//                    trust=actual.getReliability()*user.getReliability()+actual.getThroughPut()*user.getThroughPut()+actual.getResponseTime()*user.getResponseTime()+actual.getUsability()*user.getUsability();
//                }
//            }
//            else{
                //计算可信值
                trust=actual.getReliability()*user.getReliability()+actual.getThroughPut()*user.getThroughPut()+actual.getResponseTime()*user.getResponseTime()+actual.getUsability()*user.getUsability();
//            }

            //录入反馈记录
            r.setTime(gen);
            r.setTrust(trust);
            stringRedisTemplate.opsForValue().append(user.getId()+"&"+services.get(ran).getId(),","+gson.toJson(r));
            //如果服务最终评估为实际可信，则证明推荐和评估过程成功了。
            if(trust>=0.8){
                userMapper.count(user.getId());
            }

            //记录服务被调用次数
            serviceMapper.count(services.get(ran).getId());
            //记录用户调用的服务
            stringRedisTemplate.opsForSet().add((services.get(ran).getId()+""),(user.getId()+""));
            stringRedisTemplate.opsForSet().add((user.getId()+"u"),(services.get(ran).getId()+""));

        }
        else{
            System.out.print(len);
        }
    }



}
