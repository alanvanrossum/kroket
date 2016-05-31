package nl.tudelft.kroket.minigame.minigames;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

public class ColorSequenceMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Singleton instance. */
  private static Minigame instance = new ColorSequenceMinigame();

  private List<String> buttonList = new ArrayList<String>();

  private List<String> sequenceList = new ArrayList<String>();

  private ColorSequenceMinigame() {

  }

  public static Minigame getInstance() {
    return instance;
  }

  @Override
  public void start() {

    log.info(className, "Starting ColorSequenceMinigame");
    buttonList.clear();

  }

  @Override
  public void stop() {
    buttonList.clear();

  }

  @Override
  public void update(float tpf) {

    if ((System.currentTimeMillis() % 10000) == 0) {
      System.out.println("Required sequence:");
      printList(this.sequenceList);
      System.out.println("Entered sequence:");
      printList(this.buttonList);
    }

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

  private void printList(List<String> list) {
    for (String button : list) {
      System.out.println(button);
    }
  }

}
