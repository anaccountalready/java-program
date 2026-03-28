package cn.nankai.edu.cn;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.*;

public class M1 {
	JTextField textfield=new JTextField();
public static void main(String []args)
{
	new M1().begin();

}

private void begin() {
	// TODO Auto-generated method stub
	JFrame frame=new JFrame("¼ÆËãÆ÷");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(new BorderLayout());
	frame.getContentPane().add(textfield,BorderLayout.NORTH);
	JPanel panel =new JPanel();
	panel.setLayout(new GridLayout(6,4));
	frame.getContentPane().add(panel,BorderLayout.CENTER);
	for(int i=0;i<24;i++){
		JButton button=new JButton(""+i);
		panel.add(button);
	}
	frame.setSize(400, 500);
	frame.setVisible(true);
}
}
