package nl.tudelft.kroket.scene.scenes;

import nl.tudelft.kroket.scene.Scene;

import com.jme3.asset.AssetManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.FogFilter;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.PointLightShadowRenderer;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.texture.Texture.WrapMode;

/**
 * EscapeScene object.
 * 
 * @author Team Kroket
 *
 */
public class EscapeScene extends Scene {

  int shadowmapSize = 2048;

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

    /** A white ambient light source. */
    AmbientLight ambient = new AmbientLight();
    ambient.setColor(ColorRGBA.White);
    rootNode.addLight(ambient);

    PointLight lampLight = new PointLight();
    lampLight.setColor(ColorRGBA.White);
    lampLight.setRadius(50f);
    lampLight.setPosition(new Vector3f(0f, 6f, 0f));
    rootNode.addLight(lampLight);

    PointLightShadowRenderer pointLightShadowRenderer = new PointLightShadowRenderer(assetManager,
        shadowmapSize);
    pointLightShadowRenderer.setLight(lampLight);
    viewPort.addProcessor(pointLightShadowRenderer);

    createWalls("Textures/brick_wall.jpg");

    createFloor("Textures/floor.jpg");

    createCeiling("Textures/dirty_floor.png");

    createDoor("Textures/door.jpg");

    // createGas();

    createPainting("Textures/Painting/painting.jpg");
    createPainting2("Textures/Painting/painting2.jpg");

    // createLight();
    // createCube();

    // addLamp();

    // createLight();

    /** A cone-shaped spotlight with location, direction, range */
    SpotLight spot = new SpotLight();
    spot = new SpotLight();
    spot.setSpotRange(500);
    spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
    spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
    spot.setDirection(new Vector3f(0, -1, 0));
    spot.setPosition(new Vector3f(0, 4, 0));
    rootNode.addLight(spot);

    /** A white, directional light source. */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

    // addTurret();
    addDesk();

    addKnight1();
    addKnight2();

    addSafe();

    // addButtons();

    createLight();

