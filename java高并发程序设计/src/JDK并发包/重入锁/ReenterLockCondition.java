package JDK并发包.重入锁;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * void await() throws InterruptedException 会使当前线程等待，同时释放当前锁
 * void awaitUninterruptibly() 
 * long awaitNanos(long nanosTimeout) throws InterruptedException 
 * boolean await(long time,TimeUnit unit) throws InterruptedException 
 * boolean awaitUntil(Date readLine) throws InterruptedException 与await()方法基本相同
 * void signal() 用于唤醒一个在等待中的线程.singalAll()方法唤醒所有等待的线程
 * void signalAll() 
 * 
 * @author Administrator
 *
 */
public class ReenterLockCondition implements Runnable{
	public static ReentrantLock lock = new ReentrantLock();
	public static Condition condition = lock.newCondition();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			lock.lock();
			condition.await();
			System.out.println("Thread is going on");
		}catch(InterruptedException e){
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}

	public static void main(String args[]) throws InterruptedException{
		ReenterLockCondition r1 = new ReenterLockCondition();
		Thread t1 = new Thread(r1);
		t1.start();
		Thread.sleep(2000);
		//通知线程t1继续执行
		lock.lock();
		condition.signal();
		lock.unlock();
	}
}
