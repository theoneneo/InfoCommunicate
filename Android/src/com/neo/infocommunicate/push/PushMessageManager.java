package com.neo.infocommunicate.push;

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

	}

	public static void stopPush() {

	}
}
