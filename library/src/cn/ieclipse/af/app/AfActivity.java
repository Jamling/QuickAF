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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.ieclipse.af.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.TitleBar;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年7月2日
 */
public abstract class AfActivity extends Activity implements OnClickListener {
    public static final String EXTRA_BUNDLE = "bundle";
    protected SystemBarTintManager mTintManager;
    private boolean overlay;
    private RelativeLayout mRootView;
    private TitleBar mTitleBar;
    private FrameLayout mContentView;
    private FrameLayout mBottomView;
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init widow feature
        initWindowFeature();
        // init main content layout
        initRootView();
        
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && isImmersive()) {
            initStatusBar();
        }
    }
    
    protected abstract int getContentLayout();
    
    @Override
    public void onClick(View v) {
    
    }
    
    protected void initWindowFeature() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }
    
    protected void initIntent(Bundle bundle) {
    
    }
    
    protected void initContentView() {
    
    }
    
    protected void initHeaderView() {
        TextView tv = new TextView(this);
        tv.setText("<");
        int padding = AppUtils.dp2px(this, 12);
        tv.setPadding(padding, padding, padding, padding);
        mTitleBar.addLeft(tv);
        
        tv = new TextView(this);
        tv.setText(getClass().getSimpleName());
        mTitleBar.addMiddle(tv);
    }
    
    protected void initData() {
    
    }
    
    protected void initBottomView() {
    
    }
    
    public boolean isOverlay() {
        return overlay;
    }
    
    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
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
        }
        else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
    }
    
    private void initRootView() {
        mRootView = new RelativeLayout(this);
        mTitleBar = new TitleBar(this);
        mTitleBar.setId(R.id.titleBar);
        
        mContentView = new FrameLayout(this);
        mBottomView = new FrameLayout(this);
        mBottomView.setId(R.id.bottomBar);
        
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        lpTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        mTitleBar.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        mRootView.addView(mTitleBar, lpTitle);
        
        RelativeLayout.LayoutParams lpBottom = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
                RelativeLayout.TRUE);
        lpBottom.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        mRootView.addView(mBottomView, lpBottom);
        
        RelativeLayout.LayoutParams lpContent = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpContent.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        if (isOverlay()) {
            lpContent.addRule(RelativeLayout.ALIGN_PARENT_TOP,
                    RelativeLayout.TRUE);
        }
        else {
            lpContent.addRule(RelativeLayout.BELOW, mTitleBar.getId());
        }
        lpContent.addRule(RelativeLayout.ABOVE, mBottomView.getId());
        mRootView.addView(mContentView, lpContent);
        
        int rootLayoutId = getContentLayout();
        if (rootLayoutId > 0) {
            // View.inflate(this, rootLayoutId, mContentView);
            // LayoutInflater.from(this).inflate(rootLayoutId, mContentView,
            // true);
            mContentView.addView(View.inflate(this, rootLayoutId, null),
                    new LayoutParams(LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
        }
        
        setContentView(mRootView, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
        // int rootLayoutId = getContentLayout();
        // if (rootLayoutId > 0) {
        // // View.inflate(this, rootLayoutId, mContentView);
        // // LayoutInflater.from(this).inflate(rootLayoutId, mContentView,
        // true);
        // mContentView.addView(View.inflate(this, rootLayoutId, null), new
        // LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        // }
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
    
    protected static void startActivity(Intent intent, Fragment f,
            Context context, int requestCode) {
        if (f != null) {
            f.startActivityForResult(intent, requestCode);
        }
        else if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        }
        else {
            context.startActivity(intent);
        }
    }
}
