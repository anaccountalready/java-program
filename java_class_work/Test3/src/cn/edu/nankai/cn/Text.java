package cn.edu.nankai.cn;

import java.util.Scanner;

class Connection{
	static Integer sum;
	Integer n;
	static{
		sum=0;
	}
	{
		n=0;
	}
	 Connection(){sum++;n=sum;}//sum记录对象个数
	 public String toString(){return ("Connection"+n);}
	}
class ConnectionManager{
	ConnectionManager() {
	}
	static Connection []a=new Connection[10];
	{for(int i=0;i<10;i++){
		 a[i]=new Connection();
	}}//初始化数组
	static public Connection getconnection(int j){
		if(Connection.sum==0||j>=Connection.sum){
			return null;
		}
		else{
			return a[j];
			}
		}
	}
public class Text {

public static void main(String []args){
	int k;
	Scanner scanner1=new Scanner(System.in);
	k=scanner1.nextInt();
	ConnectionManager test1=new ConnectionManager();
	for(int j=0;j<k;j++){
		System.out.println(ConnectionManager.getconnection(j));
	}
}
}
