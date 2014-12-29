package cn.picksomething.shopassistant.ui;

import java.util.List;

import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.adapter.SwipeListAdapter;
import cn.picksomething.shopassistant.widget.swipemenulistview.SwipeMenu;
import cn.picksomething.shopassistant.widget.swipemenulistview.SwipeMenuCreator;
import cn.picksomething.shopassistant.widget.swipemenulistview.SwipeMenuItem;
import cn.picksomething.shopassistant.widget.swipemenulistview.SwipeMenuListView;
import cn.picksomething.shopassistant.widget.swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;


public class HotFragment extends Fragment {
    private List<ApplicationInfo> mAppList;
    private SwipeListAdapter mAdapter;
    private SwipeMenuListView smListView;
    private View mHeadView;
    private ImageView mImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.hot_fragment, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void initView() {
        View contentView = getView();
        if (null == contentView) {
            return;
        }
        smListView = (SwipeMenuListView) contentView.findViewById(R.id.smListView);
        mAppList = getActivity().getPackageManager().getInstalledApplications(0);
        mAdapter = new SwipeListAdapter(getActivity(), mAppList);
        mHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.hot_head_view, null);
        mImageView = (ImageView) mHeadView.findViewById(R.id.hot_head_Image);
        mImageView.setImageResource(R.drawable.hot_headview);
        smListView.addHeaderView(mHeadView);
        smListView.setAdapter(mAdapter);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem collectItem = new SwipeMenuItem(getActivity());
                collectItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                collectItem.setWidth(dp2px(90));
                collectItem.setTitle("collect");
                collectItem.setTitleSize(18);
                collectItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(collectItem);
            }
        };

        smListView.setMenuCreator(creator);
        smListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
                // false : 关闭menu; true :不关闭menu
                return false;
            }
        });


    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}
