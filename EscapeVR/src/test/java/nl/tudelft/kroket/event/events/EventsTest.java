package nl.tudelft.kroket.event.events;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;

public class EventsTest {

  @Test
  public void testGameLostEvent() {
    GameLostEvent gls = new GameLostEvent(Mockito.mock(Object.class));
    assertNotNull(gls);
  }
  
  @Test
  public void testInteractionEvent() {
    InteractionEvent ie = new InteractionEvent(Mockito.mock(Object.class), "test");
    assertNotNull(ie);
    assertEquals(ie.getName(), "test");
  }

  @Test
  public void testButtonPressEvent() {
    ButtonPressEvent bpe = new ButtonPressEvent(Mockito.mock(Object.class), "test");
    assertNotNull(bpe);
    assertEquals(bpe.getName(), "test");
  }

  @Test
  public void testGameStartEvent() {
    GameStartEvent gse = new GameStartEvent(Mockito.mock(Object.class));
    assertNotNull(gse);
  }
  
  @Test
  public void testGameWonEvent() {
    GameWonEvent gse = new GameWonEvent(Mockito.mock(Object.class));
    assertNotNull(gse);
  }
  
  @Test
  public void testMinigameCompleteEvent() {
    MinigameCompleteEvent mce = new MinigameCompleteEvent(Mockito.mock(Object.class));
    assertNotNull(mce);
  }
  
  @Test
  public void testStartMinigameEvent() {
    StartMinigameEvent sme = new StartMinigameEvent(Mockito.mock(Object.class));
    assertNotNull(sme);
  }
  
  @Test
  public void testTimeoutEvent() {
    TimeoutEvent te = new TimeoutEvent(Mockito.mock(Object.class));
    assertNotNull(te);
  }


}
