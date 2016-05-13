package nl.tudelft.kroket.screen;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.ui.Picture;

public abstract class Screen {

	String name;
	AssetManager assetManager;
	Node guiNode;
	String rootPath = "Textures/";
	protected Picture overlay;

	public Screen(String name, AssetManager assetManager, Node guiNode) {
		this.name = name;
		this.assetManager = assetManager;
		this.guiNode = guiNode;
	}
	
	public Picture loadImage(String imgPath, float width, float height) {
		
		String fullPath = rootPath + imgPath;
		
		System.out.println("fullPath = " + fullPath);

		Picture image = new Picture(name);
		image.setImage(assetManager, fullPath, true);
		image.setWidth(width);
		image.setHeight(height);
		// move back image (so text can appear in front of
		// image)
		image.move(0, 0, -1);
		
		return image;
	}
	

	
	public void show() {
		guiNode.attachChild(overlay);
	}
	
	public void hide() {
		guiNode.detachChild(overlay);
	}



}
