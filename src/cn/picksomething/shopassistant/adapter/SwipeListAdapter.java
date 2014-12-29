package cn.picksomething.shopassistant.adapter;

import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.picksomething.shopassistant.R;


public class SwipeListAdapter extends BaseAdapter {
    private List<ApplicationInfo> mAppList;
    private Context mContext;

    public SwipeListAdapter(Context context, List<ApplicationInfo> list) {
        this.mContext = context;
        this.mAppList = list;
    }

    @Override
    public int getCount() {
        return mAppList.size();
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return mAppList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.swipe_list_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        ApplicationInfo item = getItem(position);
        holder.p_icon.setImageDrawable(item.loadIcon(mContext.getPackageManager()));
        holder.p_name.setText(item.loadLabel(mContext.getPackageManager()));
        return convertView;
    }

    class ViewHolder {
        ImageView p_icon;
        TextView p_name;

        public ViewHolder(View view) {
            p_icon = (ImageView) view.findViewById(R.id.p_icon);
            p_name = (TextView) view.findViewById(R.id.p_name);
            view.setTag(this);
        }
    }
}
