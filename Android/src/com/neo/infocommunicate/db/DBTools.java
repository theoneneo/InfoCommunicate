package com.neo.infocommunicate.db;

import java.util.ArrayList;

import com.neo.infocommunicate.db.DataBase.MESSAGE_DATA_DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class DBTools {
	private static DBTools mInstance;
	private static Context mContext;

	private DBTools(Context context) {
		mContext = context;
	}

	public static DBTools instance(Context context) {
		synchronized (DBTools.class) {
			if (mInstance == null) {
				mInstance = new DBTools(context);
			}
			return mInstance;
		}
	}

	public static DBTools instance() {
		return mInstance;
	}

	public static void closeDB() {
		DBContentProvider.closeDB();
	}

	public static Cursor getAllMessages() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					MESSAGE_DATA_DB.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public static Cursor getMessage(String key) {
		String selection = MESSAGE_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					MESSAGE_DATA_DB.CONTENT_URI, null, selection, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cursor;
	}

	public void insertMessageData(String key, String name, String message,
			String time, String place, String link, long receive_time,
			int is_remind) {
		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("name", toValidRs(name));
		value.put("message", toValidRs(message));
		value.put("time", toValidRs(time));
		value.put("place", toValidRs(place));
		value.put("link", toValidRs(link));
		value.put("receive_time", receive_time);
		value.put("is_remind", is_remind);
		mContext.getContentResolver().insert(MESSAGE_DATA_DB.CONTENT_URI, value);
	}

	public void updateMessageData(String key, String name, String message,
			String time, String place, String link, long receive_time,
			int is_remind) {
		String selection = MESSAGE_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		ContentValues value = new ContentValues();
		if(name != null)
			value.put("name", toValidRs(name));
		if(message != null)
			value.put("message", toValidRs(message));
		if(time != null)
			value.put("time", toValidRs(time));
		if(place != null)	
			value.put("place", toValidRs(place));
		if(link != null)	
			value.put("link", toValidRs(link));
		if(receive_time != 0)		
			value.put("receive_time", receive_time);
		if(is_remind != -1)
			value.put("is_remind", is_remind);
		mContext.getContentResolver().update(MESSAGE_DATA_DB.CONTENT_URI,
				value, selection, null);
	}

	public static String toValidRs(String obj) {
		if (obj == null)
			return "@*@";
		else if (obj.indexOf("'") != -1) {
			return obj.replace("'", "*@*");
		} else
			return obj;
	}

	public static String getUnvalidFormRs(String obj) {
		if (obj == null)
			return null;
		else if (obj.equals("@*@"))
			return null;
		else if (obj.indexOf("*@*") != -1) {
			return obj.replace("*@*", "'");
		} else
			return obj;
	}
}
