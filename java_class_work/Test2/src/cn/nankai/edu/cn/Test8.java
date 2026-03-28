package cn.nankai.edu.cn;

public class Test8 {
	public static void main(String []arg){
		
		StaticTest1 a=new StaticTest1();
		StaticTest1 b=new StaticTest1();
		StaticTest1 c=new StaticTest1();
		
		a.j++;
		b.j++;
		c.j++;
		StaticTest1.j++;
		System.out.println(a.j);
		System.out.println(b.j);
		System.out.println(c.j);
		System.out.println(StaticTest1.j);
	}

}
class StaticTest1{
	   static int j=0;
}