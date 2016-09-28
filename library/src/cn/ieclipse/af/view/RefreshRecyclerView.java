/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.ieclipse.af.R;
import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.view.recycle.GridItemDecoration;
import cn.ieclipse.af.view.recycle.ItemOffsetDecoration;
import cn.ieclipse.af.view.recycle.ListItemDecoration;
import cn.ieclipse.af.view.recycle.RefreshEmptyView;

/**
 * 封装Recyclerview,实现上拉加载，下拉刷新，局部刷新，自定义empty view等功能。
 * <p>
 * 注：(recyclerview显示scrollbar需要在布局中设置android:scrollbars="vertical");
 *
 * @author wangjian
 * @date 2015/12/25.
 */
public class RefreshRecyclerView<T> extends LinearLayout implements View.OnClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    private Logger mLogger = Logger.getLogger(RefreshRecyclerView.class);

    /**
     * 禁用刷新和加载
     */
    public static final int REFRESH_MODE_NONE = 0x01;
    /**
     * 可下拉刷新
     */
    public static final int REFRESH_MODE_TOP = 0x02;
    /**
     * 可上拉加载
     */
    public static final int REFRESH_MODE_BOTTOM = 0x03;
    /**
     * 可上拉加载和下拉刷新
     */
    public static final int REFRESH_MODE_BOTH = 0x04;
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
     * 下拉刷新view
     */
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    /**
     * foot view
     */
    protected View mFootView;
    /**
     * emptyview 容器
     */
    protected RefreshEmptyView mEmptyView;
    /**
     * 网络错误显示的view
     */
    protected View mNetworkErrorLayout;
    /**
     * 数据为空显示的view
     */
    protected View mDataEmptyLayout;
    /**
     * 默认分隔线颜色
     */
    private int mDividerColor = Color.parseColor("#ffd8d8d8");//0x80bebebe;//0xffd8d8d8;
    /**
     * 默认分隔线高度
     */
    private int mDividerHeight = 1;
    /**
     * recyclerview 布局id
     */
    private int mRecyclerViewId;
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
    private int mPageSize = PAGE_SIZE;
    /**
     * 默认刷新方向
     */
    private int mRefreshMode = REFRESH_MODE_TOP;
    /**
     * 当前刷新方向
     */
    protected boolean isRefresh = true;
    /**
     * 是否正在加载
     */
    private boolean isLoading;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RefreshRecyclerView);
            mDividerColor = array.getColor(R.styleable.RefreshRecyclerView_dividerColor, mDividerColor);
            mDividerHeight = array.getDimensionPixelOffset(R.styleable.RefreshRecyclerView_dividerHeight,
                mDividerHeight);
            mRecyclerViewId = array.getResourceId(R.styleable.RefreshRecyclerView_recyclerView, mRecyclerViewId);
            array.recycle();
        }

        // 初始化xml中获取的recyclerview
        if (mRecyclerViewId > 0) {
            RecyclerView recyclerView = (RecyclerView) View.inflate(getContext(), mRecyclerViewId, null);
            setRecyclerView(recyclerView);
        }
    }

    /**
     * 设置recyclerview，此方需要显示调用，或在xml中配置recyclerView属性
     *
     * @param recyclerView 要显示的recyclerView
     */
    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
        if (mRecyclerView != null) {
            initView();
        }
    }

    /**
     * 获取设置的recyclerView
     *
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    private void initView() {
        if (mSwipeRefreshLayout == null) {
            // 用于存放mRecyclerView
            mSwipeRefreshLayout = new SwipeRefreshLayout(getContext());
            mSwipeRefreshLayout.setOnRefreshListener(this);
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
            // SwipeRefreshLayout中添加recyclerview
            mSwipeRefreshLayout.addView(mRecyclerView, params);

            // 设置默认布局管理器
            setLinearLayoutManager(LinearLayoutManager.VERTICAL);
            // 添加 recyclerview view
            addView(mSwipeRefreshLayout);
            //mSwipeRefreshLayout.setVisibility(GONE);

            observeRecycleViewStatus();
        }
    }

    /**
     * 初始换emptyView
     *
     * @param emptyView
     */
    public void initEmptyView(RefreshEmptyView emptyView) {
        if (emptyView != null) {
            if (mEmptyView != null) {
                removeView(mEmptyView);
            }
            // 设置emptyview
            mEmptyView = emptyView;
            addView(mEmptyView, getLayoutCenterLyParams());
            // 设置emptyview刷新监听
            mEmptyView.setOnRefreshListener(emptyViewRefreshListener);

            mDataEmptyLayout = mEmptyView.getDataEmptyLayout();
            mDataEmptyLayout.setOnClickListener(this);
            mNetworkErrorLayout = mEmptyView.getNetworkErrorLayout();
            mNetworkErrorLayout.setOnClickListener(this);

            // 初始换mEmptyView隐藏refresh view，显示EmptyView
            mSwipeRefreshLayout.setVisibility(GONE);
            mEmptyView.setVisibility(VISIBLE);
        }
    }

    public SwipeRefreshLayout getSwipyRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * empty view 的下拉监听
     */
    private SwipeRefreshLayout.OnRefreshListener emptyViewRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            // empty view 刷新触发mAfRecyclerView的刷新事件
            if (mOnRefreshListener != null) {
                mEmptyView.setEmptyType(RefreshEmptyView.TYPE_LOADING);
                // 触发RefreshRecyclerView的下拉刷新方法
                RefreshRecyclerView.this.onRefresh();
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v == mNetworkErrorLayout) {
            emptyRefresh();
        }
        else if (v == mDataEmptyLayout) {
            if (mOnEmptyRetryListener != null) {
                mOnEmptyRetryListener.onDataEmptyClick();
            }
            else {
                emptyRefresh();
            }
        }
        else if (v == mFootView) {
            if (mOnLoadRetryListener != null) {
                mOnLoadRetryListener.onFootViewClick();
            }
            else {
                doAutoLoadMore();
            }
        }
    }

    private void emptyRefresh() {
        if (!mEmptyView.isRefreshing()) {
            emptyViewRefreshListener.onRefresh();
        }
        if (mEmptyView != null) {
            mEmptyView.setRefreshing(true);
        }
    }

    private LinearLayout.LayoutParams getLayoutCenterLyParams() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        return params;
    }

    /**
     * 监听adapter中数据的变化
     */
    private RecyclerView.AdapterDataObserver mEmptyObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onChanged() {
            if (mAdapter != null && mEmptyView != null) {
                // 数据为空时
                if (mAdapter.getDataItemCount() <= 0) {
                    // listview数据为空时显示empty view
                    mEmptyView.setVisibility(VISIBLE);
                    // empty view显示空数据的界面
                    mEmptyView.setEmptyType(RefreshEmptyView.TYPE_DATA_EMPTY);
                    mSwipeRefreshLayout.setVisibility(GONE);
                }
                // 数据不为空时
                else if (mAdapter.getDataItemCount() > 0) {
                    // 恢复mEmptyView正在加载的初始状态
                    mEmptyView.setEmptyType(RefreshEmptyView.TYPE_LOADING);
                    mEmptyView.setVisibility(GONE);
                    mSwipeRefreshLayout.setVisibility(VISIBLE);
                }
            }
        }
    };

    /**
     * 设置RecyclerView adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null && mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
            if (adapter instanceof AfRecyclerAdapter) {
                mAdapter = (AfRecyclerAdapter) adapter;
                // 设置foot view 点击监听
                mFootView = mAdapter.getFootView();
                if (mFootView != null) {
                    mFootView.setOnClickListener(this);
                }
                setDataObserver();
            }
        }
    }

    /**
     * 添加adapter监听
     */
    private void setDataObserver() {
        if (mAdapter != null) {
            mAdapter.registerAdapterDataObserver(mEmptyObserver);
            // mEmptyObserver.onChanged();
        }
    }


    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public void smoothScrollToPosition(int position) {
        mRecyclerView.smoothScrollToPosition(position);
    }

    /**
     * 设置刷新indicator的颜色，默认黑色
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);
        if (mEmptyView != null) {
            mEmptyView.setColorSchemeResources(colorResIds);
        }
    }

    /**
     * 更新ItemDecoration
     *
     * @param decoration
     */
    private void removeOldItemDecoration(RecyclerView.ItemDecoration decoration) {
        if (mItemDecoration != null) {
            mRecyclerView.removeItemDecoration(mItemDecoration);
        }
        if (mOffsetDecoration != null) {
            mRecyclerView.removeItemDecoration(mOffsetDecoration);
        }

        mItemDecoration = decoration;
        mRecyclerView.addItemDecoration(mItemDecoration);
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
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setLinearLayoutManager(int orientation) {
        // 设置布局样式
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(orientation);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置分隔线
        mListDecoration = new ListItemDecoration(orientation);
        setDividerHeight(mDividerHeight);
        setDividerColor(mDividerColor);

        removeOldItemDecoration(mListDecoration);

        mRecyclerView.setLayoutManager(manager);
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
        mRecyclerView.setLayoutManager(gridManager);

        // 当itemType是head或footer时占满一行
        gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemType = mAdapter.getItemViewType(position);
                int spanCount = gridManager.getSpanCount();
                if (itemType == AfRecyclerAdapter.ITEM_VIEW_TYPE_FOOTER
                    || itemType == AfRecyclerAdapter.ITEM_VIEW_TYPE_HEADER) {
                    return spanCount;
                }
                else {
                    return 1;
                }
            }
        });
    }

    /**
     * 显示成gridview形式 支持水平和垂直方向
     *
     * @param column      列数
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setStaggeredGridLayoutManager(int column, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(column, orientation);
        //GridItemDecoration decoration = new GridItemDecoration(getContext());
        mOffsetDecoration = new ItemOffsetDecoration(mVerticalPadding, mHorizontalPadding);
        removeOldItemDecoration(mOffsetDecoration);
        mRecyclerView.setLayoutManager(manager);
    }

    /**
     * RecyclerView can perform several optimizations if it can know in advance that changes in
     * adapter content cannot change the size of the RecyclerView itself.
     * If your use of RecyclerView falls into this category, set this to true.
     *
     * @param hasFixedSize true if adapter changes cannot affect the size of the RecyclerView.
     */
    public void setHasFixedSize(boolean hasFixedSize) {
        mRecyclerView.setHasFixedSize(hasFixedSize);
    }

    /**
     * set the refresh mode to control the refresh direction
     *
     * @param refreshMode {@link #REFRESH_MODE_NONE} or
     *                    {@link #REFRESH_MODE_TOP} or
     *                    {@link #REFRESH_MODE_BOTTOM} or
     *                    {@link #REFRESH_MODE_BOTH}
     */
    public void setMode(int refreshMode) {
        this.mRefreshMode = refreshMode;
        if (refreshMode == REFRESH_MODE_NONE) {
            mSwipeRefreshLayout.setEnabled(false);
        }
        else if (refreshMode == REFRESH_MODE_TOP) {
            mSwipeRefreshLayout.setEnabled(true);
        }
        else if (refreshMode == REFRESH_MODE_BOTTOM) {
            mSwipeRefreshLayout.setEnabled(false);
        }
        else if (refreshMode == REFRESH_MODE_BOTH) {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * RecyclerView添加滚动监听
     */
    private void observeRecycleViewStatus() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 滚动停止时
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getFootView() != null) {
                    RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                    int orientation = VERTICAL;
                    if (manager instanceof StaggeredGridLayoutManager) {
                        orientation = ((StaggeredGridLayoutManager) manager).getOrientation();
                    }
                    else if (manager instanceof LinearLayoutManager) {
                        orientation = ((LinearLayoutManager) manager).getOrientation();
                    }

                    // 水平滚动加载
                    if (orientation == HORIZONTAL) {
                        // if (!recyclerView.canScrollHorizontally(-1)) {
                        //    // scrolled To start
                        // }
                        // else
                        if (!recyclerView.canScrollHorizontally(1)) {
                            // scrolled To end
                            doAutoLoadMore();
                        }
                    }
                    else {

                        // 竖直滚动加载
                        // if (!recyclerView.canScrollVertically(-1)) {
                        //    // Scrolled To Top
                        // }
                        // else
                        if (!recyclerView.canScrollVertically(1)) {
                            // scrolled To bottom
                            doAutoLoadMore();
                        }
                    }
                }
            }
        });
    }

    /**
     * 加载更多
     */
    private void doAutoLoadMore() {
        // mRecyclerView滚动到最后一条，切停止滚动 同时没有在刷新或加载
        boolean isRefreshing = mSwipeRefreshLayout.isRefreshing();
        if (!isRefreshing // 是否正在刷新
            && !isLoading// 是否正在加载
            && (mRefreshMode == REFRESH_MODE_BOTTOM // 可以上拉加载
            || mRefreshMode == REFRESH_MODE_BOTH)) {
            mLogger.d("auto load more");
            onLoadMore();
        }
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
    public void onLoadFinish(List<T> list) {
        if (mAdapter != null) {
            // isRefresh == true下拉刷新或默认是刷新操作
            if (isRefresh) {
                mAdapter.getDataList().clear();
            }
            else {//if (!isRefresh)
                // do nothing
            }
            mAdapter.addAll(list);
        }
        finishLoad();
    }

    /**
     * 加载失败，重置RecyclerView的加载状态
     */
    public void onLoadFailure() {
        if (mEmptyView != null) {
            mEmptyView.setEmptyType(RefreshEmptyView.TYPE_NET_ERROR);
        }
        finishLoad();
    }

    /**
     * 加载完成清除view的下拉或上拉刷新的状态
     */
    private void finishLoad() {
        // 清除RecyclerView的加载状态
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        // 清除empty的加载状态
        if (mEmptyView != null && mEmptyView.isRefreshing()) {
            mEmptyView.setRefreshing(false);
        }

        isLoading = false;
        // 上拉完成，恢复下拉可用状态
        // 防止mRefreshMode = REFRESH_MODE_NONE,恢复可刷新状态
        if (mRefreshMode != REFRESH_MODE_NONE) {
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    /**
     * 获取当前需要加载的页数
     *
     * @return
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * 设置pagesize
     */
    public void setPageSize(int size) {
        mPageSize = size;
    }

    /**
     * 执行刷新操作
     */
    public void refresh() {
        if (mEmptyView.getVisibility() == VISIBLE) {
            emptyRefresh();
        }
        else {
            if (!mSwipeRefreshLayout.isRefreshing()) {
                onRefresh();
            }
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        }
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

    @Override
    public void onRefresh() {
        // 下拉刷新
        if (mOnRefreshListener != null) {
            mLogger.d("onRefresh current direction = " + mRefreshMode);
            // 下拉刷新执行的操作
            mCurrentPage = PAGE_FIRST;
            isRefresh = true;
            mOnRefreshListener.onRefresh();
        }
    }

    /**
     * 上拉加载
     */
    protected void onLoadMore() {
        // 计算当前加载页数
        mLogger.d("view onLoadMore");
        calcCurrentPage();
        isRefresh = false;
        isLoading = true;
        // 上拉时禁用下拉
        mSwipeRefreshLayout.setEnabled(false);
        mOnRefreshListener.onLoadMore();
    }

    //=========================================================================
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public void setOnEmptyRetryListener(OnEmptyRetryListener onEmptyRetryListener) {
        this.mOnEmptyRetryListener = onEmptyRetryListener;
    }

    public void setOnEmptyRetryListener(OnLoadRetryListener onLoadRetryListener) {
        this.mOnLoadRetryListener = onLoadRetryListener;
    }

    /**
     * 下拉刷新监听器
     */
    private OnRefreshListener mOnRefreshListener;

    /**
     * 空数据界面点击监听器
     */
    private OnEmptyRetryListener mOnEmptyRetryListener;

    /**
     * 加载点击更多监听器
     */
    private OnLoadRetryListener mOnLoadRetryListener;

    public interface OnRefreshListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface OnEmptyRetryListener {

        void onDataEmptyClick();
    }

    public interface OnLoadRetryListener {

        void onFootViewClick();
    }
}
