package JDK并发包.重入锁;

import java.util.concurrent.locks.ReentrantLock;
/**
 * 锁中断
 * @author Administrator
 *
 */
public class IntLock implements Runnable{
	public static ReentrantLock lock1 = new ReentrantLock();
	public static ReentrantLock lock2 = new ReentrantLock();
	int lock;
	
	public IntLock(int lock){
		this.lock = lock;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			if(lock == 1){
				lock1.lockInterruptibly();
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){	}
				lock2.lockInterruptibly();
			}else{
				lock2.lockInterruptibly();
				try{
					Thread.sleep(500);
				}catch(InterruptedException e){	}
				lock1.lockInterruptibly();
			}
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			if(lock1.isHeldByCurrentThread()){
				lock1.unlock();
			}
			if(lock2.isHeldByCurrentThread()){
				lock2.unlock();
			}
			System.out.println(Thread.currentThread().getId() + ":线程退出");
		}
	}
	
	public static void main(String args[]) throws InterruptedException{
		IntLock r1 = new IntLock(1);
		IntLock r2 = new IntLock(2);
		Thread t1 = new Thread(r1);
		Thread t2 = new Thread(r2);
		t1.start();
		t2.start();
		Thread.sleep(1000);
		//中断其中一个线程
		t2.interrupt();
	}
}
