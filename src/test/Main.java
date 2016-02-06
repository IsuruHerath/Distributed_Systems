package test;

import java.net.SocketException;

import node.Node;

public class Main {

	public static void main(String[] args) throws SocketException {
		final String SERVER_HOST = "127.0.0.1";
		final int SERVER_PORT = 8888;
		Node n1 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1","node1");
		n1.register();
		Node n2 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1","node2");
		n2.register();
		Node n3 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1","node3");
		n3.register();
		n3.searchFile("bot");
		//Node n2 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1",7781,"node2");
		//n1.register();
		//n2.register();
		//n1.sendLeave("127.0.0.1", 7781);
		//System.out.println((int)(Math.random()*4));
	}

}
