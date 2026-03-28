package cn.nankai.edu.cn;
import java.util.Scanner;
public class Test6 {
	Scanner s1=new Scanner(System.in);
	String a=new String(s1.next());
	public static void main(String []arg){
		Test6 n=new Test6();
		AtypeName m=new AtypeName();
	System.out.println(m.storage(n.a));	
	}

}
class AtypeName{
	int storage(String s){
		return s.length()*2;
	}
	
}