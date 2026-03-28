package cn.nankai.edu.cn;
import java.util.Scanner;
/**
   @author Ha ha
   @version 0*/
public class Test11 {
	/**change the hue of the color
	   @param a of integer argument*/
	int a;
	 public static void main(String[]args){
		 Scanner s=new Scanner(System.in);
		 Test11 t=new Test11(); 
		 t.a=s.nextInt();
		 AllTheColorsofTheRainbow r=new AllTheColorsofTheRainbow();
		 r.changeTheHueOfTheColor(t.a);
		 System.out.println(r.anInterRepresentingColors);
		 s.close();
	}
}
class AllTheColorsofTheRainbow{
	int anInterRepresentingColors;
	void changeTheHueOfTheColor(int newHue){
		anInterRepresentingColors=newHue;
	}
}
