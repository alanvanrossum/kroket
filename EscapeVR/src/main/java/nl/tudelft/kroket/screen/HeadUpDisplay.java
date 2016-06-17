package nl.tudelft.kroket.screen;

import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.math.Vector2f;
import com.jme3.scene.Node;

/**
 * HeadUpDisplay class, used for displaying text to the user.
 * 
 * @author Team Kroket
 *
 */
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

  /** Clear text after 8 seconds. */
  private final long delayTextClear = 8 * 1000;
  
  /**
   * The HeadUpDisplay constructor.
   * 
   * @param assetManager
   *          - AssetManager
   * @param guiNode
   *          - Node
   * @param guiCanvasSize
   *          - Vector2f
   */
  public HeadUpDisplay(AssetManager assetManager, Node guiNode, Vector2f guiCanvasSize) {
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    
    
    
    timerTextLabel = createLabel(assetManager, "Interface/Fonts/Default.fnt",
        guiCanvasSize.getX() * 0.5f - 145, (guiCanvasSize.getY() * 0.5f) + 295,
        guiCanvasSize.getX(), guiCanvasSize.getY());
    timerTextLabel.setSize(24);

    guiNode.attachChild(timerTextLabel);

    centerTextLabel = createLabel(assetManager, "Interface/Fonts/Default.fnt",
        guiCanvasSize.getX() * 0.5f - 180, (guiCanvasSize.getY() * 0.5f) - 180,
        guiCanvasSize.getX(), guiCanvasSize.getY());
    centerTextLabel.setSize(28);

    guiNode.attachChild(centerTextLabel);


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

  /**
   * Set the text for the timer label.
   * 
   * @param text the text to be set
   */
  public void setTimerText(String text) {
    timerTextLabel.setText(text);
  }

  /**
   * Set the centered text in the HUD.
   * 
   * @param text
   *          the text to display
   */
  public void setCenterText(String text) {
    centerTextTime = System.currentTimeMillis() + delayTextClear;
    centerTextLabel.setText(text);
  }

  /**
   * Set the centered text in the hud and display it for a certain amount of seconds.
   * 
   * @param text
   *          the text to display
   * @param time
   *          the time to display the text
   */
  public void setCenterText(String text, int time) {
    centerTextTime = System.currentTimeMillis() + (time * 1000);
    centerTextLabel.setText(text);
  }

  /**
   * Update method, used to update the HUD.
   */
  public void update() {
    if (System.currentTimeMillis() > centerTextTime) {
      clearCenter();
    }
  }
  
  /**
   * Clear the text on the timer label.
   */
  public void clearTimer() {
    if (timerTextLabel.getText().length() > 0) {
      timerTextLabel.setText("");
      log.debug(className, "Timer text cleared.");
    }
  }

  /**
   * Clear the center text label.
   */
  public void clearCenter() {
    if (centerTextLabel.getText().length() > 0) {
      centerTextLabel.setText("");
      log.debug(className, "Center text cleared.");
    }
  }

}
