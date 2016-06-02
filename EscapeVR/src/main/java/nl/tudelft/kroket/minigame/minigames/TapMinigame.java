package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

public class TapMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Singleton instance. */
  private static Minigame instance = new TapMinigame();

  /** Private constructor. */
  private TapMinigame() {}
  
  /** The correct sequence of buttons. */
  private static List<String> sequenceList = new ArrayList<String>();
  
  private static List<String> firstSequence = new ArrayList<String>();
  private static List<String> secondSequence = new ArrayList<String>();
  private static List<String> thirdSequence = new ArrayList<String>();
  private static List<String> fourthSequence = new ArrayList<String>();
  
  public sequenceState seqState = sequenceState.sequenceOne;
  
  /** The list of buttons pressed. */
  private static List<String> buttonList = new ArrayList<String>();

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
  }
  
  public enum sequenceState{
	  sequenceOne, sequenceTwo, sequenceThree, sequenceFour, completed;
	  
	  	  
	  public sequenceState getNext() {
		  switch(this) {
          case sequenceOne: return sequenceTwo;
          case sequenceTwo: return sequenceThree;
          case sequenceThree: return sequenceFour;
          case sequenceFour: return completed;
          default: return this;
          }

	  }
	  
	  public List<String> returnSequence() {
          switch(this) {
          case sequenceOne: return firstSequence;
          case sequenceTwo: return secondSequence;
          case sequenceThree: return thirdSequence;
          case sequenceFour: return fourthSequence;
          default: return sequenceList;
          }
	  }   
      
	  public String returnCompleteMessage() {
       	  switch(this) {
       	  case completed: return "YOU GOT THEM ALL WELL DONE!";
       	  default: return "GOOD JOB NOW THE NEXT ONE!";
       	  }
       }
	  
  }
  
  /**
   * Start the minigame.
   */
  @Override
  public void start() {
    log.info(className, "Minigame B started.");
    
    screenManager.getScreen("controller").show();
    hud.setCenterText("Minigame B started!", 10);
    hud.setCenterText(
            "press the buttons in order\nspecified by the android user\nthere will be multiple sequences",
            20);
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame B completed.");
    hud.setCenterText("Minigame B complete!", 10);
    sceneManager.extendEscapeScene("C");
  }

  @Override
  public void update(float tpf) {
	  
	  if(buttonList.size() == 4){
		  if(buttonList.equals(seqState.returnSequence())){
			  seqState = seqState.getNext();
			  buttonList.clear();
			  hud.setCenterText(seqState.returnCompleteMessage(), 4);
		  } else {
			  buttonList.clear();
			  hud.setCenterText("WRONG SEQUENCE TRY AGAIN!", 4);
		  }
	  }
	      
  }

  @Override
  public void handleEvent(EventObject event) {
   
	  if (event instanceof ButtonPressEvent) {
		  String buttonName = ((ButtonPressEvent) event).getName();
	      buttonList.add(buttonName);
	   }
	  
	  
  }
  
  /**
   * Parse the colors received from the server by matching them to the button that should be 
   * pressed and add this button to the sequenceList.
   * 
   * @param params the params from the command that contain the colors.
   */
  public void parseButtons(List<String> params) {
    for (String buttonString : params) {
      switch (buttonString) {
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
    firstSequence = sequenceList.subList(0, 4);
    secondSequence = sequenceList.subList(4, 8);
    thirdSequence = sequenceList.subList(8, 12);
    fourthSequence = sequenceList.subList(12, 16);
    
  }

}