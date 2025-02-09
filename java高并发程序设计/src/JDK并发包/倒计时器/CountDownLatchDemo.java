package JDK并发包.倒计时器;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountDownLatchDemo implements Runnable{
	static final CountDownLatch end = new CountDownLatch(10);//计数的个数 10 
	static final CountDownLatchDemo demo = new CountDownLatchDemo();
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			//模拟检查任务
			Thread.sleep(new Random().nextInt(10)*1000);
			System.out.println("check complete");
			end.countDown();
		}catch(InterruptedException e){
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws InterruptedException{
		ExecutorService exec = Executors.newFixedThreadPool(10);
		for(int i=0;i<10;i++){
			Future<?> submit = exec.submit(demo);
		}
		
		//等待检查
		end.await();
		//发射火箭、、
		System.out.println("Fire!");
		exec.shutdown();
	}
}
