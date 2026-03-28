package five.edu.cn;

import java.util.ArrayList;
import java.util.LinkedList;

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
public static LinkedList<Chess>list=new LinkedList<Chess>();
public int[][] getData() {
	return data;
}
public LinkedList<Chess> getList() {
	return list;
}
private int lastrow;
private int lastcol;
public boolean putChess(int row,int col,int color){
	if(row>width||row<0||col<0||col>width||data[row][col]!=Space)return false;
	else {
		data[row][col]=color;
		list.add(new Chess(row,col,color));
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
	//同一行
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
	for(int i=lastrow-1;i>0;i--){
		if(data[i][lastcol]==m){num++;
		if(num==5)return m;}
		else break;
	}
	//left斜线
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
	//右斜线
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
	return Space;//没人赢
}
public void clearchess(){
	list=new LinkedList<>();
	data=new int [width][width];
	ChessPanel.getInstance().repaint();
}
public void back(){
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
