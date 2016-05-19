package nl.tudelft.kroket.server;

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

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.player.Player;
import nl.tudelft.kroket.player.Player.PlayerType;

public class EscapeServer implements Runnable {

	/** List of players connected to the server. */
	public static HashMap<Socket, Player> playerList = new HashMap<Socket, Player>();

	/** Value to keep track whether socket was initialized. */
	private static boolean initialized;

	private final static String className = "EscapeServer";

	/** Port number to bind server to. */
	private static final int PORTNUM = 1234;

	/** Number of VR clients required to start the game. */
	private static final int REQUIRED_VIRTUAL = 1;

	/** Number of mobile clients required to start the game. */
	private static final int REQUIRED_MOBILE = 2;

	/** How many seconds before each status print. */
	private static final int SECPRINTSTATUS = 30;

	/** How many seconds until retrying to bind to the socket. */
	private static final int SECBINDRETRY = 5;

	private static boolean gameActive = false;

	protected int serverPort;

	List<ConnectionThread> clientList = new ArrayList<ConnectionThread>();

	static Logger log = Logger.getInstance();

	public static void main(String[] args) {

		log.info(className, "Initializing...");
		initialized = false;

		// long startTime = System.currentTimeMillis();

		EscapeServer server = new EscapeServer(PORTNUM);

		while (!initialized) {
			new Thread(server).start();

			if (initialized)
				break;

			try {
				Thread.sleep(SECBINDRETRY * 1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int tickCounter = 0;

		if (initialized) {
			
			boolean breakLoop = false;

			while (!breakLoop) {

				log.info(className,
						"Game not ready. Waiting for players to register...");
				
				while (!ready()) {

					// long duration = System.currentTimeMillis() - startTime;

					tickCounter += 1;

					if ((tickCounter % SECPRINTSTATUS) == 0) {

						log.info(className,
								"Game not ready.");
						
						printPlayers();
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				log.info(className, "Server is ready to host game.");

				startGame();

				gameActive = true;

				while (gameActive && ready()) {
					tickCounter += 1;

					if ((tickCounter % SECPRINTSTATUS) == 0) {

						log.info(className, "Game is active.");
						printPlayers();

					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				gameActive = false;
				
				log.info(className, "Game session has ended.");

			}

		} else {
			log.error(className, "Initialization failed. Is the port in use?");
		}

		log.info(className, "Exiting...");
	}
	
	/**
	 * Start the game.
	 */
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
	
	/**
	 * Remove a player based on its socket.
	 * @param clientSocket the client's socket
	 */
	public static void removePlayer(Socket clientSocket) {

		Player player = playerList.get(clientSocket);

		playerList.remove(clientSocket);

		if (!clientSocket.isClosed()) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (player != null) {
			log.info(className,
					String.format("Player %s disconnected.", player.getName()));
			sendAll(String.format("Player %s has left the game.",
					player.getName()));
		}
		log.info(className, "Socket "
				+ clientSocket.getRemoteSocketAddress().toString()
				+ " removed.");
	}
	
	/**
	 * Register a player to the server.
	 * @param clientSocket the client's socket
	 * @param dOutput the dataoutputstream
	 * @param playername the player's name
	 */
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
					
					if (!ready())
					{
						player.sendMessage(countPlayers() + " players waiting...");
					}
					
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
				System.out.println("Socket already registered");

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

		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException exception) {
			log.error(className, "Error: " + exception);
		}

		if (serverSocket == null) {
			initialized = false;
		} else {

			initialized = true;

			while (!isStopped()) {
				Socket clientSocket = null;
				try {
					log.info(className, String.format(
							"Listening for incoming connections on port %d...",
							serverPort));
					clientSocket = serverSocket.accept();

					log.info(className,
							"Connection accepted. Incoming connection from "
									+ clientSocket.getRemoteSocketAddress()
											.toString());

					playerList.put(clientSocket, null);

				} catch (IOException e) {
					e.printStackTrace();
				}
				threadPool.execute(new ConnectionThread(clientSocket));

				printPlayers();
			}

			// ...

			threadPool.shutdown();
		}
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

			if (entry.getValue() == null)
				continue;

			if (entry.getValue().getType() == type)
				sum += 1;
		}

		return sum;

	}

	public static int countPlayers() {
		int sum = 0;
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() == null)
				continue;
			
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

			if (entry.getValue() == null)
				continue;

			if (playername.equals(entry.getValue().getName()))
				return entry.getValue();

		}

		return null;
	}
	
	/**
	 * Send a message to all connected players.
	 * @param message the message to be sent
	 */
	public static void sendAll(String message) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() != null)
				entry.getValue().sendMessage(message);
		}
	}
	
	/**
	 * sends a message only if a player has type mobile
	 * @param message the string to be sent.
	 */
	public static void sendMobile(String message){
	  for (Entry<Socket, Player> entry : playerList.entrySet()) {

      if (entry.getValue() != null && entry.getValue().getType() == PlayerType.MOBILE)
        entry.getValue().sendMessage(message);
    }
	}
	
	 /**
   * sends a message only if a player has type virtual
   * @param message the string to be sent.
   */
  public static void sendVirtual(String message){
    for (Entry<Socket, Player> entry : playerList.entrySet()) {

      if (entry.getValue() != null && entry.getValue().getType() == PlayerType.VIRTUAL)
        entry.getValue().sendMessage(message);
    }
  }


	private static void printPlayers() {

		if (playerList.isEmpty()) {
			System.out.println("No players currently registered.");
		} else {

			System.out.println("Registered players:");

			for (Entry<Socket, Player> entry : playerList.entrySet()) {

				if (entry.getValue() == null)
					System.out.println("Unregistered: \t"
							+ entry.getKey().getRemoteSocketAddress()
									.toString());
				else
					System.out.println("Registered: \t" + entry.getValue());
			}
		}
	}

}