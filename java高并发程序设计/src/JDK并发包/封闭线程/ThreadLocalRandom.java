package JDK并发包.封闭线程;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 随机数 性能的判断
 * @author aaa
 *
 */
public class ThreadLocalRandom {
	public static final int GEN_COUNT = 1000000;
	public static final int THREAD_COUNT = 4;
	static ExecutorService exe = Executors.newFixedThreadPool(THREAD_COUNT);
	public static Random rnd = new Random(123);
	
	public static ThreadLocal<Random> trnd = new ThreadLocal<Random>(){
		@Override
		protected Random initialValue() {
			return new Random(123);
		};
	};
	
	public static class RndTask implements Callable<Long>{
		private int mod = 0;
		
		public RndTask(int mod){
			this.mod = mod;
		}
		/**
		 * 第一是多线程共享一个Random（mod=0）
		 * 第二是多线程各分配一个Random（mod=1）
		 * @return
		 */
		public Random getRandom(){
			if(mod == 0){
				return rnd;
			}else if(mod == 1){
				return trnd.get();
			}else{
				return null;
			}
			
		}
		@Override
		public Long call() throws Exception {
			// TODO Auto-generated method stub
			long b = System.currentTimeMillis();
			for(long i = 0;i<GEN_COUNT;i++){
				getRandom().nextInt();
			}
			long e = System.currentTimeMillis();
			System.out.println(Thread.currentThread().getName()+"spend "+(e-b)+"ms");
			return e-b;
		}
	}
	
	public static void main(String args[]) throws InterruptedException, ExecutionException{
		Future<Long>[] futs = new Future[THREAD_COUNT];
		for(int i=0;i<THREAD_COUNT;i++){
			futs[i] = exe.submit(new RndTask(0));
		}
		long totaltime = 0;
		for(int i=0;i<THREAD_COUNT;i++){
			totaltime += futs[i].get();
		}
		System.out.println("多线程同时访问同一个Random实例:"+totaltime+"ms");
		
		//ThreadLocal情况
		for(int i=0;i<THREAD_COUNT;i++){
			futs[i] = exe.submit(new RndTask(1));
		}
		totaltime = 0;
		for(int i=0;i<THREAD_COUNT;i++){
			totaltime += futs[i].get();
		}
		
		System.out.println("使用ThreadLocal包装Random实例:"+totaltime+"ms");
		exe.shutdown();
	}
}
