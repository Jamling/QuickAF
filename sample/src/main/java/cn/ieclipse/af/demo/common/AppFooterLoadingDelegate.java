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
package cn.ieclipse.af.demo.common;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import cn.ieclipse.af.adapter.delegate.AdapterDelegate;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.refresh.FooterView;
import cn.ieclipse.af.view.refresh.RefreshLayout;

/**
 * Description
 *
 * @author Jamling
 */
public class AppFooterLoadingDelegate<T> extends AdapterDelegate<T> implements View.OnClickListener {

    public AppFooterLoadingDelegate(RefreshLayout refreshLayout) {
        setRefreshLayout(refreshLayout);
    }

    @Override
    public boolean isForViewType(T info, int position) {
        return position + 1 == getAdapter().getItemCount();
    }

    @Override
    public int getLayout() {
        return R.layout.ptr_footer_view;
    }

    @Override
    public void onUpdateView(RecyclerView.ViewHolder holder, T info, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        RecyclerView.ViewHolder vh = super.onCreateViewHolder(parent);
        vh.itemView.setOnClickListener(this);
        if (vh.itemView instanceof FooterView) {
            refreshLayout.setFooterView((FooterView) vh.itemView);
        }
        return vh;
    }

    private RefreshLayout refreshLayout;

    public void setRefreshLayout(RefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void onClick(View view) {
        if (refreshLayout != null && !refreshLayout.isRefresh()) {
            refreshLayout.loadMore();
        }
    }
}
