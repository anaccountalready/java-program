package five.edu.cn;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class Main {
	
public static void main(String a[]){
	JFrame f=new JFrame("五子棋");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	f.getContentPane().setLayout(new BorderLayout());
	f.getContentPane().add(ChessPanel.getInstance(),BorderLayout.CENTER);
	f.getContentPane().add(NetPanel.getInstance(),BorderLayout.NORTH);
	f.getContentPane().add(Chatpanl.getInstance(),BorderLayout.EAST);
	f.setSize(800,600);
	f.setVisible(true);
new Thread(){public void run(){
	new BackGroundMusic();
	BackGroundMusic.main(null);
}}.start();	


}

}
