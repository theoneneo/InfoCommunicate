package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.data.SendMessageInfo;
import com.neo.infocommunicate.db.DBTools;
import com.neo.infocommunicate.event.BroadCastEvent;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.neo.tools.DateUtil;

import de.greenrobot.event.EventBus;

import android.content.Intent;
import android.database.Cursor;

public class MessageManager extends BaseManager {
	private static MessageManager mInstance;
	private static ArrayList<MessageInfo> mMessageInfos = new ArrayList<MessageInfo>();
	private static ArrayList<SendMessageInfo> mSendMessageInfos = new ArrayList<SendMessageInfo>();

	private MessageManager(InfoCommApp app) {
		super(app);
		initManager();
	}

	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		startService();
		getMessageInfosFromDB();
		getSendMessageInfosFromDB();
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

	public ArrayList<MessageInfo> getMessageInfos() {
		return mMessageInfos;
	}

	public ArrayList<SendMessageInfo> getSendMessageInfos() {
		return mSendMessageInfos;
	}

	private void getMessageInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllMessage();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					String info = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("string")));
					ProtocolDataInput input = new ProtocolDataInput();
					MessageInfo messageInfo = null;
					try {
						messageInfo = input.parseInfoFromJSON(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c.moveToNext();
					}
					mMessageInfos.add(messageInfo);
					c.moveToNext();
				}
				c.close();
				EventBus.getDefault().post(
						new BroadCastEvent(BroadCastEvent.LOAD_MESSAGE_EVENT));
			}
		};
		thread.start();
	}

	public void addMessageInfo(String info) {
		MessageInfo messageInfo = null;
		try {
			messageInfo = ProtocolDataInput.parseInfoFromJSON(info);
			if (messageInfo == null)
				return;
			messageInfo.show_time = DateUtil.formatUnixTime(messageInfo.time);
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

	// ======================SEND MESSAGE===============

	private void getSendMessageInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllSendMessage();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					String info = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("string")));
					ProtocolDataInput input = new ProtocolDataInput();
					SendMessageInfo messageInfo = null;
					try {
						messageInfo = input.parseSendPushResultFromJSON(info);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						c.moveToNext();
					}
					mSendMessageInfos.add(messageInfo);
					c.moveToNext();
				}
				c.close();
				EventBus.getDefault().post(
						new BroadCastEvent(
								BroadCastEvent.LOAD_SEND_MESSAGE_EVENT));
			}
		};
		thread.start();
	}

	public void addSendMessageInfo(String info, String key) {
		SendMessageInfo messageInfo = null;
		try {
			messageInfo = ProtocolDataInput.parseSendPushResultFromJSON(info);
			if (messageInfo == null)
				return;
			messageInfo.info.key = key;
			messageInfo.info.show_time = DateUtil
					.formatUnixTime(messageInfo.info.time);
			mSendMessageInfos.add(messageInfo);
			DBTools.instance()
					.insertSendMessageData(messageInfo.info.key, info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception ee) {

		}
	}

}
