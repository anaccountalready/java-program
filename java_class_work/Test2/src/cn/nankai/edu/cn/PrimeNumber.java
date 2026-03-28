package cn.nankai.edu.cn;

public class PrimeNumber {
    private Integer mutex = 1;
    private static int num = 0;
    public static void main(String[] args) {
        PrimeNumber pn = new PrimeNumber();
        Thread1 t1 = pn.new Thread1(2, 1000);
        Thread1 t2 = pn.new Thread1(1000, 2000);
        Thread1 t3 = pn.new Thread1(2000, 3000);
        t1.start();
        t2.start();
        t3.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("共有"+num+"个素数");
    }
    class Thread1 extends Thread {
        private int start;
        private int end;
        public Thread1(int start, int end) {
            this.start = start;
            this.end = end;
        }
        public void run() {
            for (int i = start + 1; i < end; i++) {
                synchronized (mutex) {
                    int flag = 1;
                    for (int j = 2; j <= Math.ceil(Math.sqrt(i)); j++) {
                        if (i % j == 0) {
                            flag = 0;
                        }
                    }
                    if (flag == 1) {
                        num++;
                        System.out.println(i + "是素数");
                    }
                }
            }
        }
    }
}
