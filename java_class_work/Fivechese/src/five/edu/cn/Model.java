package five.edu.cn;

import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

public class Model {
private static Model instance=new Model();
private Model(){}
public static Model getInstance(){
	return instance;
}
public static final int white=1;
public static final int Black=-1;
public static final int Space=0;
public static final int width=19;
private int [][]data=new int [width][width];
private LinkedList<Chess>list=new LinkedList<Chess>();
public int[][] getData() {
	int[][] copy = new int[width][width];
	for(int i = 0; i < width; i++) {
		System.arraycopy(data[i], 0, copy[i], 0, width);
	}
	return copy;
}
public List<Chess> getList() {
	return Collections.unmodifiableList(list);
}
private int lastrow;
private int lastcol;
public synchronized boolean putChess(int row,int col,int color){
	if(row>=width||row<0||col<0||col>=width||data[row][col]!=Space)return false;
	else {
		data[row][col]=color;
		list.add(new Chess(row,col,color));
		lastrow=row;
		lastcol=col;
		return true;
	}
}
public synchronized int getChess(int row,int col){
if(row>=0&&row<width&&col>=0&&col<width)return data[row][col];
	else return Space;
}
public synchronized int judge(){
	if(list.isEmpty()) return Space;
	int m=data[lastrow][lastcol];
	int num=1;
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
	return Space;
}
public synchronized void clearchess(){
	list=new LinkedList<>();
	data=new int [width][width];
	ChessPanel.getInstance().repaint();
}
public synchronized void back(){
	if(list.size() < 2) {
		return;
	}
	int row1=list.getLast().row;
	int col1=list.getLast().col;
	data[row1][col1]=Space;
	list.removeLast();
	int row=list.getLast().row;
	int col=list.getLast().col;
	data[row][col]=Space;
	list.removeLast();
}
}
