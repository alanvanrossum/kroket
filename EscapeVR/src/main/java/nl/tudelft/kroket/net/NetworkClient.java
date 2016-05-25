package nl.tudelft.kroket.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import nl.tudelft.kroket.log.Logger;

public class NetworkClient {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Carriage-return, line-feed. */
  private static final String CRLF = "\r\n"; // newline

  /** The socket we're using to connect to the other endpoint. */
  private Socket socket = null;

  private DataOutputStream dataOutputStream;
  private DataInputStream dataInputStream;

  /** Remote host's name or IP address. */
  private String remoteHost;

  /** Remote host's portnumber. */
  private int remotePort;

  /**
   * Constructor for TCP NetworkClient object.
   * 
   * @param host
   *          string of the host to connect to
   * @param port
   *          port of the host to connect to
   */

  public NetworkClient(String host, int port) {

    log.info(className, "Initializing...");

    this.remoteHost = host;
    this.remotePort = port;
  }

  private boolean initialised;
  private boolean connected;

  /**
   * Check whether the socket is initialised.
   * 
   * @return if the socket is initialised
   */
  public boolean isInitialised() {

    if (socket == null) {
      return false;
    }

    return initialised;
  }

  /**
   * Set socket initalised value.
   * 
   * @param initialised
   *          - boolean
   */
  public void setInitialised(boolean initialised) {
    this.initialised = initialised;
  }

  /**
   * Check whether the client is connected.
   * 
   * @return true if the client is connected.
   */
  public boolean isConnected() {

    // can't be connected if socket
    // isn't even initialised

    if (!isInitialised()) {
      return false;
    }

    return connected;
  }

  public void setConnected(boolean connected) {
    this.connected = connected;
  }

  /**
   * Get the client's DataInputStream handle.
   * 
   * @return the DataInputStream handle.
   */
  public DataInputStream getStream() {
    return dataInputStream;
  }

  /**
   * Connect to a remote socket.
   * 
   * @param host
   *          string of the host to connect to
   * @param port
   *          number of the host to connect to
   * @return true if succesful
   */

  public boolean connect(String host, int port) {

    log.info(className, String.format("Trying to connect to %s:%d...", host, port));

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

  /**
   * Send a message to the dataoutputstream.
   * 
   * @param message
   *          the message to send
   */
  public void sendMessage(String message) {

    if (!isConnected()) {
      return;
    }

    if (!message.endsWith(CRLF)) {
      message += CRLF;
    }

    try {
      dataOutputStream.writeBytes(message);
    } catch (IOException e) {
      setConnected(false);
    }
  }

  /**
   * Close the socket.
   */
  public void close() {

    setInitialised(false);
    setConnected(false);

    try {
      if (socket != null) {
        socket.close();
      }

      if (dataInputStream != null) {
        dataInputStream.close();
      }

      if (dataOutputStream != null) {
        dataOutputStream.close();
      }

    } catch (IOException e) {
      System.out.println(e);
    }

    socket = null;
    dataInputStream = null;
    dataOutputStream = null;
  }

  /**
   * Connect to remote host.
   * 
   * @return true if connection established
   */
  public boolean connect() {
    return connect(remoteHost, remotePort);

  }
}
