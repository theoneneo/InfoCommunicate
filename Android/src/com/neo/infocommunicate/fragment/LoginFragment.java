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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends BaseFragment {
	private String user_id = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ServiceEvent.class,
				XgRegisterEvent.class);
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
		View v = mInflater.inflate(R.layout.fragment_login, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView title = (TextView) v.findViewById(R.id.title).findViewById(
				R.id.title_text);
		title.setText("信息通");
		final EditText editId = (EditText) v.findViewById(R.id.edit_id);
		Button btn_login = (Button) v.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editId.getText().toString().trim().length() == 0) {
					Toast.makeText(getActivity(), "请输入登录用户名",
							Toast.LENGTH_SHORT).show();
				} else { 
					user_id = editId.getText().toString();
					login(user_id);
				}
			}
		});
		
		Button btn_register = (Button) v.findViewById(R.id.btn_register);
		btn_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame,
						new RegisterFragment());
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
			}
		});
	}

	public void login(String id) {
		user_id = id;
		createProgressBar("登录中...");
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
		if ("success".equals(result)) {
			startPush(user_id);
			return;
		} else if ("fail".equals(result)) {
			text = "登录失败";
		} else if ("none".equals(result)) {
			text = "账号不存在，请确认账号";
		}
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
		destroyProgressBar();
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	private void onEventMainThread(XgRegisterEvent event) {
		String text = null;
		destroyProgressBar();
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
					fragmentTransaction.commitAllowingStateLoss();
					fragmentManager.executePendingTransactions();
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
			}
		}
	}

	private void go2MainActivity() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
}
