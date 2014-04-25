package com.neo.infocommunicate.controller;

import com.neo.infocommunicate.InfoCommApp;
import com.tencent.android.tpush.XGPushManager;

public class PushMessageManager extends BaseManager{
	private static PushMessageManager mInstance;
	public String appid, userid, channelid;

	private PushMessageManager(InfoCommApp app) {
		super(app);
		initManager();
	}
	
	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void DestroyManager() {
		// TODO Auto-generated method stub
	}

	public static PushMessageManager getInstance() {
		synchronized (PushMessageManager.class) {
			if (mInstance == null) {
				mInstance = new PushMessageManager(InfoCommApp.getApplication());
			}
			return mInstance;
		}
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
