package nl.tudelft.kroket.screen.screens;

import nl.tudelft.kroket.escape.Settings;
import nl.tudelft.kroket.screen.Screen;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * LobbyScreen object.
 * 
 * @author Team Kroket
 *
 */
public class SpookyScreen extends Screen {

  /** Name of this screen. */
  private static final String name = "spooky";

  /** The length of time this screen is displayed. */
  private long displayTime = Settings.SPOOKY_DISPLAY_TIME;

  private long hideTime = 0;

  /**
   * Constructor for LobbyScreen overlay object.
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
  public SpookyScreen(AssetManager assetManager, Node guiNode, float width, float height) {
    super(name, assetManager, guiNode);

    overlay = loadImage("Overlay/spooky_face.png", width, height);

  }

  /**
   * Show the overlay.
   */
  @Override
  public void show() {
    hideTime = System.currentTimeMillis() + displayTime;
    guiNode.attachChild(overlay);
  }

  /**
   * Hides the screen when its time is up.
   */
  public void update() {
    if (System.currentTimeMillis() > hideTime) {
      hide();
    }
  }

}
