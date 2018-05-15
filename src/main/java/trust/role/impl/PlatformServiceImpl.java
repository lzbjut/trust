package trust.role.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trust.entity.Record;
import trust.entity.Ser;
import trust.entity.TrustMeasureCustom;
import trust.entity.User;
import trust.mapper.RecordMapper;
import trust.mapper.ServiceMapper;
import trust.mapper.UsageMapper;
import trust.role.PlatformService;

import java.util.List;

/**
 * Created by inst1 on 2017/10/31.
 */


@Service("PlatformService")
public class PlatformServiceImpl implements PlatformService {
    @Autowired
    private ServiceMapper serviceMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private UsageMapper usageMapper;

    @Override
    public List<Ser> getTrustedService(int ranLow, int ranHigh, User user,int choice) {

        //根据qos需求初步筛选服务
        List<Ser> services = serviceMapper.selectByQuality(ranLow, ranHigh);
        Integer len = services.size();
        //评估每个服务的可信性
        for (int i = 0; i < len; i++) {
            //记录服务曾被选中次数，便于可能的统计工作
            serviceMapper.countA(services.get(i).getId());
            //获取所有使用过该服务的历史评估信息
            double avg=measureServiceTrust(services.get(i),choice);

            //根据平均值选择是否不考虑该服务
            if (avg < 0.9 && services.get(i).getCount() > 4) {
                //不选择则去除该服务，方便最后用户随机抽取可信服务
                services.remove(i);
                i--;
                len--;

            }
        }
        return services;
    }

    private TrustMeasureCustom getAverageByTime(List<Record> records){
        Integer reLen = records.size();
        //获取当前时间
        long now =System.currentTimeMillis();
        double avg = 0;
        double timeWeightAll = 0;
        double timeWeight = 0;
        for (int y = 0; y < reLen; y++) {
            //获取每个历史纪录到当前时间的时间差，用来计算比重
            long between=(now-records.get(y).getTime().getTime())/1000;
            //计算比重值，乘上信任值加到总计中，同时加到总比重中，最后一除就是可信值
            timeWeight=Math.pow(2.5,-between);
            timeWeightAll+=timeWeight;
            avg+=records.get(y).getTrust()*timeWeight;
        }
        //计算平均评分


        TrustMeasureCustom trustMeasureCustom=new TrustMeasureCustom();
        trustMeasureCustom.setTrust(avg);
        trustMeasureCustom.setWeight(timeWeightAll);
        return trustMeasureCustom;
    }

    private TrustMeasureCustom getAverage(List<Record> records){
        Integer reLen = records.size();
        //获取当前时间
        double avg = 0;
        for (int y = 0; y < reLen; y++) {
            //在无时间衰减记录下，直接取平均就行
            avg += records.get(y).getTrust();
        }


        //计算平均评分

        TrustMeasureCustom trustMeasureCustom=new TrustMeasureCustom();
        trustMeasureCustom.setTrust(avg);
        trustMeasureCustom.setWeight(reLen);
        return trustMeasureCustom;
    }

    double measureServiceTrust(Ser ser,int choice){
        List<Integer> users=usageMapper.selectUserUsage(ser.getId());
        double sum=0,weight=0;
        for(int i=0;i<users.size();i++){
            TrustMeasureCustom trustMeasureCustom=measureServiceTrustWithUser(users.get(i),ser.getId(),choice);
            sum+=trustMeasureCustom.getTrust();
            weight+=trustMeasureCustom.getWeight();
        }
        return sum/weight;
    }

    TrustMeasureCustom measureServiceTrustWithUser(int user, int ser, int choice){
        List<Record> records=recordMapper.selectByUserAndService(user,ser);
        if(choice==0){
            return getAverageByTime(records);
        }
        else{
            return getAverage(records);
        }

    }



}
