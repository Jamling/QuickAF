/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.recycler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.RefreshRecyclerView;

/**
 * 实现对emptyview是自定义和底部footview的自定义
 *
 * @author wangjian
 * @date 2016/9/27.
 */
public class MyRefreshRecyclerView extends RefreshRecyclerView {

    private MyEmptyView mMyEmptyView;

    public MyRefreshRecyclerView(Context context) {
        super(context);
    }

    public MyRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mMyEmptyView = new MyEmptyView(getContext());
        initEmptyView(mMyEmptyView);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
    }

    @Override
    protected void onLoadMore() {
        setBottomViewText(R.string.common_loading);
        super.onLoadMore();
    }

    @Override
    public void onLoadFinish(List list) {
        super.onLoadFinish(list);
        if (isRefresh) {
            setBottomViewText(R.string.common_click_load_more);
        }
    }

    @Override
    public void onLoadFailure() {
        super.onLoadFailure();
        setBottomViewText(R.string.error_loading_with_retry);
    }

    private void setBottomViewText(int resStr) {
        View footView = mAdapter.getFootView();
        View progress = footView.findViewById(R.id.progressBar);
        progress.setVisibility(GONE);

        TextView tv = (TextView) footView.findViewById(R.id.tv_desc);
        tv.setText(getResources().getString(resStr));
    }
}
