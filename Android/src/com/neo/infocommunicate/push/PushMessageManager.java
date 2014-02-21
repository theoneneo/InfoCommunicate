package com.neo.infocommunicate.push;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.neo.infocommunicate.InfoCommApp;

public class PushMessageManager {
	private static InfoCommApp mApp;
	private static PushMessageManager mInstance;
	public String appid, userid, channelid;

	public PushMessageManager(InfoCommApp app) {
		mApp = app;
	}

	public static PushMessageManager getInstance() {
		synchronized (PushMessageManager.class) {
			if (mInstance == null) {
				mInstance = new PushMessageManager(InfoCommApp.getApplication());
			}
			return mInstance;
		}
	}

	public static void startPush() {
		PushManager.startWork(InfoCommApp.getApplication(),
				PushConstants.LOGIN_TYPE_API_KEY,
				Utils.getMetaValue(InfoCommApp.getApplication(), "api_key"));
	}

	public static void stopPush() {
		PushManager.stopWork(InfoCommApp.getApplication());
	}
}
