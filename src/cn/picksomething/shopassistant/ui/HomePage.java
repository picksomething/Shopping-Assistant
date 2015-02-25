package cn.picksomething.shopassistant.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import cn.picksomething.shopassistant.R;

public class HomePage extends SlidingFragmentActivity implements OnClickListener {

    private ImageButton menuButton;
    private ImageButton searchFrame;

    private FragmentTabHost mTabHost;
    private Class<?> fragmentArray[] = {HotFragment.class, RecommendFragment.class};
    private int mImageViewArray[] = {R.drawable.tab_icon, R.drawable.tab_icon};
    private String mTextViewArray[] = {"热门", "推荐"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.activity_home_page);
        initView();
    }

    public void initView() {
        initActionBar();
        initSlidingMenu();

        // 实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        // 得到fragment的个数
        int count = fragmentArray.length;

        for (int i = 0; i < count; i++) {
            // 为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
        }
    }

    public void initActionBar() {
        View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
        menuButton = (ImageButton) headView.findViewById(R.id.menu_button);
        searchFrame = (ImageButton) headView.findViewById(R.id.search_frame);
        menuButton.setOnClickListener(this);
        searchFrame.setOnClickListener(this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(headView);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
    }

    private void initSlidingMenu() {
        setBehindContentView(R.layout.menu_frame);
        getFragmentManager().beginTransaction().replace(R.id.menu_frame, new CustomMenu()).commit();

        SlidingMenu menu = getSlidingMenu();
        // 设置滑动阴影的宽度
        menu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动阴影的图像资源
        menu.setShadowDrawable(R.drawable.shadow);
        // 设置滑动菜单视图的宽度
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        menu.setFadeDegree(0.35f);
        // 设置触摸屏幕的模式
        menu.setTouchModeAbove(SlidingMenu.SLIDING_CONTENT);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.menu_button:
                toggle();// SlidingMenu的打开与关闭
                break;
            case R.id.search_frame:
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextViewArray[index]);

        return view;
    }
}
