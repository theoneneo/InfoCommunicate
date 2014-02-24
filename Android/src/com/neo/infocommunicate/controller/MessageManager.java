package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.db.DBTools;
import com.neo.tools.DateUtil;

import android.database.Cursor;

public class MessageManager {
	private InfoCommApp mApp;
	private static MessageManager mInstance;
	private static ArrayList<MessageInfo> mMessageInfos = new ArrayList<MessageInfo>();

	public MessageManager(InfoCommApp app) {
		mApp = app;
		getMessageInfosFromDB();
//		testData();
	}

	public static MessageManager getInstance() {
		synchronized (MessageManager.class) {
			if (mInstance == null) {
				mInstance = new MessageManager(InfoCommApp.getApplication());
			}
			return mInstance;
		}
	}

	public ArrayList<MessageInfo> getMessageInfos() {
		return mMessageInfos;
	}

	private void getMessageInfosFromDB() {
		Thread thread = new Thread() {
			public void run() {
				Cursor c = DBTools.getAllMessages();
				if (c == null)
					return;
				for (int i = 0; i < c.getCount(); i++) {
					MessageInfo messsageInfo = new MessageInfo();
					messsageInfo.key = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("key")));
					messsageInfo.name = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("name")));
					messsageInfo.message = DBTools.getUnvalidFormRs(c
							.getString(c.getColumnIndex("message")));
					messsageInfo.place = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("place")));
					messsageInfo.link = DBTools.getUnvalidFormRs(c.getString(c
							.getColumnIndex("link")));
					messsageInfo.time = c.getLong(c.getColumnIndex("time"));
					messsageInfo.receive_time = c.getLong(c
							.getColumnIndex("receive_time"));
					messsageInfo.is_remind = c.getInt(c
							.getColumnIndex("is_remind"));
					mMessageInfos.add(messsageInfo);
					c.moveToNext();
				}
				c.close();
				mApp.eventAction(InfoCommApp.INFO_COMM_LOAD_MESSAGEDB);
			}
		};
		thread.start();
	}

	public void addMessageInfo(String key, String name, String message,
			String place, String link, long time, long receive_time,
			int is_remind) {
		MessageInfo messageInfo = new MessageInfo();
		messageInfo.key = key;
		messageInfo.name = name;
		messageInfo.message = message;
		messageInfo.place = place;
		messageInfo.link = link;
		messageInfo.time = time;
		messageInfo.show_time = DateUtil.formatUnixTime(time);
		messageInfo.receive_time = receive_time;
		messageInfo.is_remind = is_remind;
		mMessageInfos.add(messageInfo);
		DBTools.instance().insertMessageData(key, name, message, time, place,
				link, receive_time, is_remind);
	}

	public void modifyMessageInfo(String key, String name, String message,
			String place, String link, long time, long receive_time,
			int is_remind) {
		if (key == null || key == "")
			return;
		for (int i = 0; i < mMessageInfos.size(); i++) {
			MessageInfo messageInfo = mMessageInfos.get(i);
			if (messageInfo.key.equals(key)) {	
				if(name != null)
					messageInfo.name = name;
				if(message != null)
					messageInfo.message = message;
				if(place != null)
					messageInfo.place = place;
				if(link != null)
					messageInfo.link = link;
				if(time != 0){
					messageInfo.time = time;
					messageInfo.show_time = DateUtil.formatUnixTime(time);
				}
				if(receive_time != 0)
					messageInfo.receive_time = receive_time;
				if(is_remind != -1)
					messageInfo.is_remind = is_remind;				
			}
		}
		DBTools.instance().updateMessageData(key, name, message, time, place,
				link, receive_time, is_remind);
	}

	public void deleteMessageInfo(String key) {
		for (int i = 0; i < mMessageInfos.size(); i++) {
			if (mMessageInfos.get(i).key.equals(key)) {
				mMessageInfos.remove(i);
				DBTools.instance().deleteMessageInfo(key);
				return;
			}
		}
	}

	public class MessageInfo {
		public String key;
		public String name;
		public String message;
		public String place;
		public String link;
		public String show_time;
		public long time;
		public long receive_time;
		public int is_remind;
	}
	
	private void testData(){
		for(int i = 0; i < 5; i++){
			addMessageInfo("key"+i, "name"+i, "message"+i, "place"+i, "link"+i, System.currentTimeMillis(), System.currentTimeMillis(), 1);
		}
	}
}
