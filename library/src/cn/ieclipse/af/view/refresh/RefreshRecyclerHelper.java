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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.recycle.GridItemDecoration;
import cn.ieclipse.af.view.recycle.ItemOffsetDecoration;
import cn.ieclipse.af.view.recycle.ListItemDecoration;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshRecyclerHelper<T> extends RefreshHelper<T> {

    /**
     * 当前的ItemDecoration
     */
    private RecyclerView.ItemDecoration mItemDecoration;
    /**
     * 默认list分隔线
     */
    private ListItemDecoration mListDecoration;
    /**
     * 默认流式分隔线
     */
    private ItemOffsetDecoration mOffsetDecoration;

    /**
     * 默认分隔线颜色
     */
    private int mDividerColor;
    /**
     * 默认分隔线高度
     */
    private int mDividerHeight = 1;

    /**
     * 分割线的宽度（网格布局有效）
     */
    private int mOffPadding = 1;
    private int mVerticalPadding = mOffPadding;
    private int mHorizontalPadding = mOffPadding;
    /**
     * adapter
     */
    protected RecyclerView.Adapter mAdapter;
    private RecyclerView mRecyclerView;


    public RefreshRecyclerHelper(RefreshLayout refreshLayout) {
        super(refreshLayout);
        mDividerColor = AppUtils.getColor(getContext(), android.R.color.darker_gray);
        mDividerHeight = AppUtils.dp2px(getContext(), 1);
        setLinearLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public RecyclerView getRecyclerView() {
        if (mRecyclerView != null) {
            return mRecyclerView;
        }
        return (RecyclerView) refreshLayout.getContentView();
    }

    protected Context getContext() {
        return refreshLayout.getContext();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    /**
     * 设置RecyclerView adapter
     *
     * @param adapter
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
     * 更新ItemDecoration
     *
     * @param decoration
     */
    private void removeOldItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (mItemDecoration != null) {
            getRecyclerView().removeItemDecoration(mItemDecoration);
        }
        if (mOffsetDecoration != null) {
            getRecyclerView().removeItemDecoration(mOffsetDecoration);
        }

        mItemDecoration = decoration;
        getRecyclerView().addItemDecoration(mItemDecoration);
    }

    /**
     * 设置分隔线颜色,实际是设置Paint中的{@link android.graphics.Paint setColor(int)}的颜色，仅在布局管理器是LinearLayoutManager有效
     *
     * @param color the color resources id or Color.parseColor(ARGB)
     */
    public void setDividerColor(int color) {
        this.mDividerColor = color;
        if (mListDecoration != null) {
            mListDecoration.setColor(color);
        }
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    /**
     * 设置分隔线的宽度，仅在布局管理器是LinearLayoutManager有效
     *
     * @param height px
     */
    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        if (mListDecoration != null) {
            mListDecoration.setSize(height);
        }
    }

    /**
     * 获取分割线的高度px，仅在布局管理器是LinearLayoutManager有效
     *
     * @return
     */
    public int getDividerHeight() {
        return mDividerHeight;
    }

    /**
     * 设置竖向padding（网格流式布局有效）
     *
     * @param verticalSpacing px
     */
    public void setVerticalSpacing(int verticalSpacing) {
        mVerticalPadding = verticalSpacing;
    }

    /**
     * 设置水平padding（网格流式布局有效）
     *
     * @param horizontalSpacing px
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        mHorizontalPadding = horizontalSpacing;
    }

    /**
     * 设置AfRecycleView默认样式
     *
     * @param orientation {@link android.support.v7.widget.LinearLayoutManager#HORIZONTAL} or
     *                    {@link android.support.v7.widget.LinearLayoutManager#VERTICAL}
     */
    public void setLinearLayoutManager(int orientation) {
        // 设置布局样式
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(orientation);
        getRecyclerView().setItemAnimator(new DefaultItemAnimator());
        // 设置分隔线
        mListDecoration = new ListItemDecoration(orientation);
        setDividerHeight(mDividerHeight);
        setDividerColor(mDividerColor);

        removeOldItemDecoration(mListDecoration);

        getRecyclerView().setLayoutManager(manager);
    }

    /**
     * 显示成gridview形式
     *
     * @param column 列数
     */
    public void setGridLayoutManager(int column) {
        final GridLayoutManager gridManager = new GridLayoutManager(getContext(), column);
        GridItemDecoration decoration = new GridItemDecoration(getContext());
        removeOldItemDecoration(decoration);
        getRecyclerView().setLayoutManager(gridManager);
        if (mAdapter != null) {
            // mAdapter.setSpanSizeLookup(gridManager);
            if (gridManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                // 当itemType是head或footer时占满一行
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int itemType = mAdapter.getItemViewType(position);
                        int spanCount = gridManager.getSpanCount();
                        if (itemType < 0) {
                            return spanCount;
                        }
                        else {
                            return 1;
                        }
                    }
                });
            }
        }
    }

    /**
     * 显示成gridview形式 支持水平和垂直方向
     *
     * @param column      列数
     * @param orientation {@link android.support.v7.widget.LinearLayoutManager#HORIZONTAL} or
     * {@link android.support.v7.widget.LinearLayoutManager#VERTICAL}
     */
    public void setStaggeredGridLayoutManager(int column, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(column, orientation);
        //GridItemDecoration decoration = new GridItemDecoration(getContext());
        mOffsetDecoration = new ItemOffsetDecoration(mVerticalPadding, mHorizontalPadding);
        removeOldItemDecoration(mOffsetDecoration);
        getRecyclerView().setLayoutManager(manager);
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
                else {
                    adapter.addAll(list);
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
