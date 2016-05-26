package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.escape.SetUp;
import nl.tudelft.kroket.escape.Launcher;
import nl.tudelft.kroket.state.State;

public class LobbyState extends State {
  
  private SetUp escape;
  
  public LobbyState(){
    escape = Launcher.mainApplication.getSetUp();
  }
  
  /**
   * 
   */
  public void update(float tpf){
    if (Launcher.mainApplication.getSetUp() != null) {
      
    }   
  }

  @Override
  public void stopState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      escape.getAudioManager().stopAudio();
      escape.getScreenManager().hideScreen("lobby");
    }
    
  }

  @Override
  public void startState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      escape.getInputHandler().setAcceptInput(false);
      escape.getAudioManager().play("waiting");
      escape.getScreenManager().showScreen("lobby");
    }
    
  }

}
