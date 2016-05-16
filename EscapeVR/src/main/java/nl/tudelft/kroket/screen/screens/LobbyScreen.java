package nl.tudelft.kroket.screen.screens;

import nl.tudelft.kroket.screen.Screen;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public class LobbyScreen extends Screen {
	
	private final static String name = "lobby";

	public LobbyScreen(AssetManager assetManager, Node guiNode, float width, float height) {
		super(name, assetManager, guiNode);
		
		overlay = loadImage("overlay/waiting.png", width, height);
	}
}

