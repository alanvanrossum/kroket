package nl.tudelft.kroket.scene.scenes;

import nl.tudelft.kroket.scene.Scene;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;

public class EscapeScene extends Scene {

	private String materialPath = "Common/MatDefs/Misc/Unshaded.j3md";

	public EscapeScene(String name, AssetManager assetManager, Node rootNode) {
		super(name, assetManager, rootNode);
	}

	public void createScene() {

		createWalls("Textures/brick_wall.jpg");

		createFloor("Textures/floor.png");
		createCeiling("Textures/floor.png");
	}

	private void createWalls(String texturePath) {

		Texture wallTexture = assetManager.loadTexture(texturePath);
		wallTexture.setMagFilter(MagFilter.Nearest);
		wallTexture.setMinFilter(MinFilter.Trilinear);
		wallTexture.setAnisotropicFilter(16);

		Material wallMaterial = new Material(assetManager, materialPath);
		wallMaterial.setTexture("ColorMap", wallTexture);

		Geometry wall1 = new Geometry("wall1", new Box(.5f, 6f, 6f));
		wall1.setMaterial(wallMaterial);
		wall1.move(-12, 5, 0);
		rootNode.attachChild(wall1);
		objects.add(wall1);

		Geometry wall2 = new Geometry("wall2", new Box(.5f, 6f, 6f));
		wall2.setMaterial(wallMaterial);
		wall2.move(12, 5, 0);
		rootNode.attachChild(wall2);
		objects.add(wall2);

		Geometry wall3 = new Geometry("wall3", new Box(12f, 6f, .5f));
		wall3.setMaterial(wallMaterial);
		wall3.move(0, 5, 6);
		rootNode.attachChild(wall3);
		objects.add(wall3);

		Geometry wall4 = new Geometry("wall4", new Box(12f, 6f, .5f));
		wall4.setMaterial(wallMaterial);
		wall4.move(0, 5, -6);
		rootNode.attachChild(wall4);
		objects.add(wall4);
	}

	private void createFloor(String texturePath) {

		Texture floorTexture = assetManager.loadTexture(texturePath);
		floorTexture.setMagFilter(MagFilter.Nearest);
		floorTexture.setMinFilter(MinFilter.Trilinear);
		floorTexture.setAnisotropicFilter(16);

		Material floorMaterial = new Material(assetManager, materialPath);
		floorMaterial.setTexture("ColorMap", floorTexture);

		Geometry floor = new Geometry("floor", new Box(6f, 1f, 3f));

		floor.setLocalScale(2f, 0.5f, 2f);
		floor.move(0f, -1.5f, 0f);

		floor.setMaterial(floorMaterial);

		rootNode.attachChild(floor);
		objects.add(floor);
	}

	private void createCeiling(String texturePath) {
		Texture ceilingTexture = assetManager.loadTexture(texturePath);
		ceilingTexture.setMagFilter(MagFilter.Nearest);
		ceilingTexture.setMinFilter(MinFilter.Trilinear);
		ceilingTexture.setAnisotropicFilter(16);

		Material ceilingMaterial = new Material(assetManager, materialPath);
		ceilingMaterial.setTexture("ColorMap", ceilingTexture);

		Geometry ceiling = new Geometry("floor", new Box(6f, 1f, 3f));

		ceiling.setLocalScale(2f, 0.5f, 2f);
		ceiling.move(0f, 12f, 0f);

		ceiling.setMaterial(ceilingMaterial);
		
		rootNode.attachChild(ceiling);
		objects.add(ceiling);
	}

}
