/**
 * Created by inst1 on 2017/7/16.
 */
public class aaa {
    public int test;
    public synchronized  void printA(){
        System.out.println("A");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized  void printB(){
        System.out.println("B");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
