package nl.tudelft.kroket.EscapeServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import nl.tudelft.kroket.EscapeServer.Player.PlayerType;
import nl.tudelft.kroket.log.Logger;

public class ConnectionThread implements Runnable {

	static Logger log = Logger.getInstance();

	private final String className = this.getClass().getSimpleName();

	DataInputStream dInput = null;
	DataOutputStream dOutput = null;

	Player.PlayerType type = PlayerType.NONE;

	protected Socket clientSocket = null;

	public ConnectionThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

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

					type = PlayerType.valueOf(typeString);

					player.setType(type);

					player.sendMessage("Your type is now set to "
							+ type.toString());
					
					log.info(className, String.format("Player %s is now set to type %s.", player.getName(), player.getType().toString()));
				}
			}

		} else if (input.startsWith("REGISTER[")) {

			int pos = input.indexOf(']');

			if (pos > 9) {

				String name = input.substring(9, pos);

				EscapeServer.registerPlayer(clientSocket, dOutput, name);

			}
		} else if (input.startsWith("admin")) {
			adminCommand(input.substring(6));
		}

	}

	public void adminCommand(String command) {
		if (command.startsWith("sendall")) {
			EscapeServer.sendAll(command.substring(8));
		}
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();

			dInput = new DataInputStream(input);
			dOutput = new DataOutputStream(output);

			String response = null;
			while ((response = dInput.readLine()) != null) {
				processInput(response);
			}

			output.close();
			input.close();
		} catch (IOException e) {

			EscapeServer.removePlayer(clientSocket);

			// e.printStackTrace();
		}
	}
}
