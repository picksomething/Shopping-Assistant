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
	private Context context;
	private List<HashMap<String, Object>> list;

	public SearchResultAdapter(Context context, List<HashMap<String, Object>> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		this.mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		Log.d("picksomething", "size = " + list.size());
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mLayoutInflater.inflate(R.layout.goods_item, null);
			holder.goodIcon = (ImageView) convertView.findViewById(R.id.goodItem_icon);
			holder.goodName = (TextView) convertView.findViewById(R.id.goodItem_name);
			holder.goodPrice = (TextView) convertView.findViewById(R.id.goodItem_price);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (null != list) {
			holder.goodIcon.setImageBitmap((Bitmap) list.get(position).get("image"));
			holder.goodName.setText(list.get(position).get("name").toString());
			holder.goodPrice.setText("￥ " + list.get(position).get("price").toString());
		}else{
			holder.goodIcon.setImageResource(R.drawable.ic_launcher);
			holder.goodName.setText(list.get(position).get("name").toString());
			holder.goodPrice.setText("￥ " + list.get(position).get("price").toString());
		}
		Log.d("picksomething", "price = " + list.get(position).get("price").toString());
		return convertView;
	}

	private static class ViewHolder {
		ImageView goodIcon;
		TextView goodName;
		TextView goodPrice;
	}

}
