package test;

import client.Node;

public class Main {

	public static void main(String[] args) {
		final String SERVER_HOST = "127.0.0.1";
		final int SERVER_PORT = 8888;
		Node n1 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1",7780,"node1");
		Node n2 = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1",7781,"node2");
		n1.register();
		n2.register();
		n1.sendLeave("127.0.0.1", 7781);
	}

}
