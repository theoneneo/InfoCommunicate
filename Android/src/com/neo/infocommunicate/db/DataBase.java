package com.neo.infocommunicate.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class DataBase {   
    public static final class INFO_DATA_DB implements BaseColumns {
	public static final Uri CONTENT_URI = Uri.parse("content://com.neo.infocommunicate.db.provider/info_data");
	public static final String _ID = "_id";
	public static final String KEY = "key";
	public static final String STRING = "string";

	public static final String CREATE_TABLE = "CREATE TABLE message_data(_id INTEGER PRIMARY KEY AUTOINCREMENT"
	    + ", key TEXT, string TEXT);";
    }
}
