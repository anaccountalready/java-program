package cn.edu.nankai.cn;

public class T8 {
Long x=0x123l;
Long y=0123l;
public static void main(String[]args){
	T8 test1=new T8();
	System.out.println(Long.toBinaryString(test1.x));
	System.out.println(Long.toBinaryString(test1.y));
}
}
