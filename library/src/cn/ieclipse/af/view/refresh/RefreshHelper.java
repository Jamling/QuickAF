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

import java.util.List;

import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class RefreshHelper<T> {

    // protected RecyclerView recyclerView;
    protected RefreshLayout refreshLayout;

    /**
     * adapter中数据条目
     */
    private int mItemCount = 0;

    private int mTotalCount;

    /**
     * 下拉刷新时，是否保留已经加载的数据
     */
    private boolean mKeepLoaded = false;
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
    protected int mCurrentPage = PAGE_FIRST;
    /**
     * 每页数据大小
     */
    private int mPageSize = PAGE_SIZE;

    protected Logger mLogger = Logger.getLogger(getClass());

    public RefreshHelper(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    protected Context getContext() {
        return refreshLayout.getContext();
    }

    /**
     * 加载完成将数据添加到adapter中
     *
     * @param list     加载的分页数据
     * @param total    服务端数据总条目
     * @param pageSize 分页大小
     */
    public void onLoadFinish(List<T> list, int total, int pageSize) {
        if (pageSize > 0) {
            this.mPageSize = pageSize;
        }
        if (total > 0) {
            this.mTotalCount = total;
        }
        setAdapterData(list);
        calcCurrentPage();
        refreshLayout.onRefreshComplete();
    }

    /**
     * 加载失败
     */
    public void onLoadFailure(RestError error) {
        if (refreshLayout.getEmptyView() != null) {
            refreshLayout.getEmptyView().showErrorLayout(error);
        }
        if (!refreshLayout.isRefresh()) {
            setFooterError(error);
        }
        refreshLayout.onRefreshComplete();
    }

    /**
     * Whether keep loaded data when refresh
     *
     * @param keep keep load data
     */
    public void setKeepLoaded(boolean keep) {
        this.mKeepLoaded = keep;
    }

    public boolean isKeepLoaded() {
        return mKeepLoaded;
    }

    /**
     * 获取当前需要加载的页数
     *
     * @return current page
     */
    public int getCurrentPage() {
        if (refreshLayout.isRefresh()) {
            return PAGE_FIRST;
        }
        return mCurrentPage;
    }

    /**
     * 设置page size
     */
    public void setPageSize(int size) {
        mPageSize = size;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    /**
     * 计算页码
     */
    protected void calcCurrentPage() {
        mItemCount = getItemCount();
        int p = mItemCount / mPageSize;
        if (mItemCount % mPageSize >= 0) {
            mCurrentPage = p + 1;
        }
        else {
            mCurrentPage = p;
        }
        if (mCurrentPage <= 0) {
            mCurrentPage = PAGE_FIRST;
        }
        mLogger.d("current page : " + mCurrentPage + ", item count : " + mItemCount);
    }

    /**
     * Return the total actual data count in adapter/recycler view
     *
     * @return total count
     */
    protected abstract int getItemCount();

    protected abstract boolean isEmpty();

    protected abstract void setAdapterData(List<T> list);

    protected void setFooterError(RestError error) {
        if (refreshLayout.getFooterView() != null){
            refreshLayout.getFooterView().setError(error);
        }
    }

    public void setFooterLoading() {
        if (refreshLayout.getFooterView() != null){
            refreshLayout.getFooterView().setLoading(null);
        }
    }

    protected void setFooterEmpty(boolean empty) {
        if (refreshLayout.getFooterView() != null){
            refreshLayout.getFooterView().setEmpty(null);
        }
    }
}
