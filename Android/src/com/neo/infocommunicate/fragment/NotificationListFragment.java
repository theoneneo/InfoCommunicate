package com.neo.infocommunicate.fragment;

import java.util.Calendar;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.db.DBTools;

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
						R.id.content_frame, new ShowNoticeFragment(),
						MyFragmentManager.PROCESS_MAIN,
						MyFragmentManager.FRAGMENT_SHOW_NOTICE, b);
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

			final NoticeInfo messageInfo = MessageManager.getInstance()
					.getNoticeInfos().get(position);
			holder.row_name.setText(messageInfo.title);
			
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(messageInfo.time);
			StringBuffer buf = new StringBuffer();
			buf.append(c.get(Calendar.YEAR));
			buf.append("-");
			String month = null;
			if(c.get(Calendar.MONTH) < 10)
				month = "0"+ (c.get(Calendar.MONTH)+1);
			else
				month = String.valueOf(c.get(Calendar.MONTH)+1);
			buf.append(month);
			buf.append("-");
			
			String day = null;
			if(c.get(Calendar.DAY_OF_MONTH) < 10)
				day = "0"+ c.get(Calendar.DAY_OF_MONTH);
			else
				day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
			
			buf.append(day);
			buf.append(" ");
			
			String hour = null;
			if(c.get(Calendar.HOUR_OF_DAY) < 10)
				hour = "0"+ c.get(Calendar.HOUR_OF_DAY);
			else
				hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
			
			buf.append(hour);
			buf.append(":");
			String time = null;
			if(c.get(Calendar.MINUTE) < 10)
				time = "0"+ c.get(Calendar.MINUTE);
			else
				time = String.valueOf(c.get(Calendar.MINUTE));
			buf.append(time);

			holder.row_time.setText(buf.toString());
			holder.row_place.setText(messageInfo.place);
			holder.row_switch.setOnCheckedChangeListener(null);
			if (MessageManager.getInstance().getNoticeInfos().get(position).prompt == 1)
				holder.row_switch.setChecked(true);
			else
				holder.row_switch.setChecked(false);
			holder.row_switch
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton arg0,
								boolean arg1) {
							// TODO Auto-generated method stub
							if (arg1) {
								messageInfo.prompt = 1;
								MessageManager.getInstance().alarm(messageInfo.key, messageInfo.time);
							} else {
								messageInfo.prompt = 0;
								MessageManager.getInstance().cancel(messageInfo.time);
							}
							DBTools.instance(mContext).changeNoticePrompt(
									messageInfo.key, messageInfo.prompt);
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
