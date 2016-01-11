package Utils;

import client.ClientProtocol;

public class Messages {
	
	private static String getRequest(String type, String data){
		String message = " "+type+" "+data;
		message = String.format("%04d", message.length()+4)+message;
		//System.out.println(message);
		return message;
	}
	
	public static String getRegisterRequest(String ip,int port,String username){
		String data = ip+" "+port+" "+username;
		return getRequest(ClientProtocol.REGISTER,data);
	}
	
	public static String getUnregisterRequest(String ip,int port,String username){
		String data = ip+" "+port+" "+username;
		return getRequest(ClientProtocol.UNREGISTER,data);
	}
	
	public static String getJoinRequest(String ip,int port){
		String data = ip+" "+port;
		return getRequest(ClientProtocol.JOIN,data);
	}
	
	public static String getLeaveRequest(String ip,int port){
		String data = ip+" "+port;
		return getRequest(ClientProtocol.LEAVE,data);
	}
	
	public static String getSearchRequest(String ip,int port, String filename, int hops){
		String data = ip+" "+port+" "+filename+" "+hops;
		return getRequest(ClientProtocol.SEARCH,data);
	}
	
	public static String getSearchRespond(String ip,int port, int no_of_files, String filenames, int hops){
		String data = no_of_files+" "+ip+" "+port+" "+hops+" "+filenames;
		return getRequest(ClientProtocol.SEARCH_OK,data);
	}
	
	public static String getLeaveRespond(int value){
		String data = ""+value;
		return getRequest(ClientProtocol.LEAVE_OK,data);
	}
	
	public static String getJoinRespond(int value){
		String data = ""+value;
		return getRequest(ClientProtocol.JOIN_OK,data);
	}
}
