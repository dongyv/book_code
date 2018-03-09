package JDK并发包.重入锁;

import java.util.concurrent.locks.ReentrantLock;
/**
 * 公平锁
 * @author Administrator
 *
 */
public class FairLock implements Runnable{
	public static ReentrantLock fairLock = new ReentrantLock(true);//指定是锁是公平的
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				fairLock.lock();
				System.out.println(Thread.currentThread().getName() +" 获得锁");
			}finally{
				fairLock.unlock();
				System.out.println(Thread.currentThread().getName() +" 释放锁");
			}
		}
	}
	
	public static void main(String args[]){
		FairLock r1 = new FairLock();
		Thread t1 = new Thread(r1,"Thread_t1");
		Thread t2 = new Thread(r1,"Thread_t2");
		t1.start();
		t2.start();
	}
}
