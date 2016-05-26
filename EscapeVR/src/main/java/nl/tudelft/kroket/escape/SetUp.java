package nl.tudelft.kroket.escape;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.EventObject;
import java.util.Random;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.escape.EscapeVR.GameState;
import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.net.NetworkClient;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.scene.scenes.EscapeScene;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.screen.screens.LobbyScreen;
import nl.tudelft.kroket.screen.screens.SpookyScreen;
import jmevr.app.VRApplication;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;

import com.jme3.audio.AudioSource;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.util.SkyFactory;

/**
 * The EscapeVR class.
 */
public class SetUp implements EventListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */

  private Logger log = Logger.getInstance();

  /** Hostname of the gamehost. */
  private static final String HOSTNAME = "localhost";

  /** Portnumber of the gamehost. */
  private static int PORTNUM = 1234;

  private static int SECRECONN = 10;

  /** Observer object. */
  Spatial observer;

 
  // private long initTime = System.currentTimeMillis();

  private int remotePort;

  private EventManager eventManager;
  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;
  private NetworkClient client;
  private HeadUpDisplay hud;

  
  public SetUp(){
    initObjects();
    initHeadUpDisplay();
    initSceneManager();
    initAudioManager();
    eventManager = new EventManager(Launcher.mainApplication.getRootNode());
    initInputHandler();

    initScreenManager();
    initNetworkClient();

    sceneManager.getScene("escape").createScene();

    eventManager.addListener(this);
    eventManager.registerTrigger("painting", 4);
    eventManager.registerTrigger("painting2", 4);
    eventManager.registerTrigger("door", 3.5f);
  }
  
  private void initAudioManager() {
    audioManager = new AudioManager("Sound/");
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, true, 3);
    audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, true, 2);
    audioManager.loadFile("welcome", "Voice/intro2.wav", false, false, 5);
    audioManager.loadFile("letthegamebegin", "Voice/letthegamebegin3.wav", false, false, 5);
    audioManager.loadFile("muhaha", "Voice/muhaha.wav", false, false, 5);
  }

  private void initInputHandler() {
    inputHandler = new InputHandler(observer, eventManager, false);
  }

  private void initSceneManager() {
    sceneManager = new SceneManager();

    sceneManager.loadScene("escape", EscapeScene.class);

  }

  private void initScreenManager() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

    screenManager = new ScreenManager(guiCanvasSize.getX(), guiCanvasSize.getY());

    screenManager.loadScreen("lobby", LobbyScreen.class);
    screenManager.loadScreen("spooky", SpookyScreen.class);
  }

  private void initHeadUpDisplay() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
    hud = new HeadUpDisplay(guiCanvasSize);
  }

  private void initNetworkClient() {
    client = new NetworkClient(HOSTNAME, PORTNUM);

    Thread thread = new Thread() {

      private void sleep(int seconds) {
        try {
          Thread.sleep(seconds * 1000);
        } catch (InterruptedException exception) {
          exception.printStackTrace();
        }
      }

      @Override
      public void run() {

        boolean breakLoop = false;

        while (!breakLoop) {

          hud.setCenterText("Trying to connect to server...");

          while (!client.isConnected()) {
            if (!client.connect()) {
              log.info(className, "Failed to connect. Retrying...");

              hud.setCenterText("Unable to connect to server.", 2);
              client.close();
              sleep(2);

              hud.setCenterText("Trying to connect to server...", SECRECONN);
              sleep(SECRECONN);
            }
          }

          hud.setCenterText("Connected, trying to register client.");
          log.info(className, "Trying to register client...");

          client.sendMessage("REGISTER[Rift-User][VIRTUAL]");

          DataInputStream stream = client.getStream();

          String line;
          try {
            while ((line = stream.readLine()) != null && client.isConnected()) {
              receiveLoop(line);
            }
          } catch (IOException exception) {
            exception.printStackTrace();
          }
        }
      };
    };
    thread.start();

  }


  /**
   * Initialize the scene.
   */
  private void initObjects() {
    // Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
    observer = new Node("observer");

    Spatial sky = SkyFactory.createSky(Launcher.mainApplication.getAssetManager(), "Textures/Sky/Bright/spheremap.png",
        SkyFactory.EnvMapType.EquirectMap);
    Launcher.mainApplication.getRootNode().attachChild(sky);

    // test any positioning mode here (defaults to AUTO_CAM_ALL)
    VRGuiManager.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
    VRGuiManager.setGuiScale(0.4f);
    VRGuiManager.setPositioningElasticity(10f);

    observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

    VRApplication.setObserver(observer);
    Launcher.mainApplication.getRootNode().attachChild(observer);

    // do not use magic VR mouse cusor (same usage as non-VR mouse cursor)
    Launcher.mainApplication.getInputManager().setCursorVisible(false);

  }


  /**
   * Process remote input.
   * 
   * @param line
   *          incoming from remote source
   */
  public synchronized void remoteInput(final String line) {

    if (line.equals("START")) {
      screenManager.hideScreen("lobby");

      Launcher.mainApplication.setGameState(GameState.PLAYING);
      hud.setCenterText("");
    } else if (line.startsWith("INITVR[")) {

      int pos = line.indexOf(']');

      System.out.println("pos = " + pos);

      if (pos > 7) {
        String vrString = line.substring(7, pos);
        System.out.println("blah");
        if (vrString.equals("doneA")) {

          log.info(className, "Minigame A completed.");

          hud.setCenterText("Minigame A complete!", 30);

          // sceneManager.getScene("escape").destroyScene();
          // sceneManager.getScene("escape").destroyScene();
          // sceneManager.destroyScene("escape");
        } else if (vrString.equals("doneB")) {
          log.info(className, "Minigame B completed.");
          hud.setCenterText("Minigame B complete!", 30);

        }
      }

    } else {

      hud.setCenterText(line, 20);
    }

  }

  public EventManager getEventManager() {
    return eventManager;
  }

  public AudioManager getAudioManager() {
    return audioManager;
  }

  public InputHandler getInputHandler() {
    return inputHandler;
  }

  public SceneManager getSceneManager() {
    return sceneManager;
  }

  public ScreenManager getScreenManager() {
    return screenManager;
  }

  public HeadUpDisplay getHud() {
    return hud;
  }

  /**
   * Main callback method for handling remote input from socket.
   * 
   * @param messages
   *          the input received from the socket
   */
  public void receiveLoop(String message) {
    log.debug(className, "Message received: " + message);

    remoteInput(message);
  }

  @Override
  public void handleEvent(EventObject e) {

    if (e instanceof InteractionEvent) {
      InteractionEvent interactionEvent = (InteractionEvent) e;

      log.info(className, "Player interacted with object " + interactionEvent.getName());

      String objectName = interactionEvent.getName();

      client.sendMessage(String.format("INTERACT[%s]", objectName));

      switch (objectName) {
        case "door":
          // play spooky muhaha sound when player interacts with door
          audioManager.getNode("muhaha").play();
          hud.setCenterText("Muhahaha! You will never escape!", 5);
          break;
        case "painting":
          client.sendMessage("INITM[startA]");
          hud.setCenterText("Minigame A started!", 10);
          break;
        case "painting2":
          client.sendMessage("INITM[startB]");
          hud.setCenterText("Minigame B started!", 10);
          break;
        default:
          break;
      }

    }

  }


  /**
   * @return the remotePort
   */
  public int getRemotePort() {
    return remotePort;
  }

  /**
   * @param remotePort
   *          the remotePort to set
   */
  public void setRemotePort(int remotePort) {
    this.remotePort = remotePort;
  }

}