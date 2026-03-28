package cn.edu.nankai.cn;
import java.util.Scanner;
class Connection1{
	static Integer sum;
	Integer n;
	static{sum=0;}
	{n=0;}
	 Connection1(){sum++;n=sum;}//sum记录对象个数
	 public String toString(){return ("Connection"+n);}
	}
class ConnectionManager1{
	static Connection1 []a;//先定义数组后在构造函数中new才能调用Connection1的构造函数
	ConnectionManager1(int m) {
		a=new Connection1[m];
		for(int i=0;i<m;i++)
			 a[i]=new Connection1();
	}
	static public Connection1 getconnection1(int j){
		if(Connection1.sum==0||j>=Connection1.sum)
			return null;
		else
			return a[j];
		}
	}
public class Tes1 {
	public static void main(String []args){
	Scanner scanner1=new Scanner(System.in);//输入数组元素的个数
	Scanner scanner2=new Scanner(System.in);//输入需要getconnection1的元素个数
	ConnectionManager1 test1=new ConnectionManager1(scanner1.nextInt());
	int n=scanner2.nextInt();
	for(int j=0;j<n;j++){
		if(ConnectionManager1.getconnection1(j)!=null){
		System.out.println(ConnectionManager1.getconnection1(j));//输出需要得到且存在的Connection1对象
	}
		else {System.out.println("null");//若没有了Connection1对象，输出null
		break;
		}
		}
	scanner1.close();
	scanner2.close();
}
	}
