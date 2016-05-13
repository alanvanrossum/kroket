package nl.tudelft.kroket.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.tudelft.kroket.log.Logger;

public class NetworkClient {

	private final String className = this.getClass().getSimpleName();
	private Logger log = Logger.getInstance();

	private static final String CRLF = "\r\n"; // newline

	private Socket socket = null;

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	private String remoteHost;
	private int remotePort;

	public NetworkClient(String host, int port) {

		log.info(className, "Initializing...");

		this.remoteHost = host;
		this.remotePort = port;
	}

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

	public DataInputStream getStream() {
		return dataInputStream;
	}

	public boolean connect(String host, int port) {

		log.info(className,
				String.format("Trying to connect to %s:%d...", host, port));

		try {
			socket = new Socket(host, port);

			if (socket == null) {
				return false;
			}

			setInitialised(true);

			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			dataInputStream = new DataInputStream(socket.getInputStream());

			setConnected(true);
			return true;
		} catch (UnknownHostException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		setConnected(false);
		return false;
	}

	public void sendMessage(String message) {

		if (!isConnected())
			return;

		if (!message.endsWith(CRLF))
			message += CRLF;

		try {
			dataOutputStream.writeBytes(message);
		} catch (IOException e) {
			setConnected(false);
		}
	}

	public void close() {

		setInitialised(false);
		setConnected(false);

		try {
			if (socket != null)
				socket.close();

			if (dataInputStream != null)
				dataInputStream.close();

			if (dataOutputStream != null)
				dataOutputStream.close();
			
		} catch (IOException e) {
			System.out.println(e);
		}

		socket = null;
		dataInputStream = null;
		dataOutputStream = null;
	}

	public boolean connect() {
		return connect(remoteHost, remotePort);

	}
}
