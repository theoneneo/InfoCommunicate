package com.neo.infocommunicate;

import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.listener.PushListener;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushRegisterResult;

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

public class SplashActivity extends FragmentActivity implements PushListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		init();
	}

	protected void onDestroy() {
		PushMessageManager.getInstance().getPushListenerAbility().removeListener(this);
		super.onDestroy();
	}

	private void init() {
		PushMessageManager.getInstance().getPushListenerAbility().addListener(this);
		SharedPreferences mSharedPreferences = getSharedPreferences(
				"SharedPreferences", 0);
		String user_id = mSharedPreferences.getString("user_id", null);
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
					startPush(editId.getText().toString());
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
		String text;
		if (errorCode == XGPushBaseReceiver.SUCCESS) {
			SharedPreferences mSharedPreferences = getSharedPreferences(
					"SharedPreferences", 0);
			Editor editor = mSharedPreferences.edit();
			editor.putString("user_id", result.getAccount());
			editor.commit();
			text = result + "登陆成功";
			go2MainActivity();
		} else {
			text = result + "登陆失败，错误码：" + errorCode;
		}
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	private void go2MainActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}
}
