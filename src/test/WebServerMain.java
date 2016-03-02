package test;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.Scanner;

import node.Node;
import Utils.Consts;
import Utils.Util;
import client.ClientProtocol;

public class WebServerMain {

	public static void main(String[] args) throws SocketException, MalformedURLException {
		final String SERVER_HOST	= Util.getProperty(Consts.SERVER_HOST);
		final int SERVER_PORT		= Integer.parseInt(Util.getProperty(Consts.SERVER_PORT));
		final int NODE_PORT			= Integer.parseInt(Util.getProperty(Consts.NODE_PORT));
		
		Node node = new Node(SERVER_HOST,SERVER_PORT,NODE_PORT,Util.getProperty(Consts.USER_NAME));
		node.register();
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter on the folowing commands.");
		System.out.println("    1.register");
		System.out.println("    2.unregister");
		System.out.println("    3.search");
		System.out.println("    4.files");
		System.out.println("    5.hosts");
		System.out.println("    6.exit");
		System.out.println("    7.stat");
		String command = scan.nextLine();
		while(!command.equals("exit")){
			if(command.equals("register")){
				node.register();
			}else if(command.equals("unregister")){
				node.unregister();
			}else if(command.equals("search")){
				System.out.print("Insert file name : ");
				String filename = scan.nextLine();
				node.searchFile(filename);
			}else if(command.equals("files")){
				node.getFileList();
			}else if(command.equals("hosts")){
				node.getHostList();
			}else if(command.equals("stat")){
				node.getStat();
			}else{
				System.out.println("Wrong command. Try one of followings.");
				System.out.println("    1.register");
				System.out.println("    2.unregister");
				System.out.println("    3.search");
				System.out.println("    4.files");
				System.out.println("    5.hosts");
				System.out.println("    6.exit");
				System.out.println("    7.stat");
			}
			command = scan.nextLine();
		}
		System.exit(0);
	}

}
