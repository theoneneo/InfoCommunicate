package com.neo.infocommunicate.event;

import com.tencent.android.tpush.XGPushRegisterResult;

public class XgRegisterEvent {
	private int errorCode;
	private XGPushRegisterResult registerMessage;

	public XgRegisterEvent(int errorCode, XGPushRegisterResult registerMessage) {
		this.errorCode = errorCode;
		this.registerMessage = registerMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}
	
	public XGPushRegisterResult getRegisterMessage(){
		return registerMessage;
	}
}
