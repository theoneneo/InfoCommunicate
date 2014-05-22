package com.neo.infocommunicate.db;

import java.util.HashMap;

import com.neo.infocommunicate.db.DataBase.MESSAGE_DATA_DB;
import com.neo.infocommunicate.db.DataBase.NOTICE_DATA_DB;
import com.neo.infocommunicate.db.DataBase.SEND_NOTICE_DATA_DB;
import com.neo.infocommunicate.db.DataBase.USER_DATA_DB;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.net.Uri;
import android.text.TextUtils;

public class DBContentProvider extends ContentProvider {
	private static DBProviderHelper dbHelper;

	private static final String URI_AUTHORITY = "com.neo.infocommunicate.db.provider";
	private static final String DATABASE_NAME = "infocommunicate_data.db";
	private static final int DATABASE_VERSION = 1;

	private static final String TB_USER_DATA = "user_data";
	private static final String TB_NOTICE_DATA = "notice_data";
	private static final String TB_SEND_NOTICE_DATA = "send_notice_data";
	private static final String TB_MESSAGE_DATA = "message_data";

	private static final int USER_DATA = 1;
	private static final int USER_DATA_ID = 2;
	private static final int NOTICE_DATA = 3;
	private static final int NOTICE_DATA_ID = 4;
	private static final int SEND_NOTICE_DATA = 5;
	private static final int SEND_NOTICE_DATA_ID = 6;
	private static final int MESSAGE_DATA = 7;
	private static final int MESSAGE_DATA_ID = 8;

	private static HashMap<String, String> userDataMap;
	private static HashMap<String, String> noticeDataMap;
	private static HashMap<String, String> sendNoticeDataMap;
	private static HashMap<String, String> msgDataMap;

	private static final UriMatcher sUriMatcher;
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

		sUriMatcher.addURI(URI_AUTHORITY, "user_data", USER_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "user_data/#", USER_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "notice_data", NOTICE_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "notice_data/#", NOTICE_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "send_notice_data", SEND_NOTICE_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "send_notice_data/#",
				SEND_NOTICE_DATA_ID);
		sUriMatcher.addURI(URI_AUTHORITY, "message_data", MESSAGE_DATA);
		sUriMatcher.addURI(URI_AUTHORITY, "message_data/#", MESSAGE_DATA_ID);

		userDataMap = new HashMap<String, String>();
		userDataMap.put(USER_DATA_DB._ID, USER_DATA_DB._ID);
		userDataMap.put(USER_DATA_DB.USER_ID, USER_DATA_DB.USER_ID);
		userDataMap.put(USER_DATA_DB.NICK_NAME, USER_DATA_DB.NICK_NAME);

		noticeDataMap = new HashMap<String, String>();
		noticeDataMap.put(NOTICE_DATA_DB._ID, NOTICE_DATA_DB._ID);
		noticeDataMap.put(NOTICE_DATA_DB.KEY, NOTICE_DATA_DB.KEY);
		noticeDataMap.put(NOTICE_DATA_DB.MESSAGE, NOTICE_DATA_DB.MESSAGE);

		sendNoticeDataMap = new HashMap<String, String>();
		sendNoticeDataMap.put(SEND_NOTICE_DATA_DB._ID, SEND_NOTICE_DATA_DB._ID);
		sendNoticeDataMap.put(SEND_NOTICE_DATA_DB.KEY, SEND_NOTICE_DATA_DB.KEY);
		sendNoticeDataMap.put(SEND_NOTICE_DATA_DB.MESSAGE,
				SEND_NOTICE_DATA_DB.MESSAGE);

