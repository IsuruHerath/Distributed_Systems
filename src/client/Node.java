package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import Utils.Messages;

public class Node {
	//class variables
	private String SERVER_IP;
	private int SERVER_PORT;
	
	private String MY_IP;
	private int MY_PORT;
	private String HOST_NAME;
	private NodeService server;
	private Vector<String> routingTable;
	private HashSet<String> fileList = new HashSet<String>();
	
	//constructor
	public Node(String ip,int port,String myip,int myPort,String hostname) throws SocketException{
		SERVER_IP	= ip;
		SERVER_PORT	= port;		
		MY_IP		= myip;
		MY_PORT		= myPort;
		HOST_NAME	= hostname;
		server = new NodeService(MY_PORT,this);
		server.start();
		routingTable=new Vector<String>();
	}
	
	//register to the system
	public void register(){
		try {
			Socket socket = new Socket(SERVER_IP,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
	        out.print(Messages.getRegisterRequest(MY_IP, MY_PORT, HOST_NAME));
	        out.flush();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String responce = "";
			responce = reader.readLine();
	        socket.close();
	        System.out.println(responce);
	        String[] s = responce.split(" ");
	        String operation = s[1];
			
			if(operation.equalsIgnoreCase(ClientProtocol.REGISTER_OK)){
				int nodeCount = Integer.parseInt(s[2]);
				if(nodeCount == 1){
					addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
				}else if(nodeCount == 2){
					addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
					addNodeToRoutingTable(s[5], Integer.parseInt(s[6]));
				}else if(nodeCount > 2){
					int host1 = (int)(Math.random()*nodeCount);
					int host2 = (int)(Math.random()*nodeCount);
					while(host1 == host2){
						host2 = (int)(Math.random()*nodeCount);
					}
					addNodeToRoutingTable(s[2*host1+3], Integer.parseInt(s[2*host1+4]));
					addNodeToRoutingTable(s[2*host2+3], Integer.parseInt(s[2*host2+4]));
				}
				String host	= s[2];
				int port	= Integer.parseInt(s[3]);
				int value;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	//unregister from the system
	public void unregister(){
		try {
			Socket socket = new Socket(SERVER_IP,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
	        out.print(Messages.getUnregisterRequest(MY_IP, MY_PORT, HOST_NAME));
	        //out.println(Requests.getRegisterMesage("129.82.123.45", 5001, "1234abcd"));
	        out.flush();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String responce = "";
	        responce = reader.readLine();
	        socket.close();
	        System.out.println(responce);
	        String[] s = responce.split(" ");
	        String operation = s[1];
			
			if(operation.equalsIgnoreCase(ClientProtocol.UNREGISTER_OK)){
				
				String host	= s[2];
				int port	= Integer.parseInt(s[3]);
				int value;
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void massageUDP(String msg, String ip, int port){
		try{
			DatagramSocket socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip); 
			
			byte[] outToServer = new byte[300];
		  
			outToServer = msg.getBytes(); 
			DatagramPacket sendPacket =new DatagramPacket(outToServer, outToServer.length, IPAddress, port);
	        socket.send(sendPacket);
			socket.close();
		}catch(Exception e){
			System.out.println("Error sending " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	//join with the nodes
	private void sendJoin(String ip,int port){
		massageUDP(Messages.getJoinRequest(MY_IP, MY_PORT),ip,port);
	}
	
	//leave the System
	public void sendLeave(String ip,int port){
		massageUDP(Messages.getLeaveRequest(MY_IP, MY_PORT),ip,port);
	}
	
	//send the search request to specific node
	public void sendSearch(String filename,int hops,String ip,int port){
		massageUDP(Messages.getSearchRequest(MY_IP, MY_PORT, filename, hops),ip,port);
	}
	
	public void respondLeave(String ip,int port,int value){
		massageUDP(Messages.getLeaveRespond(value),ip,port);
	}
	
	public void respondJoin(String ip,int port,int value){
		massageUDP(Messages.getJoinRespond(value),ip,port);
	}
	
	public void respondSearch(String ip,int port,int no_of_files, String filenames, int hops ){
		massageUDP(Messages.getSearchRespond(MY_IP, MY_PORT, no_of_files, filenames, hops),ip,port);
	}
	
	//search for a filename
	public String search(String fileName){
		String results = null;
		Iterator<String> itr = fileList.iterator();
		String s;
		while(itr.hasNext()){
			s = itr.next();
			if(s.contains(fileName)){
				if(results == null){
					results = fileName;
				}else{
					results = results + " " +fileName;
				}
			}
		}
		if(results == null){
			results = "";
		}
		return results;
	}
	
	
	public boolean removeNodeFromRountingTable(String ip,int port){
		
		String str=ip+" "+port;
		if(routingTable.contains(str)){
			routingTable.remove(str);
			return true;
		}
		
		else{
			return false;
		}
		
	}
	
	public boolean addNodeToRoutingTable(String ip,int port){
		
		
		String str = ip+" "+port;
		
		if(!routingTable.contains(str)){
			routingTable.add(str);
			sendJoin(ip, port);
			return true;
		}
		
		else{
			return false;
		}
	}
	
	public Vector<String> getRoutingTable(){
		return routingTable;
	}
	
	public void addFile(String filename){
		fileList.add(filename);
	}
	
}

