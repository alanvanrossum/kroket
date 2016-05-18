package nl.tudelft.kroket.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nl.tudelft.kroket.event.events.InteractionEvent;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class EventManager {
	private List<EventObject> eventList = new ArrayList<EventObject>();
	private List<EventListener> listenerList = new ArrayList<EventListener>();

	ActionListener actionListener;

	Node rootNode;

	private HashMap<String, Float> triggers = new HashMap<String, Float>();

	public EventManager(Node rootNode) {
		this.rootNode = rootNode;

		actionListener = new ActionListener() {

			public void onAction(String name, boolean keyPressed, float tpf) {

				for (Entry<String, Float> entry : triggers.entrySet()) {

					Spatial object = rootNode.getChild(entry.getKey());

					if (InteractionEvent.checkConditions(object,
							entry.getValue(), name)) {
						InteractionEvent event = new InteractionEvent(this, entry.getKey());
						addEvent(event);

					}
				}

				fireEvents();
			}
		};
	}

	public void registerTrigger(String objName, float threshold) {
		triggers.put(objName, threshold);
	}

	private synchronized void fireEvents() {
		Iterator<EventListener> i = listenerList.iterator();
		for (EventObject event : eventList) {
			while (i.hasNext()) {

				((EventListener) i.next()).handleEvent(event);
			}
		}
		
	eventList.clear();
	}

	public synchronized void addEvent(EventObject event) {
		eventList.add(event);
	}

	public synchronized void addListener(EventListener listener) {
		listenerList.add(listener);
	}

	public synchronized void removeListener(EventListener listener) {
		listenerList.remove(listener);
	}

	public InputListener getActionListener() {
		return actionListener;
	}

}