package big.work.cn;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * 该计算器只能通过按按钮进行计算，PS【在运行程序的时候，发现按下Button的时候，会在Button内自动显示虚线框】
 * 说明：
 * 显示不同颜色的部分表示了不同的组件
 * public void show(String s)【s=public String geton()】实现对每次的按button的结果都会显示在textfield上，即会显示操作数和操作符，按CE也会显示回退后的结果，按C会清除所有
 *public String result()实现基本的加减乘除，1/x，开根号，平方,求阶乘，
 *并将每次的运算结果显示并重新返回到第一个操作数;
 *即对于每次按了等号后产生的结果可以继续按CE修改得到的结果也可以继续按操作符和下一个操作数进行操作；
 *如果想重新开始需要按C 全部清除，然后就可以重新从第一个操作数开始按入
 【PS：对于1/x，开根号，平方,求阶乘这种只针对一个操作数的，也需要按=后才会计算并显示结果
 对于”+/-“的按钮，会直接改变施加的操作数，将其变成当前的负数，不论变之后是正是负，输出都是带括号的，之后可以正常继续进行按入操作】
 *public void remove()实现按C按钮清除之前所有输入；并可以继续输入；
 *public void back()实现按CE按钮会一个一个字符地清除输入的内容，并在清除后可以继续输入
 */
public class MainC {
	JTextField textfield=new JTextField();
	public void show(String s){
		textfield.setText(s);
		
	}
	private static MainC instance;
	private MainC(){}
	public static MainC getinstance(){
		if(instance==null)instance= new MainC();
		return instance;
	}
public static void main(String []args){
	getinstance().begin();
}
private void begin() {
	// TODO Auto-generated method stub
	//通过setPreferredSize(new Dimension(width，height))设置有布局内的组件的大小，例如BorderLayout，塞在North，South的组件要设定不为零的高度，塞在EAst和West的组件要设定不为零的宽度，才会有效；
	textfield.setPreferredSize(new Dimension(0,50));//在北，设置宽度
	Font myfont = new Font("Courier New",Font.BOLD,22);
	textfield.setFont(myfont);//设置字体
	Font myfont1 = new Font("Courier New",Font.BOLD,30);
	Font myfont3 = new Font("Microsoft Yahei",Font.ITALIC,45);
	Font myfont4 = new Font("Courier New",Font.BOLD,25);
	JFrame frame=new JFrame("计算器");
	JPanel panel =new JPanel();
	JPanel nump=new JPanel();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new BorderLayout(10,10));//(int hgap, int vgap)
	frame.getContentPane().add(textfield,BorderLayout.NORTH);
	frame.getContentPane().add(panel,BorderLayout.CENTER);
	panel.setLayout(new BorderLayout(10,10));
	panel.add(nump,BorderLayout.CENTER);
	nump.setLayout(new GridLayout(4,4,10,10));//(int rows, int cols, int hgap, int vgap)
	JPanel fu=new JPanel();
	JPanel fu1=new JPanel();
	panel.add(fu1,BorderLayout.NORTH);
	panel.add(fu,BorderLayout.EAST);
	fu1.setLayout(new GridLayout(2,4,10,10));
	fu.setLayout(new GridLayout(4,1,10,10));
	fu.setPreferredSize(new Dimension(90,0));//在东，设置宽度不为零
	fu1.setPreferredSize(new Dimension(0,120));//在北，设置高度不为零
	ActionListener actionlistener=new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String s=arg0.getActionCommand();
			Control.getinstance().press(s);
		}
	};
	for(int i=1;i<10;i++){
		Button b1=new Button(""+i);
		b1.setBackground(Color.gray);///按钮的背景颜色
		b1.setFont(myfont);//按钮的字体以及加粗以及大小
		b1.setForeground(Color.WHITE);//按钮的字体颜色
		b1.setActionCommand(""+i);
		b1.addActionListener(actionlistener);
		nump.add(b1);
		}
	{
		String a[]={"+/-","0","."};
		for(int i=0;i<3;i++)
		{Button b1=new Button(a[i]);
		b1.setBackground(Color.WHITE);
		b1.setFont(myfont1);
		b1.setForeground(Color.BLACK);
		b1.setActionCommand(a[i]);
		b1.addActionListener(actionlistener);
			nump.add(b1);}
	
	}
	
	{
		String c[]={"+","-","×","÷"};
		String d[]={"%","CE","C","=","1/x","x^2","√x","x!"};
		for(int i=0;i<4;i++){
			Button b1=new Button(c[i]);
			b1.setBackground(Color.WHITE);
			b1.setFont(myfont3);
			b1.setForeground(Color.ORANGE);
			b1.setActionCommand(c[i]);
			b1.addActionListener(actionlistener);
			fu.add(b1);
		}
		for(int i=0;i<8;i++){
			Button b1=new Button(d[i]);
			b1.setBackground(Color.DARK_GRAY);
			b1.setFont(myfont4);
			b1.setForeground(Color.WHITE);
			b1.setActionCommand(d[i]);
			b1.addActionListener(actionlistener);
			fu1.add(b1);
		}
	}
	frame.setSize(400, 500);
	frame.setVisible(true);
}

}
