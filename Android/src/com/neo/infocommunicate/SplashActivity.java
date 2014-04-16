package com.neo.infocommunicate;

import org.json.JSONException;

import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.listener.PushListener;
import com.neo.infocommunicate.listener.ServiceListener;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

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

public class SplashActivity extends FragmentActivity implements PushListener,
		ServiceListener {
	private String user_id = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init();
	}

	protected void onDestroy() {
		PushMessageManager.getInstance().getPushListenerAbility()
				.removeListener(this);
		ServiceManager.getInstance().getServiceListenerAbility()
				.removeListener(this);
		super.onDestroy();
	}

	private void init() {
		PushMessageManager.getInstance().getPushListenerAbility()
				.addListener(this);
		ServiceManager.getInstance().getServiceListenerAbility()
				.addListener(this);

		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		// user_id 本地用户名
		user_id = mSharedPreferences.getString("user_id", null);
		if (user_id == null) {
			TextView textId = (TextView) findViewById(R.id.text_id);
			textId.setVisibility(View.VISIBLE);
			final EditText editId = (EditText) findViewById(R.id.edit_id);
			editId.setVisibility(View.VISIBLE);
			Button btnId = (Button) findViewById(R.id.btn_id);
			btnId.setVisibility(View.VISIBLE);
			btnId.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					user_id = editId.getText().toString();
					startPush(user_id);
				}
			});
		} else {
			startPush(user_id);
		}
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	@Override
	public void onLogin(int errorCode, XGPushRegisterResult result) {
		// TODO Auto-generated method stub
		String text = null;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"SharedPreferences", 0);
			// server_id 服务器是否记住 user_id
			boolean server_id = mSharedPreferences.getBoolean("server_id",
					false);
			if (!server_id) {
				ServiceManager.getInstance().regsiterUserId(user_id);
			}
		} else {
			text = result + "登录失败，错误码：" + errorCode;
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onRegister(String result) {
		// TODO Auto-generated method stub
		String text = "登录失败";
		if ("success".equals(result)) {
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"SharedPreferences", 0);
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean("server_id", true);
			editor.putString("user_id", user_id);
			editor.commit();
			text = "登录成功";
			InfoCommApp.user_id = user_id;
			go2MainActivity();
		} else if("other_one".equals(result)){
			text = "账号已被注册";
		} else if("fail".equals(result)){
			text = "登录失败";
		}
		
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPushMessage(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetReceiverList() {
		// TODO Auto-generated method stub

	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void onUnLogin(int errorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSetTagResult(int errorCode, String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleteTagResult(int errorCode, String tag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextMessage(XGPushTextMessage msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotifactionClickedResult(XGPushClickedResult message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNotifactionShowedResult(XGPushShowedResult notifiShowedRlt) {
		// TODO Auto-generated method stub
		
	}
}
