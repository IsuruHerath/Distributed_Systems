package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import Utils.HostPortMapper;
import Utils.Messages;
import Utils.Util;

public class Node {
	//class variables
	private String SERVER_IP;
	private int SERVER_PORT;
	
	private String MY_IP;
	private int MY_PORT;
	private String HOST_NAME;
	private NodeService server;
	private Vector<String> routingTable;
	private HashSet<String> fileList = new HashSet<String>();
	private final static int MAX_HOPS = 2;
	private HashSet<String> sessionQuries = new HashSet<String>();
	
	//constructor
	public Node(String ip,int port,int myPort,String hostname) throws SocketException{
		SERVER_IP	= ip;
		SERVER_PORT	= port;		
		MY_IP		= HostPortMapper.getIP();
		MY_PORT		= myPort;
		HOST_NAME	= hostname;
		server = new NodeService(MY_PORT,this);
		server.start();
		routingTable=new Vector<String>();
	}
	
	public String getIp() {
		return MY_IP;
	}

	public int getPort() {
		return MY_PORT;
	}

	public String getHostname() {
		
		return HOST_NAME;
	}

	// register to the system
	public void register() {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			out.print(Messages.getRegisterRequest(MY_IP, MY_PORT, HOST_NAME));
			out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String responce = "";
			responce = reader.readLine();
			socket.close();
			System.out.println(responce);
			String[] s = responce.split(" ");
			String operation = s[1];

			if (operation.equalsIgnoreCase(ClientProtocol.REGISTER_OK)) {
				int nodeCount = Integer.parseInt(s[2]);
				if (nodeCount == 1) {
					sendJoinRequest(s[3], Integer.parseInt(s[4]));
					addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
				} else if (nodeCount == 2) {
					sendJoinRequest(s[3], Integer.parseInt(s[4]));
					addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
					sendJoinRequest(s[6], Integer.parseInt(s[7]));
					addNodeToRoutingTable(s[6], Integer.parseInt(s[7]));
				} else if (nodeCount == 3) {
					int host1 = (int) (Math.random() * nodeCount);
					int host2 = (int) (Math.random() * nodeCount);
					while (host1 == host2) {
						host2 = (int) (Math.random() * nodeCount);
					}
					sendJoinRequest(s[3 * host1 + 3],
							Integer.parseInt(s[3 * host1 + 4]));
					addNodeToRoutingTable(s[3 * host1 + 3],
							Integer.parseInt(s[3 * host1 + 4]));
					sendJoinRequest(s[3 * host2 + 3],
							Integer.parseInt(s[3 * host2 + 4]));
					addNodeToRoutingTable(s[3 * host2 + 3],
							Integer.parseInt(s[3 * host2 + 4]));
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}

	// unregister from the system
	public void unregister() {
		try {
			Socket socket = new Socket(SERVER_IP, SERVER_PORT);
			System.out.println("Just connected to "
					+ socket.getRemoteSocketAddress());
			OutputStream outToServer = socket.getOutputStream();
			PrintWriter out = new PrintWriter(outToServer);
			out.print(Messages.getUnregisterRequest(MY_IP, MY_PORT, HOST_NAME));
			// out.println(Requests.getRegisterMesage("129.82.123.45", 5001,
			// "1234abcd"));
			out.flush();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String responce = "";
			responce = reader.readLine();
			socket.close();
			System.out.println(responce);
			String[] s = responce.split(" ");
			String operation = s[1];

			if (operation.equalsIgnoreCase(ClientProtocol.UNREGISTER_OK)) {

				/*String host = s[2];
				int port = Integer.parseInt(s[3]);
				int value;*/
				for (String entry : routingTable) {
					String data[] = entry.split(" ");
					sendLeaveRequest(data[0], Integer.parseInt(data[1]));
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

	}
	
	public void messageUDP(String msg, String ip, int port){
		try{
			DatagramSocket socket = new DatagramSocket();
			InetAddress IPAddress = InetAddress.getByName(ip); 
			
			byte[] outToServer = new byte[300];
		  
			outToServer = msg.getBytes(); 
			DatagramPacket sendPacket =new DatagramPacket(outToServer, outToServer.length, IPAddress, port);
	        socket.send(sendPacket);
			socket.close();
		}catch(Exception e){
			System.out.println("Error sending " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void searchFile(String filename) {
		for (String entry : routingTable) {
			String data[] = entry.split(" ");
			String msgID = Util.getMessageID(MY_IP, MY_PORT);
			sessionQuries.add(msgID);
			sendSearchRequest(data[0], Integer.parseInt(data[1]),
					Messages.getSearchRequest(msgID,MY_IP, MY_PORT, filename,
							1));
		}
	}

	private void sendJoinRequest(String ip, int port) {
		messageUDP(Messages.getJoinRequest(MY_IP, MY_PORT), ip, port);
	}

	private void sendJoinResponse(String ip, int port, int value) {
		messageUDP(Messages.getJoinRespond(value), ip, port);
	}

	private void sendLeaveRequest(String ip, int port){
		messageUDP(Messages.getLeaveRequest(MY_IP, MY_PORT), ip, port);
	}

	private void sendLeaveResponse(String ip, int port, int value) {
		messageUDP(Messages.getLeaveRespond(value), ip, port);
	}

	private void sendSearchRequest(String ip, int port, String query) {
		messageUDP(query, ip, port);
	}

	private void sendSearchResponse(String ip, int port, int no_of_files,
			String filenames, int hops) {
		messageUDP(Messages.getSearchRespond(MY_IP, MY_PORT, no_of_files,
				filenames, hops), ip, port);
	}

	// search for a filename
	private ArrayList<String> search(String[] fileNameWords) {
		ArrayList<String> results = new ArrayList<String>();
		Iterator<String> itr = fileList.iterator();
		String s;
		int fileNameWordCount = fileNameWords.length;
		int length = 0;
		while (itr.hasNext()) {
			s = itr.next();
			String[] words = s.split(" ");
			length = words.length;
			if(length>=fileNameWordCount){
				boolean matched = false;
				for(int i=0;i<=(length-fileNameWordCount);i++){
					matched = true;
					for(int j=0;j<fileNameWordCount;j++){
						if(!words[i].equalsIgnoreCase(fileNameWords[j])){
							matched = false;
							break;
						}
					}
					if(matched){
						results.add(s);
						break;
					}
				}
			}
			
		}
		return results;
	}

	public boolean removeNodeFromRountingTable(String ip, int port) {

		String str = ip + " " + port;
		if (routingTable.contains(str)) {
			routingTable.remove(str);
			return true;
		}

		else {
			return false;
		}

	}

	public boolean addNodeToRoutingTable(String ip, int port) {

		String str = ip + " " + port;

		if (!routingTable.contains(str)) {
			routingTable.add(str);
			return true;
		}

		else {
			return false;
		}
	}

	public Vector<String> getRoutingTable() {
		return routingTable;
	}

	public void addFile(String filename) {
		fileList.add(filename);
	}

	public void processJoin(String message) {
		String[] s = message.split(" ");
		// TODO validate request
		System.out.println(s[1]);
		String operation = s[1];
		String host = s[2];
		int port = Integer.parseInt(s[3]);
		boolean response = addNodeToRoutingTable(host, port);
		int value;

		if (response == true) {
			value = 0;
		} else {
			value = 9999;
		}
		sendJoinResponse(host, port, value);
	}

	public void processLeave(String message) {

		String[] s = message.split(" ");
		String ip = s[2];
		int port = Integer.parseInt(s[3]);
		boolean response = removeNodeFromRountingTable(ip, port);

		int value;

		if (response == true) {
			value = 0;
		} else {
			value = 9999;
		}
		sendLeaveResponse(ip, port, value);
	}

	public void processLeaveOK(String message) {

		String[] s = message.split(" ");
		int value = Integer.parseInt(s[2]);

		if (value == 0) {
			System.out.println("Successful");
		} else if (value == 9999) {

			System.out
					.println("Error while removing node from the routing table");
		}

	}

	public void processSearch(String message) {
		String[] s = message.split(" ");
		// TODO validate request
		System.out.println(s[1]);
		String operation = s[1];
		String msgID = s[2];
		if(sessionQuries.contains(msgID)){
			return;
		}
		sessionQuries.add(msgID);
		String host = s[3];
		int port = Integer.parseInt(s[4]);
		//String filename = s[5];
		String msg = s[0];
		for(int i=1;i<s.length-1;i++){
			msg = msg + " "+s[i];
		}
		int hops = Integer.parseInt(s[s.length-1]);
		msg = msg + " " + (hops + 1);
		if (hops < MAX_HOPS) {
			for (String entry : routingTable) {
				String data[] = entry.split(" ");
				sendSearchRequest(data[0], Integer.parseInt(data[1]),
						msg);
			}
		}
		int wordCount = s.length-6;
		String[] words = new String[wordCount];
		for(int i=0;i<wordCount;i++){
			words[i] = s[i+5];
			System.out.println(words[i]);
		}
		ArrayList<String> results = search(words);
		String files = "";
		int fileCount = results.size();
		for(int i=0;i<fileCount;i++){
			if(i == 0){
				files = results.get(i);
			}else{
				files = files + " " + results.get(i);
			}
		}
		
		sendSearchResponse(host, port, fileCount, files, hops);
	}

	public void processSearchOK(String message) {
		String[] s = message.split(" ");
		// TODO validate request
		String operation = s[1];
		int fileCount = Integer.parseInt(s[2]);
		String ip = s[3];
		int port = Integer.parseInt(s[4]);
		int hops = Integer.parseInt(s[5]);
	}

	public void selectRandomFiles(String path) {

		int namesToRead = getRandomNumber(3, 6);

		FileReader fr;
		BufferedReader br;
		try {

			int lineNumbers = 0;
			int randomfileNumber = 0;
			fr = new FileReader(path);
			br = new BufferedReader(fr);
			String line = br.readLine();

			while (line != null) {
				line = br.readLine();
				lineNumbers++;
			}
			// br.close();
			int count = 0;
			int setSize = 0;

			while ((namesToRead - count) > 0) {
				randomfileNumber = getRandomNumber(1, lineNumbers);
				int counter = 0;
				setSize = fileList.size();
				fr = new FileReader(path);
				br = new BufferedReader(fr);
				line = br.readLine();
				// System.out.println(line);
				while (line != null) {
					counter++;
					if (counter == randomfileNumber && !fileList.contains(line)) {
						fileList.add(line);
						System.out.println(line);
					}
					line = br.readLine();
				}
				if ((fileList.size() - setSize) > 0) {
					count++;
				}
			}

			br.close();
			fr.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private int getRandomNumber(int low, int high) {

		int number = 0;
		Random r = new Random();

		number = r.nextInt(high - low) + low;

		return number;

	}
	
}

