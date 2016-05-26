package nl.tudelft.kroket.state;

import nl.tudelft.kroket.escape.SetUp;
import nl.tudelft.kroket.escape.Launcher;

public abstract class State {
  
  public State(){
    
  }
  
  public abstract void update(float tpf);
  
  public abstract void stopState();
  
  public abstract void startState();
  
}
