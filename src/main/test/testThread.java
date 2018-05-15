/**
 * Created by inst1 on 2017/7/16.
 */
public class testThread implements Runnable {
    int count=0;

    @Override
    public void run() {
            for(int i=0;i<10000;i++) {
                add();
            }
    }
    public void add(){
        count++;
        System.out.println(this.hashCode() + ":" + count);
    }
}
