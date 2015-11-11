/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年7月2日
 */
public abstract class BaseActivity extends Activity implements OnClickListener {
    public static final String EXTRA_BUNDLE = "bundle";
    protected SystemBarTintManager mTintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init main content layout
        int rootLayoutId = getContentLayout();
        if (rootLayoutId > 0) {
            setContentView(getContentLayout());
        }

        // init intent
        Bundle bundle = getIntent().getBundleExtra(EXTRA_BUNDLE);
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            initIntent(bundle);
        }

        // init header
        initHeaderView();

        // init content layout
        initContentView();

        // init bottom bar
        initBottomView();

        // init data
        initData();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            initStatusBar();
        }
    }

    protected abstract int getContentLayout();

    @Override
    public void onClick(View v) {

    }

    protected void initIntent(Bundle bundle) {

    }

    protected void initContentView() {

    }

    protected void initHeaderView() {

    }

    protected void initData() {

    }

    protected void initBottomView() {

    }

    protected void setOnClickListener(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.setOnClickListener(this);
                }
            }
        }
    }

    public int getStatusBarHeight() {
        if (mTintManager != null) {
            return mTintManager.getConfig().getStatusBarHeight();
        }
        return 0;
    }

    protected void initStatusBar() {
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintColor(getStatusBarColor());
    }

    protected int getStatusBarColor() {
        return getResources().getColor(android.R.color.black);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected static void startActivity(Intent intent, Fragment f, Context context, int requestCode) {
        if (f != null) {
            f.startActivityForResult(intent, requestCode);
        } else if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }
}
