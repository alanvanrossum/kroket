package nl.tudelft.kroket.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.log.Logger;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class EventManager {

	/** Current class, used as tag for logger. */
	private final String className = this.getClass().getSimpleName();

	/** Singleton logger instance. */
	private Logger log = Logger.getInstance();

	private HashMap<String, EventObject> eventList = new HashMap<String, EventObject>();
	private List<EventListener> listenerList = new ArrayList<EventListener>();

	private final int INPUT_GRACE_PERIOD = 400;

	ActionListener actionListener;

	long prevInput = 0;

	private Node rootNode;

	private HashMap<String, Float> triggers = new HashMap<String, Float>();

	/**
	 * constructor of the event manager.
	 * 
	 * @param root
	 *            - node
	 */
	public EventManager(Node root) {
		
		log.info(className, "Initializing...");

		this.rootNode = root;

		actionListener = new ActionListener() {

			private final Node rn = rootNode;

			public void onAction(String name, boolean keyPressed, float tpf) {

				if (!keyPressed) {
					return;
				}

				long now = System.currentTimeMillis();
				long delta = now - prevInput;

				if (delta < INPUT_GRACE_PERIOD) {
					return;
				}

				prevInput = now;

				for (Entry<String, Float> entry : triggers.entrySet()) {

					Spatial object = rn.getChild(entry.getKey());

					if (InteractionEvent.checkConditions(object,
							entry.getValue(), name)) {
						InteractionEvent event = new InteractionEvent(this,
								entry.getKey());
						addEvent("interaction", event);

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
		for (EventObject event : eventList.values()) {
			while (i.hasNext()) {

				((EventListener) i.next()).handleEvent(event);
			}
		}

		eventList.clear();
	}

	public synchronized void addEvent(String type, EventObject event) {
		eventList.put(type, event);
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