package util.test;

/**
 * Created by inst1 on 2018/2/17.
 */
public class Main {

    public static void main(String[] args) {
        int i=10;
        Integer a=10;
        System.out.println(a==i);


    }



    static String add(String a,String b){
        char[] listA=a.toCharArray(),listB=b.toCharArray();
        int lenA=a.length(),lenB=b.length();
        boolean left=false;
        if(lenA>lenB){
            left=true;
        }
        int[] list=new int[Math.max(lenA,lenB)+1];
        int temp=0,next=0,curr;
        for(int i=list.length-1;i>=1;i--){
            if(left){
                if(lenB!=0){
                    curr=listA[--lenA]+listB[--lenB]+next-96;next=0;
                }
                else{
                    curr=list[--lenA]-48;
                    curr+=next; next=0;
                }
            }
            else{
                if(lenA!=0){
                    curr=listA[--lenA]+listB[--lenB]+next-97;
                    next=0;
                }
                else{
                    curr=listB[--lenB]-48+next; next=0;
                }
            }
            if(curr>=20||curr<0){
                return "error";
            }
            if(curr>=10){
                curr-=10;next=1;
            }
            else{
                next=0;
            }
            list[i]=curr;
        }
        list[0]=next;
        StringBuilder sb=new StringBuilder();
        for(int i:list){
            if(i!=0){
                sb.append(i);
            }
        }
        return sb.toString();
    }



}
