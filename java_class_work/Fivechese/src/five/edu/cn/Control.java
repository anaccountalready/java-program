package five.edu.cn;

import javax.swing.JOptionPane;

public class Control {
private static Control instance=new Control();
private Control(){
}
public static Control getInstance(){
	return instance;
}
private int localColor=Model.Black;
private boolean netMode=false;
private boolean allowPutChess=true;
private int otherColor=Model.white;
public int getLocalColor() {
	return localColor;
}
public void setLocalColor(int localColor) {
	this.localColor = localColor;
}
public int getOtherColor() {
	return otherColor;
}
public void setOtherColor(int otherColor) {
	this.otherColor = otherColor;
}
public void setMode(){
	Object[] MOde={"本地模式","网络对战"};
	int x=JOptionPane.showOptionDialog(null, "请选择游戏模式", "游戏模式", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, MOde, MOde[0]);
	if(x==0)netMode=false;
	else netMode=true;
}
public void setColor(){
	if(!netMode){setLocalColor();}
	else{
		netmodesetcolor();
	}
}
private void setLocalColor() {
		Object[] MOde={"黑棋","白棋"};
		int x=JOptionPane.showOptionDialog(null, "请选择你的棋子颜色", "选择棋子颜色", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, MOde, MOde[0]);
		if(x==0){
			localColor=Model.Black;
		}else {
			localColor=Model.white;
		}
	}
private void netmodesetcolor() {
		Object[] MOde={"黑棋","白棋"};
		int x=JOptionPane.showOptionDialog(null, "主机玩家将优先选择棋子颜色，请选择你的棋子颜色", "选择棋子颜色", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, MOde, MOde[0]);
		if(x==0){
			localColor=Model.Black;
		}else {
			localColor=Model.white;
		}
	    otherColor=-localColor;
	}
public boolean isAllowPutChess() {
	return allowPutChess;
}
public void setAllowPutChess(boolean allowPutChess) {
	this.allowPutChess = allowPutChess;
}
public boolean isNetMode() {
	return netMode;
}
public void setNetMode(boolean netMode) {
	this.netMode = netMode;
}
public void localPutChess(int row,int col){
	if(!netMode){
	localModePutChess(row, col);}
	else{
		netModePutChess(row,col);
	}
}
public void localremoveChess(){
	if(Model.getInstance().getList().size() < 2) {
		JOptionPane.showMessageDialog(null, "没有足够的棋子可以悔棋");
		return;
	}
	if(!netMode){
		Model.getInstance().back();
		ChessPanel.getInstance().repaint();
	}
	else{
		netModeremoveChess();
	}
}
public void netOtherPutChess(int row,int col){
	boolean success=Model.getInstance().putChess(row, col, otherColor);
	if(success){
		ChessPanel.getInstance().repaint();
		allowPutChess=true;
		int winner=Model.getInstance().judge();
		if(winner==-1){
			JOptionPane.showMessageDialog(null, "黑方获胜");
		}
		else if(winner==1){
			JOptionPane.showMessageDialog(null, "白方获胜");
		}
	}
}
private void netModePutChess(int row, int col) {
		if(!allowPutChess)return;
		boolean success=Model.getInstance().putChess(row, col, localColor);
		if(success){
			ChessPanel.getInstance().repaint();
			NetHelper.getInstance().sentChess(row, col);
			allowPutChess=false;
			int winner=Model.getInstance().judge();
			if(winner==-1){
				JOptionPane.showMessageDialog(null, "黑方获胜");
			}
			else if(winner==1){
				JOptionPane.showMessageDialog(null, "白方获胜");
			}
		}
	}
private void localModePutChess(int row, int col) {
		boolean success=Model.getInstance().putChess(row, col, localColor);
		if(success){
			ChessPanel.getInstance().repaint();
			localColor=-localColor;
			int winner=Model.getInstance().judge();
			if(winner==-1){
				JOptionPane.showMessageDialog(null, "黑方获胜");
			}
			else if(winner==1){
				JOptionPane.showMessageDialog(null, "白方获胜");
			}
		}
	}
public void beginlisten() {
	// TODO Auto-generated method stub
	NetHelper.getInstance().beginListen();
}
public void connect(String ip) {
	// TODO Auto-generated method stub
	allowPutChess=false;
	NetHelper.getInstance().connect(ip);
}
public void netModeremoveChess(){
	if(Model.getInstance().getList().isEmpty() || Model.getInstance().getList().get(Model.getInstance().getList().size()-1).color==otherColor){
		Model.getInstance().back();
		ChessPanel.getInstance().repaint();
		NetHelper.getInstance().sentbackmsg();
	}
	else {
		JOptionPane.showMessageDialog(null, "等待对方下棋后才能悔棋");
	}
}
public void netotherremoveChess() {
	// TODO Auto-generated method stub
	Model.getInstance().back();
	allowPutChess=false;
	ChessPanel.getInstance().repaint();
}
public void netOthershowmsg(String line){
	Chatpanl.getInstance().appendMessage("对方说:"+line);
}


}
