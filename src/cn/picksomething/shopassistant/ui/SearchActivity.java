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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.http.HttpTools;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SearchActivity extends SherlockFragmentActivity implements OnClickListener {

	private EditText mGoodName;
	private Button mSearch;
	private TextView goodId;
	private TextView goodPrice;
	private TextView goodOriginPrice;
	private String goodName;
	private String jdSearchURL;
	private ImageButton menuButton;
	private ImageButton searchFrame;
	private EditText searchEdit;
	ArrayList<HashMap<String, Object>> resultData;
	private MyHandler myHandler;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();	
		initDatas();
		setListener();
	}
	
	public void initView(){
		initActionBar();
		findViews();
	}
	public void initActionBar(){
		View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
		menuButton=(ImageButton) headView.findViewById(R.id.menu_button);
		searchFrame=(ImageButton) headView.findViewById(R.id.search_frame);
		searchEdit=(EditText) headView.findViewById(R.id.editText);
		mSearch = (Button) headView.findViewById(R.id.search);
		menuButton.setVisibility(View.GONE);
		searchFrame.setVisibility(View.GONE);
		searchEdit.setVisibility(View.VISIBLE);
		mSearch.setVisibility(View.VISIBLE);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setCustomView(headView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
	}


	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void findViews() {
		mGoodName = (EditText) findViewById(R.id.editText);
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
			goodOriginPrice.setText(ma.get("name").toString());
		}
	}

}
