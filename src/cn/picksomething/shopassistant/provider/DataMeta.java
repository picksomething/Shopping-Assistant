package cn.picksomething.shopassistant.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class DataMeta {
	public static final String AUTHORIY = "cn.picksomething.shopassistant.provider.dataprovider";
	public static final int DATABASE_VERSION = 13;
	
	public static final class CollectionTable implements BaseColumns {
		public static final String TABLE_NAME = "collection";
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORIY + "/collection");
		public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/collection";
		public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/collection";
		public static final String _ID = "_id";
		public static final String JID = "jid";
		public static final String NAME = "name";
		public static final String PRICE = "price";
		public static final String IMAGEURL = "imageurl";
	
	}
}
