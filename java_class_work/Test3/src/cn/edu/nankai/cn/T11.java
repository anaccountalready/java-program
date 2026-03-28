package cn.edu.nankai.cn;

public class T11 {
 Long x=0b1110l;
public static void main(String[]args){
	T11 test1=new T11();
	System.out.println(Long.toBinaryString(test1.x));
	while(test1.x!=0){
		test1.x=(test1.x>>1);
		System.out.println(Long.toBinaryString(test1.x));
	}
}
}
