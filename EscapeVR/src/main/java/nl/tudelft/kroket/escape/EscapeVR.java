package nl.tudelft.kroket.escape;

import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

import jmevr.app.VRApplication;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;
import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.input.interaction.CollisionHandler;
import nl.tudelft.kroket.input.interaction.MovementHandler;
import nl.tudelft.kroket.input.interaction.RotationHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.log.Logger.LogLevel;
import nl.tudelft.kroket.minigame.MinigameManager;
import nl.tudelft.kroket.minigame.minigames.ColorSequenceMinigame;
import nl.tudelft.kroket.minigame.minigames.GyroscopeMinigame;
import nl.tudelft.kroket.minigame.minigames.PictureCodeMinigame;
import nl.tudelft.kroket.minigame.minigames.TapMinigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.net.protocol.CommandParser;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.scene.scenes.EscapeScene;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.screen.screens.ControllerScreen;
import nl.tudelft.kroket.screen.screens.LobbyScreen;
import nl.tudelft.kroket.screen.screens.SpookyScreen;
import nl.tudelft.kroket.state.GameState;
import nl.tudelft.kroket.state.StateManager;
import nl.tudelft.kroket.state.states.LobbyState;
import nl.tudelft.kroket.state.states.PlayingState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

/**
 * The EscapeVR class.
 */
public class EscapeVR extends VRApplication implements EventListener {

  /** Debug flag. */
  private final boolean DEBUG = false;

  private static final Vector3f spawnPosition = new Vector3f(0, 0, 0);

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Portnumber of the gamehost. */
  private static int PORTNUM = 1234;

  /** Observer object. */
  Spatial observer;

  private String remoteHost;
  private int remotePort;
  private String playerName = "VR-USER";

  private StateManager stateManager;
  private EventManager eventManager;
  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;
  private HeadUpDisplay hud;

  private MinigameManager mgManager;

  /** Thread reference used for the TCP connection. */
  private ClientThread clientThread;

  /** The initial state of the game. */
  private GameState initialState = LobbyState.getInstance();

  /** List of all rigid objects. */
  private List<String> rigidObjects = new ArrayList<String>(Arrays.asList("safe-objnode",
      "knight1-geom-0", "knight2-geom-0", "Desk-objnode"));

  // private CollisionHandler collisionHandler;
  private MovementHandler movementHandler;

  private GameState currentState;

  private void initStateManager() {
    stateManager = new StateManager(audioManager, inputHandler, sceneManager, screenManager,
        initialState);
  }

