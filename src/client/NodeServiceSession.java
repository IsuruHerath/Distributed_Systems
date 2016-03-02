package client;

import java.util.Vector;

import Utils.Messages;
import Utils.Util;


public class NodeServiceSession extends Thread{
	private String response;
	private Node node;
	public NodeServiceSession(String receive,Node node){
		this.response	= receive;
		this.node		= node;
	}
	
	public void run(){
		Util.getTime();
		String[] s = response.trim().split(" ");
		//TODO validate request
		System.out.println(s[1]);
		String operation = s[1];
				
		if(operation.equalsIgnoreCase(ClientProtocol.JOIN)){
			System.out.println(response.trim());
			node.processJoin(response.trim());
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.SEARCH)){
			System.out.println(response.trim());
			node.processSearch(response.trim());
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.LEAVE)){
			System.out.println(response.trim());
			node.processLeave(response.trim());
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.JOIN_OK)){
			System.out.println(response.trim());
			int value=Integer.parseInt(s[2]);
			if(value == 0){
				System.out.println("Successful");
			}
			else{
				System.out.println("Error while adding new node to routing table");
			}
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.LEAVE_OK)){	
			System.out.println(response.trim());
			node.processLeaveOK(response.trim());
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.SEARCH_OK)){
			System.out.println(response.trim());
			node.processSearchOK(response.trim());
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.ERROR)){	
			System.out.println("Error");			
		}
		else{
			System.out.println("Error");
		}
	}
}