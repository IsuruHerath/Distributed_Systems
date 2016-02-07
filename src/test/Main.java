package test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Scanner;

import client.ClientProtocol;
import node.Node;

public class Main {

	public static void main(String[] args) throws SocketException, MalformedURLException {
		final String SERVER_HOST	= args[0];
		final int SERVER_PORT		= Integer.parseInt(args[1]);
		final int NODE_PORT			= Integer.parseInt(args[2]);
		
		Node node = new Node(SERVER_HOST,SERVER_PORT,NODE_PORT,args[3]);
		node.register();
		Scanner scan = new Scanner(System.in);
		
		String command = scan.next();
		while(!command.equalsIgnoreCase("EXIT")){
			if(command.equalsIgnoreCase(ClientProtocol.REGISTER)){
				node.register();
			}else if(command.equalsIgnoreCase(ClientProtocol.UNREGISTER)){
				node.unregister();
			}else if(command.equalsIgnoreCase(ClientProtocol.SEARCH)){
				System.out.print("Insert file name : ");
				String filename = scan.next();
				node.searchFile(filename);
			}else{
				System.out.println("Wrong command. Try one of followings.");
				System.out.println("    1.REG");
				System.out.println("    2.UNREG");
				System.out.println("    3.SER");
				System.out.println("    4.EXIT");
			}
			command = scan.next();
		}
		System.exit(0);
	}

}
