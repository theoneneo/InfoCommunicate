package com.neo.infocommunicate.event;

public class ServiceEvent {
	public final static int BASE_EVENT = 0;
	public final static int SERVICE_REGIST_EVENT = BASE_EVENT + 1;
	public final static int SERVICE_SETNICK_EVENT = BASE_EVENT + 2;
	public final static int SERVICE_LOGIN_EVENT = BASE_EVENT + 3;
	public final static int SERVICE_GET_USERID_EVENT = BASE_EVENT + 4;
	public final static int SERVICE_SEND_PUSH_NOTICE_EVENT = BASE_EVENT + 5;
	public final static int SERVICE_SEND_PUSH_MESSAGE_EVENT = BASE_EVENT + 6;
	
	private int type;
	private Object obj;

	public ServiceEvent(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}
	
	public Object getObject(){
		return obj;
	}
}
