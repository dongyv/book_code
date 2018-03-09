package JDK������.������;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
/**
 * void await() throws InterruptedException ��ʹ��ǰ�̵߳ȴ���ͬʱ�ͷŵ�ǰ��
 * void awaitUninterruptibly() 
 * long awaitNanos(long nanosTimeout) throws InterruptedException 
 * boolean await(long time,TimeUnit unit) throws InterruptedException 
 * boolean awaitUntil(Date readLine) throws InterruptedException ��await()����������ͬ
 * void signal() ���ڻ���һ���ڵȴ��е��߳�.singalAll()�����������еȴ����߳�
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
		//֪ͨ�߳�t1����ִ��
		lock.lock();
		condition.signal();
		lock.unlock();
	}
}
