package nl.tudelft.kroket.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Client {
	
	private static int PORTNUM = 1234;
        private static String HOST = "145.94.213.233";
        private Socket socket = null;
        
        private DataOutputStream outToServer;
        private DataInputStream inFromServer;
    private String startMiniGame;
	
	public Client() throws IOException {
            createSocket();
            outToServer.writeBytes("hallo Jochem!! :D:D");
	}
        
        /**
         * creates the socket.
         */
        public void createSocket () {
            try {
                socket = new Socket(HOST, PORTNUM);
                outToServer = new DataOutputStream(socket.getOutputStream());
                inFromServer = new DataInputStream(socket.getInputStream());
            }
            catch (UnknownHostException e) {System.out.println(e);}
            catch (IOException e) {System.out.println(e);}
        }
        
        
        /**
         * Data that needs to be sent after an interaction with an object.
         * @param message, string with message to be sent.
         */
        public void sendMessage(String message) throws IOException{
            String StartMiniGame = "123";
            outToServer.writeBytes(startMiniGame);
        }
        
        /**
         * method for reading a message.
         * @return string with the message
         * @throws IOException 
         */
        public String receiveMessage() throws IOException{
            String res = inFromServer.readLine();  
            return res;
        }
        

    
}
