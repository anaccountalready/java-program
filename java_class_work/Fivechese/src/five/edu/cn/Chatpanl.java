package five.edu.cn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Chatpanl extends JPanel {
private static Chatpanl instance=new Chatpanl();
private JScrollPane scrollpane=new JScrollPane();
private JButton snebt=new JButton("����");
private JTextField writeboard=new JTextField();
public JTextArea readboard=new JTextArea();
private JPanel pane1=new JPanel();
private JPanel pane2=new JPanel();
private JPanel pane3=new JPanel();
Image imageIcon=new ImageIcon("painting/R-C.jpg").getImage();
private Chatpanl() {
	addComponentListener(new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent arg0) {
			int width=getParent().getWidth();
			int height=getParent().getHeight();
			snebt.setMinimumSize(new Dimension(5,5));
			writeboard.setMinimumSize(new Dimension(100,20));
			scrollpane.setMinimumSize(new Dimension(200,5));
			scrollpane.setPreferredSize(new Dimension(width/3,height/2));
			readboard.setPreferredSize(new Dimension(width/3,height/2));
	        writeboard.setPreferredSize(new Dimension(width/3,height/4));
		   
		}
	});
	new Thread(){
		public void run(){
			snebt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					NetHelper.getInstance().setChat(writeboard.getText());
				}
			});
		}
	}.start();
	setLayout(new BorderLayout());
	pane1.add(new JLabel("��Ϣ��ʾ��"));
    scrollpane.setViewportView(readboard);
    pane1.add(scrollpane);
	pane2.add(new JLabel("���������"));
	pane2.add(writeboard,BorderLayout.WEST);
	pane2.add(snebt,BorderLayout.EAST);
	add(pane2,BorderLayout.SOUTH);
	add(pane1,BorderLayout.NORTH);
	repaint();
}
public static Chatpanl getInstance(){
	return instance;
}
protected void paintComponent(Graphics g) {
	  super.paintComponent(g);
	
	g.drawImage(imageIcon, 0, 0, this.getWidth(), this.getHeight(), this);
	 
	}
}
