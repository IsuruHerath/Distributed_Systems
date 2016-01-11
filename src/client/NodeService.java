package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class NodeService extends Thread{
	
	private int port;
	private DatagramSocket server;
	private Node node;
	public NodeService(int port, Node node) throws SocketException{
		this.port = port;
		this.node = node;
		server = new DatagramSocket(port);
	}
	
	public void run(){
		while(true){
			byte[] data = new byte[300];
			DatagramPacket receivePacket =new DatagramPacket(data, data.length);
			try {
				server.receive(receivePacket);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Got a connection. Now serving...");
			String responce = new String(receivePacket.getData());
			NodeServiceSession session = new NodeServiceSession(responce,node);
			session.start();
		}
	}
}
