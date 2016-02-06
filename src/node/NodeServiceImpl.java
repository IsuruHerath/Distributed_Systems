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
	public String join(String message) {
		// TODO Auto-generated method stub
		return "response";
	}

	@Override
	public String leave(String message) {
		// TODO Auto-generated method stub
		return "response";
	}

	@Override
	public String search(String message) {
		// TODO Auto-generated method stub
		return "response";
	}

}
