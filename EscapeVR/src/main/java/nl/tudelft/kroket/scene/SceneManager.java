package nl.tudelft.kroket.scene;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import nl.tudelft.kroket.log.Logger;

public class SceneManager {

	private final String className = this.getClass().getSimpleName();
	private Logger log = Logger.getInstance();

	HashMap<String, Scene> scenes = new HashMap<String, Scene>();
	private AssetManager assetManager;
	private Node rootNode;

	public SceneManager(AssetManager assetManager, Node rootNode) {
		log.info(className, "Initializing...");

		this.assetManager = assetManager;
		this.rootNode = rootNode;
	}

	public void loadScene(String name, Class<? extends Scene> sceneClass) {

		Scene newScene = null;
		try {
			// newScene = class1.newInstance();
			Constructor<? extends Scene> ctor = sceneClass
					.getDeclaredConstructor(String.class, AssetManager.class,
							Node.class);
			newScene = ctor.newInstance(name, assetManager, rootNode);
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

		scenes.put(name, newScene);
	}

	public void destroyScene(String name) {
		getScene(name).destroyScene();
	}

	public Scene getScene(String name) {

		if (!scenes.containsKey(name)) {
			log.error(className,
					String.format("Scene %s does not exist.", name));
			return null;
		}

		return scenes.get(name);
	}

}