		msgDataMap = new HashMap<String, String>();
		msgDataMap.put(MESSAGE_DATA_DB._ID, MESSAGE_DATA_DB._ID);
		msgDataMap.put(MESSAGE_DATA_DB.KEY, MESSAGE_DATA_DB.KEY);
		msgDataMap.put(MESSAGE_DATA_DB.MESSAGE, MESSAGE_DATA_DB.MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#onCreate()
	 */
	@Override
	public boolean onCreate() {
		dbHelper = new DBProviderHelper(getContext(), DATABASE_NAME, null,
				DATABASE_VERSION);
		return true;
	}

	public static void closeDB() {
		dbHelper.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#query(android.net.Uri,
	 * java.lang.String[], java.lang.String, java.lang.String[],
	 * java.lang.String)
	 */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case USER_DATA:
			qb.setTables(TB_USER_DATA);
			qb.setProjectionMap(userDataMap);
			break;
		case USER_DATA_ID:
			qb.setTables(TB_USER_DATA);
			qb.setProjectionMap(userDataMap);
			qb.appendWhere(USER_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case NOTICE_DATA:
			qb.setTables(TB_NOTICE_DATA);
			qb.setProjectionMap(noticeDataMap);
			break;
		case NOTICE_DATA_ID:
			qb.setTables(TB_NOTICE_DATA);
			qb.setProjectionMap(noticeDataMap);
			qb.appendWhere(NOTICE_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case SEND_NOTICE_DATA:
			qb.setTables(TB_SEND_NOTICE_DATA);
			qb.setProjectionMap(sendNoticeDataMap);
			break;
		case SEND_NOTICE_DATA_ID:
			qb.setTables(TB_SEND_NOTICE_DATA);
			qb.setProjectionMap(sendNoticeDataMap);
			qb.appendWhere(SEND_NOTICE_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		case MESSAGE_DATA:
			qb.setTables(TB_MESSAGE_DATA);
			qb.setProjectionMap(msgDataMap);
			break;
		case MESSAGE_DATA_ID:
			qb.setTables(TB_MESSAGE_DATA);
			qb.setProjectionMap(msgDataMap);
			qb.appendWhere(MESSAGE_DATA_DB._ID + "="
					+ uri.getPathSegments().get(1));
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#insert(android.net.Uri,
	 * android.content.ContentValues)
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initValues) {
		ContentValues values = null;
		if (initValues == null) {
			return null;
		} else {
			values = new ContentValues(initValues);
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Uri myURI;
		long retval = 0;
		switch (sUriMatcher.match(uri)) {
		case USER_DATA:
			retval = db.insert(TB_USER_DATA, USER_DATA_DB._ID, values);
			myURI = USER_DATA_DB.CONTENT_URI;
			break;
		case NOTICE_DATA:
			retval = db.insert(TB_NOTICE_DATA, NOTICE_DATA_DB._ID, values);
			myURI = NOTICE_DATA_DB.CONTENT_URI;
			break;
		case SEND_NOTICE_DATA:
			retval = db.insert(TB_SEND_NOTICE_DATA, SEND_NOTICE_DATA_DB._ID,
					values);
			myURI = SEND_NOTICE_DATA_DB.CONTENT_URI;
			break;			
		case MESSAGE_DATA:
			retval = db.insert(TB_MESSAGE_DATA, MESSAGE_DATA_DB._ID, values);
			myURI = MESSAGE_DATA_DB.CONTENT_URI;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		if (retval > 0) {
			Uri retUri = ContentUris.withAppendedId(myURI, retval);
			retUri.buildUpon().build();
			getContext().getContentResolver().notifyChange(retUri, null);

			return retUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#update(android.net.Uri,
	 * android.content.ContentValues, java.lang.String, java.lang.String[])
	 */
	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int retval = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (sUriMatcher.match(uri)) {
		case USER_DATA:
			retval = db.update(TB_USER_DATA, values, selection, selectionArgs);
			break;
		case USER_DATA_ID:
			retval = db.update(TB_USER_DATA, values,
					USER_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case NOTICE_DATA:
			retval = db.update(TB_NOTICE_DATA, values, selection,
					selectionArgs);
			break;
		case NOTICE_DATA_ID:
			retval = db.update(TB_NOTICE_DATA, values,
					NOTICE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case SEND_NOTICE_DATA:
			retval = db.update(TB_SEND_NOTICE_DATA, values, selection,
					selectionArgs);
			break;
		case SEND_NOTICE_DATA_ID:
			retval = db.update(
					TB_SEND_NOTICE_DATA,
					values,
					SEND_NOTICE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;			
		case MESSAGE_DATA:
			retval = db.update(TB_MESSAGE_DATA, values, selection,
					selectionArgs);
			break;
		case MESSAGE_DATA_ID:
			retval = db.update(TB_MESSAGE_DATA, values,
					MESSAGE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			break;

		}

		getContext().getContentResolver().notifyChange(uri, null);
		return retval;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.content.ContentProvider#delete(android.net.Uri,
	 * java.lang.String, java.lang.String[])
	 */
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int retval = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		switch (sUriMatcher.match(uri)) {
		case USER_DATA:
			retval = db.delete(TB_USER_DATA, selection, selectionArgs);
			break;
		case USER_DATA_ID:
			retval = db.delete(TB_USER_DATA,
					USER_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case NOTICE_DATA:
			retval = db.delete(TB_NOTICE_DATA, selection, selectionArgs);
			break;
		case NOTICE_DATA_ID:
			retval = db.delete(TB_NOTICE_DATA,
					NOTICE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		case SEND_NOTICE_DATA:
			retval = db.delete(TB_SEND_NOTICE_DATA, selection, selectionArgs);
			break;
		case SEND_NOTICE_DATA_ID:
			retval = db.delete(
					TB_SEND_NOTICE_DATA,
					SEND_NOTICE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;			
		case MESSAGE_DATA:
			retval = db.delete(TB_MESSAGE_DATA, selection, selectionArgs);
			break;
		case MESSAGE_DATA_ID:
			retval = db.delete(TB_MESSAGE_DATA,
					MESSAGE_DATA_DB._ID
							+ "="
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			break;
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return retval;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	public SQLiteDatabase getHelper() {
		return dbHelper.getWritableDatabase();
	}

}

/**
 * @author jeff
 * 
 */
class DBProviderHelper extends SQLiteOpenHelper {
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	private static final String TB_USER_DATA = "user_data";
	private static final String TB_NOTICE_DATA = "notice_data";
	private static final String TB_SEND_NOTICE_DATA = "send_notice_data";
	private static final String TB_MESSAGE_DATA = "message_data";

	public DBProviderHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(USER_DATA_DB.CREATE_TABLE);
		db.execSQL(NOTICE_DATA_DB.CREATE_TABLE);
		db.execSQL(SEND_NOTICE_DATA_DB.CREATE_TABLE);
		db.execSQL(MESSAGE_DATA_DB.CREATE_TABLE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_USER_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_NOTICE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_SEND_NOTICE_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TB_MESSAGE_DATA);
		onCreate(db);
	}
}
