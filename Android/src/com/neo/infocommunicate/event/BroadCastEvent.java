package com.neo.infocommunicate.event;

public class BroadCastEvent {
	public final static int BASE_EVENT = 0;

	public final static int LOAD_NOTICE_EVENT = BASE_EVENT + 1;
	public final static int LOAD_SEND_NOTICE_EVENT = BASE_EVENT + 2;
	public final static int LOAD_MESSAGE_EVENT = BASE_EVENT + 3;
	public final static int LOAD_SEND_MESSAGE_EVENT = BASE_EVENT + 4;

	public final static int NEW_NOTICE_EVENT = BASE_EVENT + 5;
	public final static int NEW_MESSAGE_EVENT = BASE_EVENT + 6;
	public final static int NEW_FRIEND_EVENT = BASE_EVENT + 7;
	
	public final static int CHANGE_PROMPT_EVENT = BASE_EVENT + 8;

	private int type;
	private Object obj;

	public BroadCastEvent(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getObject() {
		return obj;
	}
}
