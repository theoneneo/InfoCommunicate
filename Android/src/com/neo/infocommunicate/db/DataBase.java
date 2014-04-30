package com.neo.infocommunicate.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {   
    public static final class MESSAGE_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.infocommunicate.db.provider/message_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String MESSAGE = "message";

	public static final String CREATE_TABLE = "CREATE TABLE message_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, message TEXT);";
    }
    
    public static final class USER_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.infocommunicate.db.provider/user_data");
	public static final String _ID = "_id";
	public static final String USER_ID = "user_id";

	public static final String CREATE_TABLE = "CREATE TABLE user_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", user_id TEXT);";
    }
}
