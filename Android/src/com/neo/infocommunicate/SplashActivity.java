package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;
import com.neo.infocommunicate.fragment.RegisterFragment;
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
		EventBus.getDefault().register(this, ServiceEvent.class,
				XgRegisterEvent.class);
		init();
	}

	protected void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class,
				XgRegisterEvent.class);
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
			login(user_id);
		}
	}

	private void login(String id) {
		ServiceManager.getInstance().loginUserId(id);
	}

	private void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_LOGIN_EVENT:
			onLogin((String)event.getObject());
			break;
		default:
			break;
		}
	}

	private void onLogin(String result) {
		// TODO Auto-generated method stub
		String text = "登录失败";
		if ("success".equals(result)) {
			startPush(user_id);
			return;
		} else if ("fail".equals(result)) {
			text = "登录失败";
		} else if ("none".equals(result)) {
			text = "账号不存在，请确认账号";
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	private void onEventMainThread(XgRegisterEvent event) {
		String text = null;
		if (event.getRegisterMessage() != null) {
			if (event.getErrorCode() == XGPushBaseReceiver.SUCCESS) {
				SharedPreferences mSharedPreferences = getSharedPreferences(
						"SharedPreferences", 0);
				Editor editor = mSharedPreferences.edit();
				editor.putBoolean("server_id", true);// 注册成功了
				editor.putString("user_id", event.getRegisterMessage()
						.getAccount());
				editor.commit();
				text = "登录成功";
				InfoCommApp.setUserId(event.getRegisterMessage().getAccount());
				InfoCommApp.setNickName(mSharedPreferences.getString("nick_name", null));
				go2MainActivity();
			} else {
				text = event.getRegisterMessage() + "登录失败，错误码："
						+ event.getErrorCode();
			}
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
