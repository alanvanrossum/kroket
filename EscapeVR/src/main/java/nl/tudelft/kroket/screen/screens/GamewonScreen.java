package nl.tudelft.kroket.screen.screens;

import nl.tudelft.kroket.screen.Screen;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * LobbyScreen object.
 * 
 * @author Team Kroket
 *
 */
public class GamewonScreen extends Screen {

  /** Name of this screen. */
  private static final String name = "gamewon";

  /** How long this screen will be displayed. */
  private long displayTime = 5 * 1000;

  private long hideTime = 0;

  /**
   * Constructor for GamewonScreen overlay object.
   * 
   * @param assetManager
   *          reference to the AssetManager object
   * @param guiNode
   *          reference to the guiNode
   * @param width
   *          the width of the overlay
   * @param height
   *          the height of the overlay
   */
  public GamewonScreen(AssetManager assetManager, Node guiNode, float width, float height) {
    super(name, assetManager, guiNode);

    overlay = loadImage("Overlay/gamewon.png", width, height);
  }

  /**
   * Show the overlay.
   */
  public void show() {
    hideTime = System.currentTimeMillis() + displayTime;
    guiNode.attachChild(overlay);
  }

  /**
   * Hide the overlay.
   */
  public void hide() {
    guiNode.detachChild(overlay);
  }

  /**
   * Update method for this screen.
   * Hide the screen if its time is up.
   */
  @Override
  public void update() {
    if (System.currentTimeMillis() > hideTime) {
      hide();
    }
  }

}
