package nl.tudelft.kroket.escape;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for EscapeVR class.
 * @author Team Kroket
 *
 */
public class EscapeVRTest {

  /**
   * Test method for getter and setter of remotehost.
   */
  @Test
  public void testGetSetRemoteHost() {
    EscapeVR vr = new EscapeVR();
    vr.setRemoteHost("test");
    assertEquals(vr.getRemoteHost(), "test");  
  }
  
  /**
   * Test method for getter and setter of remote port.
   */
  @Test
  public void testGetSetRemoteport() {
    EscapeVR vr = new EscapeVR();
    vr.setRemotePort(4);
    assertEquals(vr.getRemotePort(), 4);  
  }
  
  /**
   * Test method for getter and setter for the playername.
   */
  @Test
  public void testGetSetPlayername() {
    EscapeVR vr = new EscapeVR();
    vr.setPlayerName("test");
    assertEquals(vr.getPlayerName(), "test");  
  }
  
}
