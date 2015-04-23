package com.fy.msgsys.appclient.napi.interf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import com.fy.msgsys.appclient.api.exception.ConnectWebsocketException;
import com.fy.msgsys.appclient.api.exception.IllegalWebsocketException;
import com.fy.msgsys.appclient.api.exception.VerifyWebsocketException;

public interface FeedbackInterf {

	
	
	/**
	 * 接收二进制消息
	 * @param msg
	 */
	public  void onMessageB(ByteBuffer msg);
	
	/**
	 * 接收文本消息
	 * @param msg 文件消息内容
	 */
	public  void onMessageT(ByteBuffer msg);
	
	/**
	 * 读取和写入异常		
	 * @param e 异常
	 * @param info 异常的补充消息
	 */
	public  void onError(Exception e,String info);
	
	/**
	 * 异常关闭
	 * @param e 异常
	 * @param info 异常的补充消息
	 */
	public  void onClose(Exception e,String info);
	
	
	/**
	 * 获取握手信息
	 * <br>
	 * 返回服务器发送access
	 * @throws IllegalWebsocketException 
	 */
	public void onHandshake(String access) ;
	
	/**
	 * 用户验证结果
	 * @throws VerifyWebsocketException 
	 * @throws IllegalWebsocketException 
	 */
	public void onVirify(ByteBuffer msg,boolean pass) throws VerifyWebsocketException ;
	

}
