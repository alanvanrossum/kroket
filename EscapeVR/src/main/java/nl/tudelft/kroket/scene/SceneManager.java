package nl.tudelft.kroket.scene;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.scenes.EscapeScene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * Manager for all scenes. Handles loading and destroying of scenes.
 * 
 * @author Team Kroket
 */
public class SceneManager {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** HashMap with all scenes, by name. */
  HashMap<String, Scene> scenes = new HashMap<String, Scene>();

  /** Reference to the AssetManager. */
  private AssetManager assetManager;

  /** Reference to the RootNode object. */
  private Node rootNode;

  /** Reference to the ViewPort object. */
  private ViewPort viewPort;

  // private SceneManager instance = new SceneManager(getAssetManager(), getRootNode(),
  // getViewPort());

  /**
   * Constructor for SceneManager object.
   * 
   * @param assetManager
   *          reference to AssetManager
   * @param rootNode
   *          reference to the rootNode used in the scene
   * @param viewPort
   *          reference to the ViewPort
   */
  public SceneManager(AssetManager assetManager, Node rootNode, ViewPort viewPort) {

    log.info(className, "Initializing...");

    this.assetManager = assetManager;
    this.rootNode = rootNode;

    this.viewPort = viewPort;
  }

  /**
   * Dynamically load a scene.
   * 
   * @param name
   *          the name of the scene
   * @param sceneClass
   *          the class of the scene
   */
  public void loadScene(String name, Class<? extends Scene> sceneClass) {

    Scene newScene = null;
    try {

      Constructor<? extends Scene> ctor = sceneClass.getDeclaredConstructor(String.class,
          AssetManager.class, Node.class, ViewPort.class);
      newScene = ctor.newInstance(name, assetManager, rootNode, viewPort);
    } catch (InstantiationException e) {

      e.printStackTrace();
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();

    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    // after initiating the scene, store it so we can reference it later
    scenes.put(name, newScene);
  }

  /**
   * Destroy a scene by name.
   * 
   * @param name
   *          the name of the scene.
   */

  public void destroyScene(String name) {
    getScene(name).destroyScene();
  }

  /**
   * Get a screen by name.
   * 
   * @param name
   *          the name of the scene
   * @return the scene object
   */
  public Scene getScene(String name) {

    if (!scenes.containsKey(name)) {
      log.error(className, String.format("Scene %s does not exist.", name));
      return null;
    }

    return scenes.get(name);
  }

  /**
   * Get all the registered scenes.
   * 
   * @return a hashmap with all scenes
   */
  public HashMap<String, Scene> getScenes() {
    return scenes;
  }

  /**
   * Adds new objects to the escape scene.
   * @param state specifies which object.
   */
  public void extendEscapeScene(String state) {
    switch (state) {
      case "C":
        ((EscapeScene) scenes.get("escape")).addButtons();
        break;
      case "A":
        ((EscapeScene) scenes.get("escape")).addOpenSafe();
        break;
      case "D":
        ((EscapeScene) scenes.get("escape")).addCode13("Textures/Painting/questionmark.jpg", "D1");
        break;
      default:
        break;
    }

  }

}
