package node;

import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.HashSet;
import java.util.Vector;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import Utils.HostPortMapper;

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
		
		public void callJoin() throws MalformedURLException{
			URL url 		= new URL("http://"+MY_IP+":"+MY_PORT+"/server?wsdl");
			QName qName 	= new QName("http://node/","NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService calService = service.getPort(NodeService.class);
			System.out.println(calService.join(""));
		}
		
		public void callLeave() throws MalformedURLException{
			URL url 		= new URL("http://"+MY_IP+":"+MY_PORT+"/server?wsdl");
			QName qName 	= new QName("http://node/","NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService calService = service.getPort(NodeService.class);
			System.out.println(calService.leave(""));
		}
		
		public void callSearch() throws MalformedURLException{
			URL url 		= new URL("http://"+MY_IP+":"+MY_PORT+"/server?wsdl");
			QName qName 	= new QName("http://node/","NodeServiceImplService");
			Service service = Service.create(url, qName);
			NodeService calService = service.getPort(NodeService.class);
			System.out.println(calService.search(""));
		}
}
