package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.PushMessageManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;
import com.tencent.android.tpush.XGPushBaseReceiver;

import de.greenrobot.event.EventBus;

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
import android.widget.Toast;

public class RegisterFragment extends BaseFragment {
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
		View v = mInflater.inflate(R.layout.fragment_register, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		final EditText editId = (EditText) v.findViewById(R.id.edit_id);
		Button btnRegister = (Button) v.findViewById(R.id.btn_register);
		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editId.getText().toString().trim().length() == 0) {
					Toast.makeText(getActivity(), "请输入需要注册的用户名", Toast.LENGTH_SHORT).show();
				} else {
					user_id = editId.getText().toString();
					register(user_id);
				}
			}
		});
	}

	private void register(String id) {
		ServiceManager.getInstance().regsiterUserId(id);
	}

	private void startPush(String id) {
		PushMessageManager.getInstance().startPush(id);
	}

	public void onRegister(String result) {
		// TODO Auto-generated method stub
		String text = "注册失败";
		if ("success".equals(result)) {
			startPush(user_id);
			return;
		} else if ("other_one".equals(result)) {
			text = "账号已被注册";
		} else if ("fail".equals(result)) {
			text = "注册失败";
		}
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	public void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_REGIST_EVENT:
			onRegister((String)event.getObject());
			break;
		default:
			break;
		}
	}

	public void onEventMainThread(XgRegisterEvent event) {
		String text = null;
		if (event.getRegisterMessage() != null) {
			if (event.getErrorCode() == XGPushBaseReceiver.SUCCESS) {	
				SharedPreferences mSharedPreferences = mContext.getSharedPreferences(
						"SharedPreferences", 0);
				Editor editor = mSharedPreferences.edit();
				editor.putBoolean("server_id", true);// 注册成功了
				editor.putString("user_id", event.getRegisterMessage()
						.getAccount());
				editor.commit();
				text = "登录成功";
				InfoCommApp.setUserId(event.getRegisterMessage().getAccount());
				
				android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame,
						new SetNickFragment());
				fragmentTransaction.commitAllowingStateLoss();
				fragmentManager.executePendingTransactions();
			} else {
				text = event.getRegisterMessage() + "登录失败，错误码："
						+ event.getErrorCode();
				Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
			}
		}

	}
}
