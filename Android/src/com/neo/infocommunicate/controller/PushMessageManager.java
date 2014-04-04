package com.neo.infocommunicate.controller;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.listener.PushListenerAbility;
import com.neo.infocommunicate.protocol.ProtocolDataOutput;
import com.neo.infocommunicate.task.SendPushMessageTask;
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

	public void sendPushMessage(String[] ids, String title,
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

		PostJsonTask mTask = new PostJsonTask();
		mTask.execute("http://infocomm.duapp.com/sendpushmessage.py", msg);
	}
}
