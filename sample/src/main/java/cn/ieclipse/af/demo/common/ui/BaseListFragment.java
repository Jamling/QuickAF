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
package cn.ieclipse.af.demo.common.ui;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.AppRefreshRecyclerHelper;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshRecyclerHelper;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class BaseListFragment<T> extends BaseFragment implements RefreshLayout.OnRefreshListener {
    protected RefreshLayout mRefreshLayout;
    protected RefreshRecyclerHelper mRefreshHelper;
    protected RecyclerView mListView;
    protected AfRecyclerAdapter<T> mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_recycler;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);
        mListView = (RecyclerView) mRefreshLayout.findViewById(R.id.rv);

        mRefreshHelper = generateRefreshHelper();
        mAdapter = generateAdapter();
        mRefreshHelper.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        load(false);
    }

    @Override
    public void onLoadMore() {
        load(false);
    }

    protected void load(boolean needCache) {
    }

    protected RefreshRecyclerHelper generateRefreshHelper() {
        AppRefreshRecyclerHelper helper = new AppRefreshRecyclerHelper(mRefreshLayout);
        return helper;
    }

    protected abstract AfRecyclerAdapter<T> generateAdapter();
}
