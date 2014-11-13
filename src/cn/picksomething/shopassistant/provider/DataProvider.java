package cn.picksomething.shopassistant.provider;

import java.util.regex.Pattern;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import cn.picksomething.shopassistant.provider.DataMeta.CollectionTable;

public class DataProvider extends ContentProvider {
	private static final String TAG = "DataProvider";
	private static final int COLLECTION = 1;
	private static final int COLLECTION_ID = 2;
	private static final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
	
	public static final UriMatcher uriMatcher;
	public static SqliteHelper sqliteHelper = null;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(DataMeta.AUTHORIY, CollectionTable.TABLE_NAME, COLLECTION);
		uriMatcher.addURI(DataMeta.AUTHORIY, CollectionTable.TABLE_NAME + "/#", COLLECTION_ID);
	}
		
		
	@Override
	public boolean onCreate() {
		sqliteHelper=new SqliteHelper(this.getContext(), "shopAsisatant.db", null, DataMeta.DATABASE_VERSION);
		return true;
	}


	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
			return ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CollectionTable.TABLE_NAME;
		case COLLECTION_ID:
			return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CollectionTable.TABLE_NAME;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
		switch (uriMatcher.match(uri)) {

		case COLLECTION:
			builder.setTables(CollectionTable.TABLE_NAME);
			break;
		case COLLECTION_ID:
			builder.setTables(CollectionTable.TABLE_NAME);
			builder.appendWhere(CollectionTable._ID + "=" + uri.getPathSegments().get(1));
			break;
		default:
			break;
		}
		String orderBy = TextUtils.isEmpty(sortOrder) ? "_id desc" : sortOrder;

		String limit = uri.getQueryParameter("limit");
		if (TextUtils.isEmpty(limit)) {
			limit = null;
		} else if (!sLimitPattern.matcher(limit).matches()) {
			Log.w(TAG, "Your limit clause is not match the limit pattern, please check it!");
			limit = null;
		}

		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		Cursor c = builder.query(db, projection, selection, selectionArgs, null, null, orderBy, limit);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
		
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		String matchedTable = null;
		Uri matchedUri = null;
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
		case COLLECTION_ID:
			matchedTable = CollectionTable.TABLE_NAME;
			matchedUri = CollectionTable.CONTENT_URI;
			break;
		default:
			break;
		}

		long rowId = db.insert(matchedTable, null, values);
		Uri insertUri = null;
		if (rowId > 0) {
			insertUri = ContentUris.withAppendedId(matchedUri, rowId);
			getContext().getContentResolver().notifyChange(insertUri, null);
		} else if (rowId == -1) {
			throw new SQLException("duplicated row " + insertUri);
		} else {
			throw new SQLException("failed insert " + insertUri);
		}

		return insertUri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		String matchedTable = null;
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
		case COLLECTION_ID:
			matchedTable = CollectionTable.TABLE_NAME;
			break;
		default:
			break;
		}

		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		count = db.delete(matchedTable, selection, selectionArgs);
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}

		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		String matchedTable = null;
		switch (uriMatcher.match(uri)) {
		case COLLECTION:
		case COLLECTION_ID:
			matchedTable = CollectionTable.TABLE_NAME;
			break;
		default:
			break;
		}
		SQLiteDatabase db = sqliteHelper.getWritableDatabase();
		count = db.update(matchedTable, values, selection, selectionArgs);
		getContext().getContentResolver().notifyChange(CollectionTable.CONTENT_URI, null);
		return count;
	}

	
}
