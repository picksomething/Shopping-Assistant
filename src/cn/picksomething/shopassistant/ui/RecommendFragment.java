package cn.picksomething.shopassistant.ui;

import android.app.ListFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.http.HttpTools;
import cn.picksomething.shopassistant.model.RecommendItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RecommendFragment extends android.support.v4.app.ListFragment {
  private RecommendAdapter mAdapter;


  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    loadData();

  }

  private void loadData() {
    List<RecommendItem> list = new ArrayList<RecommendItem>();
    RecommendItem jdItem = new RecommendItem();
    jdItem.mall = "京东";
    List<HashMap<String, Object>> jdEntries = new ArrayList<HashMap<String, Object>>();
    HashMap<String, Object> jdHashMap;
    for (int i = 0; i < 6; i++) {
      jdHashMap = new HashMap<String, Object>();
      jdHashMap.put("img", "http://img10.360buyimg.com/vclist/jfs/t595/26/1437391554/34168/ebc33f42/54eda311N45118613" +
          ".jpg");
      jdHashMap.put("desc", "this is a test");
      jdEntries.add(jdHashMap);
    }

    jdItem.entries = jdEntries;
    list.add(jdItem);
    mAdapter = new RecommendAdapter(getActivity(), 0, list);
    setListAdapter(mAdapter);


  }

  private class RecommendAdapter extends ArrayAdapter<RecommendItem> {
    private LayoutInflater inflater;

    public RecommendAdapter(Context context, int resource, List<RecommendItem> objects) {
      super(context, resource, objects);
      inflater = LayoutInflater.from(context);
    }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
      RecommendItem item = getItem(position);
      //      final int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12,
      // getContext().getResources().getDisplayMetrics());
      ViewHolder viewHolder;
      if (convertView == null) {
        convertView = inflater.inflate(R.layout.recommend_entry, null);
      }
      TextView title = (TextView) convertView.findViewById(R.id.title);
      title.setText(item.mall);
      LinearLayout rowLayout = new LinearLayout(getContext());
      ;
      ImageView image;
      Bitmap bitmap;
      TextView textView;
      for (int i = 0; i < item.entries.size(); i++) {
        HashMap<String, Object> entry = item.entries.get(i);
        if (i % 3 == 0) {
          rowLayout = new LinearLayout(getContext());
          rowLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.WRAP_CONTENT));
          rowLayout.setOrientation(LinearLayout.HORIZONTAL);
          ((ViewGroup) convertView).addView(rowLayout);
        }
        LinearLayout entryLayout = new LinearLayout(getContext());
        entryLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        entryLayout.setOrientation(LinearLayout.VERTICAL);

//        bitmap = HttpTools.getGoodsImage((String) entry.get("img"));
        image = new ImageView(getContext());
        image.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));

        textView = new TextView(getContext());
        textView.setText((String) entry.get("desc"));
        entryLayout.addView(image);
        entryLayout.addView(textView);
        rowLayout.addView(entryLayout);
      }

      return convertView;
    }

    private class ViewHolder {
      TextView title;

    }

  }
}
