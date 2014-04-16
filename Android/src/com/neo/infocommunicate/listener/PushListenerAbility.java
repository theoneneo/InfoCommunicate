package com.neo.infocommunicate.listener;

import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

public class PushListenerAbility extends ListenerAbility {
	public void notifyLoginListener(int errorCode, XGPushRegisterResult result) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onLogin(errorCode, result);
			}
		}
	}
	
	public void notifyUnLoginListener(int errorCode) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onUnLogin(errorCode);
			}
		}
	}
	
	public void notifySetTagListener(int errorCode, String tag) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onSetTagResult(errorCode, tag);
			}
		}
	}
	
	public void notifyDeleteTagListener(int errorCode, String tag) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onDeleteTagResult(errorCode, tag);
			}
		}
	}
	
	public void notifyTextMessageListener(XGPushTextMessage msg) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onTextMessage(msg);
			}
		}
	}
	
	public void notifyNotifactionClickedResultListener(XGPushClickedResult message) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onNotifactionClickedResult(message);
			}
		}
	}
	
	public void notifyNotifactionShowedResultListener(XGPushShowedResult notifiShowedRlt) {
		synchronized (myListener) {
			for (int i = 0; i < myListener.size(); i++) {
				((PushListener) myListener.get(i)).onNotifactionShowedResult(notifiShowedRlt);
			}
		}
	}
}
