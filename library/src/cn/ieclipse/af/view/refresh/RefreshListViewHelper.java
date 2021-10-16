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

import android.database.DataSetObserver;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshListViewHelper<T> extends RefreshHelper<T> {
    private AbsListView mListView;
    private ListAdapter mAdapter;
    private final DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            if (getListView().getAdapter() != null && refreshLayout.getEmptyView() != null) {
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

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public RefreshListViewHelper(RefreshLayout refreshLayout) {
        super(refreshLayout);
    }

    public void setListView(AbsListView listView) {
        this.mListView = listView;
    }

    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        if (adapter != null) {
            adapter.registerDataSetObserver(mDataSetObserver);
            mAdapter = adapter;
            getListView().setAdapter(adapter);
        }
    }

    public AbsListView getListView() {
        if (mListView != null) {
            return mListView;
        }
        return (AbsListView) refreshLayout.getContentView();
    }

    @Override
    protected int getItemCount() {
        if (mAdapter instanceof AfBaseAdapter) {
            return ((AfBaseAdapter) mAdapter).getDataList().size();
        }
        return mAdapter.getCount();
    }

    @Override
    protected boolean isEmpty() {
        if (getListView() instanceof ListView) {
            return mAdapter.getCount() - ((ListView) getListView()).getFooterViewsCount() <= 0;
        }
        return mAdapter.getCount() <= 0;
    }

    @Override
    protected void setAdapterData(List<T> list) {
        if (mAdapter != null) {
            if (mAdapter instanceof AfBaseAdapter) {
                AfBaseAdapter adapter = (AfBaseAdapter) mAdapter;
                // 下拉刷新或默认是刷新操作
                if (refreshLayout.isRefresh()) {
                    if (isKeepLoaded()) {
                        adapter.add2Top(list);
                    } else {
                        adapter.setDataList(list);
                    }
                } else if (refreshLayout.isLoadMore()) {
                    adapter.addAll(list);
                } else {
                    adapter.setDataList(list);
                }
            }
            if (mAdapter instanceof BaseAdapter) {
                ((BaseAdapter) mAdapter).notifyDataSetChanged();
            }
        } else {
            refreshLayout.showEmptyView();
        }
    }
}
