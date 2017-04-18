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
package cn.ieclipse.af.view.refresh;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.view.recycle.ListDividerItemDecoration;
import cn.ieclipse.af.view.recycle.RecyclerHelper;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshRecyclerHelper<T> extends RefreshHelper<T> {

    /**
     * adapter
     */
    protected RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;
    protected RecyclerHelper mRecyclerHelper;


    public RefreshRecyclerHelper(RefreshLayout refreshLayout) {
        super(refreshLayout);
        mRecyclerHelper = new RecyclerHelper(getRecyclerView());
        // set default layout manager
        setLinearLayoutManager(RecyclerHelper.VERTICAL);
        // set default divider
        setItemDecoration(new ListDividerItemDecoration(getContext(), RecyclerHelper.VERTICAL));
    }

    public RecyclerView getRecyclerView() {
        if (mRecyclerView != null) {
            return mRecyclerView;
        }
        return (RecyclerView) refreshLayout.getContentView();
    }

    public RecyclerHelper getRecyclerHelper() {
        return mRecyclerHelper;
    }

    protected Context getContext() {
        return refreshLayout.getContext();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        mRecyclerHelper.setRecyclerView(getRecyclerView());
    }

    /**
     *  Set RecyclerView adapter ({@link cn.ieclipse.af.adapter.AfRecyclerAdapter})
     *
     * @param adapter {@link cn.ieclipse.af.adapter.AfRecyclerAdapter}
     */
    public void setAdapter(AfRecyclerAdapter adapter) {
        if (adapter != null && getRecyclerView() != null) {
            if (getRecyclerView().getAdapter() != null) {
                getRecyclerView().getAdapter().unregisterAdapterDataObserver(mEmptyObserver);
            }
            this.mAdapter = adapter;
            getRecyclerView().setAdapter(adapter);
            adapter.registerAdapterDataObserver(mEmptyObserver);
        }
    }

    /**
     * Set {@link cn.ieclipse.af.view.recycle.ListDividerItemDecoration} color
     *
     * @param color the color (ARGB format)
     */
    public void setDividerColor(int color) {
        mRecyclerHelper.setDividerColor(color);
    }

    /**
     * @see cn.ieclipse.af.view.recycle.RecyclerHelper#setDividerHeight(int)
     */
    public void setDividerHeight(int height) {
        mRecyclerHelper.setDividerHeight(height);
    }

    /**
     * @see RecyclerHelper#setItemDecoration(android.support.v7.widget.RecyclerView.ItemDecoration)
     */
    public void setItemDecoration(RecyclerView.ItemDecoration decoration) {
        mRecyclerHelper.setItemDecoration(decoration);
    }

    /**
     * @see RecyclerHelper#setLinearLayoutManager(int)
     */
    public void setLinearLayoutManager(int orientation) {
        mRecyclerHelper.setLinearLayoutManager(orientation);
    }

    /**
     * @see RecyclerHelper#setGridLayoutManager(int)
     */
    public void setGridLayoutManager(int column) {
        mRecyclerHelper.setGridLayoutManager(column);
    }

    /**
     * @see RecyclerHelper#setStaggeredGridLayoutManager(int, int)
     */
    public void setStaggeredGridLayoutManager(int column, int orientation) {
        mRecyclerHelper.setStaggeredGridLayoutManager(column, orientation);
    }
    
    /**
     * @see cn.ieclipse.af.view.recycle.RecyclerHelper#scrollToPosition(int)
     */
    public void scrollToPosition(int position) {
        mRecyclerHelper.scrollToPosition(position);
    }

    /**
     * RecyclerView can perform several optimizations if it can know in advance that changes in
     * adapter content cannot change the size of the RecyclerView itself.
     * If your use of RecyclerView falls into this category, set this to true.
     *
     * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
     */
    public void setHasFixedSize(boolean hasFixedSize) {
        getRecyclerView().setHasFixedSize(hasFixedSize);
    }

    /**
     * Return the total actual data count in adapter/recycler view
     *
     * @return total count
     */
    protected int getItemCount() {
        if (mAdapter instanceof AfRecyclerAdapter) {
            return ((AfRecyclerAdapter) mAdapter).getDataList().size();
        }
        return mAdapter.getItemCount();
    }

    protected boolean isEmpty() {
        if (mAdapter instanceof AfRecyclerAdapter) {
            return mAdapter.getItemCount() - ((AfRecyclerAdapter) mAdapter).getFooterCount() <= 0;
        }
        return getRecyclerView().getAdapter().getItemCount() <= 0;
    }

    protected void setAdapterData(List<T> list) {
        if (mAdapter != null) {
            if (mAdapter instanceof AfRecyclerAdapter) {
                AfRecyclerAdapter adapter = (AfRecyclerAdapter) mAdapter;
                // 下拉刷新或默认是刷新操作
                if (refreshLayout.isRefresh()) {
                    if (isKeepLoaded()) {
                        adapter.add2Top(list);
                    }
                    else {
                        adapter.setDataList(list);
                    }
                }
                else if (refreshLayout.isLoadMore()) {
                    adapter.addAll(list);
                }
                else {
                    adapter.setDataList(list);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        else {
            refreshLayout.showEmptyView();
        }
    }

    /**
     * 监听adapter中数据的变化
     */
    private RecyclerView.AdapterDataObserver mEmptyObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            if (getRecyclerView().getAdapter() != null && refreshLayout.getEmptyView() != null) {
                // 数据为空时
                if (isEmpty()) {
                    if (refreshLayout.getEmptyView() != null) {
                        refreshLayout.getEmptyView().showEmptyLayout();
                    }
                    refreshLayout.showEmptyView();
                }
                // 数据不为空时
                else {
                    // 恢复mEmptyView正在加载的初始状态
                    refreshLayout.hideEmptyView();
                }
            }
        }
    };
}
