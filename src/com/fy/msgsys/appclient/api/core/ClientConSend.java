package com.fy.msgsys.appclient.api.core;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.List;

import com.fy.msgsys.appclient.api.exception.ConnectWebsocketException;
import com.fy.msgsys.appclient.api.exception.IllegalWebsocketException;
import com.fy.msgsys.appclient.api.service.WebsocketClientInterface;
import com.fy.msgsys.appclient.api.util.ReceiveMsgQueue;
import com.fy.msgsys.appclient.api.util.SendMsgQueue;

/**
 * java代码编写的利用socket连接模拟websocket
 * add at 20150317 at b8101
 * @author wurunzhou
 *
 */
public abstract class ClientConSend implements WebsocketClientInterface{

	// socket 连接对象
	private Socket appSocket = null;
	// 判断用户验证是否通过
	private boolean virifyPass = false;
	// 判断握手是否成功
	private boolean handshakePass = false;
	
	// 触发器反馈
	private FeedbackInterface feedback;
	
	public ClientConSend(){};
	
	public ClientConSend(String host,int port){
		try {
			appSocket = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	};
	
	public ClientConSend(FeedbackInterface feedback,String host,int port){
		this.feedback = feedback;
		try {
			appSocket = new Socket(host, port);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 客户端发送握手信息
	 */
	private void sendhandshake(){
		
	}
	
	/**
	 * 客户端模拟验证握手信息 
	 * @return
	 */
	private void dealhandshake(){
		
		
		handshakePass = true;
	}
	
	@Override
	public void connection() {
		
	}

	@Override
	public void Connection(String userKey, String virifyCode)
			throws IOException, ConnectWebsocketException {
		if(!handshakePass)
			throw new ConnectWebsocketException(200, "握手失败");
			
		virifyPass = true;
	}

	@Override
	public void sendMsgBinary(ByteBuffer msg, long timeout) {
		
	}

	@Override
	public void sendMsgBinary(List<ByteBuffer> msg, long timeout) {
		
	}

	@Override
	public void sendMsgText(ByteBuffer msg, long timeout) throws IllegalWebsocketException {
		if(!virifyPass){
			throw new IllegalWebsocketException();
		}
		sendMsgQueue.insert(msg);
		
	}

	@Override
	public void close(long timeout) {
		
	}

	// 底层的发送消息
	private void sendMsg(ByteBuffer msgb){
		System.out.println("从发送队列中取出一个消息，发送出去");
	}

	/**
	 * 处理消息线程
	 * <br>
	 * 该线程负责从通道中读取消息
	 * 并将该消息放到待处理队列中，
	 * 如果待处理队列满，则线程阻塞
	 * @author wurunzhou
	 *
	 */
	class ReadIOMsgThread implements Runnable{

		private DataInputStream ind ;
		
		ReadIOMsgThread(){
			try {
				this.ind = new DataInputStream(appSocket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			// 不断的读IO
			while(true){
				
				ByteBuffer rmsg = ByteBuffer.allocate(12);
				rmsg.put("待处理消息".getBytes());
				rmsg.flip();
				receiveMsgQueue.insert(rmsg);
			}
		}
		
	}
	
	/**
	 * 
	 * 发送线程
	 * <br> 
	 * 从待发送消息队列中取出一个消息发送出去，
	 * 如果待发送消息队列为空，则该线程阻塞。
	 * @author wurunzhou
	 *
	 */
	class WriteIOMsgThread implements Runnable{

		private DataOutputStream outd ;
		WriteIOMsgThread(){
			try {
				DataOutputStream outd = new DataOutputStream(appSocket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		@Override
		public void run() {
			while(sendMsgQueue.hasPendingStatus()){
				sendMsg(sendMsgQueue.get());
				
			}
		}
		
	}
	
	
}
