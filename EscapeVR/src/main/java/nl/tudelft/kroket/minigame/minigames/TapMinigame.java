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
  
  private int numberofButtons = 1;
  
  /** Private constructor. */
  private TapMinigame() {}
  
  /** The correct sequence of buttons. */
  private static List<String> sequenceList = new ArrayList<String>();
  
  private String firstButton, secondButton, thirdButton, fourthButton;
  
  private boolean holdFirst, holdSecond, holdThird, holdFourth;

private static boolean finished = false;

  public static boolean Completable = true;

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
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
            "Hold down the buttons specified\nby the android user\nDO NOT LET GO OF THEM!",
            20);
    
    System.out.println("below is the list of buttons");
    System.out.println(sequenceList);
    System.out.println("above is the list of buttons");
    firstButton = sequenceList.get(0);
    secondButton = sequenceList.get(1);
    thirdButton = sequenceList.get(2);
    fourthButton = sequenceList.get(3);
    System.out.println(firstButton);
    System.out.println(secondButton);
    System.out.println(thirdButton);
    System.out.println(fourthButton);
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
    if(numberofButtons == 1){
    	holdFirst = EventManager.getHold(firstButton);
    	if(holdFirst == false){
    		Completable  = false;
    	}
    }if(numberofButtons == 2){
    	holdFirst = EventManager.getHold(firstButton);
    	holdSecond = EventManager.getHold(secondButton);
    	if(holdFirst == false || holdSecond == false){
    		Completable  = false;
    	}
    }if(numberofButtons == 3){
    	holdFirst = EventManager.getHold(firstButton);
    	holdSecond = EventManager.getHold(secondButton);
    	holdThird = EventManager.getHold(thirdButton);
    	if(holdFirst == false || holdSecond == false || holdThird == false){
    		Completable  = false;
    	}
    }if(numberofButtons == 4){
    	holdFirst = EventManager.getHold(firstButton);
    	holdSecond = EventManager.getHold(secondButton);
    	holdThird = EventManager.getHold(thirdButton);
    	holdFourth = EventManager.getHold(fourthButton);
    	if(holdFirst == false || holdSecond == false || holdThird == false|| holdFourth == false ){
    		Completable  = false;
    	}
    }
    
  }

  @Override
  public void handleEvent(EventObject event) {
   
	  if (event instanceof ButtonPressEvent) {
		  
		  String buttonName = ((ButtonPressEvent) event).getName();
		  
		  if(numberofButtons == 0 && buttonName == firstButton) {
			  numberofButtons++;
			  holdFirst = EventManager.getHold(firstButton);
			  
		  }
		  if(numberofButtons == 1 && buttonName == secondButton) {
			  numberofButtons++;
			  holdSecond = EventManager.getHold(secondButton);
			  
		  }
		  if(numberofButtons == 2 && buttonName == thirdButton) {
			  numberofButtons++;
			  holdThird = EventManager.getHold(thirdButton);
			  
		  }
		  if(numberofButtons == 3 && buttonName == fourthButton) {
			  numberofButtons++;
			  holdFourth = EventManager.getHold(fourthButton);
			  finished  = true;
		  }
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
  }

}
