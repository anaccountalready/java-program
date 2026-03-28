package cn.edu.nankai.cn;

public class T19 {
	void in(String a[]){
		for(String m:a){
			System.out.println(m);
		}
	}
	public static void main(String []a){
	T19 test1=new T19();
	String b[]={"ab","cd","ef","gh"};
	String c[]=new String[3];
	test1.in(b);
	test1.in(c);
	
}
}
