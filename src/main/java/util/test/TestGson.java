package util.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lz.entity.TrustMeasureCustom;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by inst1 on 2017/11/11.
 */
public class TestGson {
    static Gson gson=new Gson();

    public static void main(String args[]){
        List<TrustMeasureCustom> trustMeasureCustoms1=new ArrayList<>();
        String a="";
        for(int i=0;i<5;i++) {
            TrustMeasureCustom trustMeasureCustom = new TrustMeasureCustom();
            trustMeasureCustom.setWeight(1.1);
            trustMeasureCustom.setTrust(1.2);
            String s=gson.toJson(trustMeasureCustom);
            a=a+","+s;
        }
        String s=gson.toJson(trustMeasureCustoms1);
        Type type = new TypeToken<ArrayList<TrustMeasureCustom>>() {}.getType();
        ArrayList<TrustMeasureCustom> trustMeasureCustoms=gson.fromJson("["+a+"]",type);
        System.out.print(trustMeasureCustoms);
    }
}
