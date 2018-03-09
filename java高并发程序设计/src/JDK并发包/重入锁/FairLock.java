package JDK������.������;

import java.util.concurrent.locks.ReentrantLock;
/**
 * ��ƽ��
 * @author Administrator
 *
 */
public class FairLock implements Runnable{
	public static ReentrantLock fairLock = new ReentrantLock(true);//ָ�������ǹ�ƽ��
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				fairLock.lock();
				System.out.println(Thread.currentThread().getName() +" �����");
			}finally{
				fairLock.unlock();
				System.out.println(Thread.currentThread().getName() +" �ͷ���");
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
