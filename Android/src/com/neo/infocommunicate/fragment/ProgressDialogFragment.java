package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressDialogFragment extends DialogFragment {
	private TextView tvMsg;
	private String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dlg);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_progress,
				container, false);
		initView(contentView);
		return contentView;
	}

	private void initView(View contentView) {
		tvMsg = (TextView) contentView.findViewById(R.id.msg);
		if (message != null && !message.equals("")) {
			tvMsg.setText(message);
		}
	}

	public void setMessage(String msg) {
		if (msg != null && !msg.equals("")) {
			message = msg;
		}
	}

	@Override
	public void show(FragmentManager manager, String tag) {
		// TODO Auto-generated method stub
		super.show(manager, tag);
	}

	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
	}

}
