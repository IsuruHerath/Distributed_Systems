package node;

import java.net.MalformedURLException;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService(endpointInterface="node.NodeService")
@SOAPBinding(style=Style.DOCUMENT)
public class NodeServiceImpl implements NodeService{

	Node node;
	
	public NodeServiceImpl(Node node){
		this.node = node;
	}
	
	
	public void join(String message) {
		System.out.println(message);
		node.processJoin(message);
	}

	
	public void leave(String message) {		
		System.out.println(message);
		node.leaveNode(message);
		
	}

	
	public void search(String message) {
		System.out.println(message);
		node.processSearch(message);
	}

	
	public void joinOK(String message) {
		System.out.println(message);
		// TODO Auto-generated method stub
	}

	
	public void leaveOK(String message) {
		System.out.println(message);
		node.leaveOK(message);
	}

	
	public void searchOK(String message) {
		System.out.println(message);
		node.processSearchOK(message);
		// TODO Auto-generated method stub
	}
}
