package node;

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
	
	@Override
	public void join(String message) {
		System.out.println(message);
		node.processJoin(message);
	}

	@Override
	public void leave(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void search(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void joinOK(String message) {
		System.out.println(message);
		// TODO Auto-generated method stub
	}

	@Override
	public void leaveOK(String message) {
		// TODO Auto-generated method stub
	}

	@Override
	public void searchOK(String message) {
		// TODO Auto-generated method stub
	}

}
