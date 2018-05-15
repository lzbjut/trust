/**
 * Created by inst1 on 2017/7/7.
 */
public  class Father {


    static int length;
    static int height;
    static int[][] arr1 = {{1,2,8,9},{2,4,9,12},{4,7,10,13},{6,8,11,15}};
    public static boolean Find(int target, int[][] array) {
        height=array[0].length;
        length=array.length;

        return cut(target,length-1,0,array);
    }
    public static boolean cut(int target, int left, int down, int[][] array){

        if(left<0||down==height){
            return false;
        }

        if(array[left][down]==target){
            return true;
        }
        else if(array[left][down]>target){
            return cut(target,--left,down,array);
        }
        else {
            return cut(target,left,++down,array);
        }


    }
    public static void main(String args[]){
        System.out.print(Find(7,arr1));

    }}