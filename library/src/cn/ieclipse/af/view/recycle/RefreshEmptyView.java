/*
 * Copyright 2014-2016 QuickAF
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
package cn.ieclipse.af.view.recycle;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import cn.ieclipse.af.view.RefreshRecyclerView;

/**
 * 自定义empty view
 *
 * @author wangjian
 * @date 2016/1/4.
 */
public abstract class RefreshEmptyView extends SwipeRefreshLayout implements View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    // 正在加载
    public static final int TYPE_LOADING = 0x01;
    // 网络错误
    public static final int TYPE_NET_ERROR = 0x02;
    // 数据为空
    public static final int TYPE_DATA_EMPTY = 0x03;

    private RefreshRecyclerView mRecyclerView;
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
        setOnRefreshListener(this);
        // 设置默认显示正在加载
        setEmptyType(TYPE_LOADING);
        addView(getEmptyLayout(context), getEmptyLayoutParams());
        // 设置监听
        if (mLoadingLayout != null && mNetworkErrorLayout != null && mDataEmptyLayout != null) {
            mNetworkErrorLayout.setOnClickListener(this);
            mDataEmptyLayout.setOnClickListener(this);
        }
    }

    /**
     * 此处必须实现对方法mLoadingLayout、mNetworkErrorLayout、mDataEmptyLayout的赋值操作
     *
     * @param context
     * @return
     */
    protected abstract View getEmptyLayout(Context context);

    @Override
    public void onRefresh() {
        if (mRecyclerView != null) {
            setEmptyType(RefreshEmptyView.TYPE_LOADING);
            mRecyclerView.onRefresh();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mNetworkErrorLayout) {
            clickRefresh();
        }
        else if (v == mDataEmptyLayout) {
            RefreshRecyclerView.OnEmptyRetryListener listener = mRecyclerView.getOnEmptyRetryListener();
            if (listener != null) {
                listener.onDataEmptyClick();
            }
            else {
                clickRefresh();
            }
        }
    }

    public void clickRefresh() {
        if (!isRefreshing()) {
            onRefresh();
        }
        setRefreshing(true);
    }

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

    public void setRecyclerView(RefreshRecyclerView recyclerView) {
        mRecyclerView = recyclerView;
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

    protected ViewGroup.LayoutParams getEmptyLayoutParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        return params;
    }
}
