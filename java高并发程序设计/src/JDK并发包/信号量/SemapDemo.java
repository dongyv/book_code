package JDK并发包.信号量;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量模拟  5个线程一组为单位，依次输出带有线程ID的 提示文本
 * @author Administrator
 *
 */
public class SemapDemo implements Runnable{
	final Semaphore semp = new Semaphore(5);
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			semp.acquire();//申请信号量
			//模拟耗时操作
			Thread.sleep(2000);
			System.out.println(Thread.currentThread().getId()+":done!");
			semp.release();//释放信号量
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
