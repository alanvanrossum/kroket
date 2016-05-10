package nl.tudelft.kroket.EscapeServer;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nl.tudelft.kroket.EscapeServer.Player.PlayerType;
import nl.tudelft.kroket.log.Logger;

public class EscapeServer implements Runnable {

	public static HashMap<Socket, Player> playerList = new HashMap<Socket, Player>();

	private final static String className = "EscapeServer";

	private static final int PORTNUM = 1234;

	private static final int REQUIRED_VIRTUAL = 1;
	private static final int REQUIRED_MOBILE = 2;

	protected int serverPort;

	List<ConnectionThread> clientList = new ArrayList<ConnectionThread>();

	static Logger log = Logger.getInstance();

	public static void main(String[] args) {

		log.info(className, "Initializing...");

		long startTime = System.currentTimeMillis();

		EscapeServer server = new EscapeServer(PORTNUM);
		new Thread(server).start();

		int time = 0;

		while (!ready()) {

			// long duration = System.currentTimeMillis() - startTime;

			time += 1;

			if (time >= 10) {

				log.info("Server",
						"Game not ready. Waiting for players to register...");
				printPlayers();

				time = 0;
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		startGame();

	}

	private static void startGame() {

		log.info(className, "Starting game...");
		sendAll("Starting game...");
		sendAll("START");

	}

	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	protected ExecutorService threadPool = Executors.newFixedThreadPool(10);

	public EscapeServer(int port) {
		serverPort = port;
	}

	public static void registerPlayer(Socket clientSocket,
			DataOutputStream dOutput, String playername) {
		if (playername.length() > 0) {

			if (getPlayer(clientSocket) == null) {

				if (getPlayer(playername) == null) {

					Player player = new Player(clientSocket, dOutput,
							playername);

					playerList.put(clientSocket, player);

					player.sendMessage("You are now registered as "
							+ player.toString());
					sendAll("Player " + player.getName() + " entered the game.");
				} else {
					System.out.println("player " + playername
							+ " already registered");

					try {
						dOutput.writeBytes("Name already in use.\r\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				System.out.println("socket already registered");

				try {
					dOutput.writeBytes("You are already registered.\r\n");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public void run() {
		synchronized (this) {
			runningThread = Thread.currentThread();
		}

		log.info(className, "Creating socket...");
		createSocket();

		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				log.info(className, "Listening for incoming connections.");
				clientSocket = serverSocket.accept();

				log.info(className, "Incoming connection from "
						+ clientSocket.getRemoteSocketAddress().toString());

			} catch (IOException e) {
				e.printStackTrace();
			}
			threadPool.execute(new ConnectionThread(clientSocket));

			printPlayers();
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

	public static int countPlayers(PlayerType type) {
		int sum = 0;
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue().getType() == type)
				sum += 1;
		}

		return sum;

	}

	public static boolean ready() {
		return (countPlayers(PlayerType.VIRTUAL) == REQUIRED_VIRTUAL && countPlayers(PlayerType.MOBILE) == REQUIRED_MOBILE);
	}

	public static Player getPlayer(Socket socket) {
		return playerList.get(socket);
	}

	public static Player getPlayer(String playername) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (playername.equals(entry.getValue().getName()))
				return entry.getValue();

		}

		return null;
	}

	public static void sendAll(String message) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {
			entry.getValue().sendMessage(message);
		}
	}

	private static void printPlayers() {

		if (playerList.isEmpty()) {
			System.out.println("No players currently registered.");
		} else {

			System.out.println("Registered players:");

			for (Entry<Socket, Player> entry : playerList.entrySet()) {
				System.out.println(entry.getValue());
			}
		}
	}

	private void createSocket() {
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot listen on port " + serverPort, e);
		}
	}
}