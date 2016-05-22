package nl.tudelft.kroket.screen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

/**
 * ScreenManager used for handling overlays.
 * 
 * @author Team Kroket
 *
 */
public class ScreenManager {

	/** Current class, used as tag for logger. */
	private final String className = this.getClass().getSimpleName();

	/** Singleton logger instance. */

	private Logger log = Logger.getInstance();

	HashMap<String, Screen> screens = new HashMap<String, Screen>();

	AssetManager assetManager;
	Node guiNode;

	float width;
	float height;

	/**
	 * Constructor for ScreenManager.
	 * 
	 * @param assetManager
	 *            reference to the AssetManager
	 * @param guiNode
	 *            reference to the guiNode
	 * @param width
	 *            the width of the overlays
	 * @param height
	 *            the height of the overlays
	 */
	public ScreenManager(AssetManager assetManager, Node guiNode, float width,
			float height) {

		log.info(className, "Initializing...");

		this.assetManager = assetManager;
		this.guiNode = guiNode;
		this.width = width;
		this.height = height;
	}

	/**
	 * The load screen method.
	 * 
	 * @param name
	 *            - String
	 * @param screenClass
	 *            - Class<? extends Screen>
	 */
	public void loadScreen(String name, Class<? extends Screen> screenClass) {

		Screen newScreen = null;
		try {
			Constructor<? extends Screen> ctor = screenClass
					.getDeclaredConstructor(AssetManager.class, Node.class,
							float.class, float.class);
			newScreen = ctor.newInstance(assetManager, guiNode, width, height);

		} catch (InstantiationException e) {

			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// after initiating the screen, store it so we can reference it later
		screens.put(name, newScreen);
	}

	/**
	 * Get a screen by name.
	 * 
	 * @param name
	 *            the name of the screen
	 * @return Screen object, could be null if not found
	 */

	public Screen getScreen(String name) {

		if (!screens.containsKey(name)) {
			log.error(className,
					String.format("Screen %s does not exist.", name));
			return null;
		}

		return screens.get(name);
	}

	/**
	 * Show a screen by name.
	 * 
	 * @param name
	 *            the name of the screen
	 */
	public void showScreen(String name) {

		log.info(className, "Showing screen: " + name);
		getScreen(name).show();
	}

	/**
	 * Hide a screen by name.
	 * 
	 * @param name
	 *            the name of the screen
	 */
	public void hideScreen(String name) {
		getScreen(name).hide();
	}
	
	public void update() {
		for (Screen screen : screens.values()) {
			screen.update();
		}
	}
}
