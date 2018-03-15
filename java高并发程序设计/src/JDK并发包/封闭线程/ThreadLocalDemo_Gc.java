package JDK并发包.封闭线程;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ThreadLocalDemo_Gc {
	static ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<>();
	protected void finalize() throws Throwable{
		System.out.println(this.toString() + " is gc");
	}
	static volatile CountDownLatch cd = new CountDownLatch(10000);
	
	public static class ParseDate implements Runnable{
		int i = 0;
		public ParseDate(int i) {
			// TODO Auto-generated constructor stub
			this.i = i;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				if(t1.get() == null){
					t1.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"){
						protected void finalize() throws Throwable{
							System.out.println(this.toString() + " is gc");
						}
					});
					System.out.println(Thread.currentThread().getId() + ":create SimpleDateFormat");
				}
				Date t = t1.get().parse("2018-03-15 19:38:"+i%60);
			}catch (ParseException e) {
				// TODO: handle exception
				e.printStackTrace();
			}finally{
				cd.countDown();
			}
		}
		
		public static void main(String args[]) throws InterruptedException{
			ExecutorService es = Executors.newFixedThreadPool(10);
			for(int i=0;i<1000;i++){
				es.execute(new ParseDate(i));
			}
			cd.await();
			System.out.println("mission complete!!");
			t1 = null;
			System.gc();
			System.out.println("first GC complete!!");
			t1 = new ThreadLocal<SimpleDateFormat>();
			cd = new CountDownLatch(1000);
			for(int i=0;i<1000;i++){
				es.execute(new ParseDate(i));
			}
			cd.await();
			Thread.sleep(1000);
			System.gc();
			System.out.println("second GC complete!!");
		}
	}
}
