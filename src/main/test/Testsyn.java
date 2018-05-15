/**
 * Created by inst1 on 2017/7/16.
 */
public class Testsyn extends Thread{
    aaa aa=new aaa();
   public void run(){
        while(true){
            aa.printA();
            aa.printB();
        }
    }
    public static void main(String args[]){
       Testsyn testsyn=new Testsyn();
       new Thread(testsyn).start();
        new Thread(testsyn).start();
    }
}
