package Utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;


public class Util {
	public static long getTime(){
		Date d = new Date();
		long time = d.getTime();
		//System.out.println(time);
		return time;
	}
	
	public static String getMessageID(String host, int port){
		String ID = new Date().getTime()+":"+host+":"+port;
		return ID;
	}
	
	public static String getProperty(String property){
		Properties prop = new Properties();
		
		try {
			FileInputStream fi = new FileInputStream(Consts.CONFIG_PATH);
			prop.load(fi);
			fi.close();
			
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		
		return prop.getProperty(property);
	}
}
