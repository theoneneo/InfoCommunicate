package com.neo.infocommunicate.event;

public class ServiceEvent {
	public final static int BASE_EVENT = 0;
	public final static int SERVICE_REGIST_EVENT = BASE_EVENT + 1;
	public final static int SERVICE_LOGIN_EVENT = BASE_EVENT + 2;
	public final static int SERVICE_GET_USERID_EVENT = BASE_EVENT + 3;
	public final static int SERVICE_SEND_PUSH_EVENT = BASE_EVENT + 4;
	
	private int type;
	private String result;

	public ServiceEvent(int type, String result) {
		this.type = type;
		this.result = result;
	}

	public int getType() {
		return type;
	}
	
	public String getResult(){
		return result;
	}
}
