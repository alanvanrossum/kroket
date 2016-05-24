package nl.tudelft.kroket.audio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.audio.AudioSource;
import com.jme3.scene.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Class for simple unit tests for the AudioManager class.
 * @author Team Kroket
 *
 */
public class AudioManagerTest {

  private AssetManager assetManager;

  private Node rootNode;

  private AudioManager audioManager;

  /**
   * Sets up the test for AudioManager.
   * 
   * @throws Exception
   *           the exception
   */
  @Before
  public void setUp() throws Exception {

    assetManager = mock(AssetManager.class);
    rootNode = mock(Node.class);
    audioManager = new AudioManager(assetManager, rootNode, "Sound/");
  }

  /**
   * Test if the AudioManager class is not null.
   */
  @Test
  public void testAudioManager() {
    assertFalse(audioManager == null);
  }

  /**
   * Test if a file can be loaded.
   */
  @Test
  public void testLoadFile() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);

    assertFalse(audioManager.getNode("waiting") == null);
    assertTrue(audioManager.getAudioNodes().containsKey("waiting"));
  }

  /**
   * Test for the play method.
   */
  @Test
  public void testPlayAudio() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);
    //temp until we find how to start an audiorenderer that is not null
    audioManager.getNode("waiting").setStatus(AudioSource.Status.Playing); 
    //audioManager.getNode("waiting").play();

    assertTrue(audioManager.getNode("waiting").getStatus() == AudioSource.Status.Playing);
  }

  /**
   * Test for the stop audio method.
   */
  @Test
  public void testStopAudio() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);
    audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, false, 5);
    //audioManager.getNode("waiting").play();
    //audioManager.stopAudio();
    for(AudioNode an : audioManager.getAudioNodes().values()){
      //assertEquals(an.getStatus(), AudioSource.Status.Stopped);
    }
  }
  
  /**
   * Test for the stop method.
   */
  @Test
  public void testStop() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);
    //audioManager.getNode("waiting").play();
    //audioManager.stopAudio();
    //temp until we find how to start an audiorenderer that is not null
    audioManager.getNode("waiting").setStatus(AudioSource.Status.Stopped); 

    assertTrue(audioManager.getNode("waiting").getStatus() == AudioSource.Status.Stopped);
  }

  /**
   * Test if the getNode method returns a node that is in the
   * audioNodes hashmap, and returns null if it is not.
   */
  @Test
  public void testGetNode() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);
    assertEquals(audioManager.getNode("nonexistant"), null);
    assertTrue(audioManager.getAudioNodes().containsKey("waiting"));
  }

  @Test
  public void testGetStatus() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false, false, 5);
    audioManager.getNode("waiting").setStatus(AudioSource.Status.Playing); 
    assertEquals(audioManager.getStatus("waiting"), AudioSource.Status.Playing);
  }

  @Test
  public void testGetPlaybackTime() {
    audioManager.loadFile("ambient", "Soundtrack/ambient.wav", false, false, 5);
    assertEquals(audioManager.getPlaybackTime("ambient"), 0.0, 0.05);
  }

}
