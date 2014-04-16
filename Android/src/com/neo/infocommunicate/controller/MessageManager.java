package com.neo.infocommunicate.controller;

import java.util.ArrayList;

import org.json.JSONException;

import com.neo.infocommunicate.InfoCommApp;
import com.neo.infocommunicate.data.MessageInfo;
import com.neo.infocommunicate.db.DBTools;
import com.neo.infocommunicate.protocol.ProtocolDataInput;
import com.neo.tools.DateUtil;

import android.database.Cursor;

public class MessageManager extends BaseManager {
	private static MessageManager mInstance;
	private static ArrayList<MessageInfo> mMessageInfos = new ArrayList<MessageInfo>();

	private MessageManager(InfoCommApp app) {
		super(app);	
	}
	
	@Override
	protected void initManager() {
		// TODO Auto-generated method stub
		getMessageInfosFromDB();
	}

	@Override
	protected void DestroyManager() {
		// TODO Auto-generated method stub
		
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
				Cursor c = DBTools.getAllInfo();
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
				mApp.eventAction(InfoCommApp.INFO_COMM_LOAD_MESSAGEDB);
			}
		};
		thread.start();
	}

	public void addMessageInfo(String info) {
		ProtocolDataInput input = new ProtocolDataInput();
		MessageInfo messageInfo = null;
		try {
			messageInfo = input.parseInfoFromJSON(info);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		messageInfo.show_time = DateUtil.formatUnixTime(messageInfo.time);
		mMessageInfos.add(messageInfo);
		DBTools.instance().insertInfoData(messageInfo.key, info);
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
}
