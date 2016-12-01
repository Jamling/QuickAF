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

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.refresh.RefreshLayout;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshScrollViewSample extends BaseFragment implements RefreshLayout.OnRefreshListener {
    RefreshLayout refreshLayout;
    ListView listView;
    StringAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_sv;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
        listView = (ListView) refreshLayout.findViewById(R.id.listView);
        adapter = new StringAdapter();
        listView.setAdapter(adapter);
        setData();
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
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }
                adapter.setDataList(dataList);
                adapter.notifyDataSetChanged();
                refreshLayout.hideEmptyView();
                refreshLayout.onRefreshComplete();
            }
        }, 2000);
    }

    private void addData() {
        listView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 5; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }
                adapter.addAll(dataList);
                adapter.notifyDataSetChanged();
                refreshLayout.hideEmptyView();
                refreshLayout.onRefreshComplete();
            }
        }, 2000);
    }

    private class StringAdapter extends AfBaseAdapter<String> {

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(String.valueOf(position) + getItem(position));
        }
    }
}
