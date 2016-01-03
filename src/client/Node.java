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
	
	//constructor
	public Node(String host,int port,String myHost,int myPort,String username){
		SERVER_HOST	= host;
		SERVER_PORT	= port;
		
		MY_HOST		= host;
		MY_PORT		= port;
		USER_NAME	= username;
	}
	
	public void register(){
		try {
			Socket socket = new Socket(SERVER_HOST,SERVER_PORT);
			System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String response = "";
	        out.println(Requests.getRegisterMesage(MY_HOST, MY_PORT, USER_NAME));
	        //out.println(Requests.getRegisterMesage("129.82.123.45", 5001, "1234abcd"));
	        out.flush();
	        response = reader.readLine();
	        System.out.println(response);
	        socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
