package cn.edu.nankai.cn;
import java.util.*;
public class T14 {
	String x,y;
	T14(){}
	T14(String x,String y){
		this.x=x;
		this.y=y;
	}
    public static void main(String []args){
    	Scanner scanner1=new Scanner(System.in);
    	T14 t=new T14(scanner1.next(),scanner1.next());//输入两个字符串作为参数传入
    	 System.out.println("==的结果:                "+(t.x==t.y));
    	 System.out.println("equals的结果:            "+t.x.equals(t.y));
    	 System.out.println("!=的结果:                "+(t.x!=t.y));
    	 System.out.println("x compare y的结果:       "+(t.x.compareTo(t.y)));
    	 System.out.println("x.length>y.length的结果: "+(t.x.length()>t.y.length()));
    }
   
}
