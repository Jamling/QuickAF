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
import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.recycle.GridItemDecoration;
import cn.ieclipse.af.view.recycle.ItemOffsetDecoration;
import cn.ieclipse.af.view.recycle.ListItemDecoration;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshRecyclerHelper<T> {

    // protected RecyclerView recyclerView;
    protected RefreshLayout refreshLayout;
    protected AfRecyclerAdapter adapter;

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
    protected AfRecyclerAdapter mAdapter;
    /**
     * 分页第一页索引
     */
    private static final int PAGE_FIRST = 1;
    /**
     * 分页加载每一页的大小
     */
    private static final int PAGE_SIZE = 10;
    /**
     * 分页加载当前页数 mCurrentPage
     */
    private int mCurrentPage = PAGE_FIRST;
    /**
     * adapter中数据条目
     */
    private int mItemCount = 0;
    /**
     * 每页数据大小
     */
    private int mPageSize = PAGE_SIZE;

    protected Logger mLogger = Logger.getLogger(getClass());

    public RefreshRecyclerHelper(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
        mDividerColor = AppUtils.getColor(getContext(), android.R.color.darker_gray);
        mDividerHeight = AppUtils.dp2px(getContext(), 1);
        setLinearLayoutManager(LinearLayoutManager.VERTICAL);
    }

    public RecyclerView getRecyclerView() {
        return (RecyclerView) refreshLayout.getContentView();
    }

    private Context getContext() {
        return refreshLayout.getContext();
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
            mAdapter.setSpanSizeLookup(gridManager);
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

    //------------------------update item start----------------------------

    /**
     * 更新item (item 必须重写equals())
     *
     * @param item
     */
    public void updateItem(T item) {
        if (mAdapter != null) {
            int idx = getChooseItemIndex(item);
            mLogger.d("try to refresh item idx = " + idx);
            if (idx >= 0) {
                mAdapter.updateItem(idx, item);
            }
        }
    }

    /**
     * 删除item (item 必须重写equals())
     *
     * @param item
     */
    public void deleteItem(T item) {
        if (mAdapter != null) {
            int idx = getChooseItemIndex(item);
            mLogger.d("try to delete item idx= " + idx);
            if (idx >= 0) {
                mAdapter.deleteItem(idx);
            }
        }
    }

    /**
     * adapter 中的T 必须重写equals()
     *
     * @param item
     *
     * @return
     */
    private int getChooseItemIndex(T item) {
        int idx = -1;
        if (mAdapter != null && item != null) {
            for (int i = 0; i < mAdapter.getDataList().size(); i++) {
                if (item.equals(mAdapter.getItem(i))) {
                    idx = i;
                    return idx;
                }
            }
            return idx;
        }
        return idx;
    }

    /**
     * 加载完成将数据添加到adapter中
     *
     * @param list 数据
     */
    public void onLoadFinish(List<T> list, int total, int pageSize) {
        if (pageSize > 0) {
            this.mPageSize = pageSize;
        }
        if (mAdapter != null) {
            // 下拉刷新或默认是刷新操作
            if (refreshLayout.isRefresh()) {
                mAdapter.getDataList().clear();
            }
            else {
                // do nothing
            }
            mAdapter.addAll(list);
        }
        else {
            refreshLayout.showEmptyView();
        }

        refreshLayout.onRefreshComplete();
    }

    /**
     * 加载失败，重置RecyclerView的加载状态
     */
    public void onLoadFailure(RestError error) {
        if (refreshLayout.getEmptyView() != null) {
            refreshLayout.getEmptyView().showErrorLayout(error);
        }
        refreshLayout.onRefreshComplete();
    }

    /**
     * 计算页码
     */
    private void calcCurrentPage() {
        if (mAdapter != null) {
            mItemCount = mAdapter.getDataItemCount();
            int p = mItemCount / mPageSize;
            if (mItemCount % mPageSize >= 0) {
                mCurrentPage = p + 1;
            }
            else {
                mCurrentPage = p;
            }
        }
        if (mCurrentPage <= 0) {
            mCurrentPage = PAGE_FIRST;
        }
        mLogger.d("current page : " + mCurrentPage + ", item count : " + mItemCount);
    }

    /**
     * 监听adapter中数据的变化
     */
    private RecyclerView.AdapterDataObserver mEmptyObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            if (getRecyclerView().getAdapter() != null && refreshLayout.getEmptyView() != null) {
                RecyclerView.Adapter adapter = getRecyclerView().getAdapter();
                // 数据为空时
                if (adapter.getItemCount() <= 0) {
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
