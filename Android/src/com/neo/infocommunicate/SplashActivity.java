package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;
import com.neo.infocommunicate.fragment.RegisterFragment;
import com.neo.infocommunicate.fragment.SplashFragment;
import com.tencent.android.tpush.XGPushBaseReceiver;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

public class SplashActivity extends FragmentActivity {
	private String user_id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		user_id = mSharedPreferences.getString("user_id", null);
		if (user_id == null) {
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame,
					new RegisterFragment());
			fragmentTransaction.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		} else {
			android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame,
					new SplashFragment());
			fragmentTransaction.commitAllowingStateLoss();
			fragmentManager.executePendingTransactions();
		}
	}


}
