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
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import java.util.HashMap;
import java.util.Map;

import cn.ieclipse.af.R;
import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.view.VScrollView;

/**
 * RefreshLayout is the parent view of {@link android.support.v4.widget.SwipeRefreshLayout SwipeRefreshLayout}.
 * <p>
 * The struct is:
 * <pre>
 *     &lt;RefreshLayout&gt;
 *          &lt;android.support.v4.widget.SwipeRefreshLayout&gt;
 *              &lt;content_view... &gt;
 *          &lt;android.support.v4.widget.SwipeRefreshLayout&gt;
 *              &lt;empty_view... &gt;
 * </pre>
 * </p>
 *
 * @author Jamling
 */
public class RefreshLayout extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener,
    EmptyView.RetryListener {

    /**
     * 禁用刷新和加载
     */
    public static final int REFRESH_MODE_NONE = 0x00;
    /**
     * 可下拉刷新
     */
    public static final int REFRESH_MODE_TOP = 0x01;
    /**
     * 可上拉加载
     */
    public static final int REFRESH_MODE_BOTTOM = 0x02;
    /**
     * 可上拉加载和下拉刷新
     */
    public static final int REFRESH_MODE_BOTH = 0x03;

    // 无刷新
    private static final int LOADING_NONE = 0;
    // 下拉刷新
    private static final int LOADING_REFRESH = 1;
    // 上拉加载更多
    private static final int LOADING_MORE = -1;

    private int mLoading = LOADING_NONE;

    private int mRefreshMode = REFRESH_MODE_TOP;

    /**
     * 是否滚动到底部自动加载
     */
    private boolean mAutoLoad = true;

    private boolean mEnableLoadMore = true;

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected OnRefreshListener mOnRefreshListener;
    protected SwipeRefreshLayout mContentViewWrapper;
    protected SwipeRefreshLayout mEmptyViewWrapper;
    protected View mContentView;
    protected EmptyView mEmptyView;
    protected FooterView mFooterView;
    protected LayoutInflater mLayoutInflater;
    protected Logger mLogger = Logger.getLogger(getClass());

    protected void init(Context context, AttributeSet attrs) {
        mContentViewWrapper = new SwipeRefreshLayout(context);
        mEmptyViewWrapper = new SwipeRefreshLayout(context);
        mContentViewWrapper.setOnRefreshListener(this);
        mEmptyViewWrapper.setOnRefreshListener(this);

        addView(mContentViewWrapper,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(mEmptyViewWrapper,
            new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mLayoutInflater = LayoutInflater.from(context);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RefreshLayout);
            handleStyledAttributes(a);
            a.recycle();
        }

        if (mEmptyView == null) {
            mEmptyViewWrapper.setVisibility(View.GONE);
        }
        else {
            mContentViewWrapper.setVisibility(GONE);
        }
        internalRegisterDetector(VScrollView.class, new RefreshVScrollDetector());
        internalRegisterDetector(RecyclerView.class, new RefreshRecyclerDetector());
        internalRegisterDetector(AbsListView.class, new RefreshListViewDetector());
        findDetector();
    }

    protected void handleStyledAttributes(TypedArray a) {
        int contentId = a.getResourceId(R.styleable.RefreshLayout_ptr_content, 0);
        if (contentId > 0) {
            setContentView(contentId);
        }
        int emptyId = a.getResourceId(R.styleable.RefreshLayout_ptr_empty, 0);
        if (emptyId > 0) {
            setEmptyView(emptyId);
        }

        mAutoLoad = a.getBoolean(R.styleable.RefreshLayout_ptr_autoLoad, mAutoLoad);
    }

    private void findDetector() {
        if (mContentView == null) {
            return;
        }
        if (getDetector() != null) {
            getDetector().setEnabled(false);
        }
        for (Class key : mDetectorMap.keySet()) {
            if (key.isInstance(mContentView)) {
                RefreshDetector detector = mDetectorMap.get(key);
                if (detector != null) {
                    this.mDetector = detector;
                    detector.setEnabled(true);
                }
                break;
            }
        }
    }

    @Override
    public void onDataEmptyClick() {
        onRefresh();
    }

    @Override
    public void onErrorClick() {
        onRefresh();
    }

    @Override
    public void onRefresh() {
        if (!mContentViewWrapper.isRefreshing() && mContentViewWrapper.getVisibility() == View.VISIBLE) {
            mContentViewWrapper.setRefreshing(true);
        }
        else if (!mEmptyViewWrapper.isRefreshing() && mEmptyViewWrapper.getVisibility() == View.VISIBLE) {
            mEmptyViewWrapper.setRefreshing(true);
        }
        mLoading = LOADING_REFRESH;
        if (getFooterView() != null) {
            getFooterView().setLoading(null);
        }
        if (mOnRefreshListener != null) {
            mOnRefreshListener.onRefresh();
        }
    }

    public void loadMore() {
        boolean isRefreshing = mContentViewWrapper.isRefreshing();
        if (!isRefreshing // 是否正在刷新
            && mLoading == LOADING_NONE // 是否正在加载
            && (mRefreshMode & REFRESH_MODE_BOTTOM) != 0 && isEnableLoadMore()) {
            mLogger.d("load more");
            if (getFooterView() != null) {
                getFooterView().setLoading(null);
            }
            mContentViewWrapper.setRefreshing(true);
            mLoading = LOADING_MORE;
            if (mOnRefreshListener != null) {
                mOnRefreshListener.onLoadMore();
            }
        }
    }

    public void onRefreshComplete() {
        mLogger.v("onRefreshComplete");
        // 清除RecyclerView的加载状态
        if (mContentViewWrapper.isRefreshing()) {
            mContentViewWrapper.setRefreshing(false);
        }

        // 清除empty的加载状态
        if (mEmptyViewWrapper.isRefreshing()) {
            mEmptyViewWrapper.setRefreshing(false);
        }

        mLoading = LOADING_NONE;
        // 上拉完成，恢复下拉可用状态
        // 防止mRefreshMode = REFRESH_MODE_NONE,恢复可刷新状态
        if (mRefreshMode != REFRESH_MODE_NONE) {
            mContentViewWrapper.setEnabled(true);
        }
    }

    public void showEmptyLoading() {
        if (mEmptyView != null) {
            mEmptyView.showLoadingLayout();
        }
        showEmptyView();
    }

    public void showEmptyView() {
        mEmptyViewWrapper.setVisibility(View.VISIBLE);
        mContentViewWrapper.setVisibility(View.GONE);
    }

    public void hideEmptyView() {
        mEmptyViewWrapper.setVisibility(View.GONE);
        mContentViewWrapper.setVisibility(View.VISIBLE);
    }

    public void setFooterView(FooterView footerView) {
        this.mFooterView = footerView;
    }

    public FooterView getFooterView() {
        return mFooterView;
    }

    public static abstract class RefreshDetector<T> {
        protected RefreshLayout mRefresh;
        protected T view;

        private void setRefresh(RefreshLayout refreshLayout) {
            this.mRefresh = refreshLayout;
        }

        public RefreshLayout getRefresh() {
            return mRefresh;
        }

        private void setView(T t) {
            this.view = t;
        }

        public T getView() {
            return view;
        }

        public abstract void setEnabled(boolean enable);
    }

    public enum Mode {
        /**
         * Disable all Pull-to-Refresh gesture and Refreshing handling
         */
        DISABLED(0x0),

        /**
         * Only allow the user to Pull from the start of the Refreshable View to
         * refresh. The start is either the Top or Left, depending on the
         * scrolling direction.
         */
        PULL_FROM_START(0x1),

        /**
         * Only allow the user to Pull from the end of the Refreshable View to
         * refresh. The start is either the Bottom or Right, depending on the
         * scrolling direction.
         */
        PULL_FROM_END(0x2),

        /**
         * Allow the user to both Pull from the start, from the end to refresh.
         */
        BOTH(0x3);
        private int mIntValue;

        Mode(int modeInt) {
            mIntValue = modeInt;
        }
    }

    private Map<Class, RefreshDetector> mDetectorMap = new HashMap<>();
    private RefreshDetector mDetector;

    public void registerDetector(Class clazz, RefreshDetector detector) {
        internalRegisterDetector(clazz, detector);
        findDetector();
    }

    private void internalRegisterDetector(Class clazz, RefreshDetector detector) {
        if (detector != null) {
            detector.setView(mContentView);
            detector.setRefresh(this);
            mDetectorMap.put(clazz, detector);
        }
    }

    public RefreshDetector getDetector() {
        return mDetector;
    }

    public interface OnRefreshListener extends SwipeRefreshLayout.OnRefreshListener {
        void onLoadMore();
    }

    // getter & setter
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
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
            mContentViewWrapper.setEnabled(false);
            mEmptyViewWrapper.setEnabled(false);
        }
        else {
            mContentViewWrapper.setEnabled(true);
            mEmptyViewWrapper.setEnabled(true);
        }
    }

    public void setAutoLoad(boolean auto) {
        this.mAutoLoad = auto;
    }

    public boolean isAutoLoad() {
        return mAutoLoad;
    }

    public void setEnableLoadMore(boolean enable) {
        this.mEnableLoadMore = enable;
    }

    public boolean isEnableLoadMore() {
        return this.mEnableLoadMore;
    }

    public EmptyView getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(int layout) {
        if (layout > 0) {
            EmptyView emptyView = (EmptyView) mLayoutInflater.inflate(layout, mEmptyViewWrapper, false);
            setEmptyView(emptyView);
        }
    }

    public void setEmptyView(EmptyView view) {
        if (view != null) {
            if (mEmptyView != null && mEmptyView.getParent() == mEmptyViewWrapper) {
                mEmptyViewWrapper.removeView(mEmptyView);
                mEmptyView.setRetryListener(null);
            }
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            }
            mEmptyView = view;
            mEmptyViewWrapper.addView(mEmptyView);
            setEmptyRetryListener(this);
        }
    }

    public void setEmptyRetryListener(EmptyView.RetryListener listener) {
        if (mEmptyView != null) {
            mEmptyView.setRetryListener(listener);
        }
    }

    /**
     * Set error message in empty view
     *
     * @param message error message
     */
    public void setEmptyError(String message) {
        if (mEmptyView != null) {
            mEmptyView.setDesc(EmptyView.LAYER_ERROR, message);
        }
    }

    /**
     * Set data empty message in empty view
     *
     * @param message empty message
     */
    public void setEmptyText(String message) {
        if (mEmptyView != null) {
            mEmptyView.setDesc(EmptyView.LAYER_EMPTY, message);
        }
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(int layout) {
        if (layout > 0) {
            View view = mLayoutInflater.inflate(layout, mContentViewWrapper, false);
            setContentView(view);
        }
    }

    public void setContentView(View view) {
        if (view != null) {
            if (mContentView != null && mContentView.getParent() == mContentViewWrapper) {
                mContentViewWrapper.removeView(mContentView);
            }
            if (view.getLayoutParams() == null) {
                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            }
            mContentView = view;
            mContentViewWrapper.addView(mContentView);
        }
    }

    /**
     * 设置刷新indicator的颜色，默认黑色
     *
     * @param colorResIds
     */
    public void setColorSchemeResources(int... colorResIds) {
        mContentViewWrapper.setColorSchemeResources(colorResIds);
        mEmptyViewWrapper.setColorSchemeResources(colorResIds);
    }

    public boolean isRefresh() {
        return mLoading == LOADING_REFRESH;
    }

    public boolean isLoadMore() {
        return mLoading == LOADING_MORE;
    }
}
