package Utils;

import client.ClientProtocol;

public class Requests {
	
	private static String getRequest(String type, String data){
		String message = " "+type+" "+data;
		message = String.format("%04d", message.length()+4)+message;
		//System.out.println(message);
		return message;
	}
	
	public static String getRegisterRequest(String host,int port,String username){
		String data = host+" "+port+" "+username;
		return getRequest(ClientProtocol.REGISTER,data);
	}
	
	public static String getUnregisterRequest(String host,int port,String username){
		String data = host+" "+port+" "+username;
		return getRequest(ClientProtocol.UNREGISTER,data);
	}
	
	public static String getJoinRequest(String host,int port){
		String data = host+" "+port;
		return getRequest(ClientProtocol.JOIN,data);
	}
	
	public static String getLeaveRequest(String host,int port){
		String data = host+" "+port;
		return getRequest(ClientProtocol.LEAVE,data);
	}
	
	public static String getSearchRequest(String host,int port, String filename, int hops){
		String data = host+" "+port+" "+filename+" "+hops;
		return getRequest(ClientProtocol.SEARCH,data);
	}
}
