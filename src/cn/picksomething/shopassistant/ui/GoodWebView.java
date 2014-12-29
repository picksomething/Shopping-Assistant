package cn.picksomething.shopassistant.ui;

import cn.picksomething.shopassistant.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class GoodWebView extends Activity {
    private WebView myWebView = null;
    private Intent intent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.good_detail_layout);
        myWebView = (WebView) findViewById(R.id.webview);
        initWebView();
        intent = getIntent();
        String url = intent.getStringExtra("url");
        // Log.d("picksomething", "url = " + url);
        myWebView.loadUrl(url);
    }

    private void initWebView() {
        // TODO Auto-generated method stub
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new MyWebChromeClient());
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            String domain = Uri.parse(url).getHost();
            Log.d("picksomething", domain.toString());
            if (null != domain) {
                if (domain.contains("jd.com") || domain.contains("suning.com") || domain.contains("taobao.com")
                        || domain.contains("tmall.com") || domain.contains("gome.com.cn")) {
                    // Do not override; let my WebView load the page
                    return false;
                }
            }
            // This is my web site, so do not override; let my WebView load
            // the page
            // Otherwise, the link is not for a page on my site, so launch
            // another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    private class MyWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            GoodWebView.this.setProgress(newProgress * 100);
            Log.d("caobin", "ProgressChanged, new Progress = " + newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            GoodWebView.this.setTitle(title);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
