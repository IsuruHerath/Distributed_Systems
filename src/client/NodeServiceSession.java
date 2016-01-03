package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeServiceSession extends Thread{
	private Socket sessionSocket;
	private Node node;
	public NodeServiceSession(Socket socket,Node node){
		sessionSocket	= socket;
		this.node		= node;
	}
	
	public void run(){
		try {
			InputStream inpStrm	= sessionSocket.getInputStream();
			BufferedReader br	= new BufferedReader(new InputStreamReader(inpStrm));
			PrintWriter out		= new PrintWriter(sessionSocket.getOutputStream());
			String response 	= "";
			
			System.out.println("next line");
			response = br.readLine();
			String[] s = response.split(" ");
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
				if(hops > 0){
					String results = node.search(filename);
				}else{
					node.search(filename, hops-1);
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
			
			out.println("Sample out put");
			out.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
