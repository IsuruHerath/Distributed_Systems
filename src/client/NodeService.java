package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeService extends Thread{
	
	private int port;
	private ServerSocket server;
	private Node node;
	public NodeService(int port, Node node){
		this.port = port;
		this.node = node;
	}
	
	public void run(){
		try {
			server = new ServerSocket(port);
			while(true){
				Socket socket = server.accept();
				System.out.println("Got a connection. Now serving...");
				NodeServiceSession session = new NodeServiceSession(socket,node);
				session.start();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Exiting from Node Service");
	}
}
