/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.view.recycle;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

/**
 * 自定义empty view
 *
 * @author wangjian
 * @date 2016/1/4.
 */
public abstract class RefreshEmptyView extends SwipeRefreshLayout {

    // 正在加载
    public static final int TYPE_LOADING = 0x01;
    // 网络错误
    public static final int TYPE_NET_ERROR = 0x02;
    // 数据为空
    public static final int TYPE_DATA_EMPTY = 0x03;

    private ScrollView mScrollView;

    /**
     * 正在加载view
     */
    protected View mLoadingLayout;
    /**
     * 网络错误view
     */
    protected View mNetworkErrorLayout;
    /**
     * 数据为空view
     */
    protected View mDataEmptyLayout;

    public RefreshEmptyView(Context context) {
        this(context, null);
    }

    public RefreshEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mScrollView = new ScrollView(getContext());
        mScrollView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mScrollView.addView(getEmptyLayout(context), getMyLayoutParams());
        // 设置默认显示正在加载
        setEmptyType(TYPE_LOADING);
        addView(mScrollView, getMyLayoutParams());
    }

    /**
     * 此处必须实现对方法mLoadingLayout、mNetworkErrorLayout、mDataEmptyLayout的赋值操作
     *
     * @param context
     * @return
     */
    protected abstract View getEmptyLayout(Context context);

    /**
     * 设置当前显示的emptyview
     *
     * @param emptyType {@link #TYPE_LOADING } ,{@link #TYPE_NET_ERROR} ,{@link #TYPE_DATA_EMPTY}
     */
    public void setEmptyType(int emptyType) {
        if (mLoadingLayout != null && mNetworkErrorLayout != null && mDataEmptyLayout != null) {
            switch (emptyType) {
                case TYPE_LOADING:
                    mLoadingLayout.setVisibility(VISIBLE);
                    mNetworkErrorLayout.setVisibility(GONE);
                    mDataEmptyLayout.setVisibility(GONE);
                    break;
                case TYPE_NET_ERROR:
                    mLoadingLayout.setVisibility(GONE);
                    mNetworkErrorLayout.setVisibility(VISIBLE);
                    mDataEmptyLayout.setVisibility(GONE);
                    break;
                case TYPE_DATA_EMPTY:
                    mLoadingLayout.setVisibility(GONE);
                    mNetworkErrorLayout.setVisibility(GONE);
                    mDataEmptyLayout.setVisibility(VISIBLE);
                    break;
            }
        }
    }

    public View getLoadingLayout() {
        return mLoadingLayout;
    }

    public View getNetworkErrorLayout() {
        return mNetworkErrorLayout;
    }

    public View getDataEmptyLayout() {
        return mDataEmptyLayout;
    }

    protected ViewGroup.LayoutParams getMyLayoutParams() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        return params;
    }
}
