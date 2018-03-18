/**
 * Created by inst1 on 2017/7/17.
 */
public class ThreadA implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
