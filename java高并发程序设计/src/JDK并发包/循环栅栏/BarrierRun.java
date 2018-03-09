package JDK并发包.循环栅栏;

import java.util.concurrent.CyclicBarrier;

import JDK并发包.循环栅栏.CyclicBarrierDemo.Soldier;

public class BarrierRun implements Runnable{
	boolean flag;
	int N;
	
	public BarrierRun(boolean flag,int N){
		this.flag = flag;
		this.N = N;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(flag){
			System.out.println("司令:[士兵" + N + "个，任务完成!]");
		}else{
			System.out.println("司令:[士兵" + N + "个，集合完毕!]");
			flag = true;
		}
	}
	
	public static void main(String args[]) throws InterruptedException{
		final int N = 10;
		Thread[] allSoldier = new Thread[N];
		boolean flag = false;
		CyclicBarrier cyclic = new CyclicBarrier(N,new BarrierRun(flag, N));
		//设置屏障点，主要是为了执行这个方法
		System.out.println("集合队伍");
		for(int i =0;i<N;i++){
			System.out.println("士兵 "+i+" 报道!");
			allSoldier[i] = new Thread(new Soldier(cyclic,"士兵 "+i));
			allSoldier[i].start();
		}
	}
}
