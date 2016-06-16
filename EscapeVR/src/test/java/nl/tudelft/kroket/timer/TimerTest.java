package nl.tudelft.kroket.timer;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.screen.HeadUpDisplay;

public class TimerTest {
  
  private ClientThread clientThread;
  private HeadUpDisplay hud;

  /**
   * Sets up the mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    clientThread = Mockito.mock(ClientThread.class);
    hud = Mockito.mock(HeadUpDisplay.class);
  }

  @Test
  public void testTimer() {
    Timer timer = new Timer(clientThread, hud);
    assertNotNull(timer);
    assertFalse(timer.isActive());
  }

  @Test
  public void testStartTimer() {
    Timer timer = new Timer(clientThread, hud);
    timer.startTimer();
    assertNotNull(timer.getTimeLimit());
    assertTrue(timer.isActive());
  }

  @Test
  public void testStopTimer() {
    Timer timer = new Timer(clientThread, hud);
    Mockito.doNothing().when(clientThread).sendMessage(Mockito.anyString());
    timer.stopTimer();
    assertFalse(timer.isActive());
    Mockito.verify(clientThread).sendMessage(Mockito.anyString());
  }

  @Test
  public void testUpdate() {
    Timer timer = new Timer(clientThread, hud);
    Mockito.doNothing().when(hud).setTimerText(Mockito.anyString());
    timer.startTimer();
    timer.update();
    Mockito.verify(hud).setTimerText(Mockito.anyString());
  }

  @Test
  public void testBonusTime() {
    Timer timer = new Timer(clientThread, hud);
    timer.startTimer();
    long timeLimit = timer.getTimeLimit();
    timer.bonusTime();
    assertEquals(timeLimit + 20000, timer.getTimeLimit());
  }

}
