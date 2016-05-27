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
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;

/**
 * EscapeScene object.
 * 
 * @author Team Kroket
 *
 */
public class EscapeScene extends Scene {

  float translationY = 3f;

  float roomDepth = 12f;
  float roomWidth = 9f;
  float roomHeight = 6f;

  private ColorRGBA gasColor = new ColorRGBA(0.3f, 0.9f, 0.2f, 1.0f);

  private String materialPath = "Common/MatDefs/Misc/Unshaded.j3md";

  public EscapeScene(String name, AssetManager assetManager, Node rootNode, ViewPort viewPort) {
    super(name, assetManager, rootNode, viewPort);

  }

  /**
   * The create scene method.
   */
  public void createScene() {

    // createWalls("Textures/brick_wall.jpg");

    // createFloor("Textures/dirty_floor.png");

    // createCeiling("Textures/dirty_floor.png");

    // createDoor("Textures/door.jpg");

    // createGas();

    // createPainting("Textures/Painting/painting.jpg");
    // createPainting2("Textures/Painting/painting2.jpg");

    // createDesk("Models/desk.obj", "Models/desktex/DeskWood_Diffuse.tga");

     createLight(-2, 5, -2); 
    createLight(2.8f, 2.8f, -2.8f);
    createLight(2.8f, -2.8f, -2.8f);

    // createLamp("", "");
    // createIrene();

    createCube();
    
    createLamp();
    createWalls("Textures/test_wall.png");
    //createFloor("Textures/wall.png");
    
    
    createSurfaces();
    
//    createLight(5, -2, 5);
    

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
    cube.move(-2, -2, -2);
    rootNode.attachChild(cube);
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
   * Create four walls using a texture.
   * 
   * @param texturePath
   *          the relative path to the texture
   */
  private void createWalls(String texturePath) {

    Texture wallTexture = assetManager.loadTexture(texturePath);
    wallTexture.setMagFilter(MagFilter.Nearest);
    wallTexture.setMinFilter(MinFilter.Trilinear);
    wallTexture.setAnisotropicFilter(16);

    Material wallMaterial = new Material(assetManager, materialPath);
    wallMaterial.setTexture("ColorMap", wallTexture);

    // wall to the right of player spawn
    Geometry wall1 = new Geometry("wall-east", new Box(.1f, roomHeight, roomDepth));
    wall1.setMaterial(wallMaterial);
    wall1.move(-roomWidth, translationY, 0);
    addObject("wall-east", wall1);

    // wall to the left of player spawn
    Geometry wall2 = new Geometry("wall-west", new Box(.1f, roomHeight, roomDepth));
    wall2.setMaterial(wallMaterial);
    wall2.move(roomWidth, translationY, 0);
    addObject("wall-west", wall2);

    // wall in front of player
    Geometry wall3 = new Geometry("wall-north", new Box(roomWidth, roomHeight, .1f));
    wall3.setMaterial(wallMaterial);
    wall3.move(0, translationY, roomDepth);
    addObject("wall-north", wall3);

    // wall behind player spawn
    Geometry wall4 = new Geometry("wall-south", new Box(roomWidth, roomHeight, .1f));
    wall4.setMaterial(wallMaterial);
    wall4.move(0, translationY, -roomDepth);
    addObject("wall-south", wall4);
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

    Geometry floor = new Geometry("floor", new Box(roomWidth, .1f, roomDepth));
    floor.move(0f, translationY - roomHeight, 0f);
    floor.setMaterial(floorMaterial);

    addObject("floor", floor);
  }

  /**
   * Create and draw a ceiling.
   * 
   * @param texturePath
   *          the relative path to the texture
   */

  private void createCeiling(String texturePath) {
    Texture ceilingTexture = assetManager.loadTexture(texturePath);
    ceilingTexture.setMagFilter(MagFilter.Nearest);
    ceilingTexture.setMinFilter(MinFilter.Trilinear);
    ceilingTexture.setAnisotropicFilter(16);

    Material ceilingMaterial = new Material(assetManager, materialPath);
    ceilingMaterial.setTexture("ColorMap", ceilingTexture);

    Geometry ceiling = new Geometry("ceiling", new Box(roomWidth, .1f, roomDepth));

    ceiling.move(0f, translationY + roomHeight, 0f);
    ceiling.setMaterial(ceilingMaterial);

    addObject("ceiling", ceiling);
  }

  /**
   * Create and draw a floor.
   * 
   * @param texturePath
   *          the relative path to the texture
   */
  private void createDoor(String texturePath) {

    float doorWidth = 1.8f;
    float doorHeight = 3.5f;

    Texture doorTexture = assetManager.loadTexture(texturePath);
    doorTexture.setMagFilter(MagFilter.Nearest);
    doorTexture.setMinFilter(MinFilter.Trilinear);
    doorTexture.setAnisotropicFilter(16);

    Material doorMaterial = new Material(assetManager, materialPath);
    doorMaterial.setTexture("ColorMap", doorTexture);

    Geometry door = new Geometry("door", new Box(doorWidth, doorHeight, .2f));

    door.move(0, translationY - 2.5f, roomDepth);
    door.setMaterial(doorMaterial);

    addObject("door", door);
  }

  private void createPainting(String texturePath) {
    float paintingWidth = 2.5f;
    float paintingHeight = 3.0f;

    Texture doorTexture = assetManager.loadTexture(texturePath);
    doorTexture.setMagFilter(MagFilter.Nearest);
    doorTexture.setMinFilter(MinFilter.Trilinear);
    doorTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", doorTexture);

    Geometry painting = new Geometry("painting", new Box(.01f, paintingHeight, paintingWidth));

    painting.move(roomWidth - 0.1f, translationY, 0);

    painting.setMaterial(paintingMaterial);

    addObject("painting", painting);
  }

  private void createPainting2(String texturePath) {
    float paintingWidth = 2.5f;
    float paintingHeight = 3.0f;

    Texture doorTexture = assetManager.loadTexture(texturePath);
    doorTexture.setMagFilter(MagFilter.Nearest);
    doorTexture.setMinFilter(MinFilter.Trilinear);
    doorTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", doorTexture);

    Geometry painting = new Geometry("painting2", new Box(paintingWidth, paintingHeight, 0.1f));

    painting.move(-2f, translationY + 2, -roomDepth + 0.1f);

    painting.setMaterial(paintingMaterial);

    addObject("painting2", painting);
  }

  /**
   * Add gas to the scene.
   */
  public void createGas() {

    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
    FogFilter fog = new FogFilter();
    fog.setFogColor(gasColor);
    fog.setFogDistance(1000);
    fog.setFogDensity(2.0f);
    fpp.addFilter(fog);
    viewPort.addProcessor(fpp);

  }

}