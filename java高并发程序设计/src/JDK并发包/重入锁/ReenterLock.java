package JDK������.������;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * ReeterantLock������Ҫ�ķ�������:
 * 1 lock():�����,�������ռ��,��ȴ�
 * 2 lockInterruptibly():�����,��������Ӧ�ж�
 * 3 tryLock():���Ի����,����ɹ�,�򷵻�true,ʧ�ܷ���false.�÷������ȴ�,��������
 * 4 tryLock(long time,TimeUnit unit):����ʱ���ڳ��Ի����
 * 5 unlock():�ͷ���
 * 
 * ����������Ҫ��
 * 1.ԭ��״̬ ʹ��CAS����
 * 2.�ȴ����� û�����������̻߳����ȴ����н��еȴ���
 * 3.����ԭ�� park()��unpark(),��������ͻָ��߳�
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
