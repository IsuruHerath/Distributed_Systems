package node;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import client.ClientProtocol;
import Utils.Consts;
import Utils.HostPortMapper;
import Utils.Messages;
import Utils.Util;

public class Node {
	// class variables
	private String SERVER_IP;
	private int SERVER_PORT;

	private String MY_IP;
	private int MY_PORT;
	private String HOST_NAME;
	private Publisher servicePublisher;
	private Vector<String> routingTable;
	private HashSet<String> fileList = new HashSet<String>();
	private static int MAX_HOPS;
	private HashSet<String> sessionQuries = new HashSet<String>();
	private int minHops = -1;
	private int minLatency = -1;
	private int messageForwarded = 0;
	private int messageRecieved = 0;
	private long beginTime = 0;

	// constructor
	public Node(String ip, int port, int myport, String hostname)
			throws SocketException {
		SERVER_IP = ip;
		SERVER_PORT = port;
		MY_IP = Util.getProperty(Consts.NODE_HOST);
		MY_PORT = myport;
		HOST_NAME = hostname;
		MAX_HOPS	= Integer.parseInt(Util.getProperty(Consts.MAX_HOPS));
		servicePublisher = new Publisher(this);
		servicePublisher.start();
		while (servicePublisher.isAlive());
		routingTable = new Vector<String>();
		selectRandomFiles(Util.getProperty(Consts.FILE_LIST_PATH));
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
				if(nodeCount > 0 && nodeCount < 9996){
					if (nodeCount == 1) {
						sendJoinRequest(s[3], Integer.parseInt(s[4]));
						addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
					} else if (nodeCount == 2) {
						sendJoinRequest(s[3], Integer.parseInt(s[4]));
						addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
						sendJoinRequest(s[6], Integer.parseInt(s[7]));
						addNodeToRoutingTable(s[6], Integer.parseInt(s[7]));
					} else{
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
				}else{
					if(nodeCount == 9999){
						System.out.println("failed, there is some error in the command");
					}else if(nodeCount == 9998){
						System.out.println("failed, already registered to you, unregister first");
					}else if(nodeCount == 9997){
						System.out.println("failed, registered to another user, try a different IP and port");
					}else if(nodeCount == 9996){
						System.out.println("failed, can’t register. BS full");
					}
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

	public void searchFile(String filename) {
		//Util.getTime();
		try {
			
			String msgID = Util.getMessageID(MY_IP, MY_PORT);
			System.out.println(msgID);
			beginTime = Util.getTime();
			minHops = -1;
			minLatency = -1;
			for (String entry : routingTable) {
				messageForwarded++;
				String data[] = entry.split(" ");
				sessionQuries.add(msgID);
				sendSearchRequest(data[0], Integer.parseInt(data[1]),
					Messages.getSearchRequest(msgID,MY_IP, MY_PORT, filename,
							1));
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//end of file
		//print hops and latency
	}

	private void sendJoinRequest(String ip, int port) {
		getNodeService(ip, port).join(Messages.getJoinRequest(MY_IP, MY_PORT));
	}

	private void sendJoinResponse(String ip, int port, int value) {
		getNodeService(ip, port).joinOK(Messages.getJoinRespond(value));
	}

	private void sendLeaveRequest(String ip, int port){
		getNodeService(ip, port).leave(Messages.getLeaveRequest(MY_IP, MY_PORT));
	}

	private void sendLeaveResponse(String ip, int port, int value) {
		getNodeService(ip, port).leaveOK(Messages.getLeaveRespond(value));
	}

	private void sendSearchRequest(String ip, int port, String query) {
		getNodeService(ip, port).search(query);
	}

	private void sendSearchResponse(String ip, int port, int no_of_files,
			String filenames, int hops) {
		getNodeService(ip, port).searchOK(
				Messages.getSearchRespond(MY_IP, MY_PORT, no_of_files,
						filenames, hops));
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
		messageRecieved++;
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
				messageForwarded++;
				String data[] = entry.split(" ");
				sendSearchRequest(data[0], Integer.parseInt(data[1]),
						msg);
			}
		}
		int wordCount = s.length-6;
		String[] words = new String[wordCount];
		for(int i=0;i<wordCount;i++){
			words[i] = s[i+5];
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
		long recievedTime = Util.getTime();
		String[] s = message.split(" ");
		// TODO validate request
		String operation = s[1];
		int fileCount = Integer.parseInt(s[2]);
		String ip = s[3];
		int port = Integer.parseInt(s[4]);
		int hops = Integer.parseInt(s[5]);
		if(fileCount > 0){
			if(minHops == -1){
				minHops = hops;
			}else if(hops < minHops){
				minHops = hops;
			}
			if(minLatency == -1){
				minLatency = (int)(recievedTime - beginTime);
			}else if((int)(recievedTime - beginTime) < minLatency){
				minLatency = (int)(recievedTime - beginTime);
			}
		}
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

	private NodeService getNodeService(String ip, int port) {
		try {
			URL url = new URL("http://" + ip + ":" + port + "/server?wsdl");
			QName qName = new QName("http://node/", "NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService nodeService = service.getPort(NodeService.class);
			return nodeService;
		} catch (MalformedURLException e) {
			System.out.println("Error : " + e.getMessage());
			return null;
		}
	}
	
	public void getFileList(){
		for(String s : fileList){
			System.out.println(s);
		}
	}
	
	public void getHostList(){
		for(String s : routingTable){
			System.out.println(s);
		}
	}
	
	public void getStat(){
		System.out.println("Recieved Messages = "+messageRecieved);
		System.out.println("Forwarded Messages = "+messageForwarded);
	}
}
