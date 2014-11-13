package cn.picksomething.shopassistant.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

	public SqliteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql;
		sql = "create table  "
				+ DataMeta.CollectionTable.TABLE_NAME
				+ "("
				+ DataMeta.CollectionTable._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ DataMeta.CollectionTable.JID
				+ " TEXT,"
				+ DataMeta.CollectionTable.NAME
				+ " TEXT,"
				+ DataMeta.CollectionTable.PRICE
				+ " TEXT,"
				+ DataMeta.CollectionTable.IMAGEURL
				+ " TEXT,"
				+ " UNIQUE(JID) ON CONFLICT REPLACE);";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
