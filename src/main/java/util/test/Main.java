package util.test;

import java.util.Scanner;

/**
 * Created by inst1 on 2018/2/17.
 */
public class Main {
    static int count=0;
    static boolean stop=false;

    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int all=sc.nextInt();
        for(int i=0;i<all;i++){
            long [] list=new long[3];
            list[0]=sc.nextLong();
            list[1]=sc.nextLong();
            list[2]=sc.nextLong();
            System.out.println(solve(list));
        }
    }
    static long solve(long[] list){
        long sum=0;
       while(true){
           sort(list);
           if(list[0]==0&&list[1]==0){
               break;
           }
           if(list[0]==list[1]&&list[1]==list[2]){
               sum+=list[0];
               break;
           }
           else{
               if(list[0]==0){
                    if(list[1]*2<list[2]){
                        sum+=list[0];
                    }
                    else{
                        sum+=(list[0]+list[2])/3;
                    }
               }
               else{
                   long temp=(list[2]-list[1])/2+1;
                   if(temp>=list[0]){
                       list[2]-=list[0]*2;
                       sum+=list[0];
                       list[0]=0;

                   }
                   else{
                       list[0]-=temp;
                       list[2]-=temp*2;
                       sum+=temp;
                   }
               }
           }

       }
        return sum;



    }
    static void sort(long[] list){
        for(int j=0;j<2;j++){
            for(int i=0;i<2;i++){
                if(list[i]>list[i+1]){
                    long temp=list[i];
                    list[i]=list[i+1];
                    list[i+1]=temp;
                }
            }
        }

    }












}
