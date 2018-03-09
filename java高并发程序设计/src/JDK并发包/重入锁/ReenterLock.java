package JDK并发包.重入锁;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * ReeterantLock几个重要的方法如下:
 * 1 lock():获得锁,如果锁被占用,则等待
 * 2 lockInterruptibly():获得锁,但优先响应中断
 * 3 tryLock():尝试获得锁,如果成功,则返回true,失败返回false.该方法不等待,立即返回
 * 4 tryLock(long time,TimeUnit unit):给定时间内尝试获得锁
 * 5 unlock():释放锁
 * 
 * 重入锁三个要素
 * 1.原子状态 使用CAS操作
 * 2.等待队列 没有请求到锁的线程会进入等待队列进行等待。
 * 3.阻塞原语 park()和unpark(),用来挂起和恢复线程
 * 
 * @author Administrator
 *
 */
public class ReenterLock implements Runnable{
	public static ReentrantLock lock = new ReentrantLock();
	public static int i =0;
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(int i=0;i<10;i++){
			lock.lock();
			lock.lock();
			try{
				i++;
			}finally{
				lock.unlock();
				lock.unlock();
			}
		}
	}
	
	public static void main(String args[]) throws InterruptedException{
		ReenterLock t1 = new ReenterLock();
		Thread th1 = new Thread(t1);
		Thread th2 = new Thread(t1);
		th1.start();
		th2.start();
		th1.join();
		th2.join();
		System.out.println(i);
	}
}
