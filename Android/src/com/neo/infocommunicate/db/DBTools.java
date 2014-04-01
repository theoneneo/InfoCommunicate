package com.neo.infocommunicate.db;

import com.neo.infocommunicate.db.DataBase.INFO_DATA_DB;

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

    public static Cursor getAllInfo() {
	Cursor cursor = null;
	try {
	    cursor = mContext.getContentResolver().query(
		    INFO_DATA_DB.CONTENT_URI, null, null, null, null);
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

    public static Cursor getInfo(String key) {
	String selection = INFO_DATA_DB.KEY + "='" + toValidRs(key) + "'";
	Cursor cursor = null;
	try {
	    cursor = mContext.getContentResolver().query(
		    INFO_DATA_DB.CONTENT_URI, null, selection, null, null);
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

    public void insertInfoData(String key, String string) {
	ContentValues value = new ContentValues();
	value.put("key", toValidRs(key));
	value.put("string", toValidRs(string));
	mContext.getContentResolver().insert(INFO_DATA_DB.CONTENT_URI, value);
    }

    public void updateInfoData(String key, String string) {
	String selection = INFO_DATA_DB.KEY + "='" + toValidRs(key) + "'";
	ContentValues value = new ContentValues();
	if (string != null)
	    value.put("string", toValidRs(string));
	mContext.getContentResolver().update(INFO_DATA_DB.CONTENT_URI, value,
		selection, null);
    }

    public void deleteMessageInfo(String key) {
	String selection = INFO_DATA_DB.KEY + "='" + toValidRs(key) + "'";
	mContext.getContentResolver().delete(INFO_DATA_DB.CONTENT_URI,
		selection, null);
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
