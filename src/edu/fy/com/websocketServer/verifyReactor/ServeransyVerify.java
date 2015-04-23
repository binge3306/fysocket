package edu.fy.com.websocketServer.verifyReactor;


import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ServeransyVerify {

    public static void main(String[] args) {

        try {
        	new NormalforJavaServer().start();
        	//new CreateUserUtil().start();
			new ServeransyVerify().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
    }
	private static final String TAG = "\r\n";
	private static final int PORT = 8877;
    
    private void start() throws IOException{
		ServerSocket server = new ServerSocket(PORT);
		Executor pool =
			    Executors.newFixedThreadPool(2);
		while (true) {
			try {
				final Socket ss = server.accept();
				if (handlerShake0(ss))
					pool.execute((new ServerThread1(ss)));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }
    
    public boolean handlerShake0(Socket serverfork) throws NoSuchAlgorithmException, IOException{
    	boolean hasHandshake = false;
    	 InputStream in = serverfork.getInputStream();
         
         PrintWriter pw = getWriter(serverfork);
         //读入缓存
         byte[] buf = new byte[1024];
         //读到字节
         int len = in.read(buf, 0, 1024);
         //读到字节数组
         byte[] res = new byte[len];
         System.arraycopy(buf, 0, res, 0, len);
         String key = new String(res);
         if(key.indexOf("Key") > 0){
             //握手
             key = key.substring(0, key.indexOf("==") + 2);
             key = key.substring(key.indexOf("Key") + 4, key.length()).trim();
             key+= "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
             MessageDigest md = MessageDigest.getInstance("SHA-1");  
             md.update(key.getBytes("utf-8"), 0, key.length());
             byte[] sha1Hash = md.digest();  
                 sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();  
             key = encoder.encode(sha1Hash);  
             pw.println("HTTP/1.1 101 Switching Protocols");
             pw.println("Upgrade: websocket");
             pw.println("Connection: Upgrade");
             pw.println("Sec-WebSocket-Accept: " + key);
             pw.println();
             pw.flush();
             hasHandshake = true;
         }
    	return hasHandshake;
    }
    
	class ServerThread1 implements Runnable, socketCallbackInterface1 {

		private Socket serverfork;

		private Charset charset = Charset.forName("UTF-8");

		public ServerThread1(Socket serverfork) {
			this.serverfork = serverfork;

		}

		@Override
		public void run() {
			try {

				DataInputStream ins = new DataInputStream(
						serverfork.getInputStream());
				DataOutputStream outs = new DataOutputStream(
						serverfork.getOutputStream());
				if (handlerShake1(ins, outs)) {
					new Thread(new ReciveThread2(ins, this, outs)).start();
					// new Thread(new SendThread2(outs,this)).start();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		@Override
		public void closeSocket() {

			try {
				if (serverfork != null)
					serverfork.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		private boolean handlerShake1(DataInputStream ins, DataOutputStream outs) {
			// verify user

			boolean pass = false;

			try {

				// 接收数据
				byte[] first = new byte[1];
				// 这里会阻塞
				int read = ins.read(first, 0, 1);
				if (read > 0) {
					int b = first[0] & 0xFF;
					// 1为字符数据，8为关闭socket
					byte opCode = (byte) (b & 0x0F);

					if (opCode == 8) {
						// socket.getOutputStream().close();
						closeSocket();
					}
					b = ins.read();
					int payloadLength = b & 0x7F;
					if (payloadLength == 126) {
						byte[] extended = new byte[2];
						ins.read(extended, 0, 2);
						int shift = 0;
						payloadLength = 0;
						for (int i = extended.length - 1; i >= 0; i--) {
							payloadLength = payloadLength
									+ ((extended[i] & 0xFF) << shift);
							shift += 8;
						}

					} else if (payloadLength == 127) {
						byte[] extended = new byte[8];
						ins.read(extended, 0, 8);
						int shift = 0;
						payloadLength = 0;
						for (int i = extended.length - 1; i >= 0; i--) {
							payloadLength = payloadLength
									+ ((extended[i] & 0xFF) << shift);
							shift += 8;
						}
					}

					// 掩码
					byte[] mask = new byte[4];
					ins.read(mask, 0, 4);
					int readThisFragment = 1;
					ByteBuffer byteBuf = ByteBuffer
							.allocate(payloadLength + 10);
					// byteBuf.put("echo: ".getBytes("UTF-8"));
					while (payloadLength > 0) {
						int masked = ins.read();
						masked = masked
								^ (mask[(int) ((readThisFragment - 1) % 4)] & 0xFF);
						byteBuf.put((byte) masked);
						payloadLength--;
						readThisFragment++;
					}
					byteBuf.flip();
					if (verifyUser(byteBuf.array())) {
// System.out.println("验证登录用户通过");
						responseUser(byteBuf, outs, true, true);
						pass = true;
					} else {
						closeSocket();
					}

					// printRes(byteBuf.array());
					// ins.read(first, 0, 1);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			return pass;

		}

		private final boolean verifyUser(byte[] array) {
			ByteArrayInputStream byteIn = new ByteArrayInputStream(array);
			InputStreamReader reader = new InputStreamReader(byteIn,
					charset.newDecoder());
			int b = 0;
			String res = "";
			try {
				while ((b = reader.read()) > 0) {
					res += (char) b;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
System.out.println("登录用户信息："+res);

			return userVerify(res.split(":"));

		}

		private final boolean userVerify(String[] lala) {
			return UtilUser.getInstance().verify(lala[0], lala[1], serverfork);
		}

		public boolean responseUser(ByteBuffer byteBuf, OutputStream out,
				boolean finalFragment, boolean ok) throws IOException {

			byteBuf.clear();
			if (ok)
				byteBuf.put("ok".getBytes());
			else
				byteBuf.put("ack".getBytes());

			byteBuf.flip();
			// OutputStream out = socket.getOutputStream();
			int first = 0x00;
			// 是否是输出最后的WebSocket响应片段
			if (finalFragment) {
				first = first + 0x80;
				first = first + 0x1;
			}
			out.write(first);

			if (byteBuf.limit() < 126) {
				out.write(byteBuf.limit());
			} else if (byteBuf.limit() < 65536) {
				out.write(126);
				out.write(byteBuf.limit() >>> 8);
				out.write(byteBuf.limit() & 0xFF);
			} else {
				// Will never be more than 2^31-1
				out.write(127);
				out.write(0);
				out.write(0);
				out.write(0);
				out.write(0);
				out.write(byteBuf.limit() >>> 24);
				out.write(byteBuf.limit() >>> 16);
				out.write(byteBuf.limit() >>> 8);
				out.write(byteBuf.limit() & 0xFF);

			}

			// Write the content
			out.write(byteBuf.array(), 0, byteBuf.limit());
			out.flush();

			return true;
		}


		class ReciveThread2 implements Runnable {

			private DataInputStream ins;
			private socketCallbackInterface1 socketOptions;
			private OutputStream out;
			private Charset charset = Charset.forName("UTF-8");

			public ReciveThread2(DataInputStream ins,
					socketCallbackInterface1 socketOptions, OutputStream out) {
				this.ins = ins;
				this.socketOptions = socketOptions;
				this.out = out;
			}

			@Override
			public void run() {

				try {

					// 接收数据
					byte[] first = new byte[1];
					// 这里会阻塞
					int read = ins.read(first, 0, 1);
					int ilala = 0;
					while (read > 0) {
						int b = first[0] & 0xFF;
						// 1为字符数据，8为关闭socket
						byte opCode = (byte) (b & 0x0F);

						if (opCode == 8) {
							// socket.getOutputStream().close();
							socketOptions.closeSocket();
							break;
						}
						b = ins.read();
						int payloadLength = b & 0x7F;
						if (payloadLength == 126) {
							byte[] extended = new byte[2];
							ins.read(extended, 0, 2);
							int shift = 0;
							payloadLength = 0;
							for (int i = extended.length - 1; i >= 0; i--) {
								payloadLength = payloadLength
										+ ((extended[i] & 0xFF) << shift);
								shift += 8;
							}

						} else if (payloadLength == 127) {
							byte[] extended = new byte[8];
							ins.read(extended, 0, 8);
							int shift = 0;
							payloadLength = 0;
							for (int i = extended.length - 1; i >= 0; i--) {
								payloadLength = payloadLength
										+ ((extended[i] & 0xFF) << shift);
								shift += 8;
							}
						}

						// 掩码
						byte[] mask = new byte[4];
						ins.read(mask, 0, 4);
						int readThisFragment = 1;
						ByteBuffer byteBuf = ByteBuffer
								.allocate(payloadLength + 10);
						// byteBuf.put("echo: ".getBytes("UTF-8"));
						while (payloadLength > 0) {
							int masked = ins.read();
							masked = masked
									^ (mask[(int) ((readThisFragment - 1) % 4)] & 0xFF);
							byteBuf.put((byte) masked);
							payloadLength--;
							readThisFragment++;
						}
						byteBuf.flip();
						
						/**
						 * liuyan update 2015-1-29  
						 * 修改消息信息，添加是否是公共互动室的属性
						 * 修改前：chatId##msgContent
						 * 修改后：chatId##isPublice##msgContent
						 * **/
						String msgStr = this.getMsg(byteBuf.array());
						String[] msgInfos = msgStr.split("##");
						String chatId = msgInfos[0];
						String isPublice = msgInfos[1];
						if("".equals(chatId)){
							if(ilala++ > 5) break;
						}else {
							transferMessage(chatId, isPublice, byteBuf);
							// responseClient(byteBuf, true);
							ilala = 0;
						}
						ins.read(first, 0, 1);
						
//						String lala = printRes(byteBuf.array());
//						if("".equals(lala)){
//							if(ilala++ > 5) break;
//						}else {
//							transferMessage(lala,byteBuf);
//							// responseClient(byteBuf, true);
//							ilala = 0;
//						}
//						ins.read(first, 0, 1);
						/********liuyan update 2015-1-29 *********/
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			
			private void transferMessage(String chatId, String isPublice, ByteBuffer byteBuf) {
				// 转发消息
				// 根据互动室 查询用户 获得connection
				List<String> olUsers = SignChatroomUtil.getInstance().sendList(chatId, isPublice);
				if(olUsers == null)
					return ;
System.out.println("互动室 ‘" + chatId + "’ 在线用户有：" + olUsers);
				for (String oluser : olUsers) {
					try {
						Socket connect;
						if (((connect = UtilUser.getInstance().getConnet(oluser)) != null) && (connect instanceof Socket)) {
							if(connect.isConnected()==true&&connect.isClosed() == false){
							transferMessage1(
									true,
									byteBuf,
									new DataOutputStream(connect
											.getOutputStream()));
System.out.println("转发…………………………给 " + oluser);
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			private void transferMessage1(boolean finalFragment,
					ByteBuffer byteBuf, DataOutputStream outs)
					throws IOException {

				// OutputStream out = socket.getOutputStream();
				int first = 0x00;
				// 是否是输出最后的WebSocket响应片段
				if (finalFragment) {
					first = first + 0x80;
					first = first + 0x1;
				}
				outs.write(first);

				if (byteBuf.limit() < 126) {
					outs.write(byteBuf.limit());
				} else if (byteBuf.limit() < 65536) {
					outs.write(126);
					outs.write(byteBuf.limit() >>> 8);
					outs.write(byteBuf.limit() & 0xFF);
				} else {
					// Will never be more than 2^31-1
					outs.write(127);
					outs.write(0);
					outs.write(0);
					outs.write(0);
					outs.write(0);
					outs.write(byteBuf.limit() >>> 24);
					outs.write(byteBuf.limit() >>> 16);
					outs.write(byteBuf.limit() >>> 8);
					outs.write(byteBuf.limit() & 0xFF);

				}
				// Write the content
				outs.write(byteBuf.array(), 0, byteBuf.limit());
				outs.flush();

			}
			
			/**
			 * 获得消息字符串
			 * @param array
			 * @return
			 */
			private String getMsg(byte[] array){
				ByteArrayInputStream byteIn = new ByteArrayInputStream(array);
				InputStreamReader reader = new InputStreamReader(byteIn,
						charset.newDecoder());
				int b = 0;
				String res = "";
				try {
					while ((b = reader.read()) > 0) {
						res += (char) b;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
System.out.println("消息："+res);
				return res;
			}

//			private String printRes(byte[] array) {
//				ByteArrayInputStream byteIn = new ByteArrayInputStream(array);
//				InputStreamReader reader = new InputStreamReader(byteIn,
//						charset.newDecoder());
//				int b = 0;
//				String res = "";
//				try {
//					while ((b = reader.read()) > 0) {
//						res += (char) b;
//					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//System.out.println("消息："+res);
//				if("".equals(res)){
//					return "";
//				}
//				return res.substring(0,res.indexOf("##"));
//			}

		}

	}

	interface socketCallbackInterface1{
    	public void closeSocket();
    }

}


