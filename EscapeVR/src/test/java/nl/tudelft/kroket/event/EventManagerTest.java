package nl.tudelft.kroket.event;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.jme3.scene.Node;
import com.jme3.scene.Spatial;


import java.util.EventObject;

public class EventManagerTest {

  @Mock private Spatial observer;
  @Mock private Node rootNode;

  @Before
  public void setUp() throws Exception {
    observer = Mockito.mock(Spatial.class);
    rootNode = Mockito.mock(Node.class);
  }

  @After
  public void tearDown() {
    observer = null;
    rootNode = null;
  }
  
  @Test
  public void testEventManager() {
    EventManager ev = new EventManager(observer, rootNode);
    assertNotNull(ev);
  }

  @Test
  public void testRegisterObjectInteractionTrigger() {
    EventManager ev = new EventManager(observer, rootNode);
    ev.registerObjectInteractionTrigger("test", 1f);
    assertTrue(ev.getTriggers().containsKey("test"));
    assertTrue(ev.getTriggers().get("test") == 1f);
  }

  @Test
  public void testAddEvent() {
    EventManager ev = new EventManager(observer, rootNode);
    EventObject event = Mockito.mock(EventObject.class);
    ev.addEvent(event);
    assertTrue(ev.getEventList().contains(event));
  }

  @Test
  public void testAddListener() {
    EventManager ev = new EventManager(observer, rootNode);
    EventListener event = Mockito.mock(EventListener.class);
    ev.addListener(event);
    assertTrue(ev.getListenerList().contains(event));
  }

  @Test
  public void testRemoveListener() {
    EventManager ev = new EventManager(observer, rootNode);
    EventListener event = Mockito.mock(EventListener.class);
    ev.addListener(event);
    assertTrue(ev.getListenerList().contains(event));
    ev.removeListener(event);
    assertFalse(ev.getListenerList().contains(event));
  }

  @Test
  public void testOnActionNotPressed() {
    EventManager ev = new EventManager(observer, rootNode);
    ev.onAction("test", false, 1f);
    Mockito.verify(rootNode, Mockito.never()).getChild(Mockito.any());
  }

  @Test
  public void testOnActionNullObject() {
    EventManager ev = new EventManager(observer, rootNode);
    EventManager evSpy = Mockito.spy(ev);
    Mockito.when(rootNode.getChild(Mockito.any())).thenReturn(null);
    ev.registerObjectInteractionTrigger("test", 1f);
    evSpy.onAction("test", true, 1f);
    Mockito.verify(evSpy, Mockito.times(1)).addEvent(Mockito.any());;
  }

  @Test(expected = NullPointerException.class)
  public void testOnAction() {
    EventManager ev = new EventManager(observer, rootNode);
    EventManager evSpy = Mockito.spy(ev);
    Mockito.when(rootNode.getChild(Mockito.any())).thenReturn(observer);
    ev.registerObjectInteractionTrigger("test", 1f);
    evSpy.onAction("test", true, 1f);
    Mockito.verify(evSpy, Mockito.times(1)).addEvent(Mockito.any());;
  }

}
