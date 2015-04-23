package edu.fy.com.websocketServer.verifyReactor;

import java.util.concurrent.TimeUnit;

public class CreateUserUtil {

	
	public void start(){
		new Thread(new CreateThread()).start();
		new Thread(new checkThread()).start();
	}
	
	class CreateThread implements Runnable{

		@Override
		public void run() {
			int i = 0;
			while(true){
				i++;
				try {
					TimeUnit.SECONDS.sleep(4);
					UtilUser.getInstance().add("user"+i, "verify"+i);
					System.out.println("===" + i);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
		}
		
	}
	
	class checkThread implements Runnable{

		@Override
		public void run() {
			
			while(true){
				try{
					TimeUnit.SECONDS.sleep(8);
					UtilUser.getInstance().iterUser();
				}catch (InterruptedException e){
					e.printStackTrace();
				}
			}
		}
		
	}

}
