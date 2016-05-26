package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.escape.SetUp;
import nl.tudelft.kroket.escape.Launcher;
import nl.tudelft.kroket.escape.EscapeVR;
import nl.tudelft.kroket.state.State;

public class PlayingState extends State{

  private SetUp escape;

  public PlayingState() {
    escape = Launcher.mainApplication.getSetUp();
  }

  public void update(float tpf){
    if (Launcher.mainApplication.getSetUp() != null) {
      escape.getInputHandler().handleInput(tpf);
      long spookyTime = Launcher.mainApplication.getSpookyTime();
      if (spookyTime < System.currentTimeMillis()) {
        spookyTime = System.currentTimeMillis() + EscapeVR.randInt(20, 120) * 1000;
        Launcher.mainApplication.setSpookyTime(spookyTime);
        escape.getScreenManager().showScreen("spooky");
      }
    }   



  }

  @Override
  public void stopState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      escape.getInputHandler().setAcceptInput(false);
      escape.getAudioManager().stopAudio();
    }  
  }

  @Override
  public void startState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      escape.getInputHandler().setAcceptInput(true);
      escape.getAudioManager().play("letthegamebegin");
      escape.getAudioManager().play("ambient");
      long spookyTime = Launcher.mainApplication.getSpookyTime();
      spookyTime = System.currentTimeMillis() + EscapeVR.randInt(20, 120) * 1000;
      Launcher.mainApplication.setSpookyTime(spookyTime);
    }
   
  }

}
