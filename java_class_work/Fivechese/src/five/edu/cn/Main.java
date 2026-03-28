package five.edu.cn;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class Main {
	
public static void main(String a[]){
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			JFrame f=new JFrame("五子棋");
			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			f.getContentPane().setLayout(new BorderLayout());
			f.getContentPane().add(ChessPanel.getInstance(),BorderLayout.CENTER);
			f.getContentPane().add(NetPanel.getInstance(),BorderLayout.NORTH);
			f.getContentPane().add(Chatpanl.getInstance(),BorderLayout.EAST);
			f.setSize(800,600);
			f.setVisible(true);
			BackGroundMusic.getInstance().play();
		}
	});
}

}
