package JDK并发包.循环栅栏;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 可以实现线程之间的计数等待
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
				//等待所有士兵到齐
				cyclic.await();
				doWork();
				//等待所有士兵完成工作
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
			System.out.println(soldier + ":任务完成");
		}
		
	}
}
