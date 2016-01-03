package test;

import client.Node;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("running");
		final String SERVER_HOST = "localhost";
		final int SERVER_PORT = 8888;
		Node n = new Node(SERVER_HOST,SERVER_PORT,"127.0.0.1",7780,"Isuru");
		n.register();
	}

}
