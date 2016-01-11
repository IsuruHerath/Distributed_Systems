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
		String host	= s[2];
		int port	= Integer.parseInt(s[3]);
		if(operation.equalsIgnoreCase(ClientProtocol.JOIN)){
			node.addNodeToRoutingTable(host, port);
		}else if(operation.equalsIgnoreCase(ClientProtocol.SEARCH)){
			String filename = s[4];
			int hops = Integer.parseInt(s[5]);
			String results = node.search(filename);
			if(hops > 0){
				//To Do - send search request to all naubours if the file is not in the node.
				//node.sendSearch(filename, hops-1,routingip,routingport);
			}
		}else if(operation.equalsIgnoreCase(ClientProtocol.LEAVE)){
			node.removeNodeFromRountingTable(host, port);
		}else{
			System.out.println("Error");
		}
		//according to the s[1] do the business
		//leave : remove ip from routing table
		//join : add ip to routing table
		//search : search for the filename
	}
}
