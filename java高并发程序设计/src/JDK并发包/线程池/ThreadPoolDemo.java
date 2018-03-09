package JDK并发包.线程池;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * public static ExecutorService newFixedThreadPool(int nThreads) 返回固定线程数量的线程池
 * public static ExecutorService newSingleThreadExecutor() 只返回一个只有一个线程的线程池
 * public static ExecutorService newCacheThreadPool() 返回一个可根据实际情况调整线程数量的线程池 
 * public static ScheduledExecutorService newSingleThreadScheduledExecutor() ScheduledExecutorService接口拓展了指定时间执行某任务功能
 * public static ScheduledExecutorService newScheduledExecutor(int corePoolSize) 可以指定线程数量
 * @author aaa
 *
 */
public class ThreadPoolDemo {
	public static class MyTask implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			System.out.println(System.currentTimeMillis() +":Thread ID:"
					+Thread.currentThread().getId());
			try{
				Thread.sleep(1000);
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String args[]){
		MyTask task = new MyTask();
		ExecutorService es = Executors.newFixedThreadPool(5);//返回固定数量的线程池
		for(int i=0;i<10;i++){
			es.submit(task);
		}
	}
}
