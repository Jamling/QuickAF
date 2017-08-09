package cn.ieclipse.af.demo.sample.recycler;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.view.refresh.RefreshLayout;

/**
 * Description
 *
 * @author Jamling
 */

public class RefreshWebViewSample extends SampleBaseFragment implements RefreshLayout.OnRefreshListener {
    RefreshLayout refreshLayout;
    WebView webView;
    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_scroll_view;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);

        webView = new WebView(view.getContext());
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        refreshLayout.setContentView(webView);
        load(false);
    }

    protected void load(boolean fromCache) {
        webView.loadUrl("http://www.ieclipse.cn/tags/Android/");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                refreshLayout.hideEmptyView();
                refreshLayout.onRefreshComplete();
            }
        });
    }

    @Override
    public void onLoadMore() {
        load(false);
    }

    @Override
    public void onRefresh() {
        load(false);
    }
}
