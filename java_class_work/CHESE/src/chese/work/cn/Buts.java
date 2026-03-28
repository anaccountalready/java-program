package chese.work.cn;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Buts extends JPanel {
Buts(){
	JButton startb=new JButton("开始游戏");
	add(startb);
	startb.setPreferredSize(new Dimension(100,50));
	startb.addMouseListener(new MouseListener() {	
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			JOptionPane initialop=new JOptionPane();
			
		}
	});
	JButton endb=new JButton("退出游戏");
	add(endb);
	endb.setPreferredSize(new Dimension(100,50));
	endb.addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			int choice=JOptionPane.showConfirmDialog(MainDemo.f, "是否退出游戏","确认退出",JOptionPane.OK_CANCEL_OPTION);
			if(choice==JOptionPane.OK_OPTION){
				System.exit(0);
			}
		}
	});

}
}
