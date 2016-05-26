package nl.tudelft.kroket.screen;

import nl.tudelft.kroket.escape.Launcher;
import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

public class HeadUpDisplay {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** The text displayed in the HUD. */
  BitmapText centerTextLabel;
  long centerTextTime = 0;

  BitmapText timerTextLabel;

  BitmapFont guiFont;

  // clear text after 20 seconds
  long delayTextClear = 8 * 1000;

  /**
   * The HeadUpDisplay constructor.
   * @param guiCanvasSize
   *          - Vector2f
   */
  public HeadUpDisplay( Vector2f guiCanvasSize) {
    AssetManager as = Launcher.mainApplication.getAssetManager();
    guiFont = as.loadFont("Interface/Fonts/Default.fnt");

    centerTextLabel = createLabel(as, "Interface/Fonts/Default.fnt",
        guiCanvasSize.getX() * 0.5f - 145, (guiCanvasSize.getY() * 0.5f) - 145,
        guiCanvasSize.getX(), guiCanvasSize.getY());
    centerTextLabel.setSize(24);

    Launcher.mainApplication.getGuiNode().attachChild(centerTextLabel);

    timerTextLabel = createLabel(as, "Interface/Fonts/Default.fnt",
        guiCanvasSize.getX() * 0.5f - 145, (guiCanvasSize.getY() * 0.5f) + 245,
        guiCanvasSize.getX(), guiCanvasSize.getY());
    timerTextLabel.setSize(24);

    Launcher.mainApplication.getGuiNode().attachChild(timerTextLabel);

  }

  /**
   * Create a label (text).
   * 
   * @param assetManager
   *          assetmanager instance
   * @param fontpath
   *          path to the font asset
   * @param xPosition
   *          the x-coordinate to position the label to
   * @param yPosition
   *          the y-coordinate to position the label to
   * @param width
   *          the width of the label
   * @param height
   *          the height of the label
   * @return the bitmap object
   */
  protected BitmapText createLabel(AssetManager assetManager, String fontpath, float xPosition,
      float yPosition, float width, float height) {
    BitmapFont fnt = assetManager.loadFont(fontpath);
    BitmapText txt = new BitmapText(fnt, false);
    txt.setBox(new Rectangle(0, 0, width, height));
    txt.setLocalTranslation(xPosition, yPosition, 0);
    return txt;
  }

  public void setTimerText(String text) {

  }

  public void setCenterText(String text) {
    centerTextTime = System.currentTimeMillis() + delayTextClear;
    centerTextLabel.setText(text);
  }

  public void setCenterText(String text, int time) {
    centerTextTime = System.currentTimeMillis() + (time * 1000);
    centerTextLabel.setText(text);
  }

  public void update() {

    if (System.currentTimeMillis() > centerTextTime) {
      clearCenter();
    }
  }

  public void clearCenter() {

    if (centerTextLabel.getText().length() > 0) {
      centerTextLabel.setText("");
      log.debug(className, "Center text cleared.");
    }
  }

}
