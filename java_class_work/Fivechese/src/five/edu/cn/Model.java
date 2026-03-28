package five.edu.cn;

import java.util.ArrayList;
import java.util.LinkedList;

public class Model {
	// Enum singleton implementation, thread-safe and prevents reflection attacks
	public enum SingletonHolder {
		INSTANCE;
		private final Model instance;
		
		SingletonHolder() {
			instance = new Model();
		}
		
		public Model getInstance() {
			return instance;
		}
	}
	
	// Private constructor to prevent external instantiation
	private Model(){}
	
	public static Model getInstance(){
		return SingletonHolder.INSTANCE.getInstance();
	}
public static final int white=1;
public static final int Black=-1;
public static final int Space=0;
public static final int width=19;
private int [][]data=new int [width][width];
private static LinkedList<Chess>list=new LinkedList<Chess>();

// Provide an unmodifiable list view to prevent external modification
public LinkedList<Chess> getList() {
	return new LinkedList<>(list);
}

// Provide dedicated methods to add and remove chess pieces to ensure data consistency
public void addChess(Chess chess) {
	list.add(chess);
}

public void removeLastChess() {
	if (!list.isEmpty()) {
		list.removeLast();
	}
}

public void clearChessList() {
	list.clear();
}

public int[][] getData() {
	// 返回数组的副本，防止外部直接修改内部数据
	int[][] dataCopy = new int[width][width];
	for (int i = 0; i < width; i++) {
		System.arraycopy(data[i], 0, dataCopy[i], 0, width);
	}
	return dataCopy;
}
private int lastrow;
private int lastcol;
public boolean putChess(int row,int col,int color){
	// Add input validation
	if(row<0||row>=width||col<0||col>=width||data[row][col]!=Space) {
		return false;
	} else {
		data[row][col]=color;
		addChess(new Chess(row,col,color));
		lastrow=row;
		lastcol=col;
		return true;
	}
}
public int getChess(int row,int col){
if(row>=0&&row<width&&col>=0&&col<width)return data[row][col];
	else return Space;
}
public int judge(){
	int m=data[lastrow][lastcol];
	int num=1;
	//ͬһ��
	for(int i=lastcol+1;i<width;i++){
		if(data[lastrow][i]==m){num++;
	if(num==5)return m;
	}
		else break;	
}
	for(int i=lastcol-1;i>=0;i--){
		if(data[lastrow][i]==m){num++;
		if(num==5)return m;}
		else break;
	}
	//samecol
	num=1;
	for(int i=lastrow+1;i<width;i++){
		if(data[i][lastcol]==m){num++;
		if(num==5)return m;}
		else break;
	}
	for(int i=lastrow-1;i>=0;i--){
		if(data[i][lastcol]==m){num++;
		if(num==5)return m;}
		else break;
	}
	//leftб��
	num=1;
	for(int i=lastrow-1, j=lastcol-1;i>=0&&j>=0;i--,j--){
		if(data[i][j]==m){num++;
		if(num==5)return m;}
		else break;
	}
	for(int i=lastrow+1, j=lastcol+1;i<width&&j<width;i++,j++){
		if(data[i][j]==m){num++;
		if(num==5)return m;}
		else break;
	}
	//��б��
	num=1;
	for(int i=lastrow-1, j=lastcol+1;i>=0&&j<width;i--,j++){
		if(data[i][j]==m){num++;
		if(num==5)return m;}
		else break;
	}
	for(int i=lastrow+1, j=lastcol-1;i<width&&j>=0;i++,j--){
		if(data[i][j]==m){num++;
		if(num==5)return m;}
		else break;
	}
	return Space;//û��Ӯ
}
public void clearchess(){
	list=new LinkedList<>();
	data=new int [width][width];
	ChessPanel.getInstance().repaint();
}
public void back(){
	if (list.size() >= 2) {
		Chess lastChess = list.getLast();
		data[lastChess.getRow()][lastChess.getCol()] = Space;
		removeLastChess();
		
		Chess secondLastChess = list.getLast();
		data[secondLastChess.getRow()][secondLastChess.getCol()] = Space;
		removeLastChess();
	}
}
}
