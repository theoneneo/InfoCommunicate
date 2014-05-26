package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.NoticeInfo;
import com.neo.infocommunicate.data.SendNoticeInfo;
import com.neo.infocommunicate.db.DBTools;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.neo.tools.DateUtil;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.database.Cursor;

public class MessageManager extends BaseManager {
	private static MessageManager mInstance;
	private static ArrayList<NoticeInfo> mNoticeInfos = new ArrayList<NoticeInfo>();
	private static ArrayList<SendNoticeInfo> mSendNoticeInfos = new ArrayList<SendNoticeInfo>();

	private static ArrayList<MessageInfo> mMessageInfos = new ArrayList<MessageInfo>();
	private static ArrayList<ArrayList<MessageInfo>> mGroupMsgInfos = new ArrayList<ArrayList<MessageInfo>>();

	private MessageManager(InfoCommApp app) {
		super(app);
		initManager();
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		startService();
//		getNoticeInfosFromDB();
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

	// ======================NOTICE======================
	private void getNoticeInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllNotice();
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

	public void addNoticeInfo(String info) {
		NoticeInfo noticeInfo = null;
		try {
			noticeInfo = ProtocolDataInput.parsePushNoticeFromJSON(info);
			if (noticeInfo == null)
				return;
			noticeInfo.show_time = DateUtil.formatUnixTime(System
					.currentTimeMillis());
			mNoticeInfos.add(noticeInfo);
			DBTools.instance().insertNoticeData(noticeInfo.key, info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
	}

	public void deleteNoticeInfo(String key) {
		for (int i = 0; i < mNoticeInfos.size(); i++) {
			if (mNoticeInfos.get(i).key.equals(key)) {
				mNoticeInfos.remove(i);
				DBTools.instance().deleteNoticeData(key);
				return;
			}
		}
	}

	// ======================SEND NOTICE======================
	private void getSendNoticeInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllSendNotice();
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
			DBTools.instance().insertSendNoticeData(noticeInfo.info.key, info);
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
				DBTools.instance().deleteSendNoticeData(key);
				return;
			}
		}
	}

	// ======================MESSAGE======================
	private void getMessageInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllMessage();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					String info = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("message")));
					MessageInfo messageInfo = null;
					try {
						messageInfo = ProtocolDataInput
								.parsePushMessageFromJSON(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c.moveToNext();
					}
					mMessageInfos.add(messageInfo);
					c.moveToNext();
				}
				c.close();
				getGroupMsgInfos();
				EventBus.getDefault().post(
						new BroadCastEvent(BroadCastEvent.LOAD_MESSAGE_EVENT,
								null));
			}
		};
		thread.start();
	}

	public void addMessageInfo(String info) {
		MessageInfo messageInfo = null;
		try {
			messageInfo = ProtocolDataInput.parsePushMessageFromJSON(info);
			if (messageInfo == null)
				return;
			messageInfo.show_time = DateUtil.formatUnixTime(System
					.currentTimeMillis());
			mMessageInfos.add(messageInfo);
			DBTools.instance().insertMessageData(messageInfo.key, info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
	}

	public void deleteMessageInfo(String key) {
		for (int i = 0; i < mMessageInfos.size(); i++) {
			if (mMessageInfos.get(i).key.equals(key)) {
				mMessageInfos.remove(i);
				DBTools.instance().deleteMessageData(key);
				return;
			}
		}
	}
	
	private void getGroupMsgInfos(){
		
	}
}
