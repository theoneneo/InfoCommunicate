package com.neo.infocommunicate.fragment;

import java.util.ArrayList;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.fragment.NotificationListFragment.MessageViewHolder;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

public class ContextMenuFragment extends DialogFragment implements
		OnItemClickListener {

	private ListView list;
	private String user_id;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setCancelable(true);
		setStyle(DialogFragment.STYLE_NO_TITLE, 0);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fragment_contextmenu,
				container, false);
		initView(contentView);
		return contentView;
	}

	public void initView(View contentView) {
		list = (ListView) contentView.findViewById(R.id.list);
		list.setOnItemClickListener(this);
		show();
	}

	public void setId(String id) {
		user_id = id;
	}

	public void show() {
		StringAdapter adapter = new StringAdapter(getActivity());
		list.setAdapter(adapter);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
	}

	public class StringAdapter extends BaseAdapter {
		private ArrayList<String> items = new ArrayList<String>();
		private LayoutInflater inflater;
		private Context mContext;

		public StringAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
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

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_menu,
						parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(items.get(position));
			return convertView;
		}
	}

	static class ViewHolder {
		TextView text;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		switch (arg2) {
		case 0: {
			Bundle b = new Bundle();
			b.putString("sender_id", user_id);
			MyFragmentManager.getInstance().replaceFragment(R.id.content_frame,
					new EditNoticeFragment(), MyFragmentManager.PROCESS_MAIN,
					MyFragmentManager.FRAGMENT_EDIT_NOTICE, b);
		}
			break;
		case 1: {
			Bundle b = new Bundle();
			b.putString("sender_id", user_id);
			MyFragmentManager.getInstance().replaceFragment(R.id.content_frame,
					new ChatRoomFragment(), MyFragmentManager.PROCESS_MAIN,
					MyFragmentManager.FRAGMENT_EDIT_MESSAGE, b);
		}
			break;
		default:
			break;
		}
		
		this.dismiss();
	}
}
