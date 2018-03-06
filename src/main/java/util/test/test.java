package util.test;


import com.lz.entity.Ser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by inst1 on 2017/11/12.
 */
public class test {
    static class TreeNode {
        int val = 0;
        TreeNode left = null;
        TreeNode right = null;

        public TreeNode(int val) {
            this.val = val;

        }
    }

    static Integer  rev(int target){
        char[] a=(target+"").toCharArray();
        char temp;
        int i=0,j=a.length-1;
        while(i<j){
            temp=a[i];
            a[i]=a[j];
            a[j]=temp;
            i++;
            j--;
        }
        i=0;
        while(a[i]=='0'){
            i++;
        }
        StringBuilder stringBuilder=new StringBuilder();
        for(int z=i;z<a.length;z++){
            stringBuilder.append(a[z]);
        }
        return Integer.parseInt(stringBuilder.toString());
    }
    static  int mid;
    public static void main(String args[]) {
        Ser ser=new Ser();
        Runnable r=()-> System.out.print(ser);
        ser.setId(123122);
        ArrayList<Integer> s=new ArrayList<>();
        Collections.sort(s, Comparator.comparingInt(a -> a));


    }

    public static  int count(int target){
        int[] t=new int[target+1];
        Arrays.fill(t,1);
        for(int i=2;i<target;i++){
            if(t[i]==1){
                int limit=target/i;
                for(int j=2;j<=limit;j++){
                    t[i*j]=0;
                }
            }
        }
        int a=2,b=target-2,count=0;
        while(a<=b){
            if(t[a]==1&&t[b]==1){
                count++;
            }
            a++;b--;
        }
        return count;
    }
    private static void countPrimePiars(int n) {
        // TODO Auto-generated method stub
        int count=0;
        boolean[] prime=scanPrimes(n);
        for (int i = 2; i < prime.length; i++) {

            if(prime[i]&&prime[n-i]){
                count++;
            }
        }
        count=count>>1;
        if(prime[n>>1]&&prime[n-(n>>1)]){
            count++;
        }
    }

    private static boolean[] scanPrimes(int n) {
        boolean[] prime=new boolean[n+1];
        prime[0]=prime[1]=false;
        prime[2]=true;
        for (int i = 3; i < n; i+=2) {
            prime[i]=true;
            prime[i+1]=false;
        }


        for (int i = 3; i <=n; i+=2) {
            if(prime[i]){
                if(!isPrime(i)){
                    prime[i]=false;
                }

                int i2=i<<1;

                for (int j = i*3; j <=n; j=j+i2) {
                    prime[j]=false;
                }
            }
        }

        return prime;
    }

    private static boolean isPrime(int x) {
        // TODO Auto-generated method stub
        int n=(int) (Math.sqrt(x)+1);
        for (int i = 2; i < n; i++) {
            if(x%i==0){
                return false;
            }
        }
        return true;
    }




}








