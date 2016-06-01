package nl.tudelft.kroket.escape;

import java.util.EventObject;
import java.util.HashMap;
import java.util.List;

import jmevr.app.VRApplication;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;
import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.input.InputHandler;

import nl.tudelft.kroket.input.interaction.MovementHandler;
import nl.tudelft.kroket.input.interaction.RotationHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.log.Logger.LogLevel;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.net.protocol.CommandParser;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.scene.scenes.EscapeScene;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.screen.screens.LobbyScreen;
import nl.tudelft.kroket.screen.screens.SpookyScreen;
import nl.tudelft.kroket.state.GameState;
import nl.tudelft.kroket.state.StateManager;
import nl.tudelft.kroket.state.states.LobbyState;
import nl.tudelft.kroket.state.states.PlayingState;

import com.jme3.bounding.BoundingSphere;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

/**
 * The EscapeVR class.
 */
public class EscapeVR extends VRApplication implements EventListener {

  /** Debug flag. */
  private final boolean DEBUG = true;

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

  private StateManager stateManager;
  private EventManager eventManager;
  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;
  private HeadUpDisplay hud;

  private ClientThread clientThread;

  private GameState initialState = LobbyState.getInstance();

  // private GameState playingState = PlayingState.getInstance();

  private boolean forceUpdate = false;

  // private CollisionHandler collisionHandler;
  private MovementHandler movementHandler;

  private void initStateManager() {
    stateManager = new StateManager(audioManager, inputHandler, sceneManager, screenManager,
        initialState);
  }

