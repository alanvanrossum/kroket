package nl.tudelft.kroket.audio;

import java.util.HashMap;

import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioSource.Status;
import com.jme3.scene.Node;

public class AudioManager {
	
	private final String className = this.getClass().getSimpleName();
	private Logger log = Logger.getInstance();
	
	private String rootPath = "Sound/";
	
	HashMap<String, AudioNode> audioNodes = new HashMap<String, AudioNode>();

	private AssetManager assetManager;

	private Node rootNode;
	
	public AudioManager(AssetManager assetManager, Node rootNode, String rootPath) {
		
		log.info(className, "Initializing...");
		
		this.assetManager = assetManager;
		this.rootPath = rootPath;
		this.rootNode = rootNode;
	}
	
	private AudioNode createNode(String path, boolean positional, boolean looping, int volume) {
		String fullPath = rootPath + path;
		
		AudioNode node = new AudioNode(assetManager, fullPath, DataType.Buffer);
		node.setPositional(positional);
		node.setLooping(looping);
		node.setVolume(volume);
		return node;
	}
	
	public void loadFile(String name, String path, boolean positional, boolean looping, int volume) {
	
		AudioNode node = createNode(path, looping, looping, volume);
		
		audioNodes.put(name, node);
		
		rootNode.attachChild(node);
	}
	
	/**
	 * Stop all audio tracks from playing.
	 */
	public void stopAudio() {
		for (AudioNode node : audioNodes.values()) {
			if (node.getStatus() == AudioSource.Status.Playing)
				node.stop();
		}
	}
	
	public AudioNode getNode(String name) {
		
		if (!audioNodes.containsKey(name))
		{
			log.error(className, String.format("AudioNode %s does not exist.", name));
			return null;
		}
		
		return audioNodes.get(name);
	}
	
	/**
	 * Play an audionode.
	 * @param name the name of the audionode
	 */
	public void play(String name) {
		getNode(name).play();
	}
	
	/**
	 * Stop an audionode.
	 * @param name the name of the audionode
	 */
	public void stop(String name) {
		getNode(name).stop();
	}
	
	/**
	 * Get the status of an audionode.
	 * @param name the name of the audionode
	 */
	public Status getStatus(String name) {
		return getNode(name).getStatus();
	}

	public float getPlaybackTime(String name) {
		return getNode(name).getPlaybackTime();
	}

}
