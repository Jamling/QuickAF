package cn.ieclipse.af.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 类/接口描述
 *
 * @author whc
 * @date 2015年8月19日
 */
public class H5Activity extends BaseActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";

    private WebView mWebView;

    private String mUrl = "";
    private String mTitle;

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mWebView = new WebView(this);
        setContentView(mWebView);
    }

    @Override
    protected void initData() {
        super.initData();
        // 能使用JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        mWebView.setWebViewClient(new H5WebViewClient());

        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(new H5WebChromeClient());

        // 优先使用缓存
        // 不是用缓存（webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);）
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        // WebView加载web资源
        mWebView.loadUrl(mUrl);
    }

    /**
     * @param context
     * @param url     请求URL（以http://开头的完整URL）
     * @param title   默认初始title
     */
    public static void forward(Context context, String url, String title) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_BUNDLE, bundle);
        intent.setClass(context, H5Activity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        mUrl = bundle.getString(EXTRA_URL);
        mTitle = bundle.getString(EXTRA_TITLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_URL, mUrl);
        outState.putString(EXTRA_TITLE, mTitle);
        super.onSaveInstanceState(outState);
    }

    protected void onUpdateTitle(WebView webView, String title) {

    }

    protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
        webView.loadUrl(url);
        return false;
    }

    private class H5WebChromeClient extends WebChromeClient {
        @Override
        public void onReceivedTitle(WebView webView, String s) {
            super.onReceivedTitle(webView, s);
            onUpdateTitle(webView, s);
        }
    }

    private class H5WebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
            return H5Activity.this.shouldOverrideUrlLoading(webView, s);
        }
    }
}
