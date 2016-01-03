package client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeService extends Thread{
	
	private int port;
	private ServerSocket server;
	public NodeService(int port){
		this.port = port;
	}
	
	public void run(){
		try {
			server = new ServerSocket(port);
			while(true){
				Socket socket = server.accept();
				System.out.println("Got a connection. Now serving...");
				NodeServiceSession session = new NodeServiceSession(socket);
				session.start();
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		System.out.println("Exiting from Node Service");
	}
}
