package com.neo.infocommunicate.listener;

public interface ServiceListener extends Listener {
	public void onRegister(String msg);
	public void onPushMessage(String msg);
	public void onGetReceiverList();
}
