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
public class LobbyScreen extends Screen {

  /** Name of this screen. */
  private static final String name = "lobby";

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
  public LobbyScreen(AssetManager assetManager, Node guiNode, float width, float height) {
    super(name, assetManager, guiNode);

    overlay = loadImage("Overlay/waiting.png", width, height);
  }

  /**
   * Show the overlay.
   */
  public void show() {
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
   */
  @Override
  public void update() {
    // TODO Auto-generated method stub
  }
  
}
