package nl.tudelft.kroket.escape;

import com.jme3.audio.AudioNode;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.SkyFactory;

import jmevr.app.VRApplication;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;
import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.GameLostEvent;
import nl.tudelft.kroket.event.events.GameStartEvent;
import nl.tudelft.kroket.event.events.GameWonEvent;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.event.events.MinigameCompleteEvent;
import nl.tudelft.kroket.event.events.StartMinigameEvent;
import nl.tudelft.kroket.event.events.TimeoutEvent;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.input.interaction.CollisionHandler;
import nl.tudelft.kroket.input.interaction.MovementHandler;
import nl.tudelft.kroket.input.interaction.RotationHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.log.Logger.LogLevel;
import nl.tudelft.kroket.minigame.MinigameManager;
import nl.tudelft.kroket.minigame.minigames.ColorSequenceMinigame;
import nl.tudelft.kroket.minigame.minigames.LockMinigame;
import nl.tudelft.kroket.minigame.minigames.PictureCodeMinigame;
import nl.tudelft.kroket.minigame.minigames.TapMinigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.net.protocol.CommandParser;
import nl.tudelft.kroket.net.protocol.Protocol;
import nl.tudelft.kroket.scene.Scene;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.scene.scenes.EscapeScene;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.screen.screens.ControllerScreen;
import nl.tudelft.kroket.screen.screens.GameoverScreen;
import nl.tudelft.kroket.screen.screens.GamewonScreen;
import nl.tudelft.kroket.screen.screens.LobbyScreen;
import nl.tudelft.kroket.screen.screens.SpookyScreen;
import nl.tudelft.kroket.state.GameState;
import nl.tudelft.kroket.state.StateManager;
import nl.tudelft.kroket.state.states.GameLostState;
import nl.tudelft.kroket.state.states.GameWonState;
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

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

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

  /** The manager for the minigames. */
  private MinigameManager mgManager;

  /** Thread reference used for the TCP connection. */
  private ClientThread clientThread;

  /** The initial state of the game. */
  private GameState initialState = LobbyState.getInstance();

  /** List of all rigid objects. */
  private List<String> rigidObjects = new ArrayList<String>(Arrays.asList("safe-objnode",
      "knight1-geom-0", "knight2-geom-0", "DeskLaptop-objnode", "safeopen-objnode"));

  // private CollisionHandler collisionHandler;
  private MovementHandler movementHandler;

  private GameState currentState;

  private int timeLimit = Settings.TIMELIMIT;

  private CollisionHandler collisionHandler;

  /**
   * Initialize the stateManager.
   */
  private void initStateManager() {
    stateManager = new StateManager(audioManager, inputHandler, sceneManager, screenManager, hud,
        eventManager, initialState);
  }

  /**
   * Initialize the audio files.
   */
  private void initAudioManager() {
    audioManager = new AudioManager(getAssetManager(), rootNode, "Sound/");
    audioManager.loadFile("waiting", "Soundtrack/alone.wav", false, true, 0.75f);
    audioManager.loadFile("alone", "Soundtrack/alone.wav", false, true, 1.0f);
    audioManager.loadFile("lobby", "Soundtrack/lobby16.wav", false, true, 0.9f);
    audioManager.loadFile("ambient", "Soundtrack/ambient16.wav", false, true, 0.75f);
    audioManager.loadFile("welcome", "Voice/intro2.wav", false, false, 0.5f);
    audioManager.loadFile("letthegamebegin", "Voice/letthegamebegin3.wav", false, false, 1.0f);
    audioManager.loadFile("muhaha", "Voice/muhaha.wav", false, false, 1.0f);
    audioManager.loadFile("turret", "Voice/portal2/turret/turret_autosearch_6.ogg", false, false,
        0.5f);

    audioManager.loadFile("click", "ui/portal2/back.wav", false, false, 0.8f);
    audioManager.loadFile("door", "ui/portal2/default_locked.wav", false, false, 0.8f);

    audioManager.loadFile("error", "ui/portal2/klaxon1.wav", false, false, 0.8f);
    audioManager.loadFile("gamecomplete", "ui/portal2/startup_02_01.wav", false, false, 0.8f);
    audioManager
        .loadFile("gamebegin", "ui/portal2/p2_store_ui_checkout_01.wav", false, false, 0.8f);
    audioManager.loadFile("gamelost", "Soundtrack/gamelost.wav", false, false, 1.0f);

  }

  /**
   * Initialize the inputHandler.
   */
  private void initInputHandler() {
    inputHandler = new InputHandler(getInputManager(), eventManager);
  }

  /**
   * Initialize the sceneManager.
   */
  private void initSceneManager() {
    sceneManager = new SceneManager(getAssetManager(), rootNode, getViewPort());
    sceneManager.loadScene("escape", EscapeScene.class);
  }

  /**
   * Initialize the screenManager.
   */
  private void initScreenManager() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

    screenManager = new ScreenManager(getAssetManager(), guiNode, guiCanvasSize.getX(),
        guiCanvasSize.getY());

    // pre-load all screens
    screenManager.loadScreen("lobby", LobbyScreen.class);
    screenManager.loadScreen("spooky", SpookyScreen.class);
    screenManager.loadScreen("controller", ControllerScreen.class);
    screenManager.loadScreen("gameover", GameoverScreen.class);
    screenManager.loadScreen("gamewon", GamewonScreen.class);
  }

  /**
   * Initialize the head up display.
   */
  private void initHeadUpDisplay() {
    Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
    hud = new HeadUpDisplay(getAssetManager(), guiNode, guiCanvasSize);

  }

  /**
   * Initialize the network client.
   */
  private void initNetworkClient() {
    clientThread = new ClientThread(this, hud);
    clientThread.setRemote(remoteHost, Settings.PORTNUM);
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

    observer.setCullHint(CullHint.Always);
    observer.setMaterial(mat);

    Spatial sky = SkyFactory.createSky(getAssetManager(), "Textures/Sky/Bright/spheremap.png",
        SkyFactory.EnvMapType.EquirectMap);
    rootNode.attachChild(sky);

    // test any positioning mode here (defaults to AUTO_CAM_ALL)
    VRGuiManager.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
    VRGuiManager.setGuiScale(0.4f);
    VRGuiManager.setPositioningElasticity(10f);

    observer.setLocalTranslation(Settings.spawnPosition);

    VRApplication.setObserver(observer);
    rootNode.attachChild(observer);

    // do not use magic VR mouse cusor (same usage as non-VR mouse cursor)
    getInputManager().setCursorVisible(true);
    // observer.setModelBound(bound);

    eventManager = new EventManager(observer, rootNode);

    initScreenManager();

    initHeadUpDisplay();
    initSceneManager();
    initAudioManager();
    initInputHandler();

    initNetworkClient();
    initStateManager();

    movementHandler = new MovementHandler(observer, rootNode);
    movementHandler.setLockHorizontal(true);
    
    inputHandler.registerMappings(new RotationHandler(observer), "left", "right", "lookup",
        "lookdown", "tiltleft", "tiltright");
    inputHandler.registerMappings(movementHandler, "forward", "back");
    inputHandler.registerMappings(eventManager, "Button A", "Button B", "Button X", "Button Y");

    collisionHandler = new CollisionHandler(observer, sceneManager.getScene("escape")
        .getBoundaries());

    inputHandler.registerListener(collisionHandler);

    eventManager.addListener(this);

    mgManager = new MinigameManager(hud, clientThread, screenManager, sceneManager);
    eventManager.addListener(mgManager);

    if (Settings.DEBUG) {
      // when in debug mode, force the game to start
      eventManager.addEvent(new GameStartEvent(this));
      log.setLevel(LogLevel.ALL);
    } else {
      log.setLevel(LogLevel.INFO);
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
        eventManager.registerObjectInteractionTrigger(object.getName(), 6f);
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

      log.info(className, "Current state is "
          + stateManager.getCurrentState().getClass().getSimpleName());

      setTimeLimit(timeLimit);
    }

    mgManager.update(tpf);
  }

  /**
   * Starts a minigame.
   * 
   * @param gameName
   *          the name of the game to be started.
   */
  private void startMinigame(String gameName) {

    log.info(className, "Trying to start game " + gameName);

    switch (gameName) {
    case "A":
      mgManager.launchGame(PictureCodeMinigame.getInstance());
      break;
    case "B":
      mgManager.launchGame(TapMinigame.getInstance());
      break;
    case "C":
      mgManager.launchGame(ColorSequenceMinigame.getInstance());
      break;
    case "D":
      mgManager.launchGame(LockMinigame.getInstance());
      registerObjects();
      break;
    default:
      log.error(className, "Unknown game: " + gameName);

      break;
    }
  }

  private void startGame() {

    log.info(className, "startGame()");

    registerObjects();
    setGameState(PlayingState.getInstance());
    hud.setCenterText("");

  }

  private void setTimeLimit(int seconds) {
    if (stateManager.getCurrentState() == null) {
      log.error(className, "currentState == null");
    } else {

      log.info(className, "setTimeLimit: Current state is "
          + stateManager.getCurrentState().getClass().getSimpleName());

      if (stateManager.getCurrentState() instanceof PlayingState) {
        log.info(className, "Updating timelimit...");
        PlayingState playingState = (PlayingState) stateManager.getCurrentState();
        playingState.setTimeLimit(seconds);
      } else {
        log.error(className, "Could not update timelimit (invalid gamestate)");

      }
    }

  }

  /**
   * Process remote input.
   * 
   * @param line
   *          incoming from remote source
   */
  public synchronized void remoteInput(final String line) {

    log.info(className, "Input received: " + line);

    HashMap<String, String> command = CommandParser.parseInput(line);
    System.out.println(command);

    if (command.containsKey("command")) {

      switch (command.get("command")) {

      case Protocol.COMMAND_START:
        eventManager.addEvent(new GameStartEvent(this));
        break;

      case Protocol.COMMAND_BEGIN:
        if (command.containsKey("param_0")) {
          String action = command.get("param_0");

          startMinigame(action);

          eventManager.addEvent(new StartMinigameEvent(this));

          if (action.equals("B")) {
            if (mgManager.getCurrent() instanceof TapMinigame) {
              TapMinigame tapGame = (TapMinigame) mgManager.getCurrent();
              tapGame.parseButtons(CommandParser.parseParams(line));
            }

          } else if (action.equals("C")) {
            if (mgManager.getCurrent() instanceof ColorSequenceMinigame) {
              ColorSequenceMinigame colorGame = (ColorSequenceMinigame) mgManager.getCurrent();
              colorGame.parseColors(CommandParser.parseParams(line));
            }
          }

        }
        break;
      case Protocol.COMMAND_DONE:
        if (command.containsKey("param_0")) {
          String action = command.get("param_0");

          if (mgManager.isActive(action)) {
            // if (mgManager.gameActive() && action.equals(mgManager.getCurrent().getName())) {
            //
            eventManager.addEvent(new MinigameCompleteEvent(this));
            mgManager.endGame();
          }

        }
        registerObjects();
        break;

      case Protocol.COMMAND_TIMELIMIT:
        if (command.containsKey("param_0")) {
          String parameter = command.get("param_0");
          if (!parameter.isEmpty()) {
            // setTimeLimit(Integer.parseInt(parameter));
            timeLimit = Integer.parseInt(parameter);
          }
        }
        break;

      case Protocol.COMMAND_GAMEOVER:
        eventManager.addEvent(new GameLostEvent(this));
        hud.setTimerText("");
        break;
      case Protocol.COMMAND_GAMEWON:
        eventManager.addEvent(new GameWonEvent(this));
        hud.setCenterText("");
        hud.setTimerText("");
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

    log.info(className, "Message received: " + message);

    remoteInput(message);
  }

  @Override
  public void handleEvent(EventObject ev) {

    log.info(className, "Event received: " + ev.toString());

    if (ev instanceof StartMinigameEvent) {
      audioManager.play("gamebegin");
    } else if (ev instanceof MinigameCompleteEvent) {
      audioManager.play("gamecomplete");
    } else if (ev instanceof TimeoutEvent) {
      // yeah this is kinda weird but I dont want to keep the behaviour for these events seperated
      setGameState(GameLostState.getInstance());
    } else if (ev instanceof GameWonEvent) {
      setGameState(GameWonState.getInstance());
      observer.setLocalTranslation(Settings.winingPosition);
      collisionHandler.disableRestriction();
      movementHandler.setLockHorizontal(false);
      
      
      movementHandler.addObject("wall-north");
      movementHandler.addObject("wall-south");
      movementHandler.addObject("wall-east");
      movementHandler.addObject("wall-west");
      movementHandler.addObject("roof");
      movementHandler.addObject("floor");
      
    } else if (ev instanceof GameStartEvent) {
      startGame();
    } else if (ev instanceof GameLostEvent) {
      setGameState(GameLostState.getInstance());
    } else if (ev instanceof InteractionEvent) {
      InteractionEvent interactionEvent = (InteractionEvent) ev;

      log.info(className, "Player interacted with object " + interactionEvent.getName());

      String objectName = interactionEvent.getName();

      EscapeScene escapeScene = null;
      Scene scene = sceneManager.getScene("escape");
      if (scene instanceof EscapeScene) {
        escapeScene = ((EscapeScene) sceneManager.getScene("escape"));
      }

      // clientThread.sendMessage(String.format("INTERACT[%s]", objectName));

      switch (objectName) {
      case "portalturret-geom-0":
        audioManager.getNode("turret").play();
        break;
      case "door-geom-0":

        // Play spooky muhaha sound when player interacts with door
        audioManager.playInstance("door");

        if (!mgManager.gameActive()) {
          log.info(className, "Muhahaha???");
          audioManager.play("muhaha");
          hud.setCenterText("Muhahaha! You will never escape!", 5);
        }
        clientThread.sendMessage("BEGIN[D]");

        break;
      case "painting":
        clientThread.sendMessage("BEGIN[A]");
        break;
      case "DeskLaptop-objnode":
        clientThread.sendMessage("BEGIN[B]");
        break;
      case "fourbuttons2-objnode":
        clientThread.sendMessage("BEGIN[C]");
        break;
      case "safeopen-objnode":
        hud.setCenterText("You found login data for the computer!");
        clientThread.sendMessage("DONE[A][ADVANCE]");
        break;

      case "D1":
        audioManager.playInstance("click");
        escapeScene.remove("D1");
        escapeScene.addCode13("Textures/Painting/13.jpg", "D_13");
        escapeScene.addCode37("Textures/Painting/questionmark.jpg", "D2");
        registerObjects();
        break;
      case "D2":
        audioManager.playInstance("click");
        escapeScene.remove("D2");
        escapeScene.addCode37("Textures/Painting/37.jpg", "D_37");
        escapeScene.addCode21("Textures/Painting/questionmark.jpg", "D3");
        registerObjects();
        break;
      case "D3":
        audioManager.playInstance("click");
        escapeScene.remove("D3");
        escapeScene.addCode21("Textures/Painting/21.jpg", "D_21");
        registerObjects();
        break;
      default:
        if (!mgManager.gameActive()) {
          audioManager.playInstance("click");
        }
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

  /**
   * Sets the player name.
   * 
   * @param playerName
   *          as string
   */
  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  /**
   * Gets the playername.
   * 
   * @return the playername as string.
   */
  public String getPlayerName() {
    return this.playerName;
  }

}
