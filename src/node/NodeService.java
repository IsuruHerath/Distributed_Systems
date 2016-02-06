package node;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

@WebService
@SOAPBinding(style=Style.DOCUMENT)
public interface NodeService {
	public void join(String message);
	public void leave(String message);
	public void search(String message);
	public void joinOK(String message);
	public void leaveOK(String message);
	public void searchOK(String message);
}
