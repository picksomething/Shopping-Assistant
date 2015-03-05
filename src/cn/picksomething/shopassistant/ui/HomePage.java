package cn.picksomething.shopassistant.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;

public class HomePage extends ActionBarActivity implements OnClickListener, DrawerLayout.DrawerListener {

  private ImageView             mSearchFrame;
  private Toolbar               mToolBar;
  private DrawerLayout          mDrawerLayout;
  private LinearLayout          mDrawer;
  private ActionBarDrawerToggle mDrawerToggle;



  private FragmentTabHost mTabHost;
  private Class<?> fragmentArray[]   = {HotFragment.class, RecommendFragment.class};
  private int      mImageViewArray[] = {R.drawable.tab_icon, R.drawable.tab_icon};
  private String   mTextViewArray[]  = {"热门", "推荐"};

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home_page);
    initView();
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    Log.d("tag2", "post");
    mDrawerToggle.syncState();
  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }


  public void initView() {

    mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
    mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

    int count = fragmentArray.length;

    for (int i = 0; i < count; i++) {
      TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
      mTabHost.addTab(tabSpec, fragmentArray[i], null);
      mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
    }
    mSearchFrame = (ImageView) findViewById(R.id.search_frame);
    mSearchFrame.setOnClickListener(this);

    mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mDrawer = (LinearLayout) findViewById(R.id.drawer);
    mToolBar = (Toolbar) findViewById(R.id.toolbar);


    setSupportActionBar(mToolBar);
    getSupportActionBar().setDisplayShowTitleEnabled(false);
    setupDrawer();
  }


  private void setupDrawer() {
    mDrawerLayout.setDrawerListener(this);
    mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
    mDrawerLayout.setDrawerTitle(Gravity.START, getString(R.string.profile));
    Log.d("tag2", "d");
    mDrawerToggle = new ActionBarDrawerToggle(
        this,
        mDrawerLayout,
        R.string.ok,
        R.string.no
    );
  }

  @Override
  public void onClick(View v) {
    // TODO Auto-generated method stub
    switch (v.getId()) {
      case R.id.menu_button:
        //        toggle();// SlidingMenu的打开与关闭
        break;
      case R.id.search_frame:
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
        break;
      default:
        break;
    }
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private View getTabItemView(int index) {
    View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);

    ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
    imageView.setImageResource(mImageViewArray[index]);

    TextView textView = (TextView) view.findViewById(R.id.textview);
    textView.setText(mTextViewArray[index]);

    return view;
  }


  @Override public void onDrawerSlide(View view, float v) {
    mDrawerToggle.onDrawerSlide(view, v);
  }

  @Override public void onDrawerOpened(View view) {
    mDrawerToggle.onDrawerOpened(view);
  }

  @Override public void onDrawerClosed(View view) {
    mDrawerToggle.onDrawerClosed(view);
  }

  @Override public void onDrawerStateChanged(int i) {
    mDrawerToggle.onDrawerStateChanged(i);
  }

  @Override public void onBackPressed() {
    if (mDrawerLayout.isDrawerOpen(mDrawer)) {
      mDrawerLayout.closeDrawer(mDrawer);
    } else {
      super.onBackPressed();
    }
  }
}
