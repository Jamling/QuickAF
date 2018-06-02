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
package cn.ieclipse.af.demo.common.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.view.refresh.FooterView;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class AppRefreshFooterView extends FooterView {
    public AppRefreshFooterView(Context context) {
        super(context);
    }

    public AppRefreshFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AppRefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppRefreshFooterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private ProgressBar progressBar;
    private TextView tvDesc;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressBar = (ProgressBar) findViewById(android.R.id.progress);
        tvDesc = (TextView) findViewById(android.R.id.text1);
    }

    @Override
    public void setEmpty(CharSequence text) {
        progressBar.setVisibility(GONE);
        if (!TextUtils.isEmpty(text)) {
            tvDesc.setText(text);
        }
        else {
            tvDesc.setText(R.string.common_footer_loading_empty);
        }
    }

    @Override
    public void reset() {
        progressBar.setVisibility(View.GONE);
        tvDesc.setText(null);
    }

    @Override
    public void setLoading(CharSequence text) {
        progressBar.setVisibility(VISIBLE);
        tvDesc.setText(R.string.common_footer_loading);
    }

    @Override
    public void setError(RestError error) {
        progressBar.setVisibility(GONE);
        tvDesc.setText(VolleyUtils.getError(getContext(), error));
    }
}
