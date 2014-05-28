package com.neo.infocommunicate.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.MainActivity;
import com.neo.infocommunicate.R;
import com.neo.infocommunicate.controller.MessageManager;
import com.neo.infocommunicate.controller.PersonManager;
import com.neo.infocommunicate.controller.ServiceManager;
import com.neo.infocommunicate.data.ChatRoomInfo;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.event.ServiceEvent;

import de.greenrobot.event.EventBus;

public class ChatRoomFragment extends BaseFragment {
	private static String sender_id = null;
	private static String nick_name = null;
	private boolean isNewChat = false;
	private ChatRoomInfo chat = null;
	private EditText editMsg;
	private ListView list;
	private MsgAdapter msgAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, ServiceEvent.class);
		Bundle args = getArguments();
		if (args != null) {
			sender_id = args.getString("sender_id");
			for (int i = 0; i < PersonManager.getInstance().getReceiverList()
					.size(); i++) {
				if (sender_id.equals(PersonManager.getInstance()
						.getReceiverList().get(i).user_id)) {
					nick_name = PersonManager.getInstance().getReceiverList()
							.get(i).nick_name;
					break;
				}
			}
		}

		for (int i = 0; i < MessageManager.getInstance().getChatRoomInfos()
				.size(); i++) {
			if (sender_id.equals(MessageManager.getInstance()
					.getChatRoomInfos().get(i).sender_id)) {
				chat = MessageManager.getInstance().getChatRoomInfos().get(i);
				return;
			}
		}
		isNewChat = true;
		chat = new ChatRoomInfo(sender_id, null);
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this, ServiceEvent.class);
		super.onDestroy();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LayoutInflater mInflater = LayoutInflater.from(getActivity());
		View v = mInflater.inflate(R.layout.fragment_chat_room, null);
		initView(v);
		return v;
	}

	private void initView(View v) {
		TextView title = (TextView) v.findViewById(R.id.title).findViewById(
				R.id.title_text);
		if (nick_name == null) {
			title.setText(sender_id + "对话中");
		} else {
			title.setText(nick_name + "对话中");
		}
		editMsg = (EditText) v.findViewById(R.id.edit_msg);
		Button btnSend = (Button) v.findViewById(R.id.btn_send);
		btnSend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (editMsg.getText().toString().trim().length() == 0) {
					Toast.makeText(getActivity(), "请输入要发送的内容",
							Toast.LENGTH_SHORT).show();
				} else {
					sendMsg(editMsg.getText().toString());
				}
			}
		});

		list = (ListView) v.findViewById(R.id.list);
		msgAdapter = new MsgAdapter(getActivity());
		list.setAdapter(msgAdapter);
	}

	private void sendMsg(String msg) {
		if (isNewChat) {
			MessageManager.getInstance().getChatRoomInfos().add(chat);
			isNewChat = false;
		}
		createProgressBar("发送中...");
		ServiceManager.getInstance().sendPushMessage(InfoCommApp.user_id,
				InfoCommApp.nick_name, sender_id, msg);
		MessageInfo msgInfo = new MessageInfo();
		msgInfo.sender_id = InfoCommApp.user_id;
		msgInfo.sender_nick = InfoCommApp.nick_name;
		msgInfo.receiver_id = sender_id;
		msgInfo.message = msg;
		
		MessageManager.getInstance().addMessageInfo(msgInfo);
		msgAdapter.notifyDataSetChanged();
		EventBus.getDefault().post(
				new BroadCastEvent(BroadCastEvent.NEW_MESSAGE_EVENT, null));
	}

	private void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_SEND_PUSH_MESSAGE_EVENT:
			destroyProgressBar();
			if ("fail".equals(event.getObject())) {
				Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
						.show();
			}else{
				editMsg.setText("");
			}
			break;
		default:
			break;
		}
	}

	public class MsgAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private Context mContext;

		public MsgAdapter(Context context) {
			mContext = context;
			inflater = LayoutInflater.from(mContext);
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
			String sender_id = chat.msg_infos.get(position).sender_id;
			if (convertView == null) {
				if (InfoCommApp.user_id.equals(sender_id)) {
					convertView = (View) inflater.inflate(
							R.layout.item_msg_right, parent, false);
				} else {
					convertView = (View) inflater.inflate(
							R.layout.item_msg_left, parent, false);
				}

				holder = new ViewHolder();
				holder.msgText = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (InfoCommApp.user_id.equals(sender_id)) {
				holder.msgText.setText(chat.msg_infos.get(position).message
						+ " : " + sender_id);
			} else {
				holder.msgText.setText(sender_id + " : "
						+ chat.msg_infos.get(position).message);
			}
			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return chat.msg_infos.size();
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

	static class ViewHolder {
		TextView msgText;
	}
}
