package nl.tudelft.kroket.EscapeServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
	private static final int PORTNUM = 1234;

	static Logger log = Logger.getInstance();

	public static void main(String[] args) {
		log.info("Server", "Initializing...");
		mainLoop(PORTNUM);
	}
	
	public static void processMessage() {
		
	}

	public static void mainLoop(int port) {
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		log.info("Server", "Started server on port " + port);

		while (true) {

			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			log.info("Server", "Accepted connection from client");

			DataInputStream in = null;
			DataOutputStream out = null;

			try {
				in = new DataInputStream(clientSocket.getInputStream());
				out = new DataOutputStream(clientSocket.getOutputStream());
			} catch (Exception e) {
				e.printStackTrace();
			}

			String s;
			try {
				while ((s = in.readLine()) != null) {
					log.info("Server", s);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.err.println("Closing connection with client");

			try {
				out.close();
				in.close();
				clientSocket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
