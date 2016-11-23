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
package cn.ieclipse.af.demo.sample.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshRecyclerHelper;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshRecyclerSample extends BaseFragment implements RefreshLayout.OnRefreshListener {
    RefreshLayout refreshLayout;
    RefreshRecyclerHelper recyclerView;
    RecyclerView listView;
    StringAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_recycler;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);

        recyclerView = new RefreshRecyclerHelper(refreshLayout);
        listView = (RecyclerView) refreshLayout.findViewById(R.id.rv);
        adapter = new StringAdapter(view.getContext());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        setData();
    }

    @Override
    public void onLoadMore() {
        addData();
    }

    private void setData() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }
                recyclerView.onLoadFinish(dataList, 0, 0);
            }
        }, 2000);
    }

    private void addData() {
        refreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }
                recyclerView.onLoadFinish(dataList, 0, 0);
            }
        }, 2000);
    }

    private class StringAdapter extends AfRecyclerAdapter<String, AfViewHolder> {

        public StringAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public AfViewHolder onBindViewHolder(View view) {
            return new AfViewHolder(view);
        }

        @Override
        public void onUpdateView(AfViewHolder holder, String data, int position) {
            TextView tv = (TextView) holder.itemView;
            tv.setText(String.valueOf(position) + data);
        }
    }
}
