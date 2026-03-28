package cn.edu.nankai.cn;



import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;



public class ServerUI {
	private JTextArea chatArea;//聊天框
	
   private JTextField messageField;
   
   private Server server;
   
   private JTextField portField;
	public ServerUI() {
		JFrame frame = new JFrame("服务器");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setSize(400, 500);
		frame.setLayout(new BorderLayout());
        chatArea = new JTextArea();
        chatArea.setFont(new Font("微软雅黑",Font.BOLD,30));
        frame.add(chatArea,BorderLayout.CENTER);
        
        JPanel jPanel = new JPanel();
        messageField = new JTextField();
        messageField.setColumns(20);
        messageField.setFont(new Font("微软雅黑",Font.BOLD,15));
        JButton sentBtn = new JButton("发送");
       
        sentBtn.addActionListener((e)->{
        	
        	String msg = messageField.getText();
        	if(msg.equals("")) {
        		
        	}
        	else{
        	chatArea.append("服务端: "+msg+'\n');
        	server.sendMessage(msg);
        	messageField.setText("");
        	}
        });
        jPanel.add(messageField);
        jPanel.add(sentBtn);
        frame.add(jPanel,BorderLayout.SOUTH);
        
        JPanel northPan = new JPanel();
        northPan.add(new JLabel("监听端口"));
        portField = new JTextField();
        portField.setColumns(20);
        JButton startBtn = new JButton("监听");
        startBtn.addActionListener((e)->{
        	startServer();
        });
        
        northPan.add(portField);
        northPan.add(startBtn);
        frame.add(northPan,BorderLayout.NORTH);
        
		frame.setVisible(true);
	}
	public void startServer() {
		if(portField.getText().equals("")) {
			portField.setText("监听失败，请重试");
			return ;
		}
		int port =Integer.parseInt(portField.getText());
		server = new Server(port,(msg)-> {
			//处理接受到的消息
			chatArea.append("客户端: "+msg+'\n');
		});
	}

	public static void main(String[] args) {
		new ServerUI();
	}
}


