package com.fy.msgsys.server.advancemain;

import java.io.*;
import java.nio.channels.*;
import java.nio.*;
import java.net.*;
import java.util.*;

/**
 * 
 * @author wurunzhou add at 20150303
 * 
 * 采用reactor设计模式处理socket连接请求，握手 ，读写处理
 *
 */
public class Reactor {

	final Selector selector;
	final ServerSocketChannel serverSocket;

	Reactor(int port) throws IOException { // Reactor初始化
		selector = Selector.open();
		serverSocket = ServerSocketChannel.open();
		serverSocket.socket().bind(new InetSocketAddress(port));
		serverSocket.configureBlocking(false); // 非阻塞
		SelectionKey sk = serverSocket.register(selector,
				SelectionKey.OP_ACCEPT); // 分步处理,第一步,接收accept事件
		sk.attach(new Acceptor()); // attach callback object, Acceptor
		/*
		 * 或者你可以用, use explicit SPI provider:
		 *  SelectorProvider p = SelectorProvider.provider(); 
		 *  selector = p.openSelector();
		 *  serverSocket = p.openServerSocketChannel();
		 */
	}

	public void run() {
		try {
			while (!Thread.interrupted()) {
				selector.select();
				Set<SelectionKey> selected = selector.selectedKeys();
				Iterator<SelectionKey> it = selected.iterator();
				while (it.hasNext())
					dispatch((SelectionKey) (it.next())); // Reactor负责dispatch收到的事件
				selected.clear();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			/* ... */
		}
	}

	void dispatch(SelectionKey k) {
		Runnable r = (Runnable) (k.attachment()); // 调用之前注册的callback对象
		if (r != null)
			r.run();
	}

	// accptor 只负责一个事情那就是 接收连接
	class Acceptor implements Runnable { 

		public void run() {
			try {
				SocketChannel c = serverSocket.accept();
				if (c != null)
					new ReactorHandler(selector, c);
			} catch (IOException ex) {
				ex.printStackTrace();
				/* ... */
			}
		}
	}
}


	
