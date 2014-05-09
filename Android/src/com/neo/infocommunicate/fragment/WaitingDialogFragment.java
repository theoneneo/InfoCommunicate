package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WaitingDialogFragment extends BaseDialogFragment {
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_wait, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		setCancelable(false);
		TextView text = (TextView)v.findViewById(R.id.text);
		text.setText("信息发送中...");
	}
}
