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
	public void DestroyManager() {
		// TODO Auto-generated method stub
	}

	public static PushMessageManager getInstance() {
		PushMessageManager instance;
		if (mInstance == null) {
			synchronized (PushMessageManager.class) {
				if (mInstance == null) {
					instance = new PushMessageManager(InfoCommApp.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	public static void setNullInstance() {
		mInstance = null;
	}

	public void startPush() {
		XGPushManager.registerPush(mContext);
	}

	public void startPush(String account) {
		XGPushManager.registerPush(mContext, account);
	}

	public void stopPush() {
		XGPushManager.unregisterPush(mContext);
	}

}
