package cn.picksomething.shopassistant.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import cn.picksomething.shopassistant.R;
import cn.picksomething.shopassistant.adapter.SearchResultAdapter;
import cn.picksomething.shopassistant.http.ParseTool;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchActivity extends ActionBarActivity implements OnClickListener {

  private ImageButton         menuButton;
  private ImageButton         searchFrame;
  private EditText            mSearchEdit;
  private Button              searchButton;
  private Toolbar             mToolBar;
  private ListView            resultsListView;
  private MyHandler           myHandler;
  private SearchResultAdapter resultAdapter;
  private ProgressDialog      progress;

  ArrayList<HashMap<String, Object>> searchResults;
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
  }

  public void initView() {
    mToolBar = (Toolbar) findViewById(R.id.toolbar);
    mSearchEdit = (EditText) findViewById(R.id.editText);
    resultsListView = (ListView) findViewById(R.id.goodresults_listView);

    mSearchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
          startSearch();
          return true;
        }
        return false;
      }
    });
  }

  //    public void initActionBar() {
  //        View headView = LayoutInflater.from(this).inflate(R.layout.main_action_bar, null);
  //        menuButton = (ImageButton) headView.findViewById(R.id.menu_button);
  //        searchFrame = (ImageButton) headView.findViewById(R.id.search_frame);
  //        searchEdit = (EditText) headView.findViewById(R.id.editText);
  //        searchButton = (Button) headView.findViewById(R.id.search);
  //        menuButton.setVisibility(View.GONE);
  //        searchFrame.setVisibility(View.GONE);
  //        searchEdit.setVisibility(View.VISIBLE);
  //        searchButton.setVisibility(View.VISIBLE);
  //        ActionBar actionBar = getSupportActionBar();
  //        actionBar.setCustomView(headView);
  //        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
  //        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bg));
  //    }


  /**
   * @author caobin
   * @created 2014年11月5日
   */
  private void initDatas() {
    searchResults = new ArrayList<HashMap<String, Object>>();
    myHandler = new MyHandler(getMainLooper());
    progress = new ProgressDialog(this);
  }

  /**
   * @author caobin
   * @created 2014年11月5日
   */


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
    goodName = mSearchEdit.getText().toString();
    jdSearchURL = "http://search.jd.com/Search?keyword=" + goodName + "&enc=utf-8";
    try {
      tmallSearchURL = "http://list.tmall.com/search_product.htm?q=" + URLEncoder.encode(goodName, "GBK");
      //suningSearchURL = "http://search.suning.com/" + URLEncoder.encode(goodName, "UTF-8") + "/cityId=9051";
      gomeSearchURL = "http://www.gome.com.cn/search?question=" + URLEncoder.encode(goodName, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }
    new GetGoodsInfo().execute(jdSearchURL, tmallSearchURL, gomeSearchURL);
  }

  private class GetGoodsInfo extends AsyncTask<String, Integer, Long> {

    @Override
    protected void onPreExecute() {
      super.onPreExecute();
      ParseTool.init();
      progress.setMessage("正在努力搜索中...");
      progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      progress.setIndeterminate(true);
      progress.show();
    }

    @Override
    protected Long doInBackground(String... params) {
      try {
        searchResults = ParseTool.getFinalReslut(params[0], 0);
        searchResults = ParseTool.getFinalReslut(params[1], 1);
        searchResults = ParseTool.getFinalReslut(params[2], 3);
        //searchResults = HttpTools.getFinalReslut(params[3], 3);
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
      ParseTool.emptyArray();
      super.handleMessage(msg);
      resultAdapter = new SearchResultAdapter(SearchActivity.this, searchResults);
      resultsListView.setAdapter(resultAdapter);
      resultAdapter.notifyDataSetChanged();
      resultsListView.setOnItemClickListener(new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
          HashMap<String, Object> map = searchResults.get(position);
          String url = (String) (map.get("detailLink"));
          Intent intent = new Intent(SearchActivity.this, GoodWebView.class);
          intent.putExtra("url", url);
          startActivity(intent);
        }
      });
    }

  }

}
