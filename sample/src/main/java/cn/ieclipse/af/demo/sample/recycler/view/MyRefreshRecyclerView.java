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
        // 设置empty view
        mMyEmptyView = new MyEmptyView(getContext());
        initEmptyView(mMyEmptyView);
        // 设置自动加载
        setAutoLoad(true);
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
        if (getCurRefreshDirection() == CUR_NONE) {
            setBottomViewText(R.string.common_click_load_more);
        }
    }

    @Override
    public void onLoadFailure() {
        super.onLoadFailure();
        setBottomViewText(R.string.error_loading_with_retry);
    }

    /**
     * 控制footer view显示文字
     *
     * @param resStr
     */
    private void setBottomViewText(int resStr) {
        View footView = mAdapter.getFooterView();
        if (footView == null) {
            return;
        }
        View progress = footView.findViewById(R.id.progressBar);
        progress.setVisibility(GONE);

        TextView tv = (TextView) footView.findViewById(R.id.tv_desc);
        tv.setText(getResources().getString(resStr));
    }
}
