/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.view.recycle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import cn.ieclipse.af.view.refresh.SwipyRefreshLayout;

/**
 * 其中包含scrollview，支持自定义布局刷新
 *
 * @author wangjian
 * @date 2016/1/4.
 */
public class RefreshEmptyView extends SwipyRefreshLayout {
    private ScrollView mScrollView;
    private FrameLayout mParentView;

    public RefreshEmptyView(Context context) {
        super(context);
        init();
    }

    public RefreshEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mScrollView = new ScrollView(getContext());
        mParentView = new FrameLayout(getContext());
        mParentView.setLayoutParams(getMyLayoutParams());

        mScrollView.addView(mParentView);
        addView(mScrollView, getMyLayoutParams());
    }

    public FrameLayout getParentView(){
        return mParentView;
    }

    public void onRefreshFinish(){
        setRefreshing(false);
    }

    private ViewGroup.LayoutParams getMyLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        return params;
    }
}
