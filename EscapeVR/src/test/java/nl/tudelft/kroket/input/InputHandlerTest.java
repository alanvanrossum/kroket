package nl.tudelft.kroket.input;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.input.InputManager;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.interaction.RotationHandler;

public class InputHandlerTest {

  private InputManager inputManager;
  private EventManager eventManager;
  
  @Before
  public void setUp() throws Exception {
    inputManager = Mockito.mock(InputManager.class);
    eventManager = Mockito.mock(EventManager.class);
  }

  @After
  public void tearDown() throws Exception {
    
  }

  @Test
  public void testInputHandler() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    assertNotNull(inputHandler);
  }

  @Test
  public void testRegisterListenerNull() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    inputHandler.registerListener(null);
    assertTrue(inputHandler.getListeners().isEmpty());
  }
  
  @Test
  public void testRegisterListenerContainsListener() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    InteractionHandler listener = new RotationHandler(Mockito.mock(Spatial.class));
    inputHandler.registerListener(listener);
    assertTrue(inputHandler.getListeners().contains(listener));
    inputHandler.registerListener(listener);
    List<InteractionHandler> list = new ArrayList<>();
    list.add(listener);
    list.add(listener);
    assertFalse(inputHandler.getListeners().equals(listener));
  }
  
  @Test
  public void testRegisterListener() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    InteractionHandler listener = new RotationHandler(Mockito.mock(Spatial.class));
    inputHandler.registerListener(listener);
    assertTrue(inputHandler.getListeners().contains(listener));
  }

  @Test
  public void testRemoveListener() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    InteractionHandler listener = new RotationHandler(Mockito.mock(Spatial.class));
    inputHandler.registerListener(listener);
    assertTrue(inputHandler.getListeners().contains(listener));
    inputHandler.removeListener(listener);
    assertFalse(inputHandler.getListeners().contains(listener));
  }

  @Test
  public void testRegisterMappings() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    InputHandler inputSpy = Mockito.spy(inputHandler);
    InteractionHandler listener = new RotationHandler(Mockito.mock(Spatial.class));
    Mockito.doNothing().when(inputManager).addListener((InputListener)listener, "test");
    inputSpy.registerMappings(listener, "test");
    Mockito.verify(inputSpy).registerListener(Mockito.any());
    Mockito.verify(inputManager).addListener((InputListener)listener, "test");
  }

  @Test
  public void testHandleInput() {
    InputHandler inputHandler = new InputHandler(inputManager, eventManager);
    InteractionHandler listener = Mockito.mock(RotationHandler.class);
    inputHandler.registerListener(listener);
    Mockito.doNothing().when(listener).update(1f);;
    inputHandler.handleInput(1f);
    Mockito.verify(listener).update(1f);
  }

}
