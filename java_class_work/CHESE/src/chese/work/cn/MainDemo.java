package chese.work.cn;

import java.awt.BorderLayout;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainDemo {
	//chessboard
	static int[][]chess=new int[15][15];
	//对方ip和客户端ip
	static String userIP;
	static InetAddress clientaddress;
	static int ClientPort;
	static int ServerPort;
	static boolean danji;
	static boolean start=false;
	//棋子的坐标
	static int x;
	static int y;
	//传送的坐标
	static int X;
	static int Y;
	static JFrame f=new JFrame("Five Chess");
	public static void main(String []args){
		f.setSize(1200, 1200);
		f.setLayout(new BorderLayout(10,10));
		f.add(new Cheseboard(),BorderLayout.WEST);
		f.add(new Buts(),BorderLayout.SOUTH);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int choice=JOptionPane.showConfirmDialog(MainDemo.f, "是否选择联网","CHOICES",JOptionPane.YES_OPTION,JOptionPane.WARNING_MESSAGE);
		if(choice==JOptionPane.YES_OPTION){
			try{
				MainDemo.userIP=JOptionPane.showInputDialog(MainDemo.f,"请输入对方的IP","输入",JOptionPane.WARNING_MESSAGE).trim();
				MainDemo.ServerPort=Integer.parseInt(JOptionPane.showInputDialog(MainDemo.f,"请输入您的端口号","端口号",JOptionPane.WARNING_MESSAGE).trim());
			}catch(Exception e){//输出错误重新输入
				JOptionPane.showMessageDialog(MainDemo.f, "输入错误", "错误", JOptionPane.ERROR_MESSAGE);
				int choice1=JOptionPane.showConfirmDialog(MainDemo.f, "是否退出游戏","确认退出",JOptionPane.OK_CANCEL_OPTION);
				if(choice1==JOptionPane.OK_OPTION){
					System.exit(0);
					e.printStackTrace();
				}
				else{MainDemo.userIP=JOptionPane.showInputDialog(MainDemo.f,"请输入对方的IP","输入",JOptionPane.WARNING_MESSAGE).trim();
				MainDemo.ServerPort=Integer.parseInt(JOptionPane.showInputDialog(MainDemo.f,"请输入您的端口号","端口号",JOptionPane.WARNING_MESSAGE).trim());}
			}
			try {
				MainDemo.clientaddress =InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			
			if(MainDemo.clientaddress!=null){
				JOptionPane.showMessageDialog(MainDemo.f, "开始游戏","游戏马上开始",JOptionPane.WARNING_MESSAGE);
				MainDemo.start=true;
				if(MainDemo.ClientPort==MainDemo.ServerPort){
					MainDemo.danji=true;
				}
				else MainDemo.danji=false;
			}
			MainDemo.run1();
		}
		else if(choice==JOptionPane.NO_OPTION||choice==JOptionPane.CLOSED_OPTION){
	JOptionPane.showMessageDialog(MainDemo.f, "将开始单机模式","单机模式",JOptionPane.INFORMATION_MESSAGE);
	try{
		MainDemo.clientaddress=InetAddress.getLocalHost();//本地IP
		MainDemo.ClientPort=MainDemo.ServerPort=7888;
		MainDemo.start=true;
		MainDemo.danji=false;
	}
	catch(UnknownHostException e){
		JOptionPane.showMessageDialog(MainDemo.f, "ERROR!!!","ERROR",JOptionPane.ERROR_MESSAGE);
		System.exit(0);
		e.printStackTrace();
	}
		}
	}
	public static void run1() {
		// TODO Auto-generated method stub
		
	}
	

}
