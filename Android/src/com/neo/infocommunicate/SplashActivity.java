package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;
import com.tencent.android.tpush.XGPushBaseReceiver;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
		EventBus.getDefault().unregister(this, ServiceEvent.class, XgRegisterEvent.class);
		super.onDestroy();
	}

	private void init() {
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		// user_id 本地用户名
		user_id = mSharedPreferences.getString("user_id", null);
		if (user_id == null) {
			TextView textId = (TextView) findViewById(R.id.text_id);
			final EditText editId = (EditText) findViewById(R.id.edit_id);
			Button btnRegister = (Button) findViewById(R.id.btn_register);
			btnRegister.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					user_id = editId.getText().toString();
					register(user_id);
				}
			});
			Button btnLogin = (Button) findViewById(R.id.btn_login);
			btnLogin.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					user_id = editId.getText().toString();
					login(user_id);
				}
			});
		} else {
			login(user_id);
		}
	}

	private void register(String id) {
		ServiceManager.getInstance().regsiterUserId(id);
	}

	private void login(String id) {
		ServiceManager.getInstance().loginUserId(id);
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	public void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_REGIST_EVENT:
			onRegister(event.getResult());
			break;
		case ServiceEvent.SERVICE_LOGIN_EVENT:
			onLogin(event.getResult());
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(XgRegisterEvent event) {
		String text = null;
		if (event.getRegisterMessage() != null) {
			if (event.getErrorCode() == XGPushBaseReceiver.SUCCESS) {
				SharedPreferences mSharedPreferences = getSharedPreferences(
						"SharedPreferences", 0);
				Editor editor = mSharedPreferences.edit();
				editor.putBoolean("server_id", true);
				editor.putString("user_id", event.getRegisterMessage()
						.getAccount());
				editor.commit();
				text = "登录成功";
				InfoCommApp.setUserId(event.getRegisterMessage().getAccount());
				go2MainActivity();
			} else {
				text = event.getRegisterMessage() + "登录失败，错误码："
						+ event.getErrorCode();
			}
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void onRegister(String result) {
		// TODO Auto-generated method stub
		String text = "注册失败";
		if ("success".equals(result)) {
			startPush(user_id);
		} else if ("other_one".equals(result)) {
			text = "账号已被注册";
		} else if ("fail".equals(result)) {
			text = "注册失败";
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void onLogin(String result) {
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

	public void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
