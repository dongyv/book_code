package JDK������.CAS;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {
	private AtomicInteger atomicI = new AtomicInteger(0);//casԭ����
	private int i = 0;
	public static void main(String args[]){
		final Counter cas = new Counter();
		List<Thread> ts = new ArrayList<>();
		long start = System.currentTimeMillis();
		for(int j=0;j<100;j++){
			Thread t = new Thread(){
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					for(int i=0;i<1000;i++){
						cas.count();
						cas.safeCount();
					}
				}
			};
			ts.add(t);
		}
		for(Thread t:ts){
			t.start();
		}
		//�ȴ������߳�ִ�����
		for(Thread t:ts){
			try {
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(cas.i);
		System.out.println(cas.atomicI.get());
		System.out.println(System.currentTimeMillis()-start);
	}
	
	/**
	 * ʹ��casʵ���̰߳�ȫ������
	 */
	private void safeCount(){
		for(;;){
			int i = atomicI.get();
			boolean suc = atomicI.compareAndSet(i, ++i);
			if(suc){
				break;
			}
		}
	}
	/**
	 * ���̰߳�ȫ������
	 */
	private void count(){
		i++;
	}
}
