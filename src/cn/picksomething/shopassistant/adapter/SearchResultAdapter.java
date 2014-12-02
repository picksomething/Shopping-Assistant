package cn.picksomething.shopassistant.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;

public class SearchResultAdapter extends BaseAdapter {
	private LayoutInflater mLayoutInflater;
	private Context mContext;
	private List<HashMap<String, Object>> list;

	public SearchResultAdapter(Context context, List<HashMap<String, Object>> list) {
		this.mContext = context;
		this.list = list;
		this.mLayoutInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		Log.d("picksomething", "size = " + list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.goods_item, null);
			holder.goodBitmap = (ImageView) convertView.findViewById(R.id.goodItem_icon);
			holder.goodName = (TextView) convertView.findViewById(R.id.goodItem_name);
			holder.goodPrice = (TextView) convertView.findViewById(R.id.goodItem_price);
			holder.goodSource = (TextView)convertView.findViewById(R.id.goodItem_source);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (null != list) {
			holder.goodBitmap.setImageBitmap((Bitmap) list.get(position).get("goodBitmap"));
			holder.goodName.setText(list.get(position).get("goodName").toString());
			holder.goodPrice.setText("￥ " + list.get(position).get("goodPrice").toString());
			holder.goodSource.setText("来自:" + list.get(position).get("goodSource").toString());
		}else{
			holder.goodBitmap.setImageResource(R.drawable.ic_launcher);
			holder.goodName.setText(list.get(position).get("name").toString());
			holder.goodPrice.setText("￥ " + list.get(position).get("price").toString());
		}
		return convertView;
	}

	private static class ViewHolder {
		ImageView goodBitmap;
		TextView goodName;
		TextView goodPrice;
		TextView goodSource;
	}

}
