package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeServiceSession extends Thread{
	Socket sessionSocket;
	public NodeServiceSession(Socket socket){
		sessionSocket = socket;
	}
	
	public void run(){
		try {
			InputStream inpStrm = sessionSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inpStrm));
			PrintWriter out = new PrintWriter(sessionSocket.getOutputStream());
			String response = "";
			System.out.println("next line");
			response = br.readLine();
			System.out.println(response);
			out.println("Sample out put");
			out.flush();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
