package nl.tudelft.kroket.escape;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.EventObject;

import nl.tudelft.kroket.audio.AudioManager;
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
import jmevr.app.VRApplication;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioSource;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

/**
 * The EscapeVR class.
 */
public class EscapeVR extends VRApplication implements EventListener {

	/** Current class, used as tag for logger. */
	private final String className = this.getClass().getSimpleName();

	/** Singleton logger instance. */

	private Logger log = Logger.getInstance();

	/** Hostname of the gamehost. */
	private static final String HOSTNAME = "localhost";

	/** Portnumber of the gamehost. */
	private static int PORTNUM = 1234;

	EventManager eventManager;

	private static int SECRECONN = 5;

	/** Enum for all GameStates. */
	public enum GameState {
		NONE, LOBBY, INTRO, PLAYING
	}

	/** Observer object. */
	Spatial observer;

	/** Current gamestate. */
	private GameState currentState = GameState.NONE;

	private boolean forceUpdateState = true;

	/** State to force game to. */
	private GameState insertState = GameState.PLAYING; // start in lobby

	private AudioManager audioManager;
	private InputHandler inputHandler;
	private SceneManager sceneManager;
	private ScreenManager screenManager;
	private NetworkClient client;
	private HeadUpDisplay hud;

	private void initAudioManager() {
		audioManager = new AudioManager(getAssetManager(), rootNode, "Sound/");
		audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, true,
				3);
		audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, true,
				2);
		audioManager.loadFile("welcome", "Voice/intro2.wav", false, false, 5);
		audioManager.loadFile("letthegamebegin", "Voice/letthegamebegin3.wav",
				false, false, 5);
		audioManager.loadFile("muhaha", "Voice/muhaha.wav", false, false, 5);
	}

	private void initInputHandler() {
		inputHandler = new InputHandler(getInputManager(), observer,
				eventManager, false);
	}

	private void initSceneManager() {
		sceneManager = new SceneManager(getAssetManager(), rootNode,
				getViewPort());

		sceneManager.loadScene("escape", EscapeScene.class);
	}

	private void initScreenManager() {
		Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();

		screenManager = new ScreenManager(getAssetManager(), guiNode,
				guiCanvasSize.getX(), guiCanvasSize.getY());

		screenManager.loadScreen("lobby", LobbyScreen.class);
	}

	private void initHeadUpDisplay() {
		Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		hud = new HeadUpDisplay(getAssetManager(), guiNode, guiCanvasSize);
	}

	private void initNetworkClient() {
		client = new NetworkClient(HOSTNAME, PORTNUM);

		Thread thread = new Thread() {
			@Override
			public void run() {

				boolean breakLoop = false;

				while (!breakLoop) {

					while (!client.isConnected()) {
						if (!client.connect()) {
							log.info(className,
									"Failed to connect. Retrying...");

							hud.setCenterText("Trying to connect to server...");
							client.close();
							try {
								Thread.sleep(SECRECONN * 1000);

							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					log.info(className, "Trying to register client...");

					client.sendMessage("REGISTER[Rift-User]");
					client.sendMessage("TYPE[VIRTUAL]");

					DataInputStream stream = client.getStream();

					String line;
					try {
						while ((line = stream.readLine()) != null
								&& client.isConnected()) {
							receiveLoop(line);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
		};
		thread.start();

	}

	/**
	 * Initialize the application.
	 */
	@Override
	public void simpleInitApp() {

		if (VRApplication.getVRHardware() != null) {
			System.out.println("Attached device: "
					+ VRApplication.getVRHardware().getName());
		}

		initObjects();

		initHeadUpDisplay();
		initSceneManager();
		initAudioManager();
		eventManager = new EventManager(rootNode);
		initInputHandler();

		initScreenManager();
		initNetworkClient();

		sceneManager.getScene("escape").createScene();

		eventManager.addListener(this);
		eventManager.registerTrigger("painting", 4);
		eventManager.registerTrigger("painting2", 4);
		eventManager.registerTrigger("door", 2);

	}

	/**
	 * Initialize the scene.
	 */
	private void initObjects() {
		// Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		observer = new Node("observer");

		Spatial sky = SkyFactory.createSky(getAssetManager(),
				"Textures/Sky/Bright/spheremap.png",
				SkyFactory.EnvMapType.EquirectMap);
		rootNode.attachChild(sky);

		// test any positioning mode here (defaults to AUTO_CAM_ALL)
		VRGuiManager
				.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
		VRGuiManager.setGuiScale(0.4f);
		VRGuiManager.setPositioningElasticity(10f);

		observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

		VRApplication.setObserver(observer);
		rootNode.attachChild(observer);

		// use magic VR mouse cusor (same usage as non-VR mouse cursor)
		getInputManager().setCursorVisible(true);

		// /** Add fog to a scene */
		// FilterPostProcessor fpp=new FilterPostProcessor(getAssetManager());
		// FogFilter fog=new FogFilter();
		// fog.setFogColor(new ColorRGBA(0.9f, 0.9f, 0.9f, 1.0f));
		// fog.setFogDistance(155);
		// fog.setFogDensity(2.0f);
		// fpp.addFilter(fog);
		// getViewPort().addProcessor(fpp);
	}

	/**
	 * 
	 * Main method to update the scene.
	 */
	@Override
	public void simpleUpdate(float tpf) {

		if (forceUpdateState) {
			setGameState(insertState);
			forceUpdateState = false;
		}

		switch (currentState) {

		case LOBBY:
			break;
		case INTRO:
			if (audioManager.getStatus("welcome") == AudioSource.Status.Playing) {
				// displayIntro(audioManager.getPlaybackTime("welcome"));
			} else {
				setGameState(GameState.PLAYING);
			}
			inputHandler.handleInput(tpf);
			break;

		case PLAYING:
			inputHandler.handleInput(tpf);
			break;
		case NONE:
			break;
		default:
			break;
		}
	}

	/**
	 * Set the current game state (not thread-safe).
	 * 
	 * @param state
	 *            the new state
	 */
	public void setGameState(GameState state) {

		// do not switch state if already in given state
		if (currentState == state) {
			return;
		}

		switchState(currentState, state);
	}

	/**
	 * Switch game states.
	 * 
	 * @param oldState
	 *            the old state
	 * @param newState
	 *            the new state
	 */
	private void switchState(GameState oldState, GameState newState) {

		// do not switch state if already in given state
		if (oldState == newState) {
			return;
		}

		currentState = newState;

		System.out.println("Switching states from " + oldState.toString()
				+ " to " + newState.toString());

		switch (oldState) {
		case LOBBY:

			audioManager.stopAudio();
			screenManager.hideScreen("lobby");
			break;
		case INTRO:
			audioManager.stopAudio();
			break;
		case PLAYING:
			// only accept input in the playing state
			inputHandler.setAcceptInput(false);
			audioManager.stopAudio();

			break;
		case NONE:
		default:
			break;
		}

		switch (newState) {
		case LOBBY:
			inputHandler.setAcceptInput(false);
			if (audioManager.getStatus("waiting") != AudioSource.Status.Playing)
				audioManager.play("waiting");
			screenManager.showScreen("lobby");
			break;
		case INTRO:
			inputHandler.setAcceptInput(true);
			if (audioManager.getStatus("welcome") != AudioSource.Status.Playing)
				audioManager.play("welcome");
			break;
		case PLAYING:
			// accept input from the user
			inputHandler.setAcceptInput(true);
			audioManager.play("letthegamebegin");
			if (audioManager.getStatus("ambient") != AudioSource.Status.Playing) {
				audioManager.play("ambient");
			}

			// System.out.println("Creating gas...");
			// EscapeScene escapeScene = ((EscapeScene) sceneManager
			// .getScene("escape"));
			// escapeScene.createGas();

			break;
		case NONE:
		default:
			break;
		}
	}

	/**
	 * Process remote input.
	 * 
	 * @param line
	 *            incoming from remote source
	 */
	public synchronized void remoteInput(final String line) {

		if (line.equals("START")) {
			screenManager.hideScreen("lobby");

			// do not call setGameState or switchState here as those run in
			// a different thread, use updateStates and insertState instead
			forceUpdateState = true;

			// to skip the intro, set insertState to PLAYING
			// insertState = GameState.PLAYING

			insertState = GameState.PLAYING;

			hud.setCenterText("");
		} else if (line.startsWith("INITVR[")) {

			int pos = line.indexOf(']');

			System.out.println("pos = " + pos);

			if (pos > 7) {
				String vrString = line.substring(7, pos);
				System.out.println("blah");
				if (vrString.equals("doneA")) {
					System.out.println("Minigame A complete");
					hud.setCenterText("Minigame A complete!");

					// sceneManager.getScene("escape").destroyScene();
					// sceneManager.getScene("escape").destroyScene();
					// sceneManager.destroyScene("escape");
				} else if (vrString.equals("doneB")) {
					System.out.println("Minigame B complete");
					hud.setCenterText("Minigame B complete!");

				}
			}

		} else {

			hud.setCenterText(line);
		}

	}

	/**
	 * Main callback method for handling remote input from socket.
	 * 
	 * @param message
	 *            the input received from the socket
	 */
	public void receiveLoop(String message) {
		System.out.println("Remote input: " + message);

		remoteInput(message);
	}

	@Override
	public void handleEvent(EventObject e) {

		if (e instanceof InteractionEvent) {
			InteractionEvent interactionEvent = (InteractionEvent) e;

			log.info(className,
					"Player interacted with " + interactionEvent.getName());

			String objectName = interactionEvent.getName();

			client.sendMessage(String.format("INTERACT[%s]", objectName));

			switch (objectName) {
			case "door":
				// play spooky muhaha sound when player interacts with door
				if (audioManager.getStatus("muhaha") != AudioSource.Status.Playing) {
					audioManager.getNode("muhaha").play();
				}
				break;
			case "painting":
				client.sendMessage("INITM[startA]");
				break;
			case "painting2":
				client.sendMessage("INITM[startB]");
				break;
			default:
				break;
			}

		}

	}

}