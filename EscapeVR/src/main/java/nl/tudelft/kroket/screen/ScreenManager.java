package nl.tudelft.kroket.screen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

public class ScreenManager {

	private final String className = this.getClass().getSimpleName();
	private Logger log = Logger.getInstance();

	HashMap<String, Screen> screens = new HashMap<String, Screen>();

	AssetManager assetManager;
	Node guiNode;
	float width, height;

	public ScreenManager(AssetManager assetManager, Node guiNode, float width,
			float height) {

		log.info(className, "Initializing...");

		this.assetManager = assetManager;
		this.guiNode = guiNode;
		this.width = width;
		this.height = height;
	}

	public void loadScreen(String name, Class<? extends Screen> screenClass) {

		Screen newScreen = null;
		try {
			Constructor<? extends Screen> ctor = screenClass
					.getDeclaredConstructor(AssetManager.class, Node.class,
							float.class, float.class);
			newScreen = ctor.newInstance(assetManager, guiNode, width, height);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		screens.put(name, newScreen);
	}

	public Screen getScreen(String name) {

		if (!screens.containsKey(name)) {
			log.error(className,
					String.format("Screen %s does not exist.", name));
			return null;
		}

		return screens.get(name);
	}

	public void showScreen(String name) {
		getScreen(name).show();
	}

	public void hideScreen(String name) {
		getScreen(name).hide();
	}
}
