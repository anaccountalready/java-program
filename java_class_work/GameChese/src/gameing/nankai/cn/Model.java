package gameing.nankai.cn;
public class Model {
	private static char[][]cheseboard;
	private static  Model board;
	
	public static int sum=0;
	   static {cheseboard=new char[15][15];
		for(int i=0;i<15;i++){
			for(int j=0;j<15;j++){
				cheseboard[i][j]='十';}
			}
		}
		private Model(){}
	public static Model singleinstance(){
		if(board==null)return new Model();
		return board;
	}
	//得到棋盘
	public char getboard(int i,int j){
		return cheseboard[i][j]; 
	}
	//判断下棋是否成功并下棋
	public boolean putchese(int x,int y){
		if(x>15||x<0||y>15||y<0){System.out.println("The Potision is over the board!!!");return false;}
		else if(cheseboard[x][y]=='○'||cheseboard[x][y]=='●'){System.out.println("The position has been occupied!!!");return false;}
		else {cheseboard[x][y]=Control.color;sum++;return true;}
	}
	//判断是否连成五子
	public static char winner(int l,int r){
		char result='0';
	int num=1;//记录棋子；
	//同一列
	for(int i=l-1;i>=0;i--){
		if(cheseboard[i][r]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	for(int i=l+1;i<15;i++){
	if(cheseboard[i][r]==Control.color){num++;
	if(num==5)result= Control.color;
	}
	else break;
	}
	num=1;
	//同一行
	for(int i=r-1;i>=0;i--){
		if(cheseboard[l][i]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	for(int i=r+1;i<15;i++){
	if(cheseboard[l][i]==Control.color){num++;
	if(num==5)result= Control.color;
	}
	else break;
	}
	num=1;
	//左斜线
	for(int i=l-1,j=r-1;i>=0&&j>=0;i--,j--){
		if(cheseboard[i][j]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	for(int i=l+1,j=r+1;i<15&&j<15;i++,j++){
		if(cheseboard[i][j]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	num=1;
	//右斜线
	for(int i=l-1,j=r+1;i>=0&&j<15;i--,j++){
		if(cheseboard[i][j]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	for(int i=l+1,j=r-1;i<15&&j>=0;i++,j--){
		if(cheseboard[i][j]==Control.color){num++;
		if(num==5)result= Control.color;
		}
		else break;
		}
	return result;
	}
}
