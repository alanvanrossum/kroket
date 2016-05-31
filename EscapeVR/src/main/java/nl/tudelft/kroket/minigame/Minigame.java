package nl.tudelft.kroket.minigame;

import java.util.EventObject;

public abstract class Minigame {

  public abstract void start();
  
  public abstract void stop();
  
  public abstract void update(float tpf);
  
  public abstract void handleEvent(EventObject event);
  
}
