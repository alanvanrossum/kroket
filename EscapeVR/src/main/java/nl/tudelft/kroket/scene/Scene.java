package nl.tudelft.kroket.scene;

import java.util.ArrayList;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class Scene {
	
	protected List<Spatial> objects = new ArrayList<Spatial>();
	
	String name;
	
	protected AssetManager assetManager;
	
	protected Node rootNode;

	public Scene(String name, AssetManager assetManager, Node rootNode) {
		this.name = name;
		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
	
	public void addObject(Spatial object) {
		rootNode.attachChild(object);
		objects.add(object);
	}

	public abstract void createScene();

	//public abstract void destroyScene();
	
	public void destroyScene() {
		for (Spatial object : objects) {
			rootNode.detachChild(object);
		}
	}
	

}