    addGrass();
  }

  private void addGrass() {

    
    

    Texture floorTexture = assetManager.loadTexture("Textures/grass.jpg");
    floorTexture.setWrap(WrapMode.Repeat);

    floorTexture.setMagFilter(MagFilter.Nearest);
    floorTexture.setMinFilter(MinFilter.Trilinear);
    floorTexture.setAnisotropicFilter(16);

    Material floorMaterial = new Material(assetManager, materialPath);
    floorMaterial.setTexture("ColorMap", floorTexture);

    Geometry grass = new Geometry("grass", new Box(200, .5f, 200));

    grass.move(0f, translationY - roomHeight - 0.5f, 0f);
    grass.setMaterial(floorMaterial);
    addObject("grass", grass);

  }

  /**
   * Add a closed safe to the scene.
   */
  private void addSafe() {
    Spatial safe = assetManager.loadModel("Models/safe/safe.j3o");
    safe.scale(0.03f);
    // 6 opzij, 8 naar achter :p
    safe.move(-6.8f, -3, -10.1f);
    safe.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(safe);
  }

  /**
   * Adds buttons for minigame c.
   */
  public void addButtons() {
    Spatial buttons = assetManager.loadModel("Models/buttons/fourbuttons2.j3o");
    buttons.scale(0.15f);
    buttons.move(5f, 2f, (float) (roomDepth - 0.1));
    buttons.rotate(0f, -0.5f * FastMath.PI, 0.5f * FastMath.PI);
    buttons.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(buttons);
  }

  /**
   * Adds the open safe after minigame a.
   */
  public void addOpenSafe() {
    System.out.println("hoi");
    rootNode.getChild("safe-objnode").removeFromParent();
    Spatial safe = assetManager.loadModel("Models/safeopen/safeopen.j3o");
    safe.scale(0.03f);

    safe.move(-6.8f, -3, -10.1f);
    safe.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(safe);
  }

  /**
   * Removes the object from the scene.
   * 
   * @param object
   *          the name of the object to be deleted.
   */
  public void remove(String object) {
    rootNode.getChild(object);
  }

  /**
   * Add Knight 1 to the scene.
   */
  private void addKnight1() {
    Spatial knight1 = assetManager.loadModel("Models/knight1/knight1.j3o");
    knight1.scale(0.15f);
    knight1.move(-6.2f, 2f, 2f);
    knight1.rotate(-0.5f * FastMath.PI, 0.5f * FastMath.PI, 0f);
    knight1.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(knight1);
  }

  /**
   * Add Knight 2 to the scene.
   */
  private void addKnight2() {
    Spatial knight2 = assetManager.loadModel("Models/knight2/knight2.j3o");
    knight2.scale(0.15f);
    knight2.move(-9, 2f, (float) (roomDepth - 3));
    knight2.rotate(-0.5f * FastMath.PI, FastMath.PI, 0f);
    knight2.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(knight2);
  }

  /**
   * Add the desk to the scene.
   */
  private void addDesk() {
    Spatial desk = assetManager.loadModel("Models/DeskLaptop/DeskLaptop.j3o");
    desk.scale(1.5f);
    desk.move(6.0f, -translationY + 0.2f, -8.9f);
    desk.setShadowMode(ShadowMode.CastAndReceive);
    rootNode.attachChild(desk);
  }

  // private void addLamp() {
  // Spatial lamp = assetManager.loadModel("Models/Petroleum_Lamp/Petroleum_Lamp.j3o");
  // // lamp.move(-2, -3, -2); // put the lamp on the floor
  // lamp.setShadowMode(ShadowMode.Cast);
  // rootNode.attachChild(lamp);
  // }

  // private void addTurret() {
  // Spatial turret = assetManager.loadModel("Models/portalturret/portalturret.j3o");
  // turret.move(-2, -3.5f, 5);
  // turret.scale(0.06f);
  // turret.setShadowMode(ShadowMode.Cast);
  // addObject("turret", turret);
  // }

  private void createLight() {
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(0, 5, 0)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);

  }

  // private void createCube() {
  // Spatial cube = assetManager.loadModel("Models/tudcube/tudcube.j3o");
  // rootNode.attachChild(cube);
  // }

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

    // Material wallMaterial = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
    // wallMaterial.setBoolean("UseMaterialColors", true);
    // wallMaterial.setBoolean("UseVertexColor", true);
    // wallMaterial.setBoolean("VertexLighting", false);
    // wallMaterial.setColor("Diffuse", ColorRGBA.White); // minimum material color
    // wallMaterial.setColor("Specular",ColorRGBA.White); // for shininess
    // wallMaterial.setFloat("Shininess", 0); // [1,128] for shininess
    // // wallMaterial.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);
    // wallMaterial.setTexture("DiffuseMap", wallTexture);
    // //wallMaterial.setTexture("ColorMap", wallTexture);

    // wall to the right of player spawn
    Geometry wall1 = new Geometry("wall-east", new Box(.1f, roomHeight, roomDepth));
    wall1.setMaterial(wallMaterial);
    wall1.move(-roomWidth, translationY, 0);
    wall1.setShadowMode(ShadowMode.Receive);
    addObject("wall-east", wall1);

    // wall to the left of player spawn
    Geometry wall2 = new Geometry("wall-west", new Box(.1f, roomHeight, roomDepth));
    wall2.setMaterial(wallMaterial);
    wall2.rotate(0, -FastMath.PI, 0);
    wall2.move(roomWidth, translationY, 0);
    wall2.setShadowMode(ShadowMode.Receive);
    addObject("wall-west", wall2);

    // wall in front of player
    Geometry wall3 = new Geometry("wall-north", new Box(roomWidth, roomHeight, .1f));
    wall3.setMaterial(wallMaterial);
    wall3.move(0, translationY, roomDepth);
    wall3.setShadowMode(ShadowMode.Receive);
    addObject("wall-north", wall3);

    // wall behind player spawn
    Geometry wall4 = new Geometry("wall-south", new Box(roomWidth, roomHeight, .1f));
    wall4.setMaterial(wallMaterial);
    wall4.move(0, translationY, -roomDepth);
    wall4.setShadowMode(ShadowMode.Receive);
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

    floor.setShadowMode(ShadowMode.Receive);
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

    ceiling.setShadowMode(ShadowMode.Receive);

    addObject("ceiling", ceiling);
  }

  /**
   * Create and draw a floor.
   * 
   * @param texturePath
   *          the relative path to the texture
   */
  private void createDoor(String texturePath) {
    //
    // float doorWidth = 1.8f;
    // float doorHeight = 3.5f;
    //
    // Texture doorTexture = assetManager.loadTexture(texturePath);
    // doorTexture.setMagFilter(MagFilter.Nearest);
    // doorTexture.setMinFilter(MinFilter.Trilinear);
    // doorTexture.setAnisotropicFilter(16);
    //
    // Material doorMaterial = new Material(assetManager, materialPath);
    // doorMaterial.setTexture("ColorMap", doorTexture);
    //
    // Geometry door = new Geometry("door", new Box(doorWidth, doorHeight, .2f));
    Spatial door = assetManager.loadModel("Models/door/door.j3o");
    door.scale(0.022f);
    door.move(0, translationY - 6f, (float) (roomDepth - 0.1));
    // door.setMaterial(doorMaterial);
    door.setShadowMode(ShadowMode.CastAndReceive);

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
    float paintingWidth = 5f;
    float paintingHeight = 3.0f;

    Texture doorTexture = assetManager.loadTexture(texturePath);
    doorTexture.setMagFilter(MagFilter.Nearest);
    doorTexture.setMinFilter(MinFilter.Trilinear);
    doorTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", doorTexture);

    Geometry painting = new Geometry("painting2", new Box(paintingWidth, paintingHeight, 0.1f));

    painting.move(0.5f, translationY, -roomDepth + 0.1f);

    painting.setMaterial(paintingMaterial);

    addObject("painting2", painting);
  }

  /**
   * Add a painting for the code 13.
   * 
   * @param texturePath
   *          the image of the painting.
   * @param name
   *          the name of the painting.
   */
  public void addCode13(String texturePath, String name) {
    float paintingWidth = 1.5f;
    float paintingHeight = 1.5f;

    Texture paintingTexture = assetManager.loadTexture(texturePath);
    paintingTexture.setMagFilter(MagFilter.Nearest);
    paintingTexture.setMinFilter(MinFilter.Trilinear);
    paintingTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", paintingTexture);

    Geometry painting = new Geometry(name, new Box(.01f, paintingHeight, paintingWidth));

    painting.move(roomWidth - 0.1f, translationY, 7);

    painting.setMaterial(paintingMaterial);

    addObject(name, painting);
  }

  /**
   * Add a painting for the code 37.
   * 
   * @param texturePath
   *          the image of the painting.
   * @param name
   *          the name of the painting.
   */
  public void addCode37(String texturePath, String name) {
    float paintingWidth = 1.5f;
    float paintingHeight = 1.5f;

    Texture paintingTexture = assetManager.loadTexture(texturePath);
    paintingTexture.setMagFilter(MagFilter.Nearest);
    paintingTexture.setMinFilter(MinFilter.Trilinear);
    paintingTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", paintingTexture);

    Geometry painting = new Geometry(name, new Box(.01f, paintingHeight, paintingWidth));

    painting.move(-roomWidth + 0.1f, translationY - 2, 2);

    painting.setMaterial(paintingMaterial);

    addObject(name, painting);
  }

  /**
   * Add a painting for the code 21.
   * 
   * @param texturePath
   *          the image of the painting.
   * @param name
   *          the name of the painting.
   */
  public void addCode21(String texturePath, String name) {
    float paintingWidth = 1.5f;
    float paintingHeight = 1.5f;

    Texture paintingTexture = assetManager.loadTexture(texturePath);
    paintingTexture.setMagFilter(MagFilter.Nearest);
    paintingTexture.setMinFilter(MinFilter.Trilinear);
    paintingTexture.setAnisotropicFilter(16);

    Material paintingMaterial = new Material(assetManager, materialPath);
    paintingMaterial.setTexture("ColorMap", paintingTexture);

    Geometry painting = new Geometry(name, new Box(.01f, paintingHeight, paintingWidth));

    painting.rotate(0, 0, (float) (0.5 * Math.PI));
    painting.move(-2, (float) (-translationY + 0.1), -5);

    painting.setMaterial(paintingMaterial);

    addObject(name, painting);
  }

  // private void addCode(String name, String texturePath, float depth, float width, float height,
  // float rotateX, float rotateY, float rotateZ) {
  // float paintingWidth = 1.5f;
  // float paintingHeight = 1.5f;
  //
  // Texture paintingTexture = assetManager.loadTexture(texturePath);
  // paintingTexture.setMagFilter(MagFilter.Nearest);
  // paintingTexture.setMinFilter(MinFilter.Trilinear);
  // paintingTexture.setAnisotropicFilter(16);
  //
  // Material paintingMaterial = new Material(assetManager, materialPath);
  // paintingMaterial.setTexture("ColorMap", paintingTexture);
  //
  // Geometry painting = new Geometry(name, new Box(.01f, paintingHeight, paintingWidth));
  //
  // painting.rotate(rotateX, rotateY, rotateZ);
  // painting.move(depth, width, height);
  //
  // painting.setMaterial(paintingMaterial);
  //
  // addObject(name, painting);
  // }

  /**
   * Add gas to the scene.
   */
  // public void createGas() {
  // FogFilter fog = new FogFilter();
  // fog.setFogColor(gasColor);
  // fog.setFogDistance(1000);
  // fog.setFogDensity(2.0f);
  // FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
  // fpp.addFilter(fog);
  // viewPort.addProcessor(fpp);
  //
  // }

  /**
   * Equals method for EscapeScene.
   * 
   * @return true if all attributes are equal
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof EscapeScene) {
      EscapeScene that = (EscapeScene) obj;
      return (this.translationY == that.translationY && this.roomDepth == that.roomDepth
          && this.roomWidth == that.roomWidth && this.roomHeight == that.roomHeight
          && this.gasColor.equals(that.gasColor) && this.materialPath.equals(that.materialPath));
    }
    return false;
  }

  @Override
  public Vector3f getBoundaries() {
    return new Vector3f(roomWidth, roomHeight, roomDepth);
  }

}
