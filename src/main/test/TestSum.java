import java.util.Date;

/**
 * Created by inst1 on 2017/7/12.
 */
public class TestSum {
    public static void main(String[] args){
        long start=new Date().getTime();
        long sum=0L;
        for(long i=0;i<Integer.MAX_VALUE;i++){
            sum+=i;
        }
        System.out.println(sum);
        System.out.println(new Date().getTime()-start);
    }
}
