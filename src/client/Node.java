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
	private String USER_NAME;
	private NodeService server;
	
	//constructor
	public Node(String ip,int port,String myip,int myPort,String username){
		SERVER_IP	= ip;
		SERVER_PORT	= port;
		
		MY_IP		= myip;
		MY_PORT		= myPort;
		USER_NAME	= username;
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
	        out.print(Messages.getRegisterRequest(MY_IP, MY_PORT, USER_NAME));
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
	        out.print(Messages.getUnregisterRequest(MY_IP, MY_PORT, USER_NAME));
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
	private void join(String ip,int port){
		massageUDP(Messages.getJoinRequest(MY_IP, MY_PORT),ip,port);
	}
	
	//leave the System
	public void leave(String host,int port){
		try {
			Socket socket = new Socket(host,port);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.println(Messages.getLeaveRequest(MY_IP, MY_PORT));
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
	
	//search for a filename
	public void search(String filename,int hops){
		try {
			Socket socket = new Socket(SERVER_IP,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Messages.getSearchRequest(MY_IP, MY_PORT, filename, hops));
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
