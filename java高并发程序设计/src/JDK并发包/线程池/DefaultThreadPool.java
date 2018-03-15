package JDK������.�̳߳�;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
	//�̳߳ص����������
	private static final int MAX_WORKER_NUMBERS = 10;
	//�̳߳ص�Ĭ������
	private static final int DEFAULT_WORKER_NUMBERS = 5;
	//�̳߳ص���С����
	private static final int MIN_WORKER_NUMNERS = 1;
	//����һ�������б�������������빤��
	private final LinkedList<Job> jobs = new LinkedList<>();
	//�������б�
	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
	//�����ߵ��߳�����
	private int workerNum = DEFAULT_WORKER_NUMBERS; 
	//�̱߳������
	private AtomicLong threadNum = new AtomicLong();
	
	public DefaultThreadPool() {
		// TODO Auto-generated constructor stub
		initializeWokers(DEFAULT_WORKER_NUMBERS);
	}
	public DefaultThreadPool(int num) {
		// TODO Auto-generated constructor stub
		workerNum= num > MAX_WORKER_NUMBERS?MAX_WORKER_NUMBERS : num<MIN_WORKER_NUMNERS?MIN_WORKER_NUMNERS:num;
		initializeWokers(workerNum);
	}
	
	//��ʼ���̹߳�����
	private void initializeWokers(int num){
		for(int i=0;i<num;i++){
			Worker worker = new Worker();
			workers.add(worker);
			Thread thread = new Thread(worker,"ThreadPool-Worker-"+threadNum.incrementAndGet());
			thread.start();
		}
	}
	@Override
	public void execute(Job job) {
		// TODO Auto-generated method stub
		if(job != null){
			//���һ��������Ȼ�����֪ͨ
			synchronized(jobs){
				jobs.addLast(job);
				jobs.notify();
			}
		}
		
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		for(Worker worker:workers){
			worker.shutdown();
		}
	}

	@Override
	public void addWorkers(int num) {
		// TODO Auto-generated method stub
		synchronized(jobs){
			//�������ӵ�worker�������ܳ������ֵ
			if(num + this.workerNum>MAX_WORKER_NUMBERS){
				num = MAX_WORKER_NUMBERS - this.workerNum;
			}
			initializeWokers(num);
			this.workerNum += num;
		}
	}

	@Override
	public void removeWorkers(int num) {
		// TODO Auto-generated method stub
		synchronized(jobs){
			if(num >= this.workerNum){
				throw new IllegalArgumentException("beyond workNum");
			}
			//���ո���������ֹͣWorker
			int count =0;
			while(count < num){
				Worker worker = workers.get(count);
				if(workers.remove(worker)){
					worker.shutdown();
					count++;
				}
			}
		}
		
	}

	@Override
	public int getJobSize() {
		// TODO Auto-generated method stub
		return jobs.size();
	}
	//�����ߣ�������������
	class Worker implements Runnable{
		//�Ƿ���
		private volatile boolean running = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(running){
				Job job = null;
				synchronized(jobs){
					//��������б�Ϊ�գ���ô��wait
					while(jobs.isEmpty()){
						try {
							jobs.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//��֪���ⲿ��WorkerThread���жϲ���������
							Thread.currentThread().interrupt();
							return;
						}
					}
					//ȡ��һ��Job
					job = jobs.removeFirst();
				}
				if(job != null){
					try{
						job.run();
					}catch (Exception e) {
						// TODO: handle exception
						//����jobִ���е�Exception
					}
				}
			}
		}
		
		public void shutdown(){
			running = false;
		}
		
	}

}
