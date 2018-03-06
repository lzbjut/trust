package com.lz.role.impl;

import org.springframework.stereotype.Service;
import com.lz.entity.Ser;
import com.lz.role.ProviderService;

import java.util.Random;

/**
 * Created by inst1 on 2017/7/16.
 */
@Service("ProviderService")
public class ProviderServiceImpl implements ProviderService {
    int signal=14;

    @Override
    public Ser ProduceServiceActualQuality(Ser ser,int round){
        Random random=new Random();
        //模拟不良商家为了获取用户信任而使用的开始正常服务后来低水平服务的不可信行为，此模拟可凸显出不使用时间衰减因子时造成的可信错误评价情况
        //此处偷了懒，直接返回的每个属性的可信值（0-1范围），后期需要考量正向反向属性问题真实计算出值来
        switch (ser.getRole()){
            case "bad" :
            if(round<signal){
                ser.setUsability((float) (random.nextFloat()/10+0.9));
                ser.setReliability((float) (random.nextFloat()/10+0.9));
                ser.setResponseTime((float) (random.nextFloat()/10+0.9));
                ser.setThroughPut((float) (random.nextFloat()/10+0.9));
            }else{
                ser.setUsability((float) (random.nextFloat()/5+0.65));
                ser.setReliability((float) (random.nextFloat()/5+0.65));
                ser.setResponseTime((float) (random.nextFloat()/5+0.65));
                ser.setThroughPut((float) (random.nextFloat()/5+0.65));
            }
            break;
            case "res":
                ser.setUsability((float) (random.nextFloat()/10+0.8));
                ser.setReliability((float) (random.nextFloat()/10+0.8));
                ser.setResponseTime((float) (random.nextFloat()/10+0.9));
                ser.setThroughPut((float) (random.nextFloat()/20+0.6));
            break;
            default:
                ser.setUsability((float) (random.nextFloat()/10+0.9));
                ser.setReliability((float) (random.nextFloat()/10+0.9));
                ser.setResponseTime((float) (random.nextFloat()/10+0.9));
                ser.setThroughPut((float) (random.nextFloat()/10+0.9));

        }


        return ser;
    }
}
