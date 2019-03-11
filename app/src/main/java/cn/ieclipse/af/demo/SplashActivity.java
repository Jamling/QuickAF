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
package cn.ieclipse.af.demo;

import android.content.Intent;
import android.os.Debug;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.my.LoginActivity;
import cn.ieclipse.af.util.AppUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class SplashActivity extends BaseActivity {
    private Handler mHandler = new Handler();
    private long mDelay = 800;

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setShowTitleBar(false);

        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            setSkipCreate(true);
            finish();
        }
    }

    @Override
    protected int getStatusBarColor() {
        return AppUtils.getColor(this, android.R.color.transparent);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        ImageView imageView = new ImageView(view.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ((FrameLayout) view).addView(imageView,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        imageView.setImageResource(R.color.colorPrimary);

        TextView textView = new TextView(view.getContext());
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(AppUtils.getColor(view.getContext(), R.color.white));
        textView.setTextSize(24);
        textView.setText(R.string.app_name);
        ((FrameLayout) view).addView(textView,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    protected void initData() {
        super.initData();

        if (AppConfig.showGuide(this)) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goGuide();
                }
            }, mDelay);
        }
        else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goHome();
                }
            }, mDelay);
        }
    }

    private void goHome() {
        startActivity(MainActivity.create(this));
        finish();
    }

    private void goGuide() {
        startActivity(IntroActivity.create(this));
        finish();
    }

    private void goLogin() {
        startActivity(LoginActivity.create(this));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Debug.stopMethodTracing();
    }
}
