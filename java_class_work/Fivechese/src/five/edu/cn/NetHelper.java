package five.edu.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class NetHelper {
public static final int PORT=8900;
public static final int MAX_MESSAGE_LENGTH = 1000;
private Socket s;
private BufferedReader reader;
private PrintWriter out;
private ServerSocket serverSocket;
private volatile boolean isRunning = false;
private static final Pattern CHESS_PATTERN = Pattern.compile("^PutChess:\\d+,\\d+$");
private static final Pattern CHAT_PATTERN = Pattern.compile("^chat.*$");
private static final Pattern REBACK_PATTERN = Pattern.compile("^reback$");

private static NetHelper instance=new NetHelper();
private NetHelper(){}
public static NetHelper getInstance(){
	return instance;
}

public void beginListen() {
	new Thread(){
		public void run(){
		try {
				serverSocket = new ServerSocket(PORT);
				 s=serverSocket.accept();
				reader=new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
					out=new PrintWriter(new java.io.OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
				isRunning = true;
				startReadThread();
				}
			catch (IOException e) {
				e.printStackTrace();
				showError("监听失败: " + e.getMessage());
			}
			
		}

	}.start();
	}

protected void startReadThread() {
	new Thread(){
		public void run(){
		while(isRunning){
			try {
				String line = reader.readLine();
				if (line == null) {
					showError("连接已断开");
					break;
				}
				if(CHESS_PATTERN.matcher(line).matches()){
					parseChess(line);
				}
				else if(CHAT_PATTERN.matcher(line).matches()){
					parseChat(line);
				}
				else if(REBACK_PATTERN.matcher(line).matches()){
					Control.getInstance().netotherremoveChess();
				}
				
			} catch (IOException e) {
				if (isRunning) {
					e.printStackTrace();
					showError("读取数据失败: " + e.getMessage());
				}
				break;
			}
			
		}
		closeResources();
	}
}.start();
}

private void showError(final String message) {
	SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			JOptionPane.showMessageDialog(null, message, "网络错误", JOptionPane.ERROR_MESSAGE);
		}
	});
}

protected void parseChess(String line) {
	try {
		line=line.substring(9);
		String []array=line.split(",");
		if (array.length != 2) {
			throw new IllegalArgumentException("无效的棋子数据格式");
		}
		int row=Integer.parseInt(array[0]);
		int col=Integer.parseInt(array[1]);
		if (row < 0 || row >= Model.width || col < 0 || col >= Model.width) {
			throw new IllegalArgumentException("棋子位置超出范围");
		}
		Control.getInstance().netOtherPutChess(row, col);
	} catch (Exception e) {
		e.printStackTrace();
		showError("解析棋子数据失败: " + e.getMessage());
	}
}

protected void parseChat(String line) {
	try {
		if (line.length() < 4) {
			throw new IllegalArgumentException("聊天消息格式错误");
		}
		line=line.substring(4);
		if (line.length() > MAX_MESSAGE_LENGTH) {
			line = line.substring(0, MAX_MESSAGE_LENGTH);
		}
		Control.getInstance().netOthershowmsg(filterMessage(line));
	} catch (Exception e) {
		e.printStackTrace();
		showError("解析聊天消息失败: " + e.getMessage());
	}
}

private String filterMessage(String message) {
	if (message == null) {
		return "";
	}
	message = message.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	message = message.replaceAll("&", "&amp;");
	return message;
}

public void sentChess(final int row,final int col){
	if (!isConnected()) {
		showError("未连接到服务器");
		return;
	}
	if (row < 0 || row >= Model.width || col < 0 || col >= Model.width) {
		showError("棋子位置超出范围");
		return;
	}
	new Thread(){
		public void run(){
			try {
				out.println("PutChess:"+row+","+col);
			} catch (Exception e) {
				e.printStackTrace();
				showError("发送棋子数据失败: " + e.getMessage());
			}
		}
	}.start();
}

public void connect(String ip) {
	if (ip == null || ip.trim().isEmpty()) {
		showError("IP地址不能为空");
		return;
	}
	try {
		closeResources();
		s=new Socket(ip,PORT);
		reader=new BufferedReader(new InputStreamReader(s.getInputStream(), StandardCharsets.UTF_8));
		out=new PrintWriter(new java.io.OutputStreamWriter(s.getOutputStream(), StandardCharsets.UTF_8), true);
		isRunning = true;
		startReadThread();
		JOptionPane.showMessageDialog(null, "连接成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
	} catch (UnknownHostException e) {
		showError("未知主机: " + e.getMessage());
	} catch (IOException e) {
		showError("连接失败: " + e.getMessage());
	}
}

public void sentbackmsg() {
	if (!isConnected()) {
		showError("未连接到服务器");
		return;
	}
	new Thread(){
		public void run(){
			try {
				out.println("reback");
			} catch (Exception e) {
				e.printStackTrace();
				showError("发送悔棋消息失败: " + e.getMessage());
			}
		}
	}.start();
}

public void setChat(final String text) {
	if (!isConnected()) {
		showError("未连接到服务器");
		return;
	}
	if (text == null || text.trim().isEmpty()) {
		showError("消息不能为空");
		return;
	}
	if (text.length() > MAX_MESSAGE_LENGTH) {
		showError("消息长度不能超过" + MAX_MESSAGE_LENGTH + "字符");
		return;
	}
	new Thread(){
		public void run(){
			try {
				out.println("chat" + text);
			} catch (Exception e) {
				e.printStackTrace();
				showError("发送聊天消息失败: " + e.getMessage());
			}
		}
	}.start();
}

public boolean isConnected() {
	return s != null && s.isConnected() && !s.isClosed();
}

public void closeResources() {
	isRunning = false;
	try {
		if (reader != null) {
			reader.close();
		}
		if (out != null) {
			out.close();
		}
		if (s != null && !s.isClosed()) {
			s.close();
		}
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
	} catch (IOException e) {
		e.printStackTrace();
	}
	reader = null;
	out = null;
	s = null;
	serverSocket = null;
}

}