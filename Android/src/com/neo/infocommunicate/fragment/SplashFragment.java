package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;
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
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class SplashFragment extends BaseFragment {
	private static String user_id;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ServiceEvent.class,
				XgRegisterEvent.class);
		SharedPreferences mSharedPreferences = getActivity()
				.getSharedPreferences("SharedPreferences", 0);
		user_id = mSharedPreferences.getString("user_id", null);
		login(user_id);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class,
				XgRegisterEvent.class);
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_splash, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
	}

	private void login(String id) {
		user_id = id;
		ServiceManager.getInstance().loginUserId(id);
	}

	private void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_LOGIN_EVENT:
			onLogin((String) event.getObject());
			break;
		default:
			break;
		}
	}
	
	private void onLogin(String result) {
		// TODO Auto-generated method stub
		String text = "登录失败";
		if ("fail".equals(result)) {
			text = "登录失败";
		} else if ("none".equals(result)) {
			text = "账号不存在，请确认账号";
		} else {
			SharedPreferences mSharedPreferences = getActivity()
					.getSharedPreferences("SharedPreferences", 0);
			Editor editor = mSharedPreferences.edit();
			editor.putString("nick_name", result);
			editor.commit();
			InfoCommApp.setNickName(result);
			startPush(user_id);
			return;
		}
		SharedPreferences mSharedPreferences = getActivity()
				.getSharedPreferences("SharedPreferences", 0);
		Editor editor = mSharedPreferences.edit();
		editor.putBoolean("server_id", false);// 注册成功了
		editor.putString("user_id", null);
		editor.putString("nick_name", null);
		editor.commit();
		android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction
				.replace(R.id.content_frame, new LoginFragment());
		fragmentTransaction.commit();
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	private void onEventMainThread(XgRegisterEvent event) {
		String text = null;
		if (event.getRegisterMessage() != null) {
			if (event.getErrorCode() == XGPushBaseReceiver.SUCCESS) {
				InfoCommApp.setUserId(event.getRegisterMessage().getAccount());

				SharedPreferences mSharedPreferences = getActivity()
						.getSharedPreferences("SharedPreferences", 0);
				String nick_name = mSharedPreferences.getString("nick_name",
						null);
				if (nick_name == null) {
					android.support.v4.app.FragmentManager fragmentManager = getActivity()
							.getSupportFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame,
							new SetNickFragment());
					fragmentTransaction.commit();
				} else {
					InfoCommApp.setUserId(event.getRegisterMessage()
							.getAccount());
					go2MainActivity();
					text = "登录成功";
					Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				text = event.getRegisterMessage() + "登录失败，错误码："
						+ event.getErrorCode();
				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();

				android.support.v4.app.FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame,
						new LoginFragment());
				fragmentTransaction.commit();
			}
		}
	}

	private void go2MainActivity() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
}
