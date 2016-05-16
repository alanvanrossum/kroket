package nl.tudelft.kroket.scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public abstract class Scene {
	
	protected HashMap<String, Spatial> objects = new HashMap<String, Spatial>();
	
	String name;
	
	protected AssetManager assetManager;
	
	protected Node rootNode;
	
	protected ViewPort viewPort;

	public Scene(String name, AssetManager assetManager, Node rootNode, ViewPort viewPort) {
		this.name = name;
		this.assetManager = assetManager;
		this.rootNode = rootNode;
		this.viewPort = viewPort;
	}
	
	public AssetManager getAssetManager() {
		return assetManager;
	}
	
	public Node getRootNode() {
		return rootNode;
	}
        
        public Spatial getObject(String name) {
            return objects.get(name);
        }
	
	public void addObject(String name, Spatial object) {
		rootNode.attachChild(object);
		objects.put(name, object);
	}

	public abstract void createScene();

	//public abstract void destroyScene();
	
	public void destroyScene() {
		for (Spatial object : objects.values()) {
			rootNode.detachChild(object);
		}
	}
	

}
