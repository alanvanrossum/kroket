package nl.tudelft.kroket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.player.Player;
import nl.tudelft.kroket.player.Player.PlayerType;

public class ConnectionThread implements Runnable {

	static Logger log = Logger.getInstance();

	private final String className = this.getClass().getSimpleName();

	DataInputStream inputStream = null;
	DataOutputStream outputStream = null;

	Player.PlayerType type = PlayerType.NONE;

	protected Socket clientSocket = null;

	public ConnectionThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/**
	 * Process remote input.
	 * 
	 * @param input
	 *            the remote input
	 */
	private void processInput(String input) {
		Logger.getInstance().info(className, input);

		if (input.startsWith("TYPE[")) {

			int pos = input.indexOf(']');
			String typeString = input.substring(5, pos);

			if (pos > 6) {

				Player player = EscapeServer.getPlayer(clientSocket);

				if (player == null) {
					System.out.println("Player not registered yet.");
				} else {

					try {
						type = PlayerType.valueOf(typeString);

						player.setType(type);

						player.sendMessage("Your type is now set to "
								+ type.toString());

						log.info(className, String.format(
								"Player %s is now set to type %s.",
								player.getName(), player.getType().toString()));

						if (!EscapeServer.ready()) {
							player.sendMessage(EscapeServer.countPlayers()
									+ " player(s) connected.");
						}
					} catch (IllegalArgumentException e) {
						player.sendMessage("Invalid client type.");
					}

				}
			}

		} else if (input.startsWith("REGISTER[")) {

			int pos = input.indexOf(']');

			if (pos > 9) {

				String name = input.substring(9, pos);

				EscapeServer.registerPlayer(clientSocket, outputStream, name);

			}
		} else if (input.startsWith("admin")) {
			adminCommand(input.substring(6));
		}

	}

	/**
	 * Process admin command.
	 * 
	 * @param command
	 */
	public void adminCommand(String command) {
		if (command.startsWith("sendall")) {
			EscapeServer.sendAll(command.substring(8));
		}
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();

			inputStream = new DataInputStream(input);
			outputStream = new DataOutputStream(output);

			String response = null;
			while ((response = inputStream.readLine()) != null) {
				processInput(response);
			}

			output.close();
			input.close();
		} catch (IOException e) {

			// in case an exception occured, remove and disconnect the client
			EscapeServer.removePlayer(clientSocket);

		}
	}
}
