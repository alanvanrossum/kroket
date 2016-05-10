package nl.tudelft.kroket.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private static int PORTNUM = 1234;
	private static String HOST = "127.0.0.1";
	private Socket socket = null;

	private DataOutputStream dOutput;
	private DataInputStream dInput;

	public Client() throws IOException {
		createSocket();
		// outToServer.writeBytes("hallo Jochem!! :D:D");
		// ja hoi Mayke
	}

	public DataInputStream getStream() {
		return dInput;
	}

	/**
	 * creates the socket.
	 */
	public void createSocket() {
		try {
			socket = new Socket(HOST, PORTNUM);

			dOutput = new DataOutputStream(socket.getOutputStream());
			dInput = new DataInputStream(socket.getInputStream());
		} catch (UnknownHostException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	/**
	 * Data that needs to be sent after an interaction with an object.
	 * 
	 * @param message
	 *            , string with message to be sent.
	 */
	public void sendMessage(String message) throws IOException {

		if (!message.endsWith("\r\n"))
			message += "\r\n";

		dOutput.writeBytes(message);
	}

	/**
	 * socket and streams closed when done.
	 */
	public void close() {
		try {
			socket.close();
			dInput.close();
			dOutput.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
