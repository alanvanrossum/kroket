package nl.tudelft.kroket.screen;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

/**
 * Abstract class for overlays.
 * 
 * @author Team Kroket
 *
 */
public abstract class Screen {

  String name;
  AssetManager assetManager;
  protected Node guiNode;
  String rootPath = "Textures/";
  protected Picture overlay;

  /**
   * Constructor for Screen overlay object.
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
  public Screen(String name, AssetManager assetManager, Node guiNode) {
    this.name = name;
    this.assetManager = assetManager;
    this.guiNode = guiNode;
  }

  /**
   * Load an image as Picture object.
   * 
   * @param imgPath
   *          the relative path to the image
   * @param width
   *          the width of the image
   * @param height
   *          the height of the image
   * @return Picture
   */
  public Picture loadImage(String imgPath, float width, float height) {

    String fullPath = rootPath + imgPath;

    // System.out.println("fullPath = " + fullPath);

    Picture image = new Picture(name);
    image.setImage(assetManager, fullPath, true);
    image.setWidth(width);
    image.setHeight(height);
    // move back image (so text can appear in front of
    // image)
    image.move(0, 0, -1);

    return image;
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

  public abstract void update();
}
