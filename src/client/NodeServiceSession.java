package client;

import java.util.Vector;

import Utils.Messages;


public class NodeServiceSession extends Thread{
	private String response;
	private Node node;
	public NodeServiceSession(String receive,Node node){
		this.response	= receive;
		this.node		= node;
	}
	
	public void run(){
		String[] s = response.split(" ");
		//TODO validate request
		System.out.println(s[1]);
		String operation = s[1];
				
		if(operation.equalsIgnoreCase(ClientProtocol.JOIN)){
			
			node.processJoin(response);
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.SEARCH)){
			node.processSearch(response);
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.LEAVE)){
			node.processLeave(response);
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.JOIN_OK)){
			int value=Integer.parseInt(s[2]);
			if(value == 0){
				System.out.println("Successful");
			}
			else{
				System.out.println("Error while adding new node to routing table");
			}
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.LEAVE_OK)){	
			node.processLeaveOK(response);
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.SEARCH_OK)){
			node.processSearchOK(response);
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.ERROR)){	
			System.out.println("Error");			
		}
		else{
			System.out.println("Error");
		}
	}
}