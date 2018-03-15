package JDK������.�̳߳�;

public interface ThreadPool<Job extends Runnable> {
	//ִ��һ��job�����job��Ҫʵ��Runnable
	void execute(Job job);
	//�ر��̳߳�
	void shutdown();
	//���ӹ����߳�
	void addWorkers(int num);
	//���ٹ����߳�
	void removeWorkers(int num);
	//�õ����ڵȴ�ִ�е���������
	int getJobSize();
}
