package cn.edu.nankai.cn;

import java.util.Scanner;

public class Fibonacci {
	int fibonaqi(int i){
		if(i==1||i==2){
		    return 1;}
		else {
			return fibonaqi(i-1)+fibonaqi(i-2) ;
			}
	}
	public static void main(String arg[]){
	Fibonacci test1=new Fibonacci();
	Scanner scanner1=new Scanner(System.in);
	int m=scanner1.nextInt();
	test1.fibonaqi(m);
	for(int j=1;j<=m;j++){
		System.out.println(test1.fibonaqi(j));
	}
	}

}
