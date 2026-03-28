package gameing.nankai.cn;

public class Control {
	//改变颜色
		static public char color='○';
		static void  changecolor(){
			if(color=='○')color='●';
			else color='○' ;
		}
		static public void report(int i,int j){
			//下棋成功
			if(Model.singleinstance().putchese(i,j)){
				if(Model.sum>=9){//判断赢家是否出现
				if(Model.winner(i,j)=='○'||Model.winner(i,j)=='●'){View.showend(i,j);View.showboard();}
				else;}
				else;
				Control.changecolor();
				View.showboard();
			}
			//下棋不成功
			else{
				View.showboard();
			}
		}
}