  private void initAudioManager() {
    audioManager = new AudioManager(getAssetManager(), rootNode, "Sound/");
    audioManager.loadFile("waiting", "Soundtrack/alone.wav", false, true, 0.75f);
    audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, true, 0.75f);
    audioManager.loadFile("welcome", "Voice/intro2.wav", false, false, 0.5f);
    audioManager.loadFile("letthegamebegin", "Voice/letthegamebegin3.wav", false, false, 1.0f);
    audioManager.loadFile("muhaha", "Voice/muhaha.wav", false, false, 1.0f);
    audioManager.loadFile("turret", "Voice/portal2/turret/turret_autosearch_6.ogg", false, false,
        0.5f);
  }

  private void initInputHandler() {
    inputHandler = new InputHandler(getInputManager(), eventManager);
  }

  private void initSceneManager() {
    sceneManager = new SceneManager(getAssetManager(), rootNode, getViewPort());
    sceneManager.loadScene("escape", EscapeScene.class);
  }

  private void initScreenManager() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

    screenManager = new ScreenManager(getAssetManager(), guiNode, guiCanvasSize.getX(),
        guiCanvasSize.getY());

    // pre-load all screens
    screenManager.loadScreen("lobby", LobbyScreen.class);
    screenManager.loadScreen("spooky", SpookyScreen.class);
    screenManager.loadScreen("controller", ControllerScreen.class);
  }

  private void initHeadUpDisplay() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
    hud = new HeadUpDisplay(getAssetManager(), guiNode, guiCanvasSize);
  }

  private void initNetworkClient() {
    clientThread = new ClientThread(this, hud);
    clientThread.setRemote(remoteHost, PORTNUM);
    clientThread.setPlayerName(playerName);
    clientThread.start();
  }

  /**
   * Initialize the application.
   */
  @Override
  public void simpleInitApp() {

    if (VRApplication.getVRHardware() != null) {
      System.out.println("Attached device: " + VRApplication.getVRHardware().getName());
    }

    // Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

    // create a sphere around the observer for our collision detection
    Sphere sphere = new Sphere(10, 50, 0.4f);
    observer = new Geometry("observer", sphere);

    // the sphere should have no shaded material
    Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    observer.setMaterial(mat);

    Spatial sky = SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/spheremap.png",
        SkyFactory.EnvMapType.EquirectMap);
    rootNode.attachChild(sky);

    // test any positioning mode here (defaults to AUTO_CAM_ALL)
    VRGuiManager.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
    VRGuiManager.setGuiScale(0.4f);
    VRGuiManager.setPositioningElasticity(10f);

    observer.setLocalTranslation(spawnPosition);

    VRApplication.setObserver(observer);
    rootNode.attachChild(observer);

    // do not use magic VR mouse cusor (same usage as non-VR mouse cursor)
    getInputManager().setCursorVisible(true);
    // observer.setModelBound(bound);

    initHeadUpDisplay();
    initSceneManager();
    initAudioManager();
    initInputHandler();
    initScreenManager();
    initNetworkClient();
    initStateManager();

    movementHandler = new MovementHandler(observer, rootNode);

    eventManager = new EventManager(observer, rootNode);
    inputHandler.registerMappings(new RotationHandler(observer), "left", "right", "lookup",
        "lookdown", "tiltleft", "tiltright");
    inputHandler.registerMappings(movementHandler, "forward", "back");
    inputHandler.registerMappings(eventManager, "Button A", "Button B", "Button X", "Button Y");

    inputHandler.registerListener(new CollisionHandler(observer, sceneManager.getScene("escape")
        .getBoundaries()));

    eventManager.addListener(this);

    mgManager = new MinigameManager(hud, clientThread, screenManager, sceneManager);
    eventManager.addListener(mgManager);

    if (DEBUG) {
      // when in debug mode, force the game to
      // playing state
      setGameState(PlayingState.getInstance());
      log.setLevel(LogLevel.ALL);
    } else {
      setGameState(initialState);
    }
  }

  /**
   * Set the current game state.
   * 
   * @param state
   *          the gamestate
   */
  private void setGameState(GameState state) {
    this.currentState = state;
  }

  /**
   * Register all objects in the environment.
   */
  private void registerObjects() {

    log.debug(className, "Registering objects...");

    List<Spatial> objects = rootNode.getChildren();

    for (Spatial object : objects) {

      // ignore objects that are null
      if (object == null) {
        continue;
      }

      // ignore AudioNodes
      if (object instanceof AudioNode) {
        continue;
      }

      // ignore the observer as we can't interact with ourselves
      if (object.getName().equals("observer")) {
        continue;
      }

      // only process objects that extend either Geomtery or Node
      if (object instanceof Geometry || object instanceof Node) {

        log.debug(className, String.format("Registering trigger for %s", object.toString()));
        eventManager.registerObjectInteractionTrigger(object.getName(), 4f);
      }
    }

    // register all rigid objects with the movementhandler
    // the movementhandler will force the observer
    // to stay away from these objects
    for (String objectName : rigidObjects) {
      movementHandler.addObject(objectName);
    }

  }

  /**
   * Main method to update the game.
   */
  @Override
  public void simpleUpdate(float tpf) {

    stateManager.update(tpf);

    hud.update();

    if (stateManager.getCurrentState() != currentState) {
      stateManager.setGameState(currentState);
      registerObjects();
    }

    mgManager.update(tpf);
  }

  /**
   * Process remote input.
   * 
   * @param line
   *          incoming from remote source
   */
  public synchronized void remoteInput(final String line) {

    HashMap<String, String> command = CommandParser.parseInput(line);

    if (command.containsKey("command")) {

      switch (command.get("command")) {
      case "START":
        registerObjects();
        setGameState(PlayingState.getInstance());
        hud.setCenterText("");
        break;

      // Messages received from the mobile player
      case "INITVR":
        if (command.containsKey("param_0")) {
          String action = command.get("param_0");

          // End minigames, which are ended by the mobile player
          if (action.equals("doneA") || action.equals("doneB")) {
            if (mgManager.getCurrent() != null) {
              mgManager.endGame();
              registerObjects();
            }

            // Start minigames
          } else if (action.equals("startC")) {
            mgManager.launchGame(ColorSequenceMinigame.getInstance());
            if (mgManager.getCurrent() instanceof ColorSequenceMinigame) {
              ((ColorSequenceMinigame) mgManager.getCurrent()).parseColors(CommandParser
                  .parseParams(line));
            }
            // } else if (action.equals("startD")) {
            // mgManager.launchGame(GyroscopeMinigame.getInstance());
          }
        }
        break;

      // Messages sent by the VR client itself, which it gets back as verification.
      case "INITM":
        if (command.containsKey("param_0")) {
          String action = command.get("param_0");

          // Verifiation from server that minigames should start
          if (action.equals("startA")) {
            mgManager.launchGame(PictureCodeMinigame.getInstance());
          } else if (action.equals("startB")) {
            mgManager.launchGame(TapMinigame.getInstance());
          }

          // End minigames, which are ended by the mobile player
          if (action.equals("doneC")) {
            if (mgManager.getCurrent() != null) {
              mgManager.endGame();
            }
          }
        }
        break;

      default:
        hud.setCenterText(line, 20);
      }
    }
  }

  /**
   * Main callback method for handling remote input from socket.
   * 
   * @param message
   *          the input received from the socket.
   */
  public void receiveLoop(String message) {

    log.debug(className, "Message received: " + message);

    remoteInput(message);
  }

  @Override
  public void handleEvent(EventObject ev) {

    if (ev instanceof InteractionEvent) {
      InteractionEvent interactionEvent = (InteractionEvent) ev;

      log.info(className, "Player interacted with object " + interactionEvent.getName());

      String objectName = interactionEvent.getName();

      clientThread.sendMessage(String.format("INTERACT[%s]", objectName));

      switch (objectName) {
      case "portalturret-geom-0":
        audioManager.getNode("turret").play();
        break;
      case "door":
        log.info(className, "Muhahaha???");
        // Play spooky muhaha sound when player interacts with door
        audioManager.getNode("muhaha").play();
        hud.setCenterText("Muhahaha! You will never escape!", 5);
        break;
      case "painting":
        clientThread.sendMessage("INITM[startA]");
        break;
      case "painting2":
        clientThread.sendMessage("INITM[startB]");
        break;
      case "fourbuttons2-objnode":
        clientThread.sendMessage("INITM[startC]");
        break;
      default:
        break;
      }

    }

  }

  /**
   * Get the remote host.
   * 
   * @return the remoteHost
   */
  public String getRemoteHost() {
    return remoteHost;
  }

  /**
   * Set the remote host.
   * 
   * @param remoteHost
   *          the remoteHost to set
   */
  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
  }

  /**
   * Get the remote port.
   * 
   * @return the remotePort
   */
  public int getRemotePort() {
    return remotePort;
  }

  /**
   * Set the remote port.
   * 
   * @param remotePort
   *          the remotePort to set
   */
  public void setRemotePort(int remotePort) {
    this.remotePort = remotePort;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;

  }

}