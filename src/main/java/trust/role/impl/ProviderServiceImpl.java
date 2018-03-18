package trust.role.impl;

import org.springframework.stereotype.Service;
import trust.entity.Ser;
import trust.role.ProviderService;

import java.util.Random;

/**
 * Created by inst1 on 2017/7/16.
 */
@Service("ProviderService")
public class ProviderServiceImpl implements ProviderService {
    public Ser ProduceServiceActualQuality(Ser ser,int round){
        Random random=new Random();
        //模拟不良商家为了获取用户信任而使用的开始正常服务后来低水平服务的不可信行为，此模拟可凸显出不使用时间衰减因子时造成的可信错误评价情况
        if(round<3||ser.getRole().equals("good")){
            ser.setUsability((float) (random.nextFloat()/10+0.9));
            ser.setReliability((float) (random.nextFloat()/10+0.9));
            ser.setResponseTime((float) (random.nextFloat()/10+0.9));
            ser.setThroughPut((float) (random.nextFloat()/10+0.9));
        }
        else{
            ser.setUsability((float) (random.nextFloat()/4+0.7));
            ser.setReliability((float) (random.nextFloat()/4+0.7));
            ser.setResponseTime((float) (random.nextFloat()/4+0.7));
            ser.setThroughPut((float) (random.nextFloat()/4+0.7));
        }

        return ser;
    }
}
