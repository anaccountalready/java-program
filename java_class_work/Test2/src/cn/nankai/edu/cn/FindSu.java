package cn.nankai.edu.cn;
public class FindSu extends Thread{
	int x;int y;int cout=0;
	FindSu(int x,int y){
		this.x=x;
		this.y=y;
	}
	public boolean ifsu(int m){
		if(m==1)return false;
		else{
			for(int i=2;i<m;i++){
				if(m%i==0)return false;
			}
		return true;}
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		for(int j=x;j<=y;j++){
    	if(ifsu(j)){
    		cout++;
    		System.out.println(Thread.currentThread().getName()+":"+j);
    }
		}
		System.out.println(Thread.currentThread().getName()+"cout="+cout);
	}
	@Override
	public  void start() {
		// TODO Auto-generated method stub
		super.start();
	}
public static void main(String arg[]){
	new FindSu(1, 1000).start();
	new FindSu(1001, 2000).start();
	new FindSu(2001, 3000).start();
}
}
