package com.neo.infocommunicate;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.Message;

import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.PushMessageManager;

public class InfoCommApp extends Application {
	private static InfoCommApp app;
	private static Handler mainHandler;
	private static MessageManager mMessageManager;
	
	public static String user_id = null;
	public static final int INFO_COMM_LOAD_MESSAGEDB = 10000;

	private ArrayList<Activity> mList = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		PushMessageManager.getInstance();
		mMessageManager = MessageManager.getInstance();
	}

	public static InfoCommApp getApplication() {
		return app;
	}

	public void addActivity(Activity activity) {
		mList.add(activity);
	}

	public void exit() {
		for (Activity activity : mList) {
			activity.finish();
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void setMainHandler(Handler handler) {
		mainHandler = handler;
	}

	public void eventAction(int eventType, Object obj) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = eventType;
		msg.obj = obj;
		InfoCommHandler.sendMessage(msg);
	}

	public void eventAction(int eventType) {
		// TODO Auto-generated method stub
		Message msg = Message.obtain();
		msg.what = eventType;
		InfoCommHandler.sendMessage(msg);
	}

	private Handler InfoCommHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
}
