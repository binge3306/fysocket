package test.java;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fy.msgsys.server.util.SocketConstant;

public class Testyu {

	public static void main(String[] args) {

		new Testyu().test16(args);
		System.out.println("111");
	}
	private void test16(String [] la){
		for(int i = 0;i<la.length;i++){
			System.out.println(la[i]);
			
		}
	}
	
	private void test15(){
		
		String la = "c";
		System.out.println((int)la.charAt(0));
		System.out.println(99);
	}
	
	private void test14(){
		if(562365236 > Math.pow(3, 5))
			System.out.println((int)Math.pow(2,31));//Math.pow(3, 5)
		else 
			System.out.println("33");
	}
	
	private void test13(){
		String la = "chatroom1##0###";
		String me[] = la.split("##");
		System.out.println(me[0]);
		System.out.println(me[1].charAt(0));
	}
	
	private void test12(){
		System.out.println((int) 0x00);
	}
	
	
	private void test11(){
		
		int len = 56;
		if(len < 126)
			System.out.println("1");
		else if(len < 255){
			System.out.println("2");
		}else{
			System.out.println("12");
		}
		
	}
	
	private void test10(){
		int len = 65536-2000;
		int len1 = 65536-2000;
		System.out.println(len>>> 8);
		System.out.println(len1 & 0xFF);
	}
	
	
	public void test9(){
		int first = 0x00;
		first = first + 0x80;
		System.out.println(first);
		first = first + 0x1;

		System.out.println(first);
		System.out.println(0x1);
	}
	
	public void test8(){
		// 测试  break  continue+
		for(int i= 0;i<5;i++){
			for(int j = 0;j<5;j++){
				if(j%2==0 && i%2 ==0 )
					continue;
				System.out.println("chatroom" + j);
			}
		}
	}
	
	public void test7(){
		// 测试 enum java
		System.out.println(SocketConstant.createBPChat);
		System.out.println(SocketConstant.createBPChat.getRssURL());
	}
	
	public void test6(){
		String lla = "22356##2563";
		String tt[] = lla.split("##");
		System.out.println(""+ tt.length);
	}
	
	public void test5(){
		Map<String,String> lala =  new ConcurrentHashMap<String, String>();
		
		lala.put("wurunzhou","666666666");
		lala.put("222", "2222");
		for(int i=0;i<6;i++){
			lala.put(("1"+i),"lal"+i);
		}
		lala.put("wurunzhou", "33333333");
		lala.remove("2");
		
		List<String> lm = new ArrayList<String>( lala.keySet());
		System.out.println(lm);
		String llm = lm.toString();
		ArrayList<String> mama = new ArrayList();
		Collections.addAll(mama, llm.substring(1, llm.length()-1).split(", "));
		for(String str:mama){
			System.out.println(str);
		}

	}
	
	public void test4(){
		// testday
		Date now  = new Date();
		System.out.println(formateTimeString(now));
		Calendar now2 = Calendar.getInstance();
		now2.setTime(now);
		now2.set(Calendar.DATE, now2.get(Calendar.DATE)-31);
		System.out.println(formateTimeString(now2.getTime()));
	}
	
	private String formateTimeString(Date dateTime){
		
		SimpleDateFormat simpleDateTimeFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");		
		return simpleDateTimeFormat.format(dateTime);
	}
	
	public void test3(){
		//String alla = "{"chatroomViewId":20840,"chatroomName":"2223","roomType":1,"content":"22","sendDate":"2015-01-12 16:29","senderAccount":"test1","senderName":"testests","chatRoomId":20236,"messageId":22363,"receiverAccounts":"wurunzhou,test2,test3,","type":1,"attachements":null}";
	
		Map<String,String> lala =  new ConcurrentHashMap<String, String>();
		
		lala.put("wurunzhou","666666666");
		lala.put("222", "2222");
		for(int i=0;i<6;i++){
			lala.put(("1"+i),"lal"+i);
		}
		lala.put("wurunzhou", "33333333");
		lala.remove("2");
		
		for(String la:lala.keySet()){
			System.out.println(lala.get(la));
		}
	}
	
	
	public void test1(){
		for(int i=0;i<10 ;i++){
			for(int j=0;j<20 ;j++){
				if(j==5){
					System.out.println(j);
					return;
				}
				System.out.println("_________"+j);
			}
		}
	}
	
	public void test2(){
		Map<String , List<String>> mm = new HashMap<String, List<String>>();
		for(int i=0;i<10;i++){
			List<String> li = new ArrayList<String>();
			for(int j=0;j<10;j++){
				li.add(j+"");
			}
			mm.put(i+"", li);
		}
		for(String list :mm.keySet()){
			for(String lala :mm.get(list)){
				if("5".equals(lala)){
					mm.get(list).remove("5");
					break;
				}
			}
		}
		
		for(String key :mm.keySet()){
			List<String > ll = mm.get(key);
			for(String lala :ll){
				System.out.print(lala+"  ");
			}
			System.out.println("");
			
		}
	}
	public void stat(){
		String lala;
		test(lala = retest());
	}
	
	public void test(String lal){
		if(lal == null)
			System.out.println("ok");
		else
			System.out.println("2");
	}
	public String retest(){
		return "22";
	}

	class ReliableWebServer {}
}
