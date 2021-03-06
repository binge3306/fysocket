package edu.fy.com.websocketServer.verifyReactor.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientMain  implements IClientCallinface{

	private IWebsocketClientObject clientObject;

	public ClientMain(){
		init();
	}
	
	public void init(){
		if(clientObject == null)
			clientObject = new WebsocketClient(this).createConnection();
	}
	
	private void start(){
		try {
			for(int i= 0;i<20;i++){
				// 发送待登录用户名和验证码
				clientObject.sendMsgText("#U#"+"user" + i+":"+"verify"+i);
				// 发送待登录用户 互动室列表
				List<String> chatrooms = new ArrayList<String>();
				for(int j = 0;j<10;j++){
					chatrooms.add("chatroom" + j);
				}
				chatrooms.add("20840");
				chatrooms.add("20896");
				chatrooms.add("20237");
				
				clientObject.sendMsgText("#C#"+"user"+i+":"+chatrooms.toString());
				TimeUnit.SECONDS.sleep(3);
				if(i == 5)
					System.out.println("111");
				if(i>4)
					TimeUnit.SECONDS.sleep(60);
			}
			clientObject.onClose();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	private void loginOutOptions(){
		
	}
	
	private void loginout1(String userKey){
		System.out.println("" + userKey);
		clientObject.sendMsgText("#OU#"+userKey);
		
	}
	
	private void recive(String reMsg){
		System.out.println("收到服务器的消息= "+reMsg);
	}
	
	// 提供一个异步返回结果的接口
	@Override
	public void onMessage(String message) {
		recive(message);
	}

	// 捕获异常
	@Override
	public void onError(String e) {
		recive(e);
	}

	
	public static void main(String[] args) {
		
		new ClientMain().start();

	}


}
