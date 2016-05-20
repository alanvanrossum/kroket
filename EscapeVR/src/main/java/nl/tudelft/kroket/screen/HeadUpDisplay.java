package nl.tudelft.kroket.screen;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class HeadUpDisplay {

  /** The text displayed in the HUD. */
  BitmapText centerText;

  BitmapText topText;

  BitmapFont guiFont;

  /**
   * The HeadUpDisplay constructor.
   * 
   * @param assetManager - AssetManager
   * @param guiNode - Node
   * @param guiCanvasSize - Vector2f
   */
  public HeadUpDisplay(AssetManager assetManager, Node guiNode,
      Vector2f guiCanvasSize) {
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

    centerText = createLabel(assetManager, "Interface/Fonts/Default.fnt",
        guiCanvasSize.getX() * 0.5f - 145,
        (guiCanvasSize.getY() * 0.5f) - 145, guiCanvasSize.getX(),
        guiCanvasSize.getY());
    centerText.setSize(24);

    guiNode.attachChild(centerText);
  }

  /**
   * Create a label (text).
   * 
   * @param assetManager
   *            assetmanager instance
   * @param fontpath
   *            path to the font asset
   * @param x
   *            the x-coordinate to position the label to
   * @param y
   *            the y-coordinate to position the label to
   * @param width
   *            the width of the label
   * @param height
   *            the height of the label
   * @return the bitmap object
   */
  protected BitmapText createLabel(AssetManager assetManager,
      String fontpath, float x, float y, float width, float height) {
    BitmapFont fnt = assetManager.loadFont(fontpath);
    BitmapText txt = new BitmapText(fnt, false);
    txt.setBox(new Rectangle(0, 0, width, height));
    txt.setLocalTranslation(x, y, 0);
    return txt;
  }

  public void setCenterText(String text) {
    centerText.setText(text);
  }
}
