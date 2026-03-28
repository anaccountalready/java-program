package cn.nankai.edu.cn;
class t{
	float f;
}
public class T2 {
	float fuction(t w){
		w.f=1.1f;
		return w.f;
	}
    public static void main(String []arg){
    	T2 f1=new T2();
    	t m=new t();
    	System.out.println(f1.fuction(m));
    	System.out.println(m);
    }
}
