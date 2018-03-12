package JDK并发包.倒计时器;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest implements Runnable{
	static CountDownLatch c = new CountDownLatch(10);
	
	public static void main(String args[]) throws InterruptedException{
//		new Thread(new Runnable(){
//			
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				System.out.println(1);
//				c.countDown();
//				System.out.println(2);
//				c.countDown();
//				
//			}
//			
//		}).start();
		for(int i=0;i<10;i++){
			new Thread(new CountDownLatchTest()).start();
		}
		
		c.await();
		System.out.println(3);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(1);
		c.countDown();
	}
}