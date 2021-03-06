package edu.fy.com.websocketServer.verifyReactor.util;

/**
 * 2015 01 31 
 * for 将可能使用的常量集中到该目录下面
 * @author wurunzhou
 *
 */
public enum SocketConstant { 
	
	
	// 待登录用户发送标志
    holeLoginUser(""), 
    
    // 登录用户标志
    olUser(""),
    
    // 用户退出标志
    quitUser(""),
    
    // 创建帮区私聊互动室
    createBPChat(""),
    
    // 删除帮区私聊互动室
    deleteBPChat(""),
    
    // 发送帮区私聊 消息
    sendBPMsg(""),
    
    // 发送公共 消息
    sendPubMsg("");
    
    // 枚举对象的 RSS 地址的属性
    private String rss_url; 
        
    // 枚举对象构造函数
    private SocketConstant(String rss) { 
        this.rss_url = rss; 
    } 
        
    // 枚举对象获取 RSS 地址的方法
    public String getRssURL() { 
        return this.rss_url; 
    } 
 }