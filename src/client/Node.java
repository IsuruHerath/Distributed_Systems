package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import Utils.Requests;

public class Node {
	//class variables
	private String SERVER_HOST;
	private int SERVER_PORT;
	
	private String MY_HOST;
	private int MY_PORT;
	private String USER_NAME;
	private NodeService server;
	
	//constructor
	public Node(String host,int port,String myHost,int myPort,String username){
		SERVER_HOST	= host;
		SERVER_PORT	= port;
		
		MY_HOST		= myHost;
		MY_PORT		= myPort;
		USER_NAME	= username;
		server = new NodeService(MY_PORT,this);
		server.start();
	}
	
	//register to the system
	public void register(){
		try {
			Socket socket = new Socket(SERVER_HOST,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Requests.getRegisterRequest(MY_HOST, MY_PORT, USER_NAME));
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
			Socket socket = new Socket(SERVER_HOST,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Requests.getUnregisterRequest(MY_HOST, MY_PORT, USER_NAME));
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
	
	//join with the nodes
	private void join(String host,int port){
		try {
			Socket socket = new Socket(host,port);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.println(Requests.getJoinRequest(MY_HOST, MY_PORT));
	        out.flush();
	        response = reader.readLine();
	        System.out.println(response);
	        socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
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
	        out.println(Requests.getLeaveRequest(MY_HOST, MY_PORT));
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
			Socket socket = new Socket(SERVER_HOST,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.print(Requests.getSearchRequest(MY_HOST, MY_PORT, filename, hops));
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
	
	public void removeNodeFromRountingTable(String host,int port){
		//TODO remove host:ip from routing table
	}
	
	public void addNodeToRoutingTable(String host,int port){
		//TODO add host:ip to routing table
	}
	
}