  private void initAudioManager() {
    audioManager = new AudioManager(getAssetManager(), rootNode, "Sound/");
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, true, 3);
    audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, true, 2);
    audioManager.loadFile("welcome", "Voice/intro2.wav", false, false, 5);
    audioManager.loadFile("letthegamebegin", "Voice/letthegamebegin3.wav", false, false, 5);
    audioManager.loadFile("muhaha", "Voice/muhaha.wav", false, false, 5);
    audioManager.loadFile("turret", "Voice/portal2/turret/turret_autosearch_6.ogg", false, false,
        1);
  }

  private void initInputHandler() {
    inputHandler = new InputHandler(getInputManager(), observer, eventManager);
  }

  private void initSceneManager() {
    sceneManager = new SceneManager(getAssetManager(), rootNode, getViewPort());
    sceneManager.loadScene("escape", EscapeScene.class);
  }

  private void initScreenManager() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

    screenManager = new ScreenManager(getAssetManager(), guiNode, guiCanvasSize.getX(),
        guiCanvasSize.getY());

    screenManager.loadScreen("lobby", LobbyScreen.class);
    screenManager.loadScreen("spooky", SpookyScreen.class);
  }

  private void initHeadUpDisplay() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
    hud = new HeadUpDisplay(getAssetManager(), guiNode, guiCanvasSize);
  }

  private void initNetworkClient() {
    clientThread = new ClientThread(this, hud);
    clientThread.setRemote(remoteHost, PORTNUM);
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

    initObjects();
    initHeadUpDisplay();
    initSceneManager();
    initAudioManager();
    initInputHandler();
    initScreenManager();
    initNetworkClient();
    initStateManager();

    movementHandler = new MovementHandler(observer);

    eventManager = new EventManager(observer, rootNode);
    inputHandler.registerMappings(new RotationHandler(observer), "left", "right", "lookup",
        "lookdown", "tiltleft", "tiltright");
    inputHandler.registerMappings(movementHandler, "forward", "back");
    inputHandler.registerMappings(eventManager, "Button A", "Button B", "Button X", "Button Y");

    // collisionHandler = new CollisionHandler(observer);

    // inputHandler.registerListener(collisionHandler);

    eventManager.addListener(this);

    if (DEBUG) {
      System.out.println("Switching gamestate");
      stateManager.setGameState(PlayingState.getInstance());
      log.setLevel(LogLevel.ALL);
    }

    delegateSceneObjects();
  }

  /**
   * Initialize the scene.
   */
  private void initObjects() {
    // Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();.5
    Sphere sp = new Sphere(10, 10, 0.2f);
    observer = new Geometry("observer", sp);
    Material mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
    observer.setMaterial(mat);

    Spatial sky = SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/spheremap.png",
        SkyFactory.EnvMapType.EquirectMap);
    rootNode.attachChild(sky);

    // test any positioning mode here (defaults to AUTO_CAM_ALL)
    VRGuiManager.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
    VRGuiManager.setGuiScale(0.4f);
    VRGuiManager.setPositioningElasticity(10f);

    observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

    VRApplication.setObserver(observer);
    rootNode.attachChild(observer);

    // do not use magic VR mouse cusor (same usage as non-VR mouse cursor)
    getInputManager().setCursorVisible(true);
    // observer.setModelBound(bound);

    // System.out.println(observer.getWo);

  }

  private void delegateSceneObjects() {

    System.out.println("delegating scene objects");

    List<Spatial> objects = rootNode.getChildren();

    for (Spatial object : objects) {

      if (object == null)
        continue;

      System.out.println(object.toString());

      if (object instanceof Geometry)
        eventManager.registerObjectInteractionTrigger(object.getName(), 4f);
      else if (object instanceof Node)
        eventManager.registerObjectInteractionTrigger(object.getName(), 4f);

    }

    movementHandler.addObject(rootNode.getChild("wall-south"));
    movementHandler.addObject(rootNode.getChild("wall-north"));
    movementHandler.addObject(rootNode.getChild("safe-objnode"));
  }

  /**
   * Main method to update the scene.
   */
  @Override
  public void simpleUpdate(float tpf) {

    stateManager.update(tpf);

    hud.update();

    if (forceUpdate) {
      stateManager.setGameState(PlayingState.getInstance());
      forceUpdate = false;
      delegateSceneObjects();
    }
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
        forceUpdate = true;
        hud.setCenterText("");
        break;
      case "INITVR":
        if (command.containsKey("param_0")) {
          if (command.get("param_0").equals("doneA")) {
            log.info(className, "Minigame A completed.");
            hud.setCenterText("Minigame A complete!", 10);
          } else if (command.get("param_0").equals("doneB")) {
            log.info(className, "Minigame B completed.");
            hud.setCenterText("Minigame B complete!", 10);
          } else if (command.get("param_0").equals("doneC")) {
            log.info(className, "Minigame C completed.");
            hud.setCenterText("Minigame C complete!", 10);
          } else if (command.get("param_0").equals("doneD")) {
            log.info(className, "Minigame D completed.");
            hud.setCenterText("Minigame D complete!", 10);
          } else if (command.get("param_0").equals("startA")) {
            log.info(className, "Minigame A started.");
            hud.setCenterText("Minigame A started!", 10);
          } else if (command.get("param_0").equals("startB")) {
            log.info(className, "Minigame B started.");
            hud.setCenterText("Minigame B started!", 10);
          } else if (command.get("param_0").equals("startC")) {
            log.info(className, "Minigame C started.");
            hud.setCenterText("Minigame C started!", 10);
          } else if (command.get("param_0").equals("startD")) {
            log.info(className, "Minigame D started.");
            hud.setCenterText("Minigame D started!", 10);
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

      clientThread.sendMessage(String.format("INTERACT[%s]", objectName));

      switch (objectName) {
      case "portalturret-geom-0":
        audioManager.getNode("turret").play();
        break;
      case "door":
        // play spooky muhaha sound when player interacts with door
        audioManager.getNode("muhaha").play();
        hud.setCenterText("Muhahaha! You will never escape!", 5);
        break;
      case "painting":
        clientThread.sendMessage("INITM[startA]");
        break;
      case "painting2":
        clientThread.sendMessage("INITM[startB]");
        break;
      default:
        break;
      }

    }

  }

  /**
   * @return the remoteHost
   */
  public String getRemoteHost() {
    return remoteHost;
  }

  /**
   * @param remoteHost
   *          the remoteHost to set
   */
  public void setRemoteHost(String remoteHost) {
    this.remoteHost = remoteHost;
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