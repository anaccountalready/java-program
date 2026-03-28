package five.edu.cn;

public class Chess {
int color;
int row;
int col;
Chess(){}
Chess(int row,int col,int color){
	this.row=row;
	this.col=col;
	this.color=color;
}
@Override
public String toString() {
	return "Chess [color=" + color + ", row=" + row + ", col=" + col + "]";
}
}
