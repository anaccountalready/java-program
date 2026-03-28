package cn.nankai.edu.cn;

public class Test7 {
  public static void main(String []arg){
	  Incrementable.increment();
	  System.out.print(StaticTest.i);
  }
}
class Incrementable{
	static void increment(){StaticTest.i++;}
}
class StaticTest{
	static int i=47;
}
