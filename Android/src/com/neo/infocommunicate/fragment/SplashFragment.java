package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;

import de.greenrobot.event.EventBus;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SplashFragment extends BaseFragment {
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
		View v = mInflater.inflate(R.layout.fragment_splash, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
	}
}
