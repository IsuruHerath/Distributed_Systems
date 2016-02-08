package node;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import Utils.Timer;

@WebService(endpointInterface="node.NodeService")
@SOAPBinding(style=Style.DOCUMENT)
public class NodeServiceImpl implements NodeService{

	Node node;
	
	public NodeServiceImpl(Node node){
		this.node = node;
	}
	
	
	public void join(String message) {
		Timer.getTime();
		System.out.println(message);
		node.processJoin(message);
	}

	
	public void leave(String message) {
		Timer.getTime();
		System.out.println(message);
		node.leaveNode(message);
		
	}

	
	public void search(String message) {

		Timer.getTime();
		System.out.println(message);
		node.processSearch(message);
	}

	
	public void joinOK(String message) {
		Timer.getTime();
		System.out.println(message);
	}

	
	public void leaveOK(String message) {
		Timer.getTime();
		System.out.println(message);
		node.leaveOK(message);
	}

	
	public void searchOK(String message) {
		Timer.getTime();
		System.out.println(message);
		node.processSearchOK(message);
	}
}
