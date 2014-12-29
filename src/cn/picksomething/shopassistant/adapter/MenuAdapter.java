package cn.picksomething.shopassistant.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.model.ItemSettingModel;


public class MenuAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemSettingModel> mLists;
    private LayoutInflater mLayoutInflater;

    public MenuAdapter(Context context, List<ItemSettingModel> pLists) {
        this.mContext = context;
        this.mLists = pLists;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mLists != null ? mLists.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (null == convertView) {
            holder = new Holder();
            convertView = mLayoutInflater.inflate(R.layout.slidemenu_item, null);
            holder.setting_image_icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.commom_or_more_text_item = (TextView) convertView.findViewById(R.id.item_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.setting_image_icon.setImageResource(mLists.get(position).getId());
        holder.commom_or_more_text_item.setText(mLists.get(position).getName());
        return convertView;
    }

    private static class Holder {
        ImageView setting_image_icon;
        TextView commom_or_more_text_item;
    }

}

