package com.neo.infocommunicate.controller;

import com.tencent.android.tpush.XGPushTextMessage;

import de.greenrobot.event.EventBus;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MessageService extends Service {
	
	private IBinder mBinder = new MessageBinder();

	public MessageService() {
	}

	@Override
	public void onCreate() {
		super.onCreate();
		EventBus.getDefault().register(this, XGPushTextMessage.class);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, XGPushTextMessage.class);
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public class MessageBinder extends Binder {
		public MessageService getService() {
			return MessageService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		if (null == mBinder)
			mBinder = new MessageBinder();
		return mBinder;
	}
	
	public void onEventBackgroundThread(XGPushTextMessage message) {
		String customContent = message.getCustomContent();
		if (customContent != null && customContent.length() != 0) {
			MessageManager.getInstance().addNoticeInfo(customContent);
		}
	}
}
