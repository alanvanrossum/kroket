package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;
import nl.tudelft.kroket.net.protocol.Protocol;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class ColorSequenceMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Singleton instance. */
  private static Minigame instance = new ColorSequenceMinigame();

  /** The list of buttons pressed. */
  private static List<String> buttonList = new ArrayList<String>();

  /** The correct sequence of buttons. */
  private static List<String> sequenceList = new ArrayList<String>();
  
  /** Boolean which shows if this game is active. */
  public static boolean running = false;

  
  private ColorSequenceMinigame() {}

  public static Minigame getInstance() {
    return instance;
  }

  /**
   * Start the game.
   */
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

  /**
   * Stop the game.
   */
  @Override
  public void stop() {
    log.info(className, "Stopping ColorSequenceMinigame");
    buttonList.clear();
    running = false;
    
    clientThread.sendMessage(Protocol.COMMAND_INIT_MOBILE + "[doneC]");
    clientThread.sendMessage(Protocol.COMMAND_INIT_VR + "[doneC]");
    
    screenManager.getScreen("controller").hide();
    hud.setCenterText("Minigame C complete!");
    minigameManager.endGame();
  }

  /**
   * The update method. Checks whether the sequence added is the correct one.
   */
  @Override
  public void update(float tpf) {

    if ((System.currentTimeMillis() % 10000) == 0) {
      System.out.println("Required sequence:");
      printList(sequenceList);
      System.out.println("Entered sequence:");
      printList(buttonList);
    }
    
    //Check if the correct sequence is entered
    Boolean correct = checkSequence();
    if (correct) {
      this.stop();
    }

  }

  /**
   * Handles the event of clicking a button.
   * Keeps the buttonlist of the same of smaller size than the sequence.
   */
  @Override
  public void handleEvent(EventObject event) {

    if (event instanceof ButtonPressEvent) {
      String buttonName = ((ButtonPressEvent) event).getName();
      buttonList.add(buttonName);
      
      //TODO Display the color pressed on the screen
      
      //Keep the lists the same size, by removing the first element
      if (buttonList.size() > sequenceList.size()) {
        buttonList = buttonList.subList(1, buttonList.size());
      }
      
    }
  }

  /**
   * Checks whether the sequence entered is the correct one.
   * 
   * @return true iff it is correct.
   */
  public boolean checkSequence() {
    // check hier of alle entries in buttonList hetzelfde zijn als sequenceList
    return sequenceList.equals(buttonList);
  }

  /**
   * Set the sequence; the correct answer;
   * 
   * @param sequence the sequence to be set.
   */
  public void setSequence(List<String> sequence) {
    sequenceList = sequence;
  }

  /**
   * Print a list with strings.
   * 
   * @param list the list to be printed.
   */
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
        default: 
      }
    }
  }

}