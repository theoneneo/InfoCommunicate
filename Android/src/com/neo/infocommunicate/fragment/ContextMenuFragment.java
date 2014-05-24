package com.neo.infocommunicate.fragment;

import java.util.ArrayList;

import com.neo.infocommunicate.R;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ContextMenuFragment extends DialogFragment implements
		View.OnClickListener, OnItemClickListener {

	private GridView gridView;
	private View btnCancel;
	private String user_id;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_contextmenu,
				container, false);
		initView(contentView);
		show();
		return contentView;
	}

	public void initView(View contentView) {
		gridView = (GridView) contentView.findViewById(R.id.grid_view);
		gridView.setOnItemClickListener(this);
		btnCancel = contentView.findViewById(R.id.btn_cancel);
		btnCancel.setOnClickListener(this);
	}
	
	public void setId(String id){
		user_id = id;
	}

	public void show() {
		StringAdapter adapter = new StringAdapter();
		gridView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		if (v == btnCancel) {
			this.dismiss();
		}
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	public class StringAdapter extends BaseAdapter {
		private ArrayList<String> items = new ArrayList<String>();

		public StringAdapter() {
			items.add("发送通知");
			items.add("发送消息");
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView textView = null;
			if (convertView == null) {
				textView = new TextView(getActivity());
			}
			textView.setText(items.get(position));
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
