package Utils;

import java.util.Date;

public class Util {
	public static void getTime(){
		Date d = new Date();
		System.out.println(d.getTime());
	}
	
	public static String getMessageID(String host, int port){
		String ID = new Date().getTime()+":"+host+":"+port;
		return ID;
	}
}
