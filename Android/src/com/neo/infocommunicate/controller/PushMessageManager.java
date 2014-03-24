package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.InfoCommApp;
import com.tencent.android.tpush.XGPushManager;

public class PushMessageManager {
    private static InfoCommApp mApp;
    private static PushMessageManager mInstance;
    public String appid, userid, channelid;

    private PushMessageManager(InfoCommApp app) {
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
	XGPushManager.registerPush(mApp.getApplicationContext());
    }

    public static void stopPush() {

    }
}
