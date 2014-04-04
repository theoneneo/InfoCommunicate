package com.neo.infocommunicate.listener;

import java.util.Vector;

public class ListenerAbility {
	protected Vector<Listener> myListener;
	public ListenerAbility() {
		myListener = new Vector<Listener>();
	}

	public void addListener(Listener listener) {
		synchronized (myListener) {
			if(!myListener.contains(listener)){				
				myListener.add(listener);
			}
		}
	}

	public void removeListener(Listener listener) {
		synchronized (myListener) {
			myListener.remove(listener);
		}
	}
	
	public void clearListener() {
		synchronized (myListener) {
			myListener.removeAllElements();
		}
	}
}
