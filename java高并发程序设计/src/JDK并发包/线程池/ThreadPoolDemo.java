package JDK������.�̳߳�;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 
 * public static ExecutorService newFixedThreadPool(int nThreads) ���ع̶��߳��������̳߳�
 * public static ExecutorService newSingleThreadExecutor() ֻ����һ��ֻ��һ���̵߳��̳߳�
 * public static ExecutorService newCacheThreadPool() ����һ���ɸ���ʵ����������߳��������̳߳� 
 * public static ScheduledExecutorService newSingleThreadScheduledExecutor() ScheduledExecutorService�ӿ���չ��ָ��ʱ��ִ��ĳ������
 * public static ScheduledExecutorService newScheduledExecutor(int corePoolSize) ����ָ���߳�����
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
		ExecutorService es = Executors.newFixedThreadPool(5);//���ع̶��������̳߳�
		for(int i=0;i<10;i++){
			es.submit(task);
		}
	}
}
