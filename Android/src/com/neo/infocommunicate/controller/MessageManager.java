package com.neo.infocommunicate.controller;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.data.ChatRoomInfo;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.data.SendNoticeInfo;
import com.neo.infocommunicate.db.DBTools;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.neo.tools.DateUtil;

import de.greenrobot.event.EventBus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class MessageManager extends BaseManager {
	private static MessageManager mInstance;
	private static ArrayList<NoticeInfo> mNoticeInfos = new ArrayList<NoticeInfo>();
	private static ArrayList<SendNoticeInfo> mSendNoticeInfos = new ArrayList<SendNoticeInfo>();

	private static ArrayList<MessageInfo> mMessageInfos = new ArrayList<MessageInfo>();
	private static ArrayList<ChatRoomInfo> mChatRoomInfos = new ArrayList<ChatRoomInfo>();
	public ChatRoomInfo mCurChatRoom;

	private MessageManager(InfoCommApp app) {
		super(app);
		initManager();
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		startService();
		getNoticeInfosFromDB();
	}

	@Override
	public void DestroyManager() {
		// TODO Auto-generated method stub
		stopService();
	}

	public static MessageManager getInstance() {
		MessageManager instance;
		if (mInstance == null) {
			synchronized (MessageManager.class) {
				if (mInstance == null) {
					instance = new MessageManager(InfoCommApp.getApplication());
					mInstance = instance;
				}
			}
		}
		return mInstance;
	}

	public static void setNullInstance() {
		mInstance = null;
	}

	public void startService() {
		Intent i = new Intent(mContext, MessageService.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startService(i);
	}

	public void stopService() {
		Intent i = new Intent(mContext, MessageService.class);
		mContext.stopService(i);
	}

	public ArrayList<NoticeInfo> getNoticeInfos() {
		return mNoticeInfos;
	}

	public ArrayList<SendNoticeInfo> getSendNoticeInfos() {
		return mSendNoticeInfos;
	}

	public ArrayList<ChatRoomInfo> getChatRoomInfos() {
		return mChatRoomInfos;
	}

	// ======================NOTICE======================
	private void getNoticeInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.instance(mContext).getAllNotice();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					String info = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("message")));
					NoticeInfo noticeInfo = null;
					try {
						noticeInfo = ProtocolDataInput
								.parsePushNoticeFromJSON(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c.moveToNext();
					}
					noticeInfo.prompt = c.getInt(c.getColumnIndex("prompt"));
					mNoticeInfos.add(noticeInfo);
					c.moveToNext();
				}
				c.close();
				EventBus.getDefault().post(
						new BroadCastEvent(BroadCastEvent.LOAD_NOTICE_EVENT,
								null));
			}
		};
		thread.start();
	}

	public NoticeInfo addNoticeInfo(String info) {
		NoticeInfo noticeInfo = null;
		try {
			noticeInfo = ProtocolDataInput.parsePushNoticeFromJSON(info);
			if (noticeInfo == null)
				return null;
			noticeInfo.prompt = 1;
			noticeInfo.show_time = DateUtil.formatUnixTime(System
					.currentTimeMillis());
			mNoticeInfos.add(noticeInfo);
			DBTools.instance(mContext).insertNoticeData(noticeInfo.key, info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
		alarm(noticeInfo.key, noticeInfo.time);
		return noticeInfo;
	}

	public void alarm(String key, long time) {
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		intent.putExtra("key", key);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
				(int)time, intent, 0);
		AlarmManager am;
		/* 获取闹钟管理的实例 */
		am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		/* 设置闹钟 */
		am.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
	}

	public void cancel(long time) {
		Intent intent = new Intent(mContext, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
				(int)time, intent, 0);
		AlarmManager am;
		/* 获取闹钟管理的实例 */
		am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		/* 取消 */
		am.cancel(pendingIntent);
	}

	public void deleteNoticeInfo(String key) {
		for (int i = 0; i < mNoticeInfos.size(); i++) {
			if (mNoticeInfos.get(i).key.equals(key)) {
				mNoticeInfos.remove(i);
				DBTools.instance(mContext).deleteNoticeData(key);
				return;
			}
		}
	}

	// ======================SEND NOTICE======================
	private void getSendNoticeInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.instance(mContext).getAllSendNotice();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					String info = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("message")));
					SendNoticeInfo noticeInfo = null;
					try {
						noticeInfo = ProtocolDataInput
								.parseSendPushNoticeFromJSON(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c.moveToNext();
					}
					mSendNoticeInfos.add(noticeInfo);
					c.moveToNext();
				}
				c.close();
				EventBus.getDefault().post(
						new BroadCastEvent(
								BroadCastEvent.LOAD_SEND_NOTICE_EVENT, null));
			}
		};
		thread.start();
	}

	public void addSendNoticeInfo(String info, String key) {
		SendNoticeInfo noticeInfo = null;
		try {
			noticeInfo = ProtocolDataInput.parseSendPushNoticeFromJSON(info);
			if (noticeInfo == null)
				return;
			noticeInfo.info.key = key;
			noticeInfo.info.show_time = DateUtil.formatUnixTime(System
					.currentTimeMillis());
			mSendNoticeInfos.add(noticeInfo);
			DBTools.instance(mContext).insertSendNoticeData(
					noticeInfo.info.key, info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
	}

	public void deleteSendNoticeInfo(String key) {
		for (int i = 0; i < mSendNoticeInfos.size(); i++) {
			if (mSendNoticeInfos.get(i).info.key.equals(key)) {
				mSendNoticeInfos.remove(i);
				DBTools.instance(mContext).deleteSendNoticeData(key);
				return;
			}
		}
	}

	// ======================MESSAGE======================
	// private void getMessageInfosFromDB() {
	// Thread thread = new Thread() {
	// public void run() {
	// Cursor c = DBTools.getAllMessage();
	// if (c == null)
	// return;
	// for (int i = 0; i < c.getCount(); i++) {
	// String info = DBTools.getUnvalidFormRs(c.getString(c
	// .getColumnIndex("message")));
	// MessageInfo messageInfo = null;
	// try {
	// messageInfo = ProtocolDataInput
	// .parsePushMessageFromJSON(info);
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// c.moveToNext();
	// }
	// mMessageInfos.add(messageInfo);
	// c.moveToNext();
	// }
	// c.close();
	// EventBus.getDefault().post(
	// new BroadCastEvent(BroadCastEvent.LOAD_MESSAGE_EVENT,
	// null));
	// }
	// };
	// thread.start();
	// }
	// 我发送的信息
	public void addMessageInfo(MessageInfo messageInfo) {
		messageInfo.show_time = DateUtil.formatUnixTime(System
				.currentTimeMillis());
		mMessageInfos.add(messageInfo);
		for (int i = 0; i < mChatRoomInfos.size(); i++) {
			if (messageInfo.receiver_id.equals(mChatRoomInfos.get(i).sender_id)) {
				mChatRoomInfos.get(i).msg_infos.add(messageInfo);
				return;
			}
		}

		ChatRoomInfo chat = new ChatRoomInfo(messageInfo.receiver_id,
				messageInfo);
		mChatRoomInfos.add(chat);
		// DBTools.instance(mContext).insertMessageData(messageInfo.key, info);
	}

	// 收取的信息
	public MessageInfo addMessageInfo(String info) {
		MessageInfo messageInfo = null;
		try {
			messageInfo = ProtocolDataInput.parsePushMessageFromJSON(info);
			if (messageInfo == null)
				return null;
			messageInfo.show_time = DateUtil.formatUnixTime(System
					.currentTimeMillis());
			mMessageInfos.add(messageInfo);
			for (int i = 0; i < mChatRoomInfos.size(); i++) {
				if (messageInfo.sender_id
						.equals(mChatRoomInfos.get(i).sender_id)) {
					mChatRoomInfos.get(i).msg_infos.add(messageInfo);
					return messageInfo;
				}
			}
			ChatRoomInfo chat = new ChatRoomInfo(messageInfo.sender_id,
					messageInfo);
			mChatRoomInfos.add(chat);
			// DBTools.instance(mContext).insertMessageData(messageInfo.key,
			// info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
		return messageInfo;
	}

	// public void deleteMessageInfo(String key) {
	// for (int i = 0; i < mMessageInfos.size(); i++) {
	// if (mMessageInfos.get(i).key.equals(key)) {
	// mMessageInfos.remove(i);
	// // DBTools.instance(mContext).deleteMessageData(key);
	// return;
	// }
	// }
	// }
}
