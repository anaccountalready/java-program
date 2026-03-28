package big.work.cn;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

public class Shape {
 private  String shapes="0";
 private  int size=10;
 private  String text="HELLO!";
 private  Color shapecolor=Color.green;
 private  boolean iffill=false;
 private  Point p1,p2;
 {p1=new Point();p2=new Point();}
 private static Color backcColor=Color.red;
 static public Shape shape=new Shape();
 Shape(){}
	public void setP2(Point p2) {
	this.p2 = p2;
}
	public  Color getBackcColor() {
	return backcColor;
}
public void setBackcColor(Color backcColor) {
	this.backcColor = backcColor;
}
	public  String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSize() {
		return size;
	}
	public  void setSize(int size) {
		this.size = size;
	}
	public  boolean isIffill() {
		return iffill;
	}
	public  void setIffill(boolean iffill) {
		this.iffill = iffill;
	}
	Shape(String s,Color a,Point c,Point d,boolean i){
		shapes=s;
		shapecolor=a;
		p1=c;
		p2=d;
		iffill=i;
	}
	Shape(String s,String n,Color a,int m,Point c){
		shapes=s;
		text=n;
		shapecolor=a;
		size=m;
		p1=c;
		
	}
	
	public String getShapes() {
		return shapes;
	}
	public  void setShapes(String shapes) {
		this.shapes = shapes;
	}
	public  Color getShapecolor() {
		return shapecolor;
	}
	public  void setShapecolor(Color shapecolor) {
		this.shapecolor = shapecolor;
	}
	
	public  Point getP1() {
		return p1;
	}
	public  void  setP1(Point p1) {
		this.p1 = p1;
	}
	public Point getP2() {
		return p2;
	}
	
	
}
