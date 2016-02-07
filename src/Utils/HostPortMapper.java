package Utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
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
	public static String getIP(){
		String nodeIp = "127.0.0.1";
		try{
			for (NetworkInterface ni :	Collections.list(NetworkInterface.getNetworkInterfaces())) {
				if(ni.getName().equalsIgnoreCase("wlan0")){
					for (InetAddress address : Collections.list(ni.getInetAddresses())) {
						if (address instanceof Inet4Address) {
							nodeIp = address.toString().substring(1);
						}
					}
				}
			}
		}catch(Exception e){
	
		}
		return nodeIp;
	}
}
