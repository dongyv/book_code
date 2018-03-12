package 并行模式.网络NIO;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NIOServer {
	private Selector selector;
	private ExecutorService tp = Executors.newCachedThreadPool();
	public static Map<Socket,Long> time_stat = new HashMap<Socket,Long>(10240);

	private void startServer() throws Exception{
		selector = SelectorProvider.provider().openSelector();
		ServerSocketChannel ssc = ServerSocketChannel.open();
		ssc.configureBlocking(false);
		
		InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(),8000);
//		InetSocketAddress isa = new InetSocketAddress(8000);
		ssc.socket().bind(isa);
		
		SelectionKey accptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
		//等待分发网络
		for(;;){
			selector.select();
			Set readyKeys = selector.selectedKeys();
			Iterator i = readyKeys.iterator();
			long e =0;
			while(i.hasNext()){
				SelectionKey sk =(SelectionKey)i.next();
				i.remove();
						
				if(sk.isAcceptable()){
					doAccept(sk);
				}else if(sk.isValid() && sk.isReadable()){
					if(!time_stat.containsKey(((SocketChannel)sk.channel()).socket())){//匹配key 如果没有找到则 添加key
						time_stat.put(((SocketChannel)sk.channel()).socket(), System.currentTimeMillis());
					}
					doRead(sk);
				}else if(sk.isWritable() && sk.isValid()){
					doWriter(sk);
					e = System.currentTimeMillis();
					long b = time_stat.remove(((SocketChannel)sk.channel()).socket());
					System.out.println("spend: "+ (e-b) + "ms");
					
				}
				
			}
		}
	}
	
	private void doAccept(SelectionKey sk){
		ServerSocketChannel server = (ServerSocketChannel)sk.channel();
		SocketChannel clientChannel;
		try{
			clientChannel = server.accept();
			clientChannel.configureBlocking(false);
			
			//注册通道
			SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
			NIOClient nioClient = new NIOClient();
			clientKey.attach(nioClient);
			
			InetAddress clientAddress = clientChannel.socket().getInetAddress();
			System.out.println("Accepted connection from "+clientAddress.getHostAddress() + ".");
		}catch(Exception e){
			System.out.println("Failed to accept new client");
			e.printStackTrace();
		}
		
	}
	
	private void doRead(SelectionKey sk){
		SocketChannel channel = (SocketChannel)sk.channel();
		ByteBuffer bb = ByteBuffer.allocate(8192);
		int len;
		
		try{
			len = channel.read(bb);
			if(len<0){
//				disconnect(sk);
				return;
			}
		}catch(Exception e){
			System.out.println("Failed to read from client.");
			e.printStackTrace();
//			disconnect(sk);
			return;
		}
		
		bb.flip();
		tp.execute(new HandleMsg(sk,bb));
	}
	
	class HandleMsg implements Runnable{
		private SelectionKey sk;
		private ByteBuffer bb;
		
		public HandleMsg(SelectionKey sk,ByteBuffer bb){
			this.sk = sk;
			this.bb = bb;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			NIOClient nioClient =(NIOClient)sk.attachment();
			nioClient.enqueue(bb);
			sk.interestOps(SelectionKey.OP_READ|SelectionKey.OP_WRITE);
			//强迫selector立即返回
			selector.wakeup();
		}
		
	}
	
	private void doWriter(SelectionKey sk){
		SocketChannel channel = (SocketChannel)sk.channel();
		NIOClient nioClient = (NIOClient)sk.attachment();
		
		LinkedList<ByteBuffer> outq = nioClient.getOutputQueue();
		
		ByteBuffer bb = outq.getLast();
		try{
			int len = channel.write(bb);
			if(len == -1){
//				disconnect(sk);
				return;
			}
			
			if(bb.remaining()==0){
				outq.removeLast();
			}
		}catch(Exception e){
			System.out.println("Failed to accept new client");
			e.printStackTrace();
//			disconnect(sk);
		}
		
		if(outq.size()==0){
			sk.interestOps(SelectionKey.OP_READ);
		}
	}
}
