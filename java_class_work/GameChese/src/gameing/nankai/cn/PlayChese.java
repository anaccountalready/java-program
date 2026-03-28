package gameing.nankai.cn;

import java.util.Scanner;
public class PlayChese {
	public static void main(String []args){
	while(true){
		View.showstart();
		Scanner s1=new Scanner(System.in);
		Scanner s2=new Scanner(System.in);
		int l,r;
		l=s1.nextInt();
		r=s2.nextInt();
		Control.report(l-1,r-1);
		}
	}
}
