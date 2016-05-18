package nl.tudelft.kroket.event.events;

import java.util.EventObject;

import jmevr.app.VRApplication;

import com.jme3.scene.Spatial;

@SuppressWarnings("serial")
public class InteractionEvent extends EventObject  {

	String name;
	
	public InteractionEvent(Object source, String name) {
		super(source);
		this.name = name;
	}

	public static boolean checkConditions(Spatial object, float threshold, String button) {
		
		float distance = VRApplication.getFinalObserverPosition().distance(
				object.getWorldBound().getCenter());
		
		return (distance <= threshold && button.equals("Button A"));
	}

	public String getName() {
		return name;
	}
}
