package nl.tudelft.kroket.minigame.minigames;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.net.protocol.Protocol;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

public class ColorSequenceMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Singleton instance. */
  private static Minigame instance = new ColorSequenceMinigame();

  private static List<String> buttonList = new ArrayList<String>();

  private static List<String> sequenceList = new ArrayList<String>();
  
  public static boolean running = false;

  
  private ColorSequenceMinigame() {}

  public static Minigame getInstance() {
    return instance;
  }

  @Override
  public void start() {
    log.info(className, "Starting ColorSequenceMinigame");
    buttonList.clear();
    running = true;
    
    screenManager.getScreen("controller").show();
    hud.setCenterText("Minigame C started!", 10);
    hud.setCenterText(
        "Enter the color sequence by\nusing the colored buttons on\nthe right of your controller!",
        20);
  }

  @Override
  public void stop() {
    log.info(className, "Stopping ColorSequenceMinigame");
    buttonList.clear();
    running = false;
    
    clientThread.sendMessage(Protocol.COMMAND_INIT_MOBILE + "[doneC]");
    //clientThread.sendMessage(Protocol.COMMAND_INIT_VR + "[doneC]");
    
    screenManager.getScreen("controller").hide();
  }

  @Override
  public void update(float tpf) {

    if ((System.currentTimeMillis() % 10000) == 0) {
      System.out.println("Required sequence:");
      printList(this.sequenceList);
      System.out.println("Entered sequence:");
      printList(this.buttonList);
    }
    
    //Check if the correct sequence is entered
    Boolean correct = checkSequence();
    if (correct) {
      this.stop();
    }

  }

  @Override
  public void handleEvent(EventObject event) {

    if (event instanceof ButtonPressEvent) {
      String buttonName = ((ButtonPressEvent) event).getName();
      //System.out.println("pressed " + buttonName);
      buttonList.add(buttonName);
      
      //TODO Display the color pressed on the screen
      
      //Keep the lists the same size, by removing the first element
      if (buttonList.size() > sequenceList.size()) {
        buttonList = buttonList.subList(1, buttonList.size());
      }
      
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
  
  /**
   * Parse the colors received from the server by matching them to the button that should be 
   * pressed and add this button to the sequenceList.
   * 
   * @param params the params from the command that contain the colors.
   */
  public void parseColors(List<String> params) {
    for (String colorString : params) {
      switch (colorString) {
        case "RED": sequenceList.add("Button B");
      break;
        case "GREEN": sequenceList.add("Button A");
      break;
        case "BLUE": sequenceList.add("Button X");
      break;
        case "YELLOW": sequenceList.add("Button Y");
      break;
      }
    }
  }

}
