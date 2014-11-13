package cn.picksomething.shopassistant.provider;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import cn.picksomething.shopassistant.model.Good;
import cn.picksomething.shopassistant.provider.DataMeta.CollectionTable;

public class GoodHelper {

	private Context mContext;

	public GoodHelper(Context context) {
		this.mContext = context;
	}
	
	
	
	public void addCollection(Good good) {

		ContentValues values = new ContentValues();
		values.put(CollectionTable.JID, good.getJid());
		values.put(CollectionTable.NAME, good.getName());
		values.put(CollectionTable.PRICE, good.getPrice());
		values.put(CollectionTable.IMAGEURL, good.getImageUrl());
		mContext.getContentResolver().insert(CollectionTable.CONTENT_URI, values);
	}
	
	public List<Good> getCollection() {
		List<Good> list = new ArrayList<Good>();
		Cursor c = mContext.getContentResolver().query(CollectionTable.CONTENT_URI, null, null, null, CollectionTable._ID + " ASC");

		while (c.moveToNext()) {
			String jid = c.getString(c.getColumnIndex(CollectionTable.JID));
			String name = c.getString(c.getColumnIndex(CollectionTable.NAME));
			String price = c.getString(c.getColumnIndex(CollectionTable.PRICE));
			String imageUrl = c.getString(c.getColumnIndex(CollectionTable.IMAGEURL));
			Good collection = new Good(jid);
			collection.setName(name);
			collection.setPrice(price);
			collection.setImageUrl(imageUrl);
			list.add(collection);
		}

		c.close();

		return list;
	}

}
