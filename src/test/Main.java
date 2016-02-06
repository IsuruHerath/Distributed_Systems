package test;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.HashMap;

import Utils.HostPortMapper;
import node.Node;

public class Main {

	public static void main(String[] args) throws SocketException, MalformedURLException {
		final String SERVER_HOST = "127.0.0.1";
		final int SERVER_PORT = 8888;
		Node n1 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1","node1");
		Node n2 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1","node2");
		n1.callJoin();
		//Node n2 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1",7781,"node2");
		//n1.register();
		//n2.register();
		//n1.sendLeave("127.0.0.1", 7781);
		//System.out.println((int)(Math.random()*4));
	}

}
