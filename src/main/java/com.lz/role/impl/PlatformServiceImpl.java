package com.lz.role.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lz.entity.Record;
import com.lz.entity.Ser;
import com.lz.entity.User;
import com.lz.mapper.RecordMapper;
import com.lz.mapper.ServiceMapper;
import com.lz.mapper.UserMapper;
import com.lz.role.PlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static java.lang.Math.sqrt;

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
    private UserMapper userMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Gson gson;
    public volatile double min=1;

    //缓慢增长窗口大小
    private static int winMin=50;
    //可信阈值
    float limit=0.8F;
    //时间衰减因子数
    float base=1.5F;
    //恶意服务商开始改变行为的时机
    private int signal=14;
    //自信因子
    private float selfTrust=0.6F;

    //是否使用时间窗口
    private boolean timewindow=true;
    //间接信任是否含有用户偏好相似度
    public boolean prefer=true;
    //间接信任是否含有评价相似度
    private boolean feedbackDiff=true;
    //评价数据缓存（同代下防止数据被连续计算）
    private ConcurrentHashMap<String,ConcurrentHashMap<Integer,Double>> cache=new ConcurrentHashMap<>();





    //获取可信服务列表
    @Override
    public List<Ser> getTrustedService(int ranLow, int ranHigh, User user,int gen) {

        //根据qos需求初步筛选服务
        List<Ser> services = serviceMapper.selectByQuality(ranLow, ranHigh);
        //如果用户是吞吐量偏好的，前期自动过滤响应时间型以积累数据
        if(user.getRole().equals("thr")) {
            for (int i = 0; i < services.size(); i++) {
                if (services.get(i).getRole().equals("res")&&gen < signal) {
                    services.remove(i);
                    i--;
                }
            }
        }
        Integer len = services.size();
        //评估每个服务的可信性
        for (int i = 0; i < len; i++) {
            //记录服务曾被选中次数，便于可能的统计工作
///            serviceMapper.countA(services.get(i).getId());

            //获取所有使用过该服务的历史评估信息
            double avg=measureServiceTrust(services.get(i),user,gen);
            //根据平均值选择是否不考虑该服务
            if (avg < limit && services.get(i).getCount() > 4) {
                //不选择则去除该服务，方便最后用户随机抽取可信服务
                services.remove(i);
                i--;
                len--;

            }
        }
        return services;
    }


    //按照时间衰减因子和滑动窗口计算直接信任
    private double getAverageByTimeAndWindow(int gen,String user,String ser){
        //查询缓存，命中则直接返回
        Double trust,s=cache.get(user+"re"+ser).get(gen);

        if(s!=null){
            return s;
        }
        //未命中去redis查询历史纪录进行计算
        Type type=new TypeToken<ArrayList<Record>>() {}.getType();
        String a=stringRedisTemplate.opsForValue().get(user+"&"+ser);
        ArrayList<Record> records=gson.fromJson("["+a+"]",type);
        //惩罚记录存在标志，用来刷新之前的记录为未知值
        boolean punishment=false;
        Integer reLen = records.size();
        double avg = 0;
        double timeWeightAll = 0;
        double timeWeight;
        //如果发现有一次的记录不达标，前面的记录全部更新为未知记录
        for (int y = reLen-1; y >0; y--) {
            trust=records.get(y).getTrust();
            if(trust>=limit){
                if(punishment){
                    timeWeightAll+=1;
                    avg+=limit;
                    continue;
                }
            }
            else{
                //记录惩罚标志，只要是低于阈值的比重值都为1
                punishment=true;
                timeWeightAll+=1;
                avg+=trust;
                continue;
            }
            //获取每个历史纪录到当前轮次的轮次差，用来计算比重
            int between=gen-records.get(y).getTime();
            //计算比重值，乘上信任值加到总计中，同时加到总比重中，最后一除就是可信值
            timeWeight= Math.pow(base,-between);
            timeWeightAll+=timeWeight;
            avg+=records.get(y).getTrust()*timeWeight;

        }
        //如果记录数量未达到缓慢增长窗口的大小，将缓慢增长窗口剩下的空值都更新为未知值，以达到缓慢增长的目的
        if(reLen<winMin){
            int remain=winMin-reLen;
            timeWeightAll+=remain;
            avg+=limit*remain;
        }
        double result=avg/timeWeightAll;
        //更新缓存
        storeMeasureResult(result,gen,user,ser);
        return result;
    }

    //按照时间衰减因子计算直接信任
    private double getAverageByTime(int gen,String user,String ser){
        //查询缓存是否命中，命中则直接返回结果
        Double trust,s=cache.get(user+"re"+ser).get(gen);
        if(s!=null){
            return s;
        }
        //缓存不命中，从redis中查询数据
        Type type=new TypeToken<ArrayList<Record>>() {}.getType();
        String a=stringRedisTemplate.opsForValue().get(user+"&"+ser);
        ArrayList<Record> records=gson.fromJson("["+a+"]",type);
        Integer reLen = records.size();
        double avg = 0;
        double timeWeightAll = 0;
        double timeWeight = 0;
        for (int y = 1; y < reLen; y++) {
            //获取每个历史纪录到当前时间的时间差，用来计算比重
            long between=gen-records.get(y).getTime();
            //计算比重值，乘上信任值加到总计中，同时加到总比重中，最后一除就是可信值
            timeWeight= Math.pow(base,-between);
            timeWeightAll+=timeWeight;
            avg+=records.get(y).getTrust()*timeWeight;
        }

        double result=avg/timeWeightAll;
        //更新缓存
        storeMeasureResult(result,gen,user,ser);
        return result;
    }

