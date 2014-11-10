package cn.picksomething.shopassistant.ui;

<<<<<<< HEAD
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

=======
import android.content.Intent;
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
<<<<<<< HEAD
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.adapter.SearchResultAdapter;
import cn.picksomething.shopassistant.http.HttpTools;
=======
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import cn.picksomething.shopassistant.R;
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55

import com.actionbarsherlock.app.ActionBar;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomePage extends SlidingFragmentActivity implements OnClickListener {

<<<<<<< HEAD
	private EditText mGoodName;
	private ListView mListView;
	private Button mSearch;
	private String goodName;
	private String jdSearchURL;
	ArrayList<HashMap<String, Object>> resultData;
	private MyHandler myHandler;
	private SearchResultAdapter mResultAdapter;
=======

	private ImageButton menuButton;
	private ImageButton searchFrame;

	private FragmentTabHost mTabHost;
	private Class fragmentArray[] = {HotFragment.class,RecommendFragment.class};
	private int mImageViewArray[] = {R.drawable.tab_icon,R.drawable.tab_icon};
	private String mTextViewArray[] = {"热门", "推荐"};
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		initView();	
	}
	
	public void initView(){
		initActionBar();
		initSlidingMenu();

		//实例化TabHost对象，得到TabHost
		mTabHost = (FragmentTabHost)findViewById(R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);	
		
		//得到fragment的个数
		int count = fragmentArray.length;	
				
		for(int i = 0; i < count; i++){	
			//为每一个Tab按钮设置图标、文字和内容
			TabSpec tabSpec = mTabHost.newTabSpec(mTextViewArray[i]).setIndicator(getTabItemView(i));
			//将Tab按钮添加进Tab选项卡中
			mTabHost.addTab(tabSpec, fragmentArray[i], null);
			//设置Tab按钮的背景
			mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
		}
	}
	
	public void initActionBar(){
		View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
		menuButton=(ImageButton) headView.findViewById(R.id.menu_button);
		searchFrame=(ImageButton) headView.findViewById(R.id.search_frame);
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

<<<<<<< HEAD
	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void findViews() {
		mGoodName = (EditText) findViewById(R.id.goods);
		mSearch = (Button) findViewById(R.id.search);
		mListView =(ListView)findViewById(R.id.goodresults_listView);
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void initDatas() {
		goodName = mGoodName.getText().toString();
		resultData = new ArrayList<HashMap<String, Object>>();
		myHandler = new MyHandler(getMainLooper());
		Log.d("caobin", "goodName = " + goodName);
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void setListener() {
		mSearch.setOnClickListener(this);
	}
=======
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
<<<<<<< HEAD
		case R.id.search:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					goodName = mGoodName.getText().toString();
					jdSearchURL = "http://search.jd.com/Search?keyword=" + goodName + "&enc=utf-8";
					try {
						resultData = HttpTools.getJsonDataByID(jdSearchURL);
						HttpTools.addNameToList();
						Log.d("picksomething", "origin size = " + resultData.size());
					} catch (IOException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.obj = resultData;
					myHandler.sendMessage(msg);
				}
			}).start();
=======
		case R.id.menu_button:
			toggle();//SlidingMenu的打开与关闭	
			break;
		case R.id.search_frame:
			Intent intent=new Intent(this,SearchActivity.class);
			startActivity(intent);
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55
			break;
		default:
			break;
		}
	}
	
	/**
	 * 给Tab按钮设置图标和文字
	 */
	private View getTabItemView(int index){
		View view = LayoutInflater.from(this).inflate(R.layout.tab_item_view, null);
	
		ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
		imageView.setImageResource(mImageViewArray[index]);
		
		TextView textView = (TextView) view.findViewById(R.id.textview);		
		textView.setText(mTextViewArray[index]);
	
		return view;
	}

<<<<<<< HEAD
	private void showResults() {
		mResultAdapter = new SearchResultAdapter(HomePage.this, resultData);
		mListView.setAdapter(mResultAdapter);
		// Iterator<HashMap<String, Object>> it = resultData.iterator();
		// while (it.hasNext()) {
		// Map<String, Object> ma = it.next();
		// }
	}
=======
>>>>>>> fcc3193f17ba0287ac58b0a9bff4897cd0ee8b55

}
