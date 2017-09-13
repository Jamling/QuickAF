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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import cn.ieclipse.af.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.TitleBar;

/**
 * Top Fragment of QuickAF, typically it's a BaseFragment in App module to extend this fragment to customize the app
 * Basic UI/Interaction.
 *
 * @author Jamling
 */
public abstract class AfFragment extends Fragment implements View.OnClickListener {

    // protected AfActivity mActivity;
    private boolean overlay = false;
    private boolean showTitleBar = false;
    private int windowBgColor = 0;
    private RelativeLayout mRootView;
    protected LayoutInflater mLayoutInflater;
    protected TitleBar mTitleBar;
    private FrameLayout mContentView;
    protected FrameLayout mBottomBar;

    protected abstract int getContentLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT >= 15 && container != null && container instanceof ViewPager) {
//            isInViewPager = true;
//        }
        // init argument
        Bundle bundle = getArguments();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            initIntent(bundle);
        }
        // init initial data
        initInitData();
        if (isTrimMode()) {
            View view = inflater.inflate(getContentLayout(), container, false);
            initContentView(view);
            initData();
            return view;
        }
        // init root view
        initRootView(inflater, container);

        if (isShowTitleBar()) {
            initHeaderView();
        }

        initContentView(mContentView);
        initBottomView();
        initData();
        return mRootView;
    }

    @Override
    public void onClick(View v) {

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

    protected void initIntent(Bundle bundle) {

    }

    protected void initInitData() {

    }

    protected void initHeaderView() {

    }

    protected void initContentView(View view) {

    }

    protected void initBottomView() {

    }

    protected void initData() {

    }

    @Override
    public Context getContext() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return super.getContext();
        }
        else {
            return getActivity();
        }
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
            this.windowBgColor = AppUtils.getColor(getActivity(), colorId);
        }
    }

    private void initRootView(LayoutInflater inflater, ViewGroup container) {
        Context context = getActivity();
        mLayoutInflater = inflater;//LayoutInflater.from(context);
        mRootView = new RelativeLayout(context);
        mRootView.setFitsSystemWindows(true);
        mRootView.setBackgroundColor(windowBgColor);
        mTitleBar = new TitleBar(context);
        mTitleBar.setId(R.id.titleBar);

        mContentView = new FrameLayout(context);
        mBottomBar = new FrameLayout(context);
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

        if (container != null) {
            mRootView.setLayoutParams(container.getLayoutParams());
        }
        else {
            mRootView.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
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
    //---------------->

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // mActivity = (AfActivity) activity;
    }

    //-------> lazy load
    /*
    1, In FragmentAdapter setUserVisibleHint->onCreateView(), so need wait onActivityCreated to call
    onFirstUserVisible()
    2, Only work after ICS_MR1 (15) in FragmentAdapter
    */
    private boolean isFirstVisible = true;
    private boolean isUIInitialize = false;
    private boolean isInViewPager = false;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private synchronized void init() {
        if (!isInViewPager || isUIInitialize) {
            onFirstUserVisible();
        }
        else {
            isUIInitialize = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isInViewPager = true;
        if (isVisibleToUser && isFirstVisible) {
            isFirstVisible = false;
            init();
        }
        if (isUIInitialize) {
            onUserVisible(isVisibleToUser);
        }
    }

    /**
     * Work greater ICS_MR1 (15) in FragmentAdapter to do something (e.g. fetching data from server) lazily (lazy load)
     */
    protected void onFirstUserVisible() {
    }

    protected void onUserVisible(boolean visible) {

    }

    //--------> for traditional mode, no TitleBar and no BottomBar
    private boolean trimMode = false;

    /**
     * Set inflate mode, if true to use tradition inflate, false (default) to create new root view with TitleBar and
     * BottomBar
     *
     * @param trimMode true to inflate content view only, default is false.
     */
    public void setTrimMode(boolean trimMode) {
        this.trimMode = trimMode;
    }

    public boolean isTrimMode() {
        return trimMode;
    }

    public FragmentManager getSubFragmentManager() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return getChildFragmentManager();
        }
        return getFragmentManager();
    }
}
