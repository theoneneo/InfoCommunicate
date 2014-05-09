package com.neo.infocommunicate.event;

public class BroadCastEvent {
	public final static int BASE_EVENT = 0;
	public final static int LOAD_MESSAGE_EVENT = BASE_EVENT + 1;
	public final static int LOAD_SEND_MESSAGE_EVENT = BASE_EVENT + 2;

	private int type;

	public BroadCastEvent(int type) {
		this.type = type;
	}
	
	public int getType() {
		return type;
	}
}
