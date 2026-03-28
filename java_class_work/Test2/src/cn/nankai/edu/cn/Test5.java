package cn.nankai.edu.cn;

public class Test5 {
	
    public static void main(String []arg){
    	DataOnly m=new DataOnly();
        m.i=5;
        m.j=1.4;
        m.b=true;
		System.out.println("m.i="+m.i);
		System.out.println("m.j="+m.j);
		System.out.println("m.b="+m.b);
		
    }
}
 class DataOnly{
	int i;
	double j;
	boolean b;
}
