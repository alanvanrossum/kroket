package nl.tudelft.kroket.scene;

import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Scene class (abstract). Scenes should extend from this class.
 * 
 * @author Team Kroket
 *
 */
public abstract class Scene {

  protected HashMap<String, Spatial> objects = new HashMap<String, Spatial>();

  String name;

  protected AssetManager assetManager;

  protected Node rootNode;

  protected ViewPort viewPort;

  /**
   * Constructor for Scene object.
   * 
   * @param name
   *          the name of the scene
   * @param assetManager
   *          reference to the AssetManager
   * @param rootNode
   *          reference to the rootNode used
   * @param viewPort
   *          rerference to the viewPort used
   */
  public Scene(String name, AssetManager assetManager, Node rootNode, ViewPort viewPort) {
    this.name = name;
    this.assetManager = assetManager;
    this.rootNode = rootNode;
    this.viewPort = viewPort;
  }

  /**
   * Get the AssetManager reference used by this Scene.
   * 
   * @return the AssetManager used
   */

  public AssetManager getAssetManager() {
    return assetManager;
  }

  /**
   * Get the rootNode used by this scene.
   * 
   * @return Node object
   */
  public Node getRootNode() {
    return rootNode;
  }

  /**
   * Get an object by name.
   * 
   * @param name
   *          the name of the object
   * @return the object
   */
  public Spatial getObject(String name) {
    return objects.get(name);
  }

  /**
   * Add an object to the rootNode to display it and store it.
   * 
   * @param name
   *          the name of the object
   * @param object
   *          the object
   */
  public void addObject(String name, Spatial object) {
    rootNode.attachChild(object);
    objects.put(name, object);

  }

  public abstract void createScene();
  
  public abstract Vector3f getSceneBounds();

  /**
   * Destroy the scene by detaching all children.
   */
  public void destroyScene() {
    for (Spatial object : objects.values()) {
      rootNode.detachChild(object);
    }
  }

}
