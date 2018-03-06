package util.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lz.TrustApplication;
import com.lz.entity.Record;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * Created by inst1 on 2017/11/11.
 */
@SpringBootTest(classes= TrustApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestRedis {


        @Autowired
        private StringRedisTemplate stringRedisTemplate;

        @Test
        public void save(){
            Gson gson=new Gson();
            Type type = new TypeToken<ArrayList<Record>>() {}.getType();
            String a=stringRedisTemplate.opsForValue().get("10105");
            ArrayList<Record> records=gson.fromJson("["+a+"]",type);
            System.out.println(records);
        }

}
