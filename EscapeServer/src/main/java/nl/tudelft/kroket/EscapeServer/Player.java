package nl.tudelft.kroket.EscapeServer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Player {

	public enum PlayerType {
		VIRTUAL, MOBILE, NONE
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
		return String.format("Player %s - %s - %s", getSocket()
				.getRemoteSocketAddress(), getName(), getType());
	}
	
	public void sendMessage(String message) {
		
		if (!message.endsWith("\r\n"))
			message += "\r\n";
			
		try {
			stream.writeBytes(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
