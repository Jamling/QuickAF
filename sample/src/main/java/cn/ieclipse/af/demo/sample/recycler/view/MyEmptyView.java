/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.recycler.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.recycle.RefreshEmptyView;

/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/9/27.
 */
public class MyEmptyView extends RefreshEmptyView {

    public MyEmptyView(Context context) {
        super(context);
    }

    public MyEmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getEmptyLayout(Context context) {
        View v = View.inflate(context, R.layout.pull_to_refresh_empty_view, null);
        // 必须实现对以下3个view的赋值，否则EmptyView不起作用
        mLoadingLayout = v.findViewById(R.id.layout_loading);
        mNetworkErrorLayout = v.findViewById(R.id.layout_network_error);
        mDataEmptyLayout = v.findViewById(R.id.layout_empty_data);
        return v;
    }
}
