package com.wisenut.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
 
public class SimpleServer {
	// 스레드 풀은 실행할 작업이 없어도 corePoolSize 를 유지한다.
	// 최초에 corePoolSize 만큼 스레드를 생성하지 않고 작업이 실행 될 때 corePoolSize 만큼 차례대로 생성함.
	// prestartAllCoreThreads() 를 사용하면 corePoolSize 만큼 스레드를 미리 생성함.
	final int corePoolSize   = 10;
	// xaximumPoolsize 는 동시에 얼마나 많은 개수의 스레드가 동작할 수 있는지를 제한하는 최대값.
	final int xaximumPoolsize  = 10;
	final int blocdingQueueSize = 100;
	final int port    = 5000;
	// keepAliveTime 는 스레드 유지 시간으로 스레드가 keepAliveTime 이상 대기하고 있으면
	// 해당 스레드는 제거 될 수 있음. 풀의 스레드 개수가 corePoolSize 를 넘어서면 제거될 수 있음.
	final long keepAliveTime = 30L;
	
	final ThreadPoolExecutor pool;
	final ServerSocket serverSocket;
	
	final BlockingQueue<Runnable> queue;
	final PoolThreadFactory threadFactory;
	 
    public static void main(String[] args) throws IOException {
        
        try {
        	System.out.println(getTime() + " 서버가 준비되었습니다.");
        	new SimpleServer().serverStart();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
    
    public SimpleServer() throws IOException{
    	queue    = new ArrayBlockingQueue<Runnable>(blocdingQueueSize);
    	threadFactory  = new PoolThreadFactory("SERVER_POOL");
    	serverSocket  = new ServerSocket(port);
    	pool    = new ThreadPoolExecutor(corePoolSize,
    			xaximumPoolsize,
    			keepAliveTime,
    			TimeUnit.SECONDS,
    			queue,
    			threadFactory,
    			new ThreadPoolExecutor.CallerRunsPolicy());
    }
    
    public void serverStart(){
    	try{
    		while(true){
    			Socket clientSocket = serverSocket.accept();
    			pool.execute(new KmaWorkerThread(clientSocket));
    		}
    	}catch(IOException e){
    		e.printStackTrace();
    	}
    }
    
    static String getTime() {
        SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss.SSS]");
        return f.format(new Date());
    }
}