package com.neo.infocommunicate.controller;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.SendPushMessageTask;
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

    public static void startPush(String account) {
	XGPushManager.registerPush(mApp.getApplicationContext(), "test");
    }

    public static void stopPush() {
	XGPushManager.unregisterPush(mApp.getApplicationContext());
    }

    public static void sendPushMessage(String[] ids, String title,
	    String message, String place, String link, String time) {
	String msg = null;
	ProtocolDataOutput output = new ProtocolDataOutput();
	try {
	    msg = output.sendPushMessageToJSON(ids, title, message, place,
		    link, time);
	} catch (JSONException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (msg == null) {
	    return;
	}

	SendPushMessageTask mTask = new SendPushMessageTask();
	mTask.execute("http://infocomm.duapp.com/sendpushmessage.py", msg);
    }
}
