package nl.tudelft.kroket.player;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Player object for EscapeServer.
 * 
 * @author Team Kroket
 *
 */
public class Player {
	
	private static final String CRLF = "\r\n"; // newline

	public enum PlayerType {
		NONE, VIRTUAL, MOBILE, ADMIN 
	}

	private Socket socket;
	private String name;
	private PlayerType type;
	private DataOutputStream stream;

	public Player(Socket socket, DataOutputStream stream, String name) {
		
		setStream(stream);
		setSocket(socket);
		setName(name);
	}
	
	public void setStream(DataOutputStream stream) {
		this.stream = stream;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PlayerType getType() {
		return type;
	}

	public void setType(PlayerType type) {
		this.type = type;
	}

	public String toString() {
		return String.format("Player %s - %s - %s", getName(),  getSocket()
				.getRemoteSocketAddress(), getType());
	}
	
	public boolean isConnected() {
		return (getSocket().isConnected() && !getSocket().isClosed());
	}
	
	public void sendMessage(String message) {
		
		if (!isConnected())
			return;
		
		if (!message.endsWith(CRLF))
			message += CRLF;
			
		try {
			stream.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
