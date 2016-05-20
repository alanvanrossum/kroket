package nl.tudelft.kroket.audio;

import java.util.HashMap;

import nl.tudelft.kroket.log.Logger;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioSource.Status;
import com.jme3.scene.Node;

/**
 * AudioManager object.
 * 
 * @author Team Kroket
 *
 */
public class AudioManager {
  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();
  
  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Relative path for audio assets. */
  private String rootPath = "Sound/";
  
  /** A HashMap object to reference all AudioNodes used. */
  HashMap<String, AudioNode> audioNodes = new HashMap<String, AudioNode>();
  
  /** Handle to the AssetManager object. */

  private AssetManager assetManager;

  private Node rootNode;
  
  /**
   * Constructor for AudioManager object.
   * 
   * @param assetManager AssetManager instance
   * @param rootNode rootNode
   * @param rootPath relative root path to assets
   */

  public AudioManager(AssetManager assetManager, Node rootNode, String rootPath) {
    
    log.info(className, "Initializing...");
    
    this.assetManager = assetManager;
    this.rootPath = rootPath;
    this.rootNode = rootNode;
  }
  
  /**
   * Load a file and create a new AudioNode.
   * 
   * @param path the path to the file
   * @param positional positional flag
   * @param looping looping flag
   * @param volume volume (integer)
   * @return the created AudioNode
   */
  private AudioNode createNode(String path, boolean positional, boolean looping, int volume) {
    String fullPath = rootPath + path;
    
    AudioNode node = new AudioNode(assetManager, fullPath, DataType.Buffer);
    node.setPositional(positional);
    node.setLooping(looping);
    node.setVolume(volume);
    return node;
  }
  
  /**
   * Load a file into AudioManager.
   * 
   * @param name the name of the object
   * @param path the path to the file
   * @param positional positional flag
   * @param looping looping flag
   * @param volume volume (integer)
   */
  public void loadFile(String name, String path, boolean positional, boolean looping, int volume) {
  
    AudioNode node = createNode(path, positional, looping, volume);
    
    audioNodes.put(name, node);
    
    rootNode.attachChild(node);
  }
  
  /**
   * Stop all audio tracks from playing.
   */
  public void stopAudio() {
    log.info(className, "Stopping all audio tracks...");
    
    for (AudioNode node : audioNodes.values()) {
      if (node.getStatus() == AudioSource.Status.Playing) {
        node.stop();
      }
    }
  }
  
  /**
   * Get a registered AudioNode object.
   * 
   * @param name the name of the object
   * @return the AudioNode object
   */
  public AudioNode getNode(String name) {
    
    if (!audioNodes.containsKey(name)) {
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
    log.info(className, String.format("Playing audio track '%s'...", name));
    
    AudioNode node = getNode(name);
    
    if (node.getStatus() != AudioSource.Status.Playing) {
      node.play();
    }
  }
  
  /**
   * Stop an audionode.
   * 
   * @param name the name of the audionode
   */
  public void stop(String name) {
    
    AudioNode node = getNode(name);
    
    if (node.getStatus() == AudioSource.Status.Playing) {
      getNode(name).stop();
    }

  }
  
  /**
   * Get the status of an audionode.
   * @param name the name of the audionode
   */
  public Status getStatus(String name) {
    return getNode(name).getStatus();
  }

  /**
   * Get playback time for an audionode.
   * @param name the name of the node
   * @return the playback time.
   */

  public float getPlaybackTime(String name) {
    return getNode(name).getPlaybackTime();
  }

}
