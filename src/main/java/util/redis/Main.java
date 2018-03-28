package util.redis;

import java.util.Scanner;

/**
 * Created by inst1 on 2018/3/27.
 */
public class Main {
    public static void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int n=sc.nextInt(),m=sc.nextInt();
        long res=1000000000;
        res+=((n-m+1)*(n-m)/2);
        int temp=m*2+1;
        for(int i=n;i>=temp;i--){
            for(int j=i-m;j>m;j--){
                if(i%j>=m){
                    res++;
                }
            }
        }
        System.out.println(res);
    }
}
