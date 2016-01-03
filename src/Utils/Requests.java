package Utils;

public class Requests {
	
	public static String getRegisterRequest(String host,int port,String username){
		int length = 9 + 3 + host.length() + Integer.toString(port).length() + username.length();
		int digits = Integer.toString(length).length();
		String message = length+" REG "+host+" "+port+" "+username;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return message;
	}
	
	public static String getUnregisterRequest(String host,int port,String username){
		int length = 9 + 5 + host.length() + Integer.toString(port).length() + username.length();
		int digits = Integer.toString(length).length();
		String message = length+" UNREG "+host+" "+port+" "+username;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return message;
	}
	
	public static String getJoinRequest(String host,int port){
		int length = 8 + 4 + host.length() + Integer.toString(port).length();
		int digits = Integer.toString(length).length();
		String message = length+" JOIN "+host+" "+port;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return message;
	}
	
	public static String getLeaveRequest(String host,int port){
		int length = 8 + 5 + host.length() + Integer.toString(port).length();
		int digits = Integer.toString(length).length();
		String message = length+" LEAVE "+host+" "+port;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return message;
	}
	
	public static String getSearchRequest(String host,int port, String filename, int hops){
		int length = 10 + 3 + host.length() + Integer.toString(port).length()
				+filename.length() + Integer.toString(hops).length();
		int digits = Integer.toString(length).length();
		String message = length+" SER "+host+" "+port+" "+filename+" "+hops;
		for(int i = 0; i<4-digits;i++){
			message = "0"+message;
		}
		System.out.println(message);
		return message;
	}
}
