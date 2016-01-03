package Utils;

public class Requests {
	
	public static String getRegisterMesage(String host,int port,String username){
		int length = 8 + 3 + host.length() + Integer.toString(port).length() + username.length();
		int digits = Integer.toString(length).length();
		String message = length+" REG "+host+" "+port+" "+username;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return "0036 REG "+host+" "+port+" "+username;
	}
}
