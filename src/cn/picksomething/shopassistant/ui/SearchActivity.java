package cn.picksomething.shopassistant.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

	private ImageButton menuButton;
	private ImageButton searchFrame;
	private EditText searchEdit;
	private Button searchButton;
	private ListView resultsListView;
	private MyHandler myHandler;
	private SearchResultAdapter resultAdapter;
	private ProgressDialog progress;

	ArrayList<HashMap<String, Object>> jdSearchResults;
	private String goodName;
	private String jdSearchURL;
	private String tmallSearchURL;
	private String suningSearchURL;
	private String gomeSearchURL;
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
		jdSearchResults = new ArrayList<HashMap<String, Object>>();
		myHandler = new MyHandler(getMainLooper());
		progress = new ProgressDialog(this);
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
		switch (v.getId()) {
		case R.id.search:
			startSearch();
			break;
		default:
			break;
		}
	}

	private void startSearch() {
		goodName = searchEdit.getText().toString();
		jdSearchURL = "http://search.jd.com/Search?keyword=" + goodName + "&enc=utf-8";
		tmallSearchURL = "http://list.tmall.com/search_product.htm?q=" + goodName;
		suningSearchURL = "http://search.suning.com/" + goodName + "/";
		gomeSearchURL = "http://www.gome.com.cn/search?question=" + goodName;
		new GetGoodsInfo().execute(jdSearchURL, tmallSearchURL);
	}

	private class GetGoodsInfo extends AsyncTask<String, Integer, Long> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			HttpTools.init();
			progress.setMessage("正在努力搜索中...");
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setIndeterminate(true);
			progress.show();
		}

		@Override
		protected Long doInBackground(String... params) {
			try {
				jdSearchResults = HttpTools.getFinalReslut(params[0], 0);
				jdSearchResults = HttpTools.getFinalReslut(params[1], 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Long result) {
			super.onPostExecute(result);
			progress.dismiss();
			// myHandler.sendEmptyMessageDelayed(DATA_OK, 1000);
			myHandler.sendEmptyMessage(DATA_OK);
		}
	}

	class MyHandler extends Handler {
		public MyHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			HttpTools.emptyArray();
			super.handleMessage(msg);
			resultAdapter = new SearchResultAdapter(SearchActivity.this, jdSearchResults);
			resultsListView.setAdapter(resultAdapter);
			resultAdapter.notifyDataSetChanged();
			resultsListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					HashMap<String, Object> map = jdSearchResults.get(position);
					String url = (String) (map.get("link"));
					Intent intent = new Intent(SearchActivity.this, GoodWebView.class);
					intent.putExtra("url", url);
					startActivity(intent);
				}
			});
		}

	}

}
