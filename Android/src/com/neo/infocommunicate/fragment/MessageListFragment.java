package com.neo.infocommunicate.fragment;

import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.MyFragmentManager;
import com.neo.infocommunicate.data.ChatRoomInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.event.ServiceEvent;
import com.neo.infocommunicate.event.XgRegisterEvent;

import de.greenrobot.event.EventBus;

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

public class MessageListFragment extends BaseListFragment {
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
				b.putString("sender_id", MessageManager.getInstance()
						.getChatRoomInfos().get(arg2).sender_id);
				MyFragmentManager.getInstance().replaceFragment(
						R.id.content_frame, new ChatRoomFragment(),
						MyFragmentManager.PROCESS_MAIN,
						MyFragmentManager.FRAGMENT_EDIT_MESSAGE, b);
			}
		});
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
				convertView = (View) inflater.inflate(R.layout.item_message,
						parent, false);
				holder = new MessageViewHolder();
				holder.row_name = (TextView) convertView
						.findViewById(R.id.row_name);
				holder.row_msg = (TextView) convertView
						.findViewById(R.id.row_msg);
				convertView.setTag(holder);
			} else {
				holder = (MessageViewHolder) convertView.getTag();
			}

			ChatRoomInfo chatInfo = MessageManager.getInstance()
					.getChatRoomInfos().get(position);
			
			if (chatInfo.msg_infos.size() > 0){
				holder.row_name.setText(chatInfo.msg_infos
						.get(chatInfo.msg_infos.size() - 1).sender_nick);
				holder.row_msg.setText(chatInfo.msg_infos
						.get(chatInfo.msg_infos.size() - 1).message);
			}
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MessageManager.getInstance().getChatRoomInfos().size();
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
		TextView row_msg;
	}
}
