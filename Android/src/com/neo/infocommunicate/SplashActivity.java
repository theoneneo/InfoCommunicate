package com.neo.infocommunicate;

import java.util.List;

import com.neo.infocommunicate.fragment.LoginFragment;
import com.neo.infocommunicate.fragment.SetNickFragment;
import com.neo.infocommunicate.fragment.SplashFragment;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager.LayoutParams;

public class SplashActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_splash);
		init();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		String user_id = mSharedPreferences.getString("user_id", null);
		String nick_name = mSharedPreferences.getString("nick_name", null);
		if (user_id == null) {
			goLoginFragment();
		} else if (user_id == "") {
			goLoginFragment();
		} else if (nick_name == null) {
			goLoginFragment();
		} else if (nick_name == "") {
			goLoginFragment();
		} else {
			if (isServiceRun(InfoCommApp.getApplication()
					.getApplicationContext())) {
				InfoCommApp.setNickName(nick_name);
				InfoCommApp.setUserId(user_id);
				go2MainActivity();
			} else {
				android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame,
						new SplashFragment());
				fragmentTransaction.commit();
			}
		}
	}

	public boolean isServiceRun(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(30);
		for (RunningServiceInfo info : list) {
			if (info.service.getClassName().equals(
					"com.tencent.android.tpush.service.XGPushService")) {
				return true;
			}
		}
		return false;
	}

	private void goLoginFragment() {
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, new LoginFragment());
		fragmentTransaction.commit();
	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
