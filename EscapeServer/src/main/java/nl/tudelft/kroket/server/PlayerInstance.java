package nl.tudelft.kroket.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.player.Player;
import nl.tudelft.kroket.player.Player.PlayerType;

/**
 * ConnectionThread object to handle interaction with a client.
 * 
 * @author Team Kroket
 *
 */
public class PlayerInstance implements Runnable {

  /** Singleton reference to logger. */
  static final Logger LOG = Logger.getInstance();

  /** Class simpleName, used as tag for logging. */
  private final String className = this.getClass().getSimpleName();

  /** Client's DataInputStream. */
  DataInputStream inputStream = null;

  /** Client's DataOutputStream. */
  DataOutputStream outputStream = null;

  /** The type of player. */
  Player.PlayerType type = PlayerType.NONE;

  /** Reference to the socket of the connected client. */
  protected Socket clientSocket = null;

  /**
   * Constructor for ConnectionThread.
   * 
   * @param clientSocket
   *            the socket of the client
   */
  public PlayerInstance(final Socket clientSocket) {
    this.clientSocket = clientSocket;
  }

  /**
   * Process remote input.
   * 
   * @param input
   *            the remote input
   */
  private void processInput(final String input) {
    Logger.getInstance().info(className, input);

    if (input.startsWith("TYPE[")) {

      // find the closing bracket
      final int pos = input.indexOf(']');
      final String typeString = input.substring(5, pos);

      if (pos > 6) {

        final Player player = EscapeServer.getPlayer(clientSocket);

        if (player == null) {
          System.out.println("Player not registered yet.");
        } else {

          try {
            type = PlayerType.valueOf(typeString);
            
            // set the type of the player
            player.setType(type);

            // tell player what his new type is
            player.sendMessage("Your type is now set to "
                + type.toString());
            
            LOG.info(className, String.format(
                "Player %s is now set to type %s.",
                player.getName(), player.getType().toString()));

            // if the game isn't ready, let the new player know
            if (!EscapeServer.ready()) {
              player.sendMessage(EscapeServer.countPlayers()
                  + " player(s) connected.");
            }
          } catch (IllegalArgumentException error) {
            player.sendMessage("Invalid client type.");
          }

        }
      }

    } else if (input.startsWith("REGISTER[")) {

      // find closing bracket
      int pos = input.indexOf(']');

      if (pos > 9) {
        final String name = input.substring(9, pos);

        // try register the new player
        EscapeServer.registerPlayer(clientSocket, outputStream, name);
      } else {
        LOG.error(className,
            "Closing bracket not found or in invalid position.");
      }

    } else if (input.startsWith("admin")) {
      adminCommand(input.substring(6));
    } else if (input.startsWith("INITM[")) {
      int pos = input.indexOf(']');
      // String name = input.substring(6, pos);
      EscapeServer.sendMobile(input);

    } else if (input.startsWith("INITVR[")) {
      int pos = input.indexOf(']');
      // String name = input.substring(7, pos);
      EscapeServer.sendVirtual(input);

    }

  }
  
  /**
   * Process admin command.
   * 
   * @param command - the command string
   */
  public void adminCommand(final String command) {
    if (command.startsWith("sendall")) {
      EscapeServer.sendAll(command.substring(8));
    }
  }

  /**
   * Run the server thread.
   */
  public void run() {
    try {
      final InputStream input = clientSocket.getInputStream();
      final OutputStream output = clientSocket.getOutputStream();

      inputStream = new DataInputStream(input);
      outputStream = new DataOutputStream(output);

      String response = null;
      while ((response = inputStream.readLine()) != null) {
        processInput(response);
      }

      output.close();
      input.close();
    } catch (IOException error) {

      // in case an exception occured, remove and disconnect the client
      EscapeServer.removePlayer(clientSocket);

    }
  }
}
