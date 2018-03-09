package JDK������.�ź���;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * �ź���ģ��  5���߳�һ��Ϊ��λ��������������߳�ID�� ��ʾ�ı�
 * @author Administrator
 *
 */
public class SemapDemo implements Runnable{
	final Semaphore semp = new Semaphore(5);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			semp.acquire();//�����ź���
			//ģ���ʱ����
			Thread.sleep(2000);
			System.out.println(Thread.currentThread().getId()+":done!");
			semp.release();//�ͷ��ź���
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]){
		ExecutorService exec = Executors.newFixedThreadPool(20);
		final SemapDemo demo = new SemapDemo();
		for(int i = 0;i<20;i++){
			exec.submit(demo);
		}
	}
}
