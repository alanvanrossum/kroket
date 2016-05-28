package nl.tudelft.kroket.scene.scenes;

import nl.tudelft.kroket.scene.Scene;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;

/**
 * Model testing scene object.
 * 
 * @author Team Kroket
 *
 */
public class ModelTestScene extends Scene {

  float translationY = 3f;

  private String materialPath = "Common/MatDefs/Misc/Unshaded.j3md";

  public ModelTestScene(String name, AssetManager assetManager, Node rootNode, ViewPort viewPort) {
    super(name, assetManager, rootNode, viewPort);

  }

  /**
   * The create scene method.
   */
  public void createScene() {

    createLight(-2, 5, -2);
    createLight(2.8f, 2.8f, -2.8f);
    createLight(2.8f, -2.8f, -2.8f);

    createCube();

    createLamp();
    createSurfaces();
    // addKroket();
    addColors();

    createLight(5, -2, 5);

    /** A cone-shaped spotlight with location, direction, range */
    SpotLight spot = new SpotLight();
    spot = new SpotLight();
    spot.setSpotRange(500);
    spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
    spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
    spot.setDirection(new Vector3f(0, -1, 0));
    spot.setPosition(new Vector3f(0, 4, 0));
    rootNode.addLight(spot);

    /** A white ambient light source. */
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient);

    /** A white, directional light source */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    createIrene();
    
    createFloor("Textures/logo.bmp");
    
    createTurret();
  }

  private void addColors() {
    Spatial surface = assetManager.loadModel("Models/colors/colors.j3o");
    rootNode.attachChild(surface);
  }

  private void addKroket() {

    Spatial surface = assetManager.loadModel("Models/kroket/kroket.j3o");
    surface.move(3, -2, -3);
    rootNode.attachChild(surface);
  }

  private void createSurfaces() {
    Spatial surface = assetManager.loadModel("Models/flat/flat.j3o");
    surface.move(0, 4, 0);
    rootNode.attachChild(surface);
  }

  private void createLamp() {
    Spatial lamp = assetManager.loadModel("Models/Petroleum_Lamp/Petroleum_Lamp.j3o");
    lamp.move(5, -0.33f, 5);
    rootNode.attachChild(lamp);
  }

  private void createCube() {
    Spatial cube = assetManager.loadModel("Models/tudcube/tudcube.j3o");
    cube.move(-2, 0, -2);
    rootNode.attachChild(cube);
  }
  
  private void createTurret() {
    Spatial turret = assetManager.loadModel("Models/portalturret/portalturret.j3o");
    turret.move(-2, -3, 5);
    turret.scale(0.06f);
    rootNode.attachChild(turret);
  }

  /**
   * Places a light in the scene.
   * 
   * @param x
   *          the x-co of the light
   * @param y
   *          the y-co of the light
   * @param z
   *          the z-co of the light
   */
  private void createLight(float x, float y, float z) {
    DirectionalLight dl = new DirectionalLight();
    dl.setColor(ColorRGBA.White);
    dl.setDirection(new Vector3f(x, y, z).normalizeLocal());
    rootNode.addLight(dl);
  }

  public void createIrene() {

    /** A white ambient light source. */
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient);

    Spatial lamp = assetManager.loadModel("Models/Petroleum_Lamp/Petroleum_Lamp.j3o");

    rootNode.attachChild(lamp);

    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(0, 5, 0)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    DirectionalLight sun2 = new DirectionalLight();
    sun2.setDirection((new Vector3f(0, -5, 0)));
    sun2.setColor(ColorRGBA.White);
    rootNode.addLight(sun2);
  }
  
  /**
   * Create a floor object.
   * 
   * @param texturePath
   *          the relative path to the texture
   */

  private void createFloor(String texturePath) {

    Texture floorTexture = assetManager.loadTexture(texturePath);
    floorTexture.setMagFilter(MagFilter.Nearest);
    floorTexture.setMinFilter(MinFilter.Trilinear);
    floorTexture.setAnisotropicFilter(16);

    Material floorMaterial = new Material(assetManager, materialPath);
    floorMaterial.setTexture("ColorMap", floorTexture);

    Geometry floor = new Geometry("floor", new Box(9, .1f, 9));
    floor.move(0f, -4, 0f);
    floor.setMaterial(floorMaterial);

    addObject("floor", floor);
  }

}