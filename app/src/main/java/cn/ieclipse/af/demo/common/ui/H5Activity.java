/*
 * Copyright (C) 2015-2016 QuickAF
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.af.demo.common.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import cn.ieclipse.af.demo.R;

/**
 * Description
 *
 * @author Jamling
 */
public class H5Activity extends BaseActivity {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_SHARE = "share";

    protected String mUrl = "";
    protected String mTitle;
    protected ImageView mShareIv;

    protected ProgressBar mPb;
    protected WebView mWebView;
    protected H5Delegate mH5Delegate;

    @Override
    protected int getContentLayout() {
        return R.layout.common_activity_h5;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(mTitle);
        mTitleTextView.setSingleLine(true);
        mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mPb = view.findViewById(android.R.id.progress);
        mWebView = view.findViewById(android.R.id.content);
        initH5Delegate();
    }

    protected void initH5Delegate() {
        mH5Delegate =
                new H5Delegate(mWebView, mPb) {
                    @Override
                    protected void onUpdateTitle(WebView webView, String title) {
                        mTitleTextView.setText(title);
                    }
                };
    }

    @Override
    protected void initData() {
        super.initData();
        mH5Delegate.load(mUrl);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    /**
     * Create H5Activity intent
     *
     * @param context
     * @param url 请求URL（以http://开头的完整URL）
     * @param title 默认初始title
     */
    public static Intent create(Context context, String url, String title) {
        Intent intent = new Intent(context, H5Activity.class);
        intent.putExtra(EXTRA_URL, url);
        intent.putExtra(EXTRA_TITLE, title);
        return intent;
    }

    public static void go(Context context, String url, String title) {
        context.startActivity(H5Activity.create(context, url, title));
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

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroy();
    }

    public static class H5Delegate {
        public static final int MSG_FAKE_PROGRESS = 0;
        public static final int MSG_REAL_PROGRESS = 1;

        protected ProgressBar mPb;
        protected WebView mWebView;

        private static final int PB_FAKE_MAX = 85;
        private final int mFakeInternal = 200;

        private final Handler mPbHandler =
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        if (mPb == null) {
                            return;
                        }
                        if (msg.what == MSG_FAKE_PROGRESS) {
                            if (mPb.getProgress() < PB_FAKE_MAX) {
                                mPb.setProgress(mPb.getProgress() + 1);
                                mPbHandler.sendEmptyMessageDelayed(
                                        MSG_FAKE_PROGRESS, getFakeInternal());
                            }
                        } else if (msg.what == MSG_REAL_PROGRESS) {
                            int p = msg.arg1;
                            if (p > mPb.getProgress()) {
                                mPb.setProgress(p);
                            }
                            if (p >= 100) {
                                mPb.setVisibility(View.GONE);
                            } else {
                                mPb.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                };

        public H5Delegate(WebView webView, ProgressBar progressBar) {
            mPb = progressBar;
            mWebView = webView;
            initWebViewSettings();
        }

        public WebView getWebView() {
            return mWebView;
        }

        public WebSettings getSettings() {
            return mWebView.getSettings();
        }

        public Context getContext() {
            return mWebView.getContext();
        }

        protected void initWebViewSettings() {
            // 能使用JavaScript
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            // 覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
            mWebView.setWebViewClient(new H5WebViewClient());

            // 设置setWebChromeClient对象
            mWebView.setWebChromeClient(new H5WebChromeClient());

            // 优先使用缓存
            // 不是用缓存（webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);）
            mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            mWebView.getSettings().setDomStorageEnabled(true);
            mWebView.setDownloadListener(
                    new H5Delegate.SystemDownloadListener(mWebView.getContext()));

            // 设置可以支持缩放
            mWebView.getSettings().setSupportZoom(true);
            // 扩大比例的缩放
            mWebView.getSettings().setUseWideViewPort(true);
            // 自适应屏幕
            mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.getSettings().setLoadWithOverviewMode(true);
        }

        public void load(String url) {
            // WebView加载web资源
            mWebView.loadUrl(url);
            startProgress();
        }

        public void startProgress() {
            if (mPb != null) {
                mPb.setProgress(0);
                mPbHandler.sendEmptyMessageDelayed(MSG_FAKE_PROGRESS, getFakeInternal());
            }
        }

        public int getFakeInternal() {
            return mFakeInternal;
        }

        protected void onUpdateTitle(WebView webView, String title) {}

        protected boolean shouldOverrideUrlLoading(WebView webView, String url) {
            if (url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                // webView.reload();
                return true;
            } else if (url.startsWith("mailto:")) {
                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getContext().startActivity(intent);
                return true;
            }
            webView.loadUrl(url);
            return true;
        }

        protected class H5WebChromeClient extends WebChromeClient {
            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
                onUpdateTitle(webView, s);
            }

            @Override
            public void onProgressChanged(WebView webView, int i) {
                Message msg = new Message();
                msg.what = MSG_REAL_PROGRESS;
                msg.arg1 = i;
                mPbHandler.sendMessage(msg);
            }
        }

        protected class H5WebViewClient extends WebViewClient {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return H5Delegate.this.shouldOverrideUrlLoading(webView, s);
            }

            @Override
            public void onReceivedSslError(
                    WebView view, final SslErrorHandler handler, SslError error) {
                AlertDialog dialog =
                        new AlertDialog.Builder(view.getContext())
                                .setTitle("SSL证书错误")
                                .setMessage("错误信息：" + getSSLErrorMsg(error) + "\n是否继续访问？")
                                .setPositiveButton(
                                        android.R.string.ok, (dialog1, which) -> handler.proceed())
                                .setNegativeButton(
                                        android.R.string.no, (dialog12, which) -> handler.cancel())
                                .create();
                dialog.show();
            }

            private String getSSLErrorMsg(SslError error) {
                String msg = "";
                switch (error.getPrimaryError()) {
                    case SslError.SSL_EXPIRED:
                        msg = "SSL证书已过期";
                        break;
                    case SslError.SSL_UNTRUSTED:
                        msg = "证书不受信任";
                        break;
                    default:
                        msg = "证书错误或无效";
                        break;
                }
                return msg;
            }
        }

        public static class SystemDownloadListener implements DownloadListener {
            private final Context context;

            public SystemDownloadListener(Context context) {
                this.context = context;
            }

            @Override
            public void onDownloadStart(
                    String url,
                    String userAgent,
                    String contentDisposition,
                    String mimetype,
                    long contentLength) {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                if (context != null) {
                    context.startActivity(intent);
                }
            }
        }
    }
}
