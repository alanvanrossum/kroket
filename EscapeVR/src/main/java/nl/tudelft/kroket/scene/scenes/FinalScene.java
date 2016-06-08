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
 * EscapeScene object.
 * 
 * @author Team Kroket
 *
 */
public class FinalScene extends Scene {

  public FinalScene(String name, AssetManager assetManager, Node rootNode, ViewPort viewPort) {
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


    /** A cone-shaped spotlight with location, direction, range */
     SpotLight spot = new SpotLight();
     spot = new SpotLight();
     spot.setSpotRange(500);
     spot.setSpotOuterAngle(20 * FastMath.DEG_TO_RAD);
     spot.setSpotInnerAngle(15 * FastMath.DEG_TO_RAD);
     spot.setDirection(new Vector3f(0, -1, 0));
     spot.setPosition(new Vector3f(0, 4, 0));
     rootNode.addLight(spot);



    /** A white, directional light source */
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-0.5f, -0.5f, -0.5f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    rootNode.addLight(sun);
    
    addIsland();
    //createFloor();
  }
  
  private void addIsland() {
    Spatial island = assetManager.loadModel("Models/Island/Island.j3o");
    island.scale((float) 0.05);
    island.move(0, -4, 0);
    rootNode.attachChild(island);
  }
  
  private void createFloor() {
    String materialPath = "Common/MatDefs/Misc/Unshaded.j3md";

    Texture floorTexture = assetManager.loadTexture("Textures/dirty_floor.png");
    floorTexture.setMagFilter(MagFilter.Nearest);
    floorTexture.setMinFilter(MinFilter.Trilinear);
    floorTexture.setAnisotropicFilter(16);

    Material floorMaterial = new Material(assetManager, materialPath);
    floorMaterial.setTexture("ColorMap", floorTexture);

    Geometry floor = new Geometry("floor", new Box(100, .1f, 100));
    floor.move(0f, -2, 0f);
    floor.setMaterial(floorMaterial);

    addObject("floor", floor);
  }

  @Override
  public Vector3f getBoundaries() {
    // TODO Auto-generated method stub
    return null;
  }

//  @Override
//  public Vector3f getBoundaries() {
//    return new Vector3f(roomWidth, roomHeight, roomDepth);
//  }

}
