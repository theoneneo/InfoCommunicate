package com.neo.infocommunicate.push;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.neo.infocommunicate.InfoCommApp;

public class PushMessageManager {
    private static InfoCommApp app;
    private static PushMessageManager instance;
    public String appid, userid, channelid;
    
    public PushMessageManager(InfoCommApp app) {
	this.app = app;
    }

    public static PushMessageManager getInstance() {
	synchronized (PushMessageManager.class) {
	    if (instance == null) {
		instance = new PushMessageManager(InfoCommApp.getApplication());
	    }
	    return instance;
	}
    }

    public static void startPush() {
	PushManager.startWork(InfoCommApp.getApplication(),
		PushConstants.LOGIN_TYPE_API_KEY,
		Utils.getMetaValue(InfoCommApp.getApplication(), "api_key"));
    }
    
    public static void stopPush(){
	PushManager.stopWork(InfoCommApp.getApplication());
    }
}
