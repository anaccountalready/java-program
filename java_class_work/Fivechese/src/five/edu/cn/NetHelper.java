package five.edu.cn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class NetHelper {
public static final int PORT=8900;
private Socket s;
private BufferedReader reader;
private PrintWriter out;
private static NetHelper instance=new NetHelper();
private NetHelper(){}
public static NetHelper getInstance(){
	return instance;
}
public void beginListen() {
	new Thread(){
		public void run(){
		try {
				ServerSocket ss = new ServerSocket(PORT);
				 s=ss.accept();
				reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
				//writer=new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				out=new PrintWriter(s.getOutputStream(),true);
				startReadThread();
				}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

	}.start();
	}

protected void startReadThread() {
	// TODO Auto-generated method stub
	new Thread(){
		public void run(){
		while(true){
			try {
				String line;
				line = reader.readLine();
				if(line.startsWith("PutChess")){
					parseChess(line);
				}
				else if(line.startsWith("chat")){
					parseChat(line);
				}
				else if(line.startsWith("reback")){
					Control.getInstance().netotherremoveChess();
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
}.start();
}

protected void parseChess(String line) {
	// TODO Auto-generated method stub
	line=line.substring(9);
	String []array=line.split(",");
	int row=Integer.parseInt(array[0]);
	int col=Integer.parseInt(array[1]);
	Control.getInstance().netOtherPutChess(row, col);
}
protected void parseChat(String line) {
	// TODO Auto-generated method stub
	line=line.substring(4);
	Control.getInstance().netOthershowmsg(line);
}
public void sentChess(final int row,final int col){
	new Thread(){
		public void run(){
			out.println("PutChess:"+row+","+col);
		}
	}.start();
}
public void connect(String ip) {
	// TODO Auto-generated method stub
	try {
		s=new Socket(ip,PORT);
		reader=new BufferedReader(new InputStreamReader(s.getInputStream()));
		out=new PrintWriter(s.getOutputStream(),true);
		startReadThread();
	} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public void sentbackmsg() {
	// TODO Auto-generated method stub
	new Thread(){
		public void run(){
			out.println("reback");
		}
	}.start();
}
public void setChat(final String text) {
	// TODO Auto-generated method stub
	new Thread(){
		public void run(){
			out.println("chat"+text);
		}
	}.start();
}

}