//    private double getAverage(List<Record> records){
//        Integer reLen = records.size();
//        //获取当前时间
//        double avg = 0;
//        for (int y = 0; y < reLen; y++) {
//            //在无时间衰减记录下，直接取平均就行
//            avg += records.get(y).getTrust();
//        }
//
//
//        //计算平均评分
//        return avg/reLen;
//    }

    //获取服务信任值
    double measureServiceTrust(Ser ser,User user,int gen){
        //获取曾经使用过该服务的用户
        Set<String> users=stringRedisTemplate.opsForSet().members(ser.getId()+"");
        //是否有记录
        boolean hasRecord=false;
        //初始化直接信任
        double self=0;
        double trust=0;
        double sum=0;
        double weightAll=0;
        String a=user.getRole(),b;
        for(String use:users){

            double diff=1;
            //获取某个用户对目标服务的信任值
            trust=measureServiceTrustWithUser(use,ser.getId()+"",gen);
            int id=Integer.parseInt(use);
            b=userRole(id);
            //当自己有记录是分开来算自己的
            if(id==user.getId()){
                hasRecord=true;
                self=trust;
                continue;
            }
            else {
                if(gen>=14&&feedbackDiff){
                    if(!userRole(id).equals(user.getRole())){
                        diff+=19;
                    }
                    //若间接接信任要考虑评价相似度
//                    diff+=measureUserFeedbackTrust(use, String.valueOf(user.getId()),gen);
                }


            }
            int weight;
            if(prefer){
                //偏好相似度
                if(isDifferent(a,b)){
                    weight=2;
                }
                else{
                    weight=3;
                }
            }
            else{
                weight=2;
            }
            if(gen>=signal&&feedbackDiff) {
                sum=sum+(trust*(weight/diff));
                weightAll=weightAll+(weight/diff);
            }
            else{
                sum=sum+(trust*weight);
                weightAll=weightAll+weight;
            }


        }

        //有记录则要按比例，没有就按间接
        if(hasRecord){
            double other=sum/weightAll;
            sum= (other)*(1-selfTrust)+self*selfTrust;

        }
        else{
            sum= sum/weightAll;
        }
        return sum;

    }

    //获取用户评价相似度
    double measureUserFeedbackTrust(String user,String base,int gen){
        Set<String> a=stringRedisTemplate.opsForSet().intersect(user+"u",base+"u");
        double sum=0;
        if(a.size()!=0){


//            sum=a.stream().mapToDouble((String s)->Math.pow(getFeedbackDiff(user,base,s,choice,gen),2)).sum();
            for(String s:a){
                sum+=Math.pow(getFeedbackDiff(user,base,s,gen),2);
            }
        }

        return sqrt(sum);
    }

    //获取某个用户对目标服务的信任值
    double measureServiceTrustWithUser(String user, String ser,int gen){
        double trust;
        if(timewindow){
                trust=getAverageByTimeAndWindow(gen,user,ser);

        }
        else{
            trust=getAverageByTime(gen,user,ser);
        }

        return trust;

    }

    //更新信任值缓存
    void storeMeasureResult(Double trust,int gen,String user,String ser){
        cache.get(user+"re"+ser).put(gen,trust);
//        stringRedisTemplate.opsForHash().put(user+"re"+ser,gen+"",trust.toString());
    }

    //获取对同一个服务两个用户间的反馈区别
    double getFeedbackDiff(String user,String base,String ser,int gen){
        if(timewindow){
            double tmcA=getAverageByTimeAndWindow(gen,user,ser);
            double tmcB=getAverageByTimeAndWindow(gen,base,ser);
            return (tmcA-tmcB);

        }
        else{
            double tmcA=getAverageByTime(gen,user,ser);
            double tmcB=getAverageByTime(gen,base,ser);
            return (tmcA-tmcB);
        }
    }

    @Override
    public double verify(int se,int gen){
        List<Record> records=recordMapper.selectByService(se);
        double trust=0;
        return trust;
    }

    @Override
    public boolean getPrefer() {
        return this.prefer;
    }

    @Override
    public void init(boolean timewindow, boolean prefer, boolean feedbackDiff) {
        this.timewindow=timewindow;
        this.prefer=prefer;
        this.feedbackDiff=feedbackDiff;
        //初始化缓存
        {
            for(int i=1;i<61;i++){
                for(int j=96;j<146;j++){
                    ConcurrentHashMap<Integer,Double> map=new ConcurrentHashMap<>();
                    cache.put(i+"re"+j,map);
                }
            }
        }
    }

    //判断用户的类别
    private String userRole(int id){
        return id<=30?"res":"thr";
    }


    private boolean isDifferent(String a,String b){
        return (a.equals("res")&&b.equals("thr"))||(a.equals("thr")&&b.equals("res"));
    }


}
