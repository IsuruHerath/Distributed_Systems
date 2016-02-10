package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Scanner;

import client.ClientProtocol;
import node.Node;

public class WebServerMain {

	public static void main(String[] args) throws SocketException, MalformedURLException {
		final String SERVER_HOST	= args[0];
		final int SERVER_PORT		= Integer.parseInt(args[1]);
		final int NODE_PORT			= Integer.parseInt(args[2]);
		
		Node node = new Node(SERVER_HOST,SERVER_PORT,NODE_PORT,args[3]);
		node.register();
		Scanner scan = new Scanner(System.in);
		
		String command = scan.nextLine();
		while(!command.equalsIgnoreCase("EXIT")){
			if(command.equalsIgnoreCase(ClientProtocol.REGISTER)){
				node.register();
			}else if(command.equalsIgnoreCase(ClientProtocol.UNREGISTER)){
				node.unregister();
			}else if(command.equalsIgnoreCase(ClientProtocol.SEARCH)){
				System.out.print("Insert file name : ");
				String filename = scan.nextLine();
				node.searchFile(filename);
			}else{
				System.out.println("Wrong command. Try one of followings.");
				System.out.println("    1.REG");
				System.out.println("    2.UNREG");
				System.out.println("    3.SER");
				System.out.println("    4.EXIT");
			}
			command = scan.nextLine();
		}
		System.exit(0);
		/*String path = "/home/isuru/a.txt";
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader(fr);
			int[] count = new int[10];
			String line = br.readLine();
			while(line!=null){
				count[Integer.parseInt(line.split(":")[1].trim())]++;
				line = br.readLine();
			}
			for(int i=0;i<count.length;i++){
				System.out.println(i+":"+count[i]);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

}
