package nl.tudelft.kroket.escape;

import java.util.Random;

import jmevr.app.VRApplication;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.state.State;
import nl.tudelft.kroket.state.states.IntroState;
import nl.tudelft.kroket.state.states.LobbyState;
import nl.tudelft.kroket.state.states.NoState;
import nl.tudelft.kroket.state.states.PlayingState;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioSource;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;

public class EscapeVR extends VRApplication{

  private SetUp setUp;
  private boolean forceUpdateState = true;
  /** Current gamestate. */
  private GameState currentState = GameState.NONE;
  /** State to force game to. */
  private GameState newState = GameState.LOBBY; // start in lobby

  private long spookyTime;
  public static Random rand = new Random();

  private State gameState = new NoState();
  private String remoteHost;

  public enum GameState {
    NONE, LOBBY, INTRO, PLAYING
  }
  
  public EscapeVR(String rh) {
    super();
    remoteHost = rh;
  }

  /**
   * Initialises the app.
   */
  @Override
  public void simpleInitApp() {
    if (VRApplication.getVRHardware() != null) {
      System.out.println("Attached device: " + VRApplication.getVRHardware().getName());

    }

    initSetUp();
    getSetUp().setRemoteHost(remoteHost);
  }

  /**
   * Main method to update the scene.
   */
  @Override
  public void simpleUpdate(float tpf) {
    if (forceUpdateState) {
      switchState(currentState, newState);
      forceUpdateState = false;
    }

    gameState.update(tpf);

    setUp.getHud().update();
    setUp.getScreenManager().update();

    // System.out.println("currentTimeMilis/1000 = " +
    // System.currentTimeMillis()/1000);
    // System.out.println("initTime/1000 = " + initTime/1000);

  }

  /**
   * Initialises escapeVR object.
   */
  public void initSetUp(){
    setUp = new SetUp();
  }

  /**
   * Gets the escapeVR object.
   * @return the escape VR
   */
  public SetUp getSetUp(){
    return setUp;
  }

  /**
   * Switch game states.
   * 
   * @param oldState
   *          the old state
   * @param newState
   *          the new state
   */
  private void switchState(GameState oldState, GameState newState) {
    // do not switch state if already in given state
    if (oldState == newState) {
      return;
    }

    currentState = newState;

    System.out.println("Switching states from " + oldState.toString() + " to "
        + newState.toString());
    gameState.stopState();
    setGameState(newState);
    gameState.startState();
  }

  /**
   * Set the current game state (not thread-safe).
   * 
   * @param state
   *          the new state
   */
  public void setGameState(GameState state) {
    if (state != currentState) {
      switch (state) {
        case NONE: gameState = new NoState();
          break;
        case LOBBY: gameState = new LobbyState();
          break;
        case INTRO: gameState = new IntroState();
           break;
        case PLAYING: gameState = new PlayingState();
           break;
        default: break;
      }
    }
  }

  /**
   * Gets the spookyTime.
   * @return the spooky time as long
   */
  public long getSpookyTime() {
    return spookyTime;
  }

  /**
   * Sets the spookyTime.
   * @param sp the new time to be set
   */
  public void setSpookyTime(long sp) {
    spookyTime = sp;
  }

  /**
   * Generates random int between min and max.
   * @param min minimum for the random number
   * @param max maximum for the random number
   * @return a random number
   */
  public static int randInt(int min, int max) {
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }

}
