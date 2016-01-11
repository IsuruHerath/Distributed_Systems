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

import Utils.Messages;

public class Node {
	//class variables
	private String SERVER_IP;
	private int SERVER_PORT;
	
	private String MY_IP;
	private int MY_PORT;
	private String HOST_NAME;
	private NodeService server;
	
	//constructor
	public Node(String ip,int port,String myip,int myPort,String hostname){
		SERVER_IP	= ip;
		SERVER_PORT	= port;
		
		MY_IP		= myip;
		MY_PORT		= myPort;
		HOST_NAME	= hostname;
		server = new NodeService(MY_PORT,this);
		server.start();
	}
	
	//register to the system
	public void register(){
		try {
			Socket socket = new Socket(SERVER_IP,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Messages.getRegisterRequest(MY_IP, MY_PORT, HOST_NAME));
	        out.flush();
	        response = reader.readLine();
	        //TODO after selecting neigbours
	        //join("",0);
	        System.out.println(response);
	        socket.close();
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
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Messages.getUnregisterRequest(MY_IP, MY_PORT, HOST_NAME));
	        //out.println(Requests.getRegisterMesage("129.82.123.45", 5001, "1234abcd"));
	        out.flush();
	        response = reader.readLine();
	        System.out.println(response);
	        socket.close();
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
	
	//search for a filename
	public String search(String fileName){
		//TODO seraching
		return "result";
	}
	
	public void removeNodeFromRountingTable(String ip,int port){
		//TODO remove host:ip from routing table
	}
	
	public void addNodeToRoutingTable(String ip,int port){
		//TODO add host:ip to routing table
	}
	
}
