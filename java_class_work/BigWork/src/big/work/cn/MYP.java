package big.work.cn;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;

import javax.swing.JPanel;
public class MYP extends JPanel {
private Point p1,p2;
public MYP(){
	p1=new Point();p2=new Point();
	addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			//p2=e.getPoint();
			//repaint();
			Shape.shape.setP2(p2);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			p1=e.getPoint();
			Shape.shape.setP1(p1);
			repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	});
	addMouseMotionListener(new MouseMotionAdapter()  {
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			//实时显示鼠标坐标
			Paint.getinstance().getTw().setText("鼠标坐标为：（"+e.getX()+","+e.getY()+")");

		}
	

		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			p2=e.getPoint();
		   repaint();
		
		}
	});
}
@Override
public void paintComponent(Graphics a) {
	// TODO Auto-generated method stub
	super.paintComponent(a);
	setOpaque(true);
	setBackground(Shape.shape.getBackcColor());
	for(Shape i:Paint.data){
		{
	Shape shape=new Shape();
		shape=i;
		drawshape(a,shape);
}}
     drawcurrentshape(a);
			
    
}
private void drawcurrentshape(Graphics a) {
	// TODO Auto-generated method stub
	String currentShape;
	currentShape=Shape.shape.getShapes();
	if(currentShape.equals("4")){
		Shape shape1=new Shape(Shape.shape.getShapes(),Shape.shape.getText(), Shape.shape.getShapecolor(), 
		Shape.shape.getSize(), Shape.shape.getP1());
		Paint.data.add(shape1);//在此添加字符串的形状的链表元素
		drawshape(a,shape1);
	}
			
		
		
	
	else if((currentShape.equals("0")||currentShape.equals("1")||currentShape.equals("2")||currentShape.equals("3"))){
	Shape shape2=new Shape(Shape.shape.getShapes(), Shape.shape.getShapecolor(),p1, p2,Shape.shape.isIffill());
	drawshape(a,shape2);
	}
	else;
	
}
private void drawshape(Graphics a,Shape b) {
	// TODO Auto-generated method stub
	//线
	if(b.getShapes().equals("0")){
		a.setColor(b.getShapecolor());
		a.drawLine(b.getP1().x,b.getP1().y,b.getP2().x,b.getP2().y);
	}
	//矩形
	else if(b.getShapes().equals("1"))
	{
		if(b.isIffill()){
			a.setColor(b.getShapecolor());
			a.fillRect(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x), Math.abs(b.getP1().y-b.getP2().y));
			
			}
		else {
			a.setColor(b.getShapecolor());
			a.drawRect(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x), Math.abs(b.getP1().y-b.getP2().y));}
	}
	//圆
	else if(b.getShapes().equals("2")){
		if(b.isIffill()){
			a.setColor(b.getShapecolor());
			a.fillOval(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x),Math.abs(b.getP1().x-b.getP2().x));
   //用的直径是横坐标的差的绝对值
		}else {
			a.setColor(b.getShapecolor());
			a.drawOval(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x), Math.abs(b.getP1().x-b.getP2().x));

	}}
	//椭圆
	else if(b.getShapes().equals("3"))
		{
		if(b.isIffill()){
		a.setColor(b.getShapecolor());
		a.fillOval(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x), Math.abs(b.getP1().y-b.getP2().y));
	}else {
		a.setColor(b.getShapecolor());
		a.drawOval(Math.min(b.getP1().x,b.getP2().x), Math.min(b.getP1().y,b.getP2().y),Math.abs(b.getP1().x-b.getP2().x), Math.abs(b.getP1().y-b.getP2().y));
}}
		else if(b.getShapes().equals("4")){
			//Shape.data.add(new Shape("4",b.getText(),b.getShapecolor(),b.getSize(),b.getP1()));
			a.setColor(b.getShapecolor());
			Font myfont = new Font("Courier New",Font.BOLD,b.getSize());
			a.setFont(myfont);
			a.drawString(b.getText(), b.getP1().x, b.getP1().y);
			
		}
    
}}

   


