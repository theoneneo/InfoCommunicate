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
import android.widget.RelativeLayout;
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
import com.neo.tools.RingTong;

import de.greenrobot.event.EventBus;

public class ChatRoomFragment extends BaseFragment {
	private static String sender_id = null;
	private static String nick_name = null;
	private boolean isNewChat = false;
	private EditText editMsg;
	private ListView list;
	private MsgAdapter msgAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this, BroadCastEvent.class,
				ServiceEvent.class);
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
				MessageManager.getInstance().mCurChatRoom = MessageManager
						.getInstance().getChatRoomInfos().get(i);
				return;
			}
		}
		ChatRoomInfo chatRoom = new ChatRoomInfo(sender_id, null);
		MessageManager.getInstance().getChatRoomInfos().add(chatRoom);

		MessageManager.getInstance().mCurChatRoom = chatRoom;
	}

	@Override
	public void onDestroy() {
		if (MessageManager.getInstance().mCurChatRoom.msg_infos.size() == 0)
			MessageManager.getInstance().getChatRoomInfos()
					.remove(MessageManager.getInstance().mCurChatRoom);
		MessageManager.getInstance().mCurChatRoom = null;
		EventBus.getDefault().unregister(this, BroadCastEvent.class,
				ServiceEvent.class);
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

	public void onEventMainThread(BroadCastEvent event) {
		switch (event.getType()) {
		case BroadCastEvent.NEW_MESSAGE_EVENT:
			msgAdapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}

	private void onEventMainThread(ServiceEvent event) {
		switch (event.getType()) {
		case ServiceEvent.SERVICE_SEND_PUSH_MESSAGE_EVENT:
			destroyProgressBar();
			if ("fail".equals(event.getObject())) {
				Toast.makeText(getActivity(), "发送失败", Toast.LENGTH_SHORT)
						.show();
			} else {
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
			String sender_id = MessageManager.getInstance().mCurChatRoom.msg_infos
					.get(position).sender_id;
			String sender_nick = MessageManager.getInstance().mCurChatRoom.msg_infos
					.get(position).sender_nick;
			if (convertView == null) {
				convertView = (View) inflater.inflate(R.layout.item_msg,
						parent, false);

				holder = new ViewHolder();
				holder.msgText = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (InfoCommApp.user_id.equals(sender_id)) {
				holder.msgText
						.setText(MessageManager.getInstance().mCurChatRoom.msg_infos
								.get(position).message + " : " + sender_nick);
			} else {
				holder.msgText.setText(sender_nick
						+ " : "
						+ MessageManager.getInstance().mCurChatRoom.msg_infos
								.get(position).message);
			}
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.msgText
					.getLayoutParams();
			if (InfoCommApp.user_id.equals(sender_id)) {
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			} else {
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			}
			holder.msgText.setLayoutParams(params); // 使layout更新

			return convertView;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return MessageManager.getInstance().mCurChatRoom.msg_infos.size();
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
