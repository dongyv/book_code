package JDK������.ѭ��դ��;

import java.util.concurrent.CyclicBarrier;

import JDK������.ѭ��դ��.CyclicBarrierDemo.Soldier;

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
			System.out.println("˾��:[ʿ��" + N + "�����������!]");
		}else{
			System.out.println("˾��:[ʿ��" + N + "�����������!]");
			flag = true;
		}
	}
	
	public static void main(String args[]) throws InterruptedException{
		final int N = 10;
		Thread[] allSoldier = new Thread[N];
		boolean flag = false;
		CyclicBarrier cyclic = new CyclicBarrier(N,new BarrierRun(flag, N));
		//�������ϵ㣬��Ҫ��Ϊ��ִ���������
		System.out.println("���϶���");
		for(int i =0;i<N;i++){
			System.out.println("ʿ�� "+i+" ����!");
			allSoldier[i] = new Thread(new Soldier(cyclic,"ʿ�� "+i));
			allSoldier[i].start();
		}
	}
}
