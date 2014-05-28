package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.data.NoticeInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class NotificationListFragment extends BaseListFragment {
	private MessageAdapter adapter;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUI();
	}

	private void initUI() {
		adapter = new MessageAdapter(getActivity());
		setListAdapter(adapter);
		getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Bundle b = new Bundle();
				b.putString("key", MessageManager.getInstance()
						.getNoticeInfos().get(arg2).key);
				MyFragmentManager.getInstance().replaceFragment(
						R.id.content_frame, new EditNoticeFragment(),
						MyFragmentManager.PROCESS_MAIN,
						MyFragmentManager.FRAGMENT_EDIT_NOTICE, b);
			}
		});
		adapter.notifyDataSetChanged();
	}

	public void updateAdapter() {
		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	public class MessageAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public MessageAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			MessageViewHolder holder;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_notice,
						parent, false);
				holder = new MessageViewHolder();
				holder.row_name = (TextView) convertView
						.findViewById(R.id.row_name);
				holder.row_time = (TextView) convertView
						.findViewById(R.id.row_time);
				holder.row_place = (TextView) convertView
						.findViewById(R.id.row_place);
				holder.row_switch = (CheckBox) convertView
						.findViewById(R.id.row_switch);
				convertView.setTag(holder);
			} else {
				holder = (MessageViewHolder) convertView.getTag();
			}

			NoticeInfo messageInfo = MessageManager.getInstance()
					.getNoticeInfos().get(position);
			holder.row_name.setText(messageInfo.title);
			holder.row_time.setText(messageInfo.show_time);
			holder.row_place.setText(messageInfo.place);
			holder.row_switch.setOnCheckedChangeListener(null);
			if (MessageManager.getInstance().getNoticeInfos().get(position).is_remind == 1)
				holder.row_switch.setChecked(true);
			else
				holder.row_switch.setChecked(false);
			holder.row_switch
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
						}
					});

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MessageManager.getInstance().getNoticeInfos().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	static class MessageViewHolder {
		TextView row_name;
		TextView row_time;
		TextView row_place;
		CheckBox row_switch;
	}
}
