package nl.tudelft.kroket.net;

import java.io.DataInputStream;
import java.io.IOException;

import nl.tudelft.kroket.escape.EscapeVR;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.screen.HeadUpDisplay;

public class ClientThread extends Thread {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private String remoteHost;
  private int remotePort;

  private final int reconnInterval = 5;

  private String playerName = "VR-USER";

  EscapeVR callback;
  HeadUpDisplay hud;
  NetworkClient client;

  public ClientThread(EscapeVR source, HeadUpDisplay hud) {
    this.hud = hud;
    this.client = new NetworkClient();
    this.callback = source;

    setRemote("localhost", 1234);
  }

  /**
   * Set the remote host details.
   * 
   * @param remoteHost
   *          the remote host
   * @param remotePort
   *          the remote port
   */
  public void setRemote(String remoteHost, int remotePort) {

    this.remoteHost = remoteHost;
    this.remotePort = remotePort;
  }

  /**
   * Set the current player's name.
   * 
   * @param playerName
   *          the player's name
   */
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Shorthand method to let the current thread sleep.
   * 
   * @param seconds
   *          the amount of seconds to sleep
   */
  private void sleep(int seconds) {
    try {
      Thread.sleep(seconds * 1000);
    } catch (InterruptedException exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Send a message to the remote endpoint.
   * 
   * @param message
   *          the message string to send
   */
  public void sendMessage(String message) {
    client.sendMessage(message);
  }

  @Override
  public void run() {

    boolean breakLoop = false;

    while (!breakLoop) {

      hud.setCenterText("Trying to connect to server...");

      while (!client.isConnected()) {
        if (!client.connect(remoteHost, remotePort)) {
          log.info(className, "Failed to connect. Retrying...");

          hud.setCenterText("Unable to connect to server.", 2);
          client.close();
          sleep(2);

          hud.setCenterText("Trying to connect to server...", reconnInterval);
          sleep(reconnInterval);
        }
      }

      hud.setCenterText("Connected, trying to register client.");
      log.info(className, "Trying to register client...");

      client.sendMessage(String.format("REGISTER[%s][VIRTUAL]", playerName));

      DataInputStream stream = client.getStream();

      String line;
      try {
        while ((line = stream.readLine()) != null && client.isConnected()) {
          callback.receiveLoop(line);
        }
      } catch (IOException exception) {
        exception.printStackTrace();
      }
    }
  };

}
