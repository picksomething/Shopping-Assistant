package cn.picksomething.shopassistant.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.http.HttpTools;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class HomePage extends SlidingFragmentActivity implements OnClickListener {

	private EditText mGoodName;
	private Button mSearch;
	private TextView goodId;
	private TextView goodPrice;
	private TextView goodOriginPrice;
	private String goodName;
	private String jdSearchURL;
	ArrayList<HashMap<String, Object>> resultData;
	private MyHandler myHandler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.HomePage);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_home_page);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.customtitle);
		initSlidingMenu(savedInstanceState);
		findViews();
		initDatas();
		setListener();
	}

	private void initSlidingMenu(Bundle savedInstanceState) {
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

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void findViews() {
		mGoodName = (EditText) findViewById(R.id.goods);
		mSearch = (Button) findViewById(R.id.search);
		goodId = (TextView) findViewById(R.id.goodID);
		goodPrice = (TextView) findViewById(R.id.goodPrice);
		goodOriginPrice = (TextView) findViewById(R.id.goodOriginPrice);
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.search:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					goodName = mGoodName.getText().toString();
					jdSearchURL = "http://search.jd.com/Search?keyword=" + goodName + "&enc=utf-8";
					try {
						resultData = HttpTools.getJsonDataByID(jdSearchURL);
					} catch (IOException e) {
						e.printStackTrace();
					}
					Message msg = Message.obtain();
					msg.obj = resultData;
					myHandler.sendMessage(msg);
				}
			}).start();
			break;

		default:
			break;
		}
	}

	class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			showResults();
		}

	}

	private void showResults() {
		// TODO Auto-generated method stub
		Iterator<HashMap<String, Object>> it = resultData.iterator();
		while (it.hasNext()) {
			Map<String, Object> ma = it.next();
			goodId.setText(ma.get("id").toString());
			goodPrice.setText(ma.get("price").toString());
			goodOriginPrice.setText(ma.get("originPrice").toString());
		}
	}

}
