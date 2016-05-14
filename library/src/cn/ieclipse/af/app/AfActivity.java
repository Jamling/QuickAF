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
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.ieclipse.af.R;
import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.TitleBar;

/**
 * Top Activity of QuickAF, typically it's a BaseActivity in App module to extend this activity to customize the app
 * basic UI/Interaction
 *
 * @author Jamling
 */
public abstract class AfActivity extends Activity implements OnClickListener {
    public static final String EXTRA_DATA = "af.data";
    public static final String EXTRA_ID = "af.id";

    protected Logger mLogger = Logger.getLogger(getClass());
    protected SystemBarTintManager mTintManager;

    private boolean overlay = false;
    private boolean showTitleBar = true;
    private int windowBgColor = 0;
    private RelativeLayout mRootView;
    private LayoutInflater mLayoutInflater;
    protected TitleBar mTitleBar;
    private FrameLayout mContentView;
    protected FrameLayout mBottomBar;
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowBgColor = AppUtils.getColor(this, android.R.color.transparent);
        // 1, init intent
        Bundle bundle = getIntent().getExtras();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            initIntent(bundle);
        }
        // 2, init widow feature
        initWindowFeature();
        
        // 3, init root content layout
        initRootView();
        // 4, init title bar
        // if (isShowTitleBar())
        {
            // init header
            initHeaderView();
        }
        // 5, init content layout
        initContentView(mContentView);
        initContentView();
        
        // 6, init bottom bar
        initBottomView();
        
        // 7, init data
        initData();
        
        // 8, init status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
    
    @Deprecated
    protected void initContentView() {

    }
    
    protected void initContentView(View view) {

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

    public boolean isImmersiveMode() {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        return (winParams.flags | bits) == winParams.flags;
    }

    /**
     * Set translucent status (immersing mode, new feature from KatKit).
     *
     * @param immersiveMode
     *
     * @throws IllegalStateException when window has been built.
     */
    public void setImmersiveMode(boolean immersiveMode) {
        if (mRootView != null) {
            throw new IllegalStateException(
                "Can't set immersive mode after the content view has been set, if you need immersive mode, please "
                    + "call it in initWindowFeature()");
        }
        setTranslucentStatus(immersiveMode);
    }

    public boolean isOverlay() {
        return overlay;
    }
    
    /**
     * Set should overlay main content with title bar (if show)
     *
     * @param overlay whether overlay or not
     */
    public void setOverlay(boolean overlay) {
        if (this.overlay != overlay) {
            if (mContentView != null) {
                RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) mContentView
                    .getLayoutParams();
                setContentViewLayoutParams(lp, overlay, isShowTitleBar());
            }
        }
        this.overlay = overlay;
    }
    
    public boolean isShowTitleBar() {
        return showTitleBar || (mTitleBar != null && mTitleBar.getVisibility() != View.GONE);
    }
    
    public void setShowTitleBar(boolean showTitleBar) {
        if (this.showTitleBar != showTitleBar) {
            this.showTitleBar = showTitleBar;
            if (mTitleBar != null) {
                mTitleBar.setVisibility(showTitleBar ? View.VISIBLE : View.GONE);
            }
            if (mContentView != null) {
                RelativeLayout.LayoutParams lp = (android.widget.RelativeLayout.LayoutParams) mContentView
                    .getLayoutParams();
                setContentViewLayoutParams(lp, isOverlay(), isShowTitleBar());
            }
        }
    }
    
    public void setWindowBackground(int colorId) {
        if (colorId > 0) {
            this.windowBgColor = AppUtils.getColor(this, colorId);
        }
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
        return getResources().getColor(android.R.color.transparent);
    }
    
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        WindowManager.LayoutParams winParams = getWindow().getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        boolean oldOn = (winParams.flags | bits) == winParams.flags;
        if (on == oldOn) {
            return;
        }
        if (on) {
            winParams.flags |= bits;
        }
        else {
            winParams.flags &= ~bits;
        }
        getWindow().setAttributes(winParams);
        if (mRootView != null) {
            //mRootView.setFitsSystemWindows(on);
        }
    }
    
    private void initRootView() {
        mLayoutInflater = LayoutInflater.from(this);
        mRootView = new RelativeLayout(this) {
//            @Override
//            protected boolean fitSystemWindows(Rect insets) {
//                return super.fitSystemWindows(insets);
//            }
//
//            @TargetApi(20)
//            @Override
//            public WindowInsets dispatchApplyWindowInsets(WindowInsets insets) {
//                mRootView.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(), insets
//                    .getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
//                return super.dispatchApplyWindowInsets(insets);
//            }
        };
        mRootView.setFitsSystemWindows(true);
        mRootView.setBackgroundColor(windowBgColor);
        mTitleBar = new TitleBar(this);
        mTitleBar.setId(R.id.titleBar);
        
        mContentView = new FrameLayout(this);
        mBottomBar = new FrameLayout(this);
        mBottomBar.setId(R.id.bottomBar);
        
        RelativeLayout.LayoutParams lpTitle = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpTitle.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        lpTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        mTitleBar.setLayoutParams(lpTitle);
        // init set title bar visibility, can't use isShowTitleBar()
        mTitleBar.setVisibility(showTitleBar ? View.VISIBLE : View.GONE);
        
        RelativeLayout.LayoutParams lpBottom = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpBottom.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        lpBottom.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        mBottomBar.setLayoutParams(lpBottom);
        mRootView.addView(mBottomBar);
        
        RelativeLayout.LayoutParams lpContent = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lpContent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        lpContent.addRule(RelativeLayout.ABOVE, mBottomBar.getId());
        setContentViewLayoutParams(lpContent, isOverlay(), isShowTitleBar());
        
        mRootView.addView(mContentView);
        mRootView.addView(mTitleBar);
        
        int rootLayoutId = getContentLayout();
        if (rootLayoutId > 0) {
            mLayoutInflater.inflate(rootLayoutId, mContentView, true);
        }
        
        setContentView(mRootView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    
    private void setContentViewLayoutParams(RelativeLayout.LayoutParams lp, boolean overlay, boolean showTitleBar) {
        if (lp != null) {
            if (overlay || !showTitleBar) {
                lp.getRules()[RelativeLayout.BELOW] = 0;
                lp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            }
            else {
                lp.getRules()[RelativeLayout.ALIGN_PARENT_TOP] = 0;
                lp.addRule(RelativeLayout.BELOW, mTitleBar.getId());
            }
            mContentView.setLayoutParams(lp);
        }
    }
    
    protected void addView(View v, boolean full) {
        final RelativeLayout.LayoutParams lp;
        if (full) {
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        }
        else {
            lp = (RelativeLayout.LayoutParams) mContentView.getLayoutParams();
        }
        if (v.getParent() == null) {
            mRootView.addView(v, lp);
        }
    }
    
    protected boolean removeView(View v) {
        if (v.getParent() == null) {
            return false;
        }
        ((ViewGroup) v.getParent()).removeView(v);
        return true;
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
}
