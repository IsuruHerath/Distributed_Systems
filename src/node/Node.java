package node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import client.ClientProtocol;
import Utils.HostPortMapper;
import Utils.Messages;

public class Node {
	//class variables
		private String SERVER_IP;
		private int SERVER_PORT;
		
		private String MY_IP;
		private int MY_PORT;
		private String HOST_NAME;
		private Publisher servicePublisher;
		private Vector<String> routingTable;
		private HashSet<String> fileList = new HashSet<String>();
		
		//constructor
		public Node(String ip,int port,String myip,String hostname) throws SocketException{
			SERVER_IP	= ip;
			SERVER_PORT	= port;		
			MY_IP		= myip;
			MY_PORT		= HostPortMapper.getPort(MY_IP);
			HOST_NAME	= hostname;
			servicePublisher = new Publisher(this);
			servicePublisher.start();
			while(servicePublisher.isAlive());
			routingTable=new Vector<String>();
		}
		
		public String getIp(){
			return MY_IP;
		}
		
		public int getPort(){
			return MY_PORT;
		}
		
		public String getHostname(){
			return HOST_NAME;
		}
		
		//register to the system
		public void register(){
			try {
				Socket socket = new Socket(SERVER_IP,SERVER_PORT);
				System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
				OutputStream outToServer = socket.getOutputStream();
				PrintWriter out = new PrintWriter(outToServer);
		        out.print(Messages.getRegisterRequest(MY_IP, MY_PORT, HOST_NAME));
		        out.flush();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String responce = "";
				responce = reader.readLine();
		        socket.close();
		        System.out.println(responce);
		        String[] s = responce.split(" ");
		        String operation = s[1];
				
				if(operation.equalsIgnoreCase(ClientProtocol.REGISTER_OK)){
					int nodeCount = Integer.parseInt(s[2]);
					if(nodeCount == 1){
						sendJoinRequest(s[3], Integer.parseInt(s[4]));
						addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
					}else if(nodeCount == 2){
						sendJoinRequest(s[3], Integer.parseInt(s[4]));
						addNodeToRoutingTable(s[3], Integer.parseInt(s[4]));
						sendJoinRequest(s[6], Integer.parseInt(s[7]));
						addNodeToRoutingTable(s[6], Integer.parseInt(s[7]));
					}else if(nodeCount > 2){
						int host1 = (int)(Math.random()*nodeCount);
						int host2 = (int)(Math.random()*nodeCount);
						while(host1 == host2){
							host2 = (int)(Math.random()*nodeCount);
						}
						sendJoinRequest(s[2*host1+3], Integer.parseInt(s[2*host1+4]));
						addNodeToRoutingTable(s[2*host1+3], Integer.parseInt(s[2*host1+4]));
						sendJoinRequest(s[2*host2+3], Integer.parseInt(s[2*host2+4]));
						addNodeToRoutingTable(s[2*host2+3], Integer.parseInt(s[2*host2+4]));
					}
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		//unregister from the system
		public void unregister(){
			try {
				Socket socket = new Socket(SERVER_IP,SERVER_PORT);
				System.out.println("Just connected to "+ socket.getRemoteSocketAddress());
				OutputStream outToServer = socket.getOutputStream();
				PrintWriter out = new PrintWriter(outToServer);
		        out.print(Messages.getUnregisterRequest(MY_IP, MY_PORT, HOST_NAME));
		        //out.println(Requests.getRegisterMesage("129.82.123.45", 5001, "1234abcd"));
		        out.flush();
		        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String responce = "";
		        responce = reader.readLine();
		        socket.close();
		        System.out.println(responce);
		        String[] s = responce.split(" ");
		        String operation = s[1];
				
				if(operation.equalsIgnoreCase(ClientProtocol.UNREGISTER_OK)){
					
					String host	= s[2];
					int port	= Integer.parseInt(s[3]);
					int value;
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		public void sendJoinRequest(String ip,int port){
			try{
				URL url 		= new URL("http://"+ip+":"+port+"/server?wsdl");
				QName qName 	= new QName("http://node/","NodeServiceImplService");
				Service service = Service.create(url, qName);
				NodeService calService = service.getPort(NodeService.class);
				calService.join(Messages.getJoinRequest(MY_IP, MY_PORT));
			}catch(MalformedURLException e){
				System.out.println("Error : "+e.getMessage());
			}
			
		}
		
		public void sendJoinResponse(String ip,int port,int value){
			try{
				URL url 		= new URL("http://"+ip+":"+port+"/server?wsdl");
				QName qName 	= new QName("http://node/","NodeServiceImplService");
				Service service = Service.create(url, qName);
				NodeService calService = service.getPort(NodeService.class);
				calService.joinOK(Messages.getJoinRespond(value));
			}catch(MalformedURLException e){
				System.out.println("Error : "+e.getMessage());
			}
			
		}
		
		public void callLeave() throws MalformedURLException{
			URL url 		= new URL("http://"+MY_IP+":"+MY_PORT+"/server?wsdl");
			QName qName 	= new QName("http://node/","NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService calService = service.getPort(NodeService.class);
			calService.leave("");
		}
		
		public void callSearch() throws MalformedURLException{
			URL url 		= new URL("http://"+MY_IP+":"+MY_PORT+"/server?wsdl");
			QName qName 	= new QName("http://node/","NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService calService = service.getPort(NodeService.class);
			calService.search("");
		}
		
		public void respondLeave(String ip,int port,int value){
			//massageUDP(Messages.getLeaveRespond(value),ip,port);
		}
		
		public String respondJoin(String ip,int port,int value){
			return Messages.getJoinRespond(value);
		}
		
		public void respondSearch(String ip,int port,int no_of_files, String filenames, int hops ){
			//massageUDP(Messages.getSearchRespond(MY_IP, MY_PORT, no_of_files, filenames, hops),ip,port);
		}
		
		//search for a filename
		public String search(String fileName){
			String results = null;
			Iterator<String> itr = fileList.iterator();
			String s;
			while(itr.hasNext()){
				s = itr.next();
				if(s.contains(fileName)){
					if(results == null){
						results = fileName;
					}else{
						results = results + " " +fileName;
					}
				}
			}
			if(results == null){
				results = "";
			}
			return results;
		}
		
		
		public boolean removeNodeFromRountingTable(String ip,int port){
			
			String str=ip+" "+port;
			if(routingTable.contains(str)){
				routingTable.remove(str);
				return true;
			}
			
			else{
				return false;
			}
			
		}
		
		public boolean addNodeToRoutingTable(String ip,int port){
			
			
			String str = ip+" "+port;
			
			if(!routingTable.contains(str)){
				routingTable.add(str);
				return true;
			}
			
			else{
				return false;
			}
		}
		
		public Vector<String> getRoutingTable(){
			return routingTable;
		}
		
		public void addFile(String filename){
			fileList.add(filename);
		}
		
		public void processJoin(String message){
			String[] s = message.split(" ");
			//TODO validate request
			System.out.println(s[1]);
			String operation = s[1];
			String host			= s[2];
			int port			= Integer.parseInt(s[3]);
			boolean response 	= addNodeToRoutingTable(host, port);
			int value;
			
			if(response==true){
				value	= 0;
			}else{
				value	= 9999;				
			}
			sendJoinResponse(host,port,value);
		}
}
