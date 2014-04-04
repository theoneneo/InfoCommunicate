package com.neo.infocommunicate.listener;

public class ServiceListenerAbility extends ListenerAbility {
	public void notifyRegisterListener(String msg) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((ServiceListener) myListener.get(i)).onRegister(msg);
			}
		}
	}
	
	public void notifyPushMessageListener(String msg) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((ServiceListener) myListener.get(i)).onPushMessage(msg);
			}
		}
	}
}
