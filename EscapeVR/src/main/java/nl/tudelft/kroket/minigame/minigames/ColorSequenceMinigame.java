package nl.tudelft.kroket.minigame.minigames;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.minigame.Minigame;

public class ColorSequenceMinigame extends Minigame {

  /** Singleton instance. */
  private static Minigame instance = new ColorSequenceMinigame();
  
  
  private List<String> buttonList = new ArrayList<String>();
  
  private List<String> sequenceList = new ArrayList<String>();

  private ColorSequenceMinigame() {

  }

  public Minigame getInstance() {
    return instance;
  }

  @Override
  public void start() {
    buttonList.clear();

  }

  @Override
  public void stop() {
    buttonList.clear();

  }

  @Override
  public void update(float tpf) {

    
    
  }

  @Override
  public void handleEvent(EventObject event) {

    if (event instanceof ButtonPressEvent) {
      String buttonName = ((ButtonPressEvent) event).getName();
      buttonList.add(buttonName);
    }
  }
  
  public boolean checkSequence() {
    // check hier of alle entries in buttonList hetzelfde zijn als sequenceList
    return sequenceList.equals(buttonList);
  }
  
  public void setSequence(List<String> sequence) {
    this.sequenceList = sequence;
  }

}
