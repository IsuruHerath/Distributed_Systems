package Utils;

import java.util.HashMap;

public class HostPortMapper {
	
	private static final int STARTING_PORT = 7780;
	private static HashMap<String,Integer> mapper = new HashMap<String, Integer>();
	
	public HostPortMapper(String serverHost, int serverPort){
		mapper.put(serverHost, 1);
	}
	
	public static int getPort(String nodeServer){
		int portCount = 0;
		if(mapper.get(nodeServer) != null){
			portCount = mapper.get(nodeServer);
		}
		mapper.put(nodeServer, portCount+1);
		return STARTING_PORT + portCount;
	}
}
