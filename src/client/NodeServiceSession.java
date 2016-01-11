package client;


public class NodeServiceSession extends Thread{
	private String responce;
	private Node node;
	public NodeServiceSession(String receive,Node node){
		this.responce	= receive;
		this.node		= node;
	}
	
	public void run(){
		String[] s = responce.split(" ");
		//TODO validate request
		System.out.println(s[1]);
		String operation = s[1];
				
		if(operation.equalsIgnoreCase(ClientProtocol.JOIN)){
			
			String host	= s[2];
			int port	= Integer.parseInt(s[3]);
			boolean response=node.addNodeToRoutingTable(host, port);
			int value;
			
			if(response==true){
				value=0;
				}
			
			else{
				value=9999;				
			}
			node.respondJoin(host,port,value);
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.SEARCH)){
			String filename = s[4];
			int hops = Integer.parseInt(s[5]);
			String results = node.search(filename);
			if(hops > 0){
				//To Do - send search request to all naubours if the file is not in the node.
				//node.sendSearch(filename, hops-1,routingip,routingport);
			}
		}
		else if(operation.equalsIgnoreCase(ClientProtocol.LEAVE)){
			
			String host	= s[2];
			int port	= Integer.parseInt(s[3]);
			boolean response=node.removeNodeFromRountingTable(host, port);
			
			int value;
			
			if(response==true){
				value=0;
				}
			
			else{
				value=9999;				
			}
			
			node.respondLeave(host,port,value);
		}
		else if (operation.equalsIgnoreCase(ClientProtocol.REGISTER_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.UNREGISTER_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.JOIN_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.LEAVE_OK)){}
		else if (operation.equalsIgnoreCase(ClientProtocol.SEARCH_OK)){}
				
		else{
			System.out.println("Error");
		}
		//according to the s[1] do the business
		//leave : remove ip from routing table
		//join : add ip to routing table
		//search : search for the filename
	}
}
