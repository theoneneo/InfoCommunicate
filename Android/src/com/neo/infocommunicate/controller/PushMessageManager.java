package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.listener.PushListenerAbility;
import com.tencent.android.tpush.XGPushManager;

public class PushMessageManager {
	private static InfoCommApp mApp;
	private static PushMessageManager mInstance;
	private static PushListenerAbility pushListenerAbility;
	public String appid, userid, channelid;

	private PushMessageManager(InfoCommApp app) {
		mApp = app;
		pushListenerAbility = new PushListenerAbility();
	}

	public static PushMessageManager getInstance() {
		synchronized (PushMessageManager.class) {
			if (mInstance == null) {
				mInstance = new PushMessageManager(InfoCommApp.getApplication());
			}
			return mInstance;
		}
	}

	public PushListenerAbility getPushListenerAbility() {
		return pushListenerAbility;
	}

	public void startPush() {
		XGPushManager.registerPush(mApp.getApplicationContext());
	}

	public void startPush(String account) {
		XGPushManager.registerPush(mApp.getApplicationContext(), account);
	}

	public void stopPush() {
		XGPushManager.unregisterPush(mApp.getApplicationContext());
	}
}
