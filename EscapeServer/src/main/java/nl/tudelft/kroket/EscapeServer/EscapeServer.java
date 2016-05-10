package nl.tudelft.kroket.EscapeServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.tudelft.kroket.log.Logger;

public class EscapeServer implements Runnable {
	
	private final String className = this.getClass().getSimpleName();

	private static final int PORTNUM = 1234;

	protected int serverPort;

	List<ConnectionThread> clientList = new ArrayList<ConnectionThread>();

	static Logger log = Logger.getInstance();

	public static void main(String[] args) {

		log.info("Server", "Initializing...");

		EscapeServer server = new EscapeServer(PORTNUM);
		new Thread(server).start();
	}

	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public EscapeServer(int port) {
		serverPort = port;
	}

	public void run() {
		synchronized (this) {
			runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				
				log.info(className, "Incoming connection from " + clientSocket.getRemoteSocketAddress().toString());
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			threadPool.execute(new ConnectionThread(clientSocket));
		}
		
		
		// ...
		
		threadPool.shutdown();
	}

	private synchronized boolean isStopped() {
		return isStopped;
	}

	public synchronized void stop() {
		isStopped = true;
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot listen on port " + serverPort, e);
		}
	}
}