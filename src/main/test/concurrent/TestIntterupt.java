package concurrent;

/**
 * Created by inst1 on 2017/7/25.
 */
public class TestIntterupt implements Runnable {
    @Override
    public void run() {
        try {
            another();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        Thread t=new Thread(new TestIntterupt());
        t.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("开始尝试终止");
        t.interrupt();

    }
    void synchronize() throws InterruptedException {
        while(!Thread.currentThread().isInterrupted()){

        }
        throw new InterruptedException();

    }
    void another() throws InterruptedException{
        int i=0;
        try {
            synchronize();
            while (true) {
                i++;

            }
        }
        catch(InterruptedException e){

            Thread.currentThread().interrupt();
        }
        while(true){

        }
    }
}
