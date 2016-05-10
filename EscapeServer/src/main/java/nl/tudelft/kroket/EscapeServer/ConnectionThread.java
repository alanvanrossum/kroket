package nl.tudelft.kroket.EscapeServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.net.Socket;

import nl.tudelft.kroket.log.Logger;

public class ConnectionThread implements Runnable {

	static Logger log = Logger.getInstance();
	
	protected Socket clientSocket = null;
	
	public ConnectionThread(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	private void processInput(String input) {
		Logger.getInstance().info("ConnectionThread", input);
	}

	public void run() {
		try {
			InputStream input = clientSocket.getInputStream();
			OutputStream output = clientSocket.getOutputStream();
			
			DataInputStream dInput = new DataInputStream(input);
			DataOutputStream dOutput = new DataOutputStream(output);
			
			String response = null;
			while ((response = dInput.readLine()) != null)
			{
				processInput(response);
			}
			
			output.close();
			input.close();
		} catch (IOException e) {
			// report exception somewhere.
			e.printStackTrace();
		}
	}
}
