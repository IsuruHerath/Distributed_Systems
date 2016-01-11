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
			
			String host			= s[2];
			int port			= Integer.parseInt(s[3]);
			boolean response 	= node.addNodeToRoutingTable(host, port);
			int value;
			
			if(response==true){
				value	= 0;
			}else{
				value	= 9999;				
			}
			node.respondJoin(host,port,value);
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.SEARCH)){
			String host		= s[2];
			int port		= Integer.parseInt(s[3]);
			String filename = s[4];
			int hops 		= Integer.parseInt(s[5]);
			if(hops > 0){
				Vector<String> table = node.getRoutingTable();
				for(String entry : table){
					String data[] = entry.split(" ");
					node.massageUDP(Messages.getSearchRequest(host, port, filename, hops-1),data[0],Integer.parseInt(data[1]));
				}
			}
			String results	= node.search(filename);
			int fileCount 	= results.split(" ").length;
			node.respondSearch(host, port, fileCount, results, hops);
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.LEAVE)){
			
			String host			= s[2];
			int port			= Integer.parseInt(s[3]);
			boolean response	= node.removeNodeFromRountingTable(host, port);
			
			int value;
			
			if(response == true){
				value	= 0;
			}else{
				value	= 9999;				
			}
			
			node.respondLeave(host,port,value);
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.REGISTER_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.UNREGISTER_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.JOIN_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.LEAVE_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.SEARCH_OK)){
			int fileCount = Integer.parseInt(s[2]);
			String ip	= s[3];
			int port 	= Integer.parseInt(s[4]);
			int hops 	= Integer.parseInt(s[5]);
			
			for(int i = 0;i<fileCount;i++){
				node.addFile(s[i+6]);
			}
		}
				
		else{
			System.out.println("Error");
		}
		//according to the s[1] do the business
		//leave : remove ip from routing table
		//join : add ip to routing table
		//search : search for the filename
	}
}
