package five.edu.cn;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NetPanel extends JPanel{
	private JButton listenButton=new JButton("开始监听");
	private JButton connectButton =new JButton("连接服务器");
	private JTextField ipTF=new JTextField(20);
private static NetPanel instance=new NetPanel();
private NetPanel () {
	setLayout(new FlowLayout());
	add(listenButton);
	add(ipTF);
	add(connectButton);
	ipTF.setText("localhost");
	listenButton.addActionListener(new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Control.getInstance().beginlisten();
			listenButton.setEnabled(false);
			connectButton.setEnabled(false);
		}
	});
	connectButton.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String ip=ipTF.getText();
			Control.getInstance().connect(ip);
			listenButton.setEnabled(false);
			connectButton.setEnabled(false);
		}
	});
	
}
public static NetPanel getInstance(){
	return instance;
}

}
