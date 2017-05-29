package com.wisenut.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wisenut.core.KmaReturn;

public class KmaWorkerThread implements Runnable {
	Socket socket;
	
	public KmaWorkerThread(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			System.out.println(getTime() + socket.getInetAddress() + " 로부터 연결요청이 들어왔습니다.");			 
			
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
			
			StringBuffer msg = new StringBuffer();
			while(bis.available()!=0){
				byte[] buffer = new byte[10240];
				bis.read(buffer);
				
				msg.append(new String(buffer, StandardCharsets.UTF_8).trim());
			}
			
			System.out.println("-> \""+ msg +"\"");
			if(msg.length()>0){
				String result = KmaReturn.getMorphTag(msg.toString());
				//String result = shineware.komoran.KmaReturn.getMorphTag(msg.toString());
				if( result != null && result.length()>0 ){
					out.println(result);					 
					System.out.println("<Success>");
				}else{
					out.println("");
					System.out.println("<Fail>");
				}
			}else{				
				out.println("");
				System.out.println("<Fail>");
			}
			
			Thread.sleep(50);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.println("Oh, dear me! " + e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static String getTime() {
		SimpleDateFormat f = new SimpleDateFormat("[hh:mm:ss.SSS]");
		return f.format(new Date());
	}
}
