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

/**
 * EscapeServer object.
 * 
 * @author Team Kroket
 *
 */
public class EscapeServer implements Runnable {

	/** List of players connected to the server. */
	public static HashMap<Socket, Player> playerList = new HashMap<Socket, Player>();

	/** Value to keep track whether socket was initialized. */
	private static boolean initialized;

	private final static String className = "EscapeServer";

	private static boolean gameActive = false;

	protected int serverPort;

	List<PlayerInstance> clientList = new ArrayList<PlayerInstance>();

	static Logger log = Logger.getInstance();

	public static void main(String[] args) {

		log.info(className, "EscapeServer by Team Kroket");
		log.info(className,
				String.format("Listening port is set to %d", Settings.PORTNUM));
		log.info(className, "Initializing...");
		initialized = false;

		// long startTime = System.currentTimeMillis();

		EscapeServer server = new EscapeServer(Settings.PORTNUM);

		while (!initialized) {

			// spawn this server in a new thread
			new Thread(server).start();

			if (initialized)
				break;

			try {
				Thread.sleep(Settings.INTERVAL_SOCKET_RETRY * 1000);
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

					if ((tickCounter % Settings.INTERVAL_REPORT_STATUS) == 0) {

						log.info(className, "Game not ready.");

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

					if ((tickCounter % Settings.INTERVAL_REPORT_STATUS) == 0) {

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
	 * 
	 * @param clientSocket
	 *            the client's socket
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
	 * 
	 * @param clientSocket
	 *            the client's socket
	 * @param dOutput
	 *            the dataoutputstream
	 * @param playername
	 *            the player's name
	 */
	public static void registerPlayer(Socket clientSocket,
			DataOutputStream dOutput, String playername) {
		if (playername.length() > 0) {

			if (getPlayer(clientSocket) == null) {

				// make sure chosen name is still available
				if (getPlayer(playername) == null) {

					// create a new player object
					Player player = new Player(clientSocket, dOutput,
							playername);

					// put new player object in playerList
					// key should already exist
					playerList.put(clientSocket, player);

					// tell player that he registered succesfully
					player.sendMessage("You are now registered as "
							+ player.toString());

					if (!ready()) {
						player.sendMessage(countPlayers()
								+ " players waiting...");
					}

					// let everyone know a new player registered with the server
					sendAll("Player " + player.getName() + " entered the game.");
				} else {

					// player's name was not available, let player know

					log.info(className, "Player " + playername
							+ " already registered");

					try {
						dOutput.writeBytes("Name already in use.\r\n");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				log.info(className, "Socket already registered.");
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

			// keep listening for incoming connections
			// until we manually terminate the server or some error (we cannot recover from) occurs
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

					// prepare an entry in the playerlist
					// we might set the value later
					// use client's socket as key
					playerList.put(clientSocket, null);

				} catch (IOException e) {
					e.printStackTrace();
				}

				// hand the client's socket to a new thread in the pool and
				// start the thread
				threadPool.execute(new PlayerInstance(clientSocket));

				// as we just accepted a new connection, immediately print
				// status
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

			// if no Player object, skip
			if (entry.getValue() == null)
				continue;

			if (entry.getValue().getType() == type)
				sum += 1;
		}

		return sum;
	}

	/**
	 * Count the number of players present in the server.
	 * 
	 * @return the number of players present
	 */
	public static int countPlayers() {
		int sum = 0;
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() == null)
				continue;

			sum += 1;
		}

		return sum;

	}

	/**
	 * Check whether the server is ready to start a game.
	 * 
	 * @return true when game is ready
	 */
	public static boolean ready() {
		return (countPlayers(PlayerType.VIRTUAL) == Settings.REQUIRED_VIRTUAL && countPlayers(PlayerType.MOBILE) == Settings.REQUIRED_MOBILE);
	}

	/**
	 * Get a player by socket.
	 * 
	 * @param socket
	 *            the socket
	 * @return Player object
	 */
	public static Player getPlayer(Socket socket) {
		return playerList.get(socket);
	}

	/**
	 * Get a player by name.
	 * 
	 * @param playername
	 *            the name of the player
	 * @return the player object
	 */
	public static Player getPlayer(String playername) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			// if entry contains no Player object, ignore
			if (entry.getValue() == null)
				continue;

			// player was found
			if (playername.equals(entry.getValue().getName()))
				return entry.getValue();

		}

		// no matches, return null
		return null;
	}

	/**
	 * Send a message to all connected players. This will omit users without a
	 * type.
	 * 
	 * @param message
	 *            the message to be sent
	 */
	public static void sendAll(String message) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() != null)
				entry.getValue().sendMessage(message);
		}
	}

	/**
	 * Send a message to all connected mobile clients.
	 * 
	 * @param message
	 *            the string to be sent
	 */
	public static void sendMobile(String message) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() != null
					&& entry.getValue().getType() == PlayerType.MOBILE)
				entry.getValue().sendMessage(message);
		}
	}

	/**
	 * Send a message to all connected virtual clients.
	 * 
	 * @param message
	 *            the string to be sent
	 */
	public static void sendVirtual(String message) {
		for (Entry<Socket, Player> entry : playerList.entrySet()) {

			if (entry.getValue() != null
					&& entry.getValue().getType() == PlayerType.VIRTUAL)
				entry.getValue().sendMessage(message);
		}
	}

	/**
	 * Print all players, registered and unregistered.
	 */
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