package JDK并发包.线程池;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
	//线程池的最大限制树
	private static final int MAX_WORKER_NUMBERS = 10;
	//线程池的默认数量
	private static final int DEFAULT_WORKER_NUMBERS = 5;
	//线程池的最小数量
	private static final int MIN_WORKER_NUMNERS = 1;
	//这是一个工作列表，将会向里面插入工作
	private final LinkedList<Job> jobs = new LinkedList<>();
	//工作者列表
	private final List<Worker> workers = Collections.synchronizedList(new ArrayList<>());
	//工作者的线程数量
	private int workerNum = DEFAULT_WORKER_NUMBERS; 
	//线程编号生产
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
	
	//初始化线程工作者
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
			//添加一个工作，然后进行通知
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
			//限制增加的worker数量不能超过最大值
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
			//按照给定的数量停止Worker
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
	//工作者，负责消费任务
	class Worker implements Runnable{
		//是否工作
		private volatile boolean running = true;
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(running){
				Job job = null;
				synchronized(jobs){
					//如果工作列表为空，那么就wait
					while(jobs.isEmpty()){
						try {
							jobs.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							//感知到外部对WorkerThread的中断操作，返回
							Thread.currentThread().interrupt();
							return;
						}
					}
					//取出一个Job
					job = jobs.removeFirst();
				}
				if(job != null){
					try{
						job.run();
					}catch (Exception e) {
						// TODO: handle exception
						//忽视job执行中的Exception
					}
				}
			}
		}
		
		public void shutdown(){
			running = false;
		}
		
	}

}
