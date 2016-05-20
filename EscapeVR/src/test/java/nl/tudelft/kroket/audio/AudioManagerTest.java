package nl.tudelft.kroket.audio;

import static org.junit.Assert.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioSource;
import com.jme3.scene.Node;

public class AudioManagerTest {
  
    private AssetManager assetManager;

  private Node rootNode;
  
  private AudioManager audioManager;
  
  
  @Before
  public void setUp() throws Exception {
    
    assetManager = mock(AssetManager.class);
    rootNode = mock(Node.class);
    
    audioManager = new AudioManager(assetManager, rootNode, "Sound/");
  }

  @Test
  public void testAudioManager() {
    assertFalse(audioManager == null);
  }

  @Test
  public void testLoadFile() {
    audioManager.loadFile("waiting", "Soundtrack/waiting.wav", false,
        false, 5);
    
    assertFalse(audioManager.getNode("waiting") == null);
  }

//  @Test
//  public void testStopAudio() {
//    audioManager.getNode("waiting").play();
//    
//    audioManager.stopAudio();
//    
//    assertFalse(audioManager.getNode("waiting").getStatus() == AudioSource.Status.Playing);
//  }

//  @Test
//  public void testGetNode() {
//    assertTrue(audioManager.getNode("nonexistant") == null);
//    assertFalse(audioManager.getNode("waiting") == null);
//  }
//
//  @Test
//  public void testPlay() {
//    audioManager.getNode("waiting").play();
//    assertTrue(audioManager.getNode("waiting").getStatus() == AudioSource.Status.Playing);
//  }

//  @Test
//  public void testStop() {
//    audioManager.getNode("waiting").stop();
//    assertFalse(audioManager.getNode("waiting").getStatus() == AudioSource.Status.Playing);
//  }

//  @Test
//  public void testGetStatus() {
//    fail("Not yet implemented");
//  }
//
//  @Test
//  public void testGetPlaybackTime() {
//    fail("Not yet implemented");
//  }

}
