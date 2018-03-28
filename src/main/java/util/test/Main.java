package util.test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by inst1 on 2018/2/17.
 */
public class Main {
    static int count=0;
    static boolean stop=false;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int n= sc.nextInt();
        int m=sc.nextInt();
        HashMap<Long,Long> pay=new HashMap<>();
        long[] Pay=new long[n];
        for(int i=0;i<n;i++){
            long jobDiff=sc.nextLong();
            long pzay=sc.nextLong();
            pay.put(pzay,jobDiff);
            Pay[i]=pzay;
        }
        Arrays.sort(Pay);
        for(int j=0;j<m;j++){
            long ar=sc.nextLong();
            for(int z=n-1;z>=0;z--){
                if(pay.get(Pay[z])<=ar){
                    System.out.println(Pay[z]);
                    break;
                }
            }
        }


    }

    static void sort(long[] list,long[] listB,int left,int right){
        if(left>=right){
            return;
        }
        long temp=list[left],tempB=listB[left];
        int start=left,end=right;
        while(start<end){
            while(end>start&&list[end]>=temp){
                end--;
            }
            list[start]=list[end];
            listB[start]=listB[end];
            while(start<end&&list[start]<=temp){
                start++;
            }
            list[end]=list[start];
            listB[end]=listB[start];
        }
        list[start]=temp;
        listB[start]=tempB;
        sort(list,listB,left,start-1);
        sort(list,listB,start+1,right);
    }









}
