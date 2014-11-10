package cn.picksomething.shopassistant.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.adapter.SearchResultAdapter;
import cn.picksomething.shopassistant.http.HttpTools;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class SearchActivity extends SherlockFragmentActivity implements OnClickListener {

	private String goodName;
	private String jdSearchURL;
	private ImageButton menuButton;
	private ImageButton searchFrame;
	private EditText searchEdit;
	private Button searchButton;
	private ListView resultsListView;
	private MyHandler myHandler;
	private SearchResultAdapter resultAdapter;

	ArrayList<HashMap<String, Object>> resultData;
	private static final int DATA_OK = 200;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		initView();
		initDatas();
		setListener();
	}

	public void initView() {
		initActionBar();
		findViews();
	}

	public void initActionBar() {
		View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
		menuButton = (ImageButton) headView.findViewById(R.id.menu_button);
		searchFrame = (ImageButton) headView.findViewById(R.id.search_frame);
		searchEdit = (EditText) headView.findViewById(R.id.editText);
		searchButton = (Button) headView.findViewById(R.id.search);
		menuButton.setVisibility(View.GONE);
		searchFrame.setVisibility(View.GONE);
		searchEdit.setVisibility(View.VISIBLE);
		searchButton.setVisibility(View.VISIBLE);
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
		resultsListView = (ListView) findViewById(R.id.goodresults_listView);
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void initDatas() {
		// TODO Auto-generated method stub
		resultData = new ArrayList<HashMap<String, Object>>();
		myHandler = new MyHandler(getMainLooper());
	}

	/**
	 * 
	 * @author caobin
	 * @created 2014年11月5日
	 */
	private void setListener() {
		searchButton.setOnClickListener(this);
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
					goodName = searchEdit.getText().toString();
					jdSearchURL = "http://search.jd.com/Search?keyword=" + goodName + "&enc=utf-8";
					try {
						resultData = HttpTools.getJsonDataByID(jdSearchURL);
						HttpTools.addNameToList();
					} catch (IOException e) {
						e.printStackTrace();
					}
					myHandler.sendEmptyMessage(DATA_OK);
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
			resultAdapter = new SearchResultAdapter(SearchActivity.this, resultData);
			resultsListView.setAdapter(resultAdapter);
		}

	}

}
