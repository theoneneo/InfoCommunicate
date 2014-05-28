package com.neo.infocommunicate;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Application;

import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;

public class InfoCommApp extends Application {
	private static InfoCommApp app;
	
	public static String user_id = "";
	public static String nick_name = "";

	private ArrayList<Activity> mList = new ArrayList<Activity>();

	@Override
	public void onCreate() {
		super.onCreate();
		app = this;
		MessageManager.getInstance();
	}

	public static InfoCommApp getApplication() {
		return app;
	}
	
	public void ExitApp() {
		MyFragmentManager.getInstance().DestroyManager();
		MyFragmentManager.setNullInstance();
		PersonManager.getInstance().DestroyManager();
		PersonManager.setNullInstance();
		PushMessageManager.getInstance().DestroyManager();
		PushMessageManager.setNullInstance();
		ServiceManager.getInstance().DestroyManager();
		ServiceManager.setNullInstance();
		MessageManager.getInstance().DestroyManager();
		MessageManager.setNullInstance();
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	public static void setUserId(String id){
		user_id = id;
	}
	
	public static void setNickName(String nick){
		nick_name = nick;
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
}
