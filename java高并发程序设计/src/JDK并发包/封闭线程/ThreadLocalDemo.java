package JDK并发包.封闭线程;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadLocalDemo {
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<>();
	public static class ParseDate implements Runnable{
		int i = 0;
		public ParseDate(int i) {
			// TODO Auto-generated constructor stub
			this.i = i;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try{
				if(t1.get() == null){
					t1.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
				}
				Date t = t1.get().parse("2018-03-15 19:38:"+i%60);
				System.out.println(i+":"+t);
			}catch (ParseException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		public static void main(String args[]){
			ExecutorService es = Executors.newFixedThreadPool(10);
			for(int i=0;i<1000;i++){
				es.execute(new ParseDate(i));
			}
		}
	}
}
