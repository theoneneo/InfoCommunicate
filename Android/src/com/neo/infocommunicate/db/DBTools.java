package com.neo.infocommunicate.db;

import com.neo.infocommunicate.db.DataBase.MESSAGE_DATA_DB;
import com.neo.infocommunicate.db.DataBase.SEND_MESSAGE_DATA_DB;
import com.neo.infocommunicate.db.DataBase.USER_DATA_DB;

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

	public void closeDB() {
		DBContentProvider.closeDB();
	}

	public static Cursor getAllMessage() {
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

	public void insertMessageData(String key, String string) {
		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("string", toValidRs(string));
		mContext.getContentResolver().insert(MESSAGE_DATA_DB.CONTENT_URI, value);
	}

	public void updateMessageData(String key, String string) {
		String selection = MESSAGE_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		ContentValues value = new ContentValues();
		if (string != null)
			value.put("string", toValidRs(string));
		mContext.getContentResolver().update(MESSAGE_DATA_DB.CONTENT_URI, value,
				selection, null);
	}

	public void deleteMessageData(String key) {
		String selection = MESSAGE_DATA_DB.KEY + "='" + toValidRs(key) + "'";
		mContext.getContentResolver().delete(MESSAGE_DATA_DB.CONTENT_URI,
				selection, null);
	}
	
	public static Cursor getAllUser() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					USER_DATA_DB.CONTENT_URI, null, null, null, null);
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
	
	public void insertUserData(String id) {
		ContentValues value = new ContentValues();
		value.put("user_id", toValidRs(id));
		mContext.getContentResolver().insert(USER_DATA_DB.CONTENT_URI, value);
	}
	
	public void deleteUserData(String id) {
		String selection = USER_DATA_DB.USER_ID + "='" + toValidRs(id) + "'";
		mContext.getContentResolver().delete(USER_DATA_DB.CONTENT_URI,
				selection, null);
	}
	
	public static Cursor getAllSendMessage() {
		Cursor cursor = null;
		try {
			cursor = mContext.getContentResolver().query(
					SEND_MESSAGE_DATA_DB.CONTENT_URI, null, null, null, null);
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
	
	public void insertSendMessageData(String key, String string) {
		ContentValues value = new ContentValues();
		value.put("key", toValidRs(key));
		value.put("string", toValidRs(string));
		mContext.getContentResolver().insert(SEND_MESSAGE_DATA_DB.CONTENT_URI, value);
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
