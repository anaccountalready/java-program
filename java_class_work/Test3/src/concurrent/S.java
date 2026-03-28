package concurrent;



import java.io.IOException;
import java.lang.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

class Helper{}
public class S {
	public static S a=new S();
	 private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
private static final ThreadLocal per=new ThreadLocal();

private Helper helper=null;
/*public   Helper getHelper(){
	if(per.get()==null){
		synchronized(this){
			if(helper==null)
				helper=new Helper();
			per.set(per);
		}
	}
	return helper;
	
}*/
public  Helper getHelper(){
	if(helper==null){
		synchronized(this){
			if(helper==null)
				helper=new Helper();
		
		}
	}
	return helper;
	
}
public static void play() throws BrokenBarrierException, InterruptedException {
    System.out.println(Thread.currentThread().getName() + " 已准备");
    cyclicBarrier.await();
    a.getHelper();
    System.out.println(Thread.currentThread().getName() + " 开始执行");
}
public static void main(String args[]){
	 ExecutorService cacheExecutor = Executors.newCachedThreadPool();

	final S a=new S();
	 for (int i=0;i<10;i++){
         try {
             Thread.sleep(1000);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        
         executorService.execute(()->{
             try {
                 play();
             } catch (BrokenBarrierException e) {
                 e.printStackTrace();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         });
         

     }
	 
}
	
}


/**
 * Auth: zhouhongliang
 * Date：2019/8/1
 * 多个线同时运行
 * CyclicBarrier

public class CyclicBarrierDemo {
    private static CyclicBarrier cyclicBarrier = new CyclicBarrier(5);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0;i<10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            executorService.execute(()->{
                try {
                    play();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void play() throws BrokenBarrierException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " 已准备");
        cyclicBarrier.await();
        System.out.println(Thread.currentThread().getName() + " 开始执行");
    }
} */
