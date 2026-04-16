package five.edu.cn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ChessPanel extends JPanel{
	private int gap=50;
	private int unit=10;
	private int lx=10;
	private int ly=10;
	Image imageIcon=new ImageIcon("painting/view.jpg").getImage();
private static ChessPanel instance=new ChessPanel();
private ChessPanel(){
	
	JButton backbt=new JButton("悔棋");
	add(backbt);
	backbt.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Control.getInstance().localremoveChess();
			repaint();
		}
	});
	JButton startbt=new JButton("重新开始游戏");
	add(startbt);
    startbt.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int choice = JOptionPane.showConfirmDialog(null,"是否重新开始游戏","重新开始游戏",JOptionPane.OK_CANCEL_OPTION);
			if(choice == JOptionPane.OK_OPTION) {
				Model.getInstance().clearchess();
				repaint();
			Control.getInstance().setMode();
			Control.getInstance().setColor();
			}
		}
	});
    JButton startbt1=new JButton("开始游戏");
    add(startbt1);
    startbt1.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Model.getInstance().clearchess();
			repaint();
			Control.getInstance().setMode();
			Control.getInstance().setColor();
			
		}
	});
	JButton endbt=new JButton("退出游戏");
	add(endbt);
	endbt.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int choice = JOptionPane.showConfirmDialog(null,"是否退出游戏","退出游戏",JOptionPane.OK_CANCEL_OPTION);
			if(choice == JOptionPane.OK_OPTION) {
				System.exit(0);
			}
					
		}
	});
	JButton fuPan=new JButton("复盘");
	add(fuPan);
	fuPan.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			final LinkedList<Chess> fupan=new LinkedList<Chess>();
			for(Chess chess:Model.list){
				fupan.add(chess);
				
			}
			Model.getInstance().clearchess();
			repaint();
			new Thread(){
				public void run(){
					for(int i=0;i<fupan.size();i++){
						Model.list.add(fupan.get(i));
						repaint();
						try {
							sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}.start();
		
		}
	});
	addComponentListener(new ComponentAdapter() {
		@Override
		public void componentResized(ComponentEvent arg0) {
			int width=getWidth();
			int height=getHeight();
			int min=width<height?width:height;
			unit=(min-2*gap)/(Model.width-1);
			lx=(width-18*unit)/2;
			ly=(height-18*unit)/2;
		    repaint();
		}
	});


addMouseListener(new MouseAdapter() {
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		int col;int row;
		int colmod=(e.getX()-lx)%unit;
		int rowmod=(e.getY()-ly)%unit;
		if(colmod>unit/2){col=((e.getX()-lx)/unit)+1;}
		else col=(e.getX()-lx)/unit;
		if(rowmod>unit/2){row=((e.getY()-ly)/unit)+1;}
		else row=(e.getY()-ly)/unit;
		Control.getInstance().localPutChess(row, col);
	}
});}
public static ChessPanel getInstance(){return instance;}
protected void paintComponent(Graphics g) {
  super.paintComponent(g);
g.drawImage(imageIcon, 0, 0, this.getWidth(), this.getHeight(), this);
  drawPanel(g);
  drawchess(g);
}
private void drawchess(Graphics g) {
	// TODO Auto-generated method stub
	Model m=Model.getInstance();
	for(Chess n:m.getList()){
	if(n.color==-1){
		g.setColor(Color.BLACK);
		g.fillOval(lx+n.col*unit-unit/2, ly+n.row*unit-unit/2, unit, unit);
	}
	else if(n.color==1){
		g.setColor(Color.WHITE);
		g.fillOval(lx+n.col*unit-unit/2, ly+n.row*unit-unit/2, unit, unit);
	}
	else if(n.color==0){};
	}
	
}
private void drawPanel(Graphics g) {
	// TODO Auto-generated method stub
	for(int i=0;i<Model.width;i++){
		g.drawLine(lx, ly+i*unit, lx+unit*(Model.width-1), ly+i*unit);
		g.drawLine(lx+i*unit, ly, lx+i*unit, ly+unit*(Model.width-1));
	}
	
	
}
}
