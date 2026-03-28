package big.work.cn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
/*
 * 实现的功能：
 * 点击左边的工具条上的按钮，可以画画，但对按钮点一次只能画一回，若要重复该形状需要再点一次该形状的按钮再画
 * 解释：画的过程，就是按下鼠标然后拖拽然后释放，可完成一次绘图，如果在只点了一次形状按钮的前提下，出现了多次画的过程【多次点击鼠标的话】，最终也只能显示最后的一次的形状
 * 可以选择comboBOX背景颜色 有默认值红，图形颜色有默认值绿，可选择三种绿红灰；
 * checkBox 可选择是否填充
 * TextField图形尺寸 可以点击上下按钮来改变图形尺寸，也可以直接在框里面改
 * TextField 文本，可以输入文本来绘制字符串，有默认值hello
 * 下方实时显示MYP面板的鼠标位置
 * */

public class Paint {
public static LinkedList<Shape>data=new LinkedList<Shape>();
static private Paint instance;
TextField tw=new TextField();
public static JFrame jframe=new JFrame("Java Painter");
private Paint(){}
static public Paint getinstance(){
	if(instance==null){instance=new Paint();}
	return instance;
}
public TextField getTw() {
	return tw;
}
public void setTw(TextField tw) {
	this.tw = tw;
}
public static void main(String []args){
	Paint.getinstance().begin();
}
private void begin() {
	// TODO Auto-generated method stub
	jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  JToolBar tb=new JToolBar(JToolBar.VERTICAL);
	  JPanel pane=new JPanel();
	  pane.add(tb);
	JButton bs[]=new JButton[]{new JButton(new ImageIcon("C:\\Users\\Ha ha\\Downloads\\line.png")),
	  	new JButton(new ImageIcon("C:\\Users\\Ha ha\\Downloads\\rectangle.jpg")),
	  	new JButton(new ImageIcon("C:\\Users\\Ha ha\\Downloads\\circle.png")),
	  	new JButton(new ImageIcon("C:\\Users\\Ha ha\\Downloads\\ellipse.png")),
	  	new JButton(new ImageIcon("C:\\Users\\Ha ha\\Downloads\\text.png"))
	
	  };
	  	for(int i=0;i<5;i++){
	  		tb.add(bs[i],new FlowLayout());
	  		bs[i].setActionCommand(""+i);
			bs[i].addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					String s=e.getActionCommand();
					//加链表元素，存变形状之前最后的结果
					Paint.data.add(new Shape(Shape.shape.getShapes(), Shape.shape.getShapecolor(),Shape.shape.getP1(), Shape.shape.getP2(),Shape.shape.isIffill()));
					Shape.shape.setShapes(s);
				}
			});
	  	}
	  	
    jframe.getContentPane().add(pane,BorderLayout.WEST);
    JPanel jp=new CB();
   jp.setPreferredSize(new Dimension(0,100));
   tw.setPreferredSize(new Dimension(0,50));
   tw.setForeground(Color.RED);
   tw.setFont(new Font("Microsoft Yahei", Font.BOLD, 22));
    jframe.getContentPane().add(jp,BorderLayout.NORTH);
    jframe.getContentPane().add(new MYP(),BorderLayout.CENTER);
    jframe.getContentPane().add(tw,BorderLayout.SOUTH);
    jframe.setVisible(true);
    jframe.setSize(1000, 1000);
    

}


}

