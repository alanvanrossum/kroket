package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.escape.SetUp;
import nl.tudelft.kroket.escape.Launcher;
import nl.tudelft.kroket.state.State;

public class NoState extends State{

  public NoState(){

  }

  public void update(float tpf){
    if (Launcher.mainApplication.getSetUp() != null) {

    }   
  }

  @Override
  public void stopState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      
    }
    
  }

  @Override
  public void startState() {
    if (Launcher.mainApplication.getSetUp() != null) {
      
    }
    
  }

}
