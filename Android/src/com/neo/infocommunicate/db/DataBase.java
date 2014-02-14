package com.neo.infocommunicate.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {
    public static final class MESSAGE_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.infocommunicate.db.provider/message_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String NAME = "name";
	public static final String MESSAGE = "message";
	public static final String TIME = "time";
	public static final String PLACE = "place";
	public static final String LINK = "link";
	public static final String RECEIVE_TIME = "receive_time";
	public static final String IS_REMIND = "is_remind";

	public static final String CREATE_TABLE = "CREATE TABLE message_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, name TEXT, message TEXT, time TEXT, place TEXT, link TEXT, receive_time INTEGER, is_remind INTEGER);";
    }
}
