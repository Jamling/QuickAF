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

import android.view.View;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.RefreshRecyclerView;

/**
 * Base RecyclerView Fragment
 *
 * @author Jamling
 */
public class BaseRVFragment extends BaseFragment implements RefreshRecyclerView.OnRefreshListener {
    protected cn.ieclipse.af.view.RefreshRecyclerView mListView;

    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_rv;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mListView = (RefreshRecyclerView) view.findViewById(R.id.refresh_rv);
        if (mListView != null) {
            initEmptyView();
            mListView.setOnRefreshListener(this);
        }
    }

    protected void initEmptyView() {
        View loadingView = getActivity().getLayoutInflater().inflate(R.layout.main_empty_view, null);
        TextView loadTv = (TextView) loadingView.findViewById(R.id.textView);
        loadTv.setText("正在努力加载中...");

        View emptyView = getAfActivity().getLayoutInflater().inflate(R.layout.main_empty_view, null);
        TextView tipTv = (TextView) emptyView.findViewById(R.id.textView);
        tipTv.setText("暂无数据请下拉刷新重试");

        mListView.setEmptyView(loadingView, emptyView);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
