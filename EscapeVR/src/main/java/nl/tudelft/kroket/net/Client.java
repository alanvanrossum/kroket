package nl.tudelft.kroket.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private static final String CRLF = "\r\n"; // newline

	private Socket socket = null;

	private boolean initialised;
	private boolean connected;

	public boolean isInitialised() {
		
		if (socket == null)
			return false;
		
		return initialised;
	}

	public void setInitialised(boolean initialised) {
		this.initialised = initialised;
	}

	public boolean isConnected() {
		
		if (!isInitialised())
			return false;
		
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	

	private DataOutputStream dOutput;
	private DataInputStream dInput;

	public Client(String host, int port) {
		connect(host, port);
	}

	public DataInputStream getStream() {
		return dInput;
	}

	public boolean connect(String host, int port) {
		try {
			socket = new Socket(host, port);

			if (socket == null) {
				return false;
			}

			dOutput = new DataOutputStream(socket.getOutputStream());
			dInput = new DataInputStream(socket.getInputStream());

			return true;
		} catch (UnknownHostException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		return false;
	}

	public void sendMessage(String message) throws IOException {

		if (!message.endsWith(CRLF))
			message += CRLF;

		dOutput.writeBytes(message);
	}

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
