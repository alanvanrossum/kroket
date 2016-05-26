package nl.tudelft.kroket.state.states;

import com.jme3.audio.AudioSource;

import nl.tudelft.kroket.escape.SetUp;
import nl.tudelft.kroket.escape.Launcher;
import nl.tudelft.kroket.escape.EscapeVR.GameState;
import nl.tudelft.kroket.state.State;

public class IntroState extends State {
  
  private SetUp setUp; 
  
  public IntroState() {
    setUp = Launcher.mainApplication.getSetUp();
  }
  
  /**
   * Update the scene.
   */
  public void update(float tpf) {
    if (Launcher.mainApplication.getSetUp() != null) {
      if (setUp.getAudioManager().getStatus("welcome") == AudioSource.Status.Playing) {
        // displayIntro(audioManager.getPlaybackTime("welcome"));
      } else {
        Launcher.mainApplication.setGameState(GameState.PLAYING);
        System.out.println("hallo");
      }
      setUp.getInputHandler().handleInput(tpf);
    }   
  }

  @Override
  public void stopState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      setUp.getAudioManager().stopAudio();
    }
  }

  @Override
  public void startState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      setUp.getInputHandler().setAcceptInput(true);
      setUp.getAudioManager().play("welcome");
    }
    
  }

}
