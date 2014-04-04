package com.neo.infocommunicate.listener;

import com.tencent.android.tpush.XGPushRegisterResult;

public class PushListenerAbility extends ListenerAbility {
	public void notifyLoginListener(int errorCode, XGPushRegisterResult result) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onLogin(errorCode, result);
			}
		}
	}
}
