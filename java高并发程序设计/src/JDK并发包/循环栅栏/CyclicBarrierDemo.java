package JDK������.ѭ��դ��;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * ����ʵ���߳�֮��ļ����ȴ�
 * 
 * @author Administrator
 *
 */
public class CyclicBarrierDemo {
	public static class Soldier implements Runnable{
		private String soldier;
		private final CyclicBarrier cyclic;
		
		Soldier(CyclicBarrier cyclic,String soldierName){
			this.cyclic = cyclic;
			this.soldier = soldierName;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				//�ȴ�����ʿ������
				cyclic.await();
				doWork();
				//�ȴ�����ʿ����ɹ���
				cyclic.await();
			}catch(InterruptedException e){
				e.printStackTrace();
			}catch(BrokenBarrierException e){
				e.printStackTrace();
			}
		}
		
		void doWork(){
			try{
				Thread.sleep(Math.abs(new Random().nextInt()%10000));
			}catch(InterruptedException e){
				e.printStackTrace();
			}
			System.out.println(soldier + ":�������");
		}
		
	}
}
