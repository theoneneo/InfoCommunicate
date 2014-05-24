package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MyFragmentManager;
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

public class SetNickFragment extends BaseFragment {
	private String nick_name = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ServiceEvent.class);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class);
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_set_nick, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView title = (TextView) v.findViewById(R.id.title).findViewById(
				R.id.title_text);
		title.setText("设置使用的昵称");
		final EditText editId = (EditText) v.findViewById(R.id.edit_id);
		Button btnLogin = (Button) v.findViewById(R.id.btn_login);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editId.getText().toString().trim().length() == 0) {
					Toast.makeText(getActivity(), "请输入需要注册的昵称",
							Toast.LENGTH_SHORT).show();
				} else {
					nick_name = editId.getText().toString();
					setNickName(nick_name);
				}
			}
		});
	}

	private void setNickName(String nick) {
		createProgressBar("设置中...");
		ServiceManager.getInstance().setNickName(InfoCommApp.user_id, nick);
	}

	private void onSetNick(String result) {
		// TODO Auto-generated method stub
		destroyProgressBar();
		String text = "注册失败";
		if ("success".equals(result)) {
			text = "昵称注册成功";
			SharedPreferences mSharedPreferences = mContext
					.getSharedPreferences("SharedPreferences", 0);
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean("server_id", true);// 注册成功了
			editor.putString("nick_name", nick_name);
			editor.commit();

			InfoCommApp.setNickName(nick_name);
			go2MainActivity();
		} else if ("other_one".equals(result)) {
			text = "昵称已被注册";
		} else if ("fail".equals(result)) {
			text = "昵称注册失败";
		} else if ("no_register_id".equals(result)) {
			text = "没有注册用户名";
		}
		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	}

	private void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_SETNICK_EVENT:
			onSetNick((String) event.getObject());
			break;
		default:
			break;
		}
	}

	private void go2MainActivity() {
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
		getActivity().finish();
	}
}
