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

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import cn.ieclipse.af.demo.R;

/**
 * WebView in fragment is special.
 *
 * @author Jamling
 * @see android.webkit.WebViewFragment
 */
public class H5Fragment extends BaseFragment {

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_SHARE = "share";

    private ProgressBar mPb;
    protected WebView mWebView;
    protected H5Activity.H5Delegate mH5Delegate;

    protected String mUrl = "";
    protected String mTitle;
    protected ImageView mShareIv;

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
        if (mWebView != null) {
            mWebView.destroy();
        }
        mPb = view.findViewById(android.R.id.progress);
        mWebView = view.findViewById(android.R.id.content);
        mH5Delegate =
                new H5Activity.H5Delegate(mWebView, mPb) {
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
     * @param url 请求URL（以http://开头的完整URL）
     * @param title 默认初始title
     */
    public static Fragment create(String url, String title) {
        H5Fragment fragment = new H5Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_URL, url);
        bundle.putString(EXTRA_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        mUrl = bundle.getString(EXTRA_URL);
        mTitle = bundle.getString(EXTRA_TITLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_URL, mUrl);
        outState.putString(EXTRA_TITLE, mTitle);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (mWebView != null) {
            mWebView.destroy();
        }
        super.onDestroyView();
    }
    /**
     * Called when the fragment is visible to the user and actively running. Resumes the WebView.
     */
    @Override
    public void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    /** Called when the fragment is no longer resumed. Pauses the WebView. */
    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    /** Called when the fragment is no longer in use. Destroys the internal state of the WebView. */
    @Override
    public void onDestroy() {
        if (mWebView != null) {
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
