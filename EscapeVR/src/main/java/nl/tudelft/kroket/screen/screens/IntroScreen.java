package nl.tudelft.kroket.screen.screens;

import java.util.ArrayList;
import java.util.List;

import nl.tudelft.kroket.screen.Screen;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 * TeamKroketScreen object.
 * 
 * @author Team Kroket
 *
 */
public class IntroScreen extends Screen {

  private static final String name = "intro";

  private List<Picture> overlays = new ArrayList<Picture>();

  int current = 0;

  /**
   * Constructor for GameoverScreen overlay object.
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
  public IntroScreen(AssetManager assetManager, Node guiNode, float width, float height) {
    super(name, assetManager, guiNode);

    overlays.add(0, loadImage("Overlay/kroketgamestudios.png", width, height));
    overlays.add(1, loadImage("Overlay/intro1.png", width, height));
    overlays.add(2, loadImage("Overlay/intro2.png", width, height));
    overlays.add(3, loadImage("Overlay/intro3.png", width, height));
    overlays.add(4, loadImage("Overlay/intro4.png", width, height));
    overlays.add(5, loadImage("Overlay/intro5.png", width, height));
    overlays.add(6, loadImage("Overlay/intro6.png", width, height));
  }

  /**
   * Show the overlay.
   */
  public void show() {
    guiNode.attachChild(overlays.get(current));
  }

  /**
   * Hide the overlay.
   */

  public void hide() {
    guiNode.detachChild(overlays.get(current));
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub

  }

  public void setCurrent(int newCurrent) {

    if (current == newCurrent) {
      return;
    }

    if (newCurrent < overlays.size()) {
      hide();
      current = newCurrent;
      show();
    }

  }

}
