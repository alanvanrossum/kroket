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
public class SpookyScreen extends Screen {

  private static final String name = "spooky";
  
  private long displayTime = 1000;

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

    overlay = loadImage("overlay/spooky_face.png", width, height);
  }

  /**
   * Show the overlay.
   */
  @Override
  public void show() {

    hideTime = System.currentTimeMillis() + displayTime;

    guiNode.attachChild(overlay);
  }
  
  @Override
  public void update() {
    if (System.currentTimeMillis() > hideTime) {
      hide();
    }
  }

}
