package node;

import javax.xml.ws.Endpoint;

public class Publisher extends Thread{
	Node node;
	public Publisher(Node node){
		this.node = node;
	}
	public void run(){
		String publishURI = "http://"+node.getIp()+":"+node.getPort()+"/server";
		System.out.println("Publishing Node Service at "+publishURI);
		Endpoint endpoint = Endpoint.publish(publishURI, new NodeServiceImpl(node));
		if(endpoint.isPublished()){
			System.out.println("Node Service successfully published.");
		}else{
			System.out.println("Node Service publishing failed.");
		}
	}
}
