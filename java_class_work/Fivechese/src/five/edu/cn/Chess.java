package five.edu.cn;

public class Chess {
	private final int color;
	private final int row;
	private final int col;
	
	public Chess(){}
	public Chess(int row,int col,int color){
		this.row=row;
		this.col=col;
		this.color=color;
	}
	
	// 提供getter方法，不提供setter以保持对象不可变
	public int getColor() {
		return color;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
@Override
public String toString() {
	return "Chess [color=" + color + ", row=" + row + ", col=" + col + "]";
}
}
