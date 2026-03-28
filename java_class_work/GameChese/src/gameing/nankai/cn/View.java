package gameing.nankai.cn;
public class View {
	public static void showstart(){
		System.out.println("please input the position: ");
	}
	//œ‘ æ∆Â≈Ã
	public static void showboard(){
		for(int i=0;i<15;i++)
			{for(int j=0;j<15;j++)
				System.out.print(Model.singleinstance().getboard(i, j));
		System.out.print('\n');}
	}
	//œ‘ æ”Æº“
	public static void showend(int l,int r){
		if(Model.winner(l,r)=='°')System.out.println("The White Chese Wins!!!");
		else if(Model.winner(l,r)=='°Ò') System.out.println("The Black Chese Wins!!!");
		else return;
	}
}
