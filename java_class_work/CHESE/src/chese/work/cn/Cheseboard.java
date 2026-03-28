package chese.work.cn;

import java.awt.Graphics;

import javax.swing.JPanel;

public class Cheseboard extends JPanel{
	public static void main(String[] args) {
		public void paintComponent(Graphics a){
			super.paintComponent(a);
			setOpaque(true);
			for(int i=50;i<=1000;i+=50){
				a.drawLine(50,i ,800 , i);
				a.drawLine(i,50 ,i , 800);
			}}
	}
	
	
}
