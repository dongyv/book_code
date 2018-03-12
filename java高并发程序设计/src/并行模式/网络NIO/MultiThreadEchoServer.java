package 并行模式.网络NIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
//支持多线程服务器
public class MultiThreadEchoServer {

	private static ExecutorService tp = Executors.newCachedThreadPool();
	
	static class HandleMsg implements Runnable{
		Socket clientScoket;

		public HandleMsg(Socket clientSocket){
			this.clientScoket = clientSocket;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			BufferedReader is = null;
			PrintWriter os = null;
			try{
				is = new BufferedReader(new InputStreamReader(clientScoket.getInputStream()));
				os = new PrintWriter(clientScoket.getOutputStream(), true);
				//从InputStream当中读取客户端发送的数据
				String inputLine = null;
				long b = System.currentTimeMillis();
				while((inputLine = is.readLine())!=null){
					os.println(inputLine);
				}
				long g = System.currentTimeMillis();
				System.out.println("spend:"+(b-g)+"ms");
			}catch(IOException e){
				e.printStackTrace();
			}finally{
				try{
					if(is!=null)is.close();
					if(os!=null)os.close();
					clientScoket.close();
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		
		
		public static void main(String args[]){
			ServerSocket echoServer = null;
			Socket clientSocket = null;
			try{
				echoServer = new ServerSocket(8100);
			}catch (IOException e) {
				// TODO: handle exception
				System.out.println(e);
			}
			while(true){
				try{
					clientSocket = echoServer.accept();
					synchronized(clientSocket){
						System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
						tp.execute(new HandleMsg(clientSocket));//开启一个线程，线程池
					}
				}catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}
}
