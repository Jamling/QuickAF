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
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.common.ui.BaseRVFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.refresh.SwipyRefreshLayoutDirection;

/**
 * Description
 *
 * @author Jamling
 */
public class SimpleRecyclerFragment extends BaseRVFragment {
    private MyAdapter mAdapter;

    public void clear() {
        mAdapter.clear();
    }

    @Override
    protected void initData() {
        super.initData();
        mAdapter = new MyAdapter(getAfActivity());
        mListView.setAdapter(mAdapter);
        mListView.setDividerColor(0xffff0000);
        mListView.setDividerHeight(AppUtils.dp2px(getActivity(), 1));
        setData();

        mListView.setRefreshDirection(SwipyRefreshLayoutDirection.TOP);
        mListView.setAutoLoadEnable(false);
    }

    private void setData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add(RandomUtils.genGBK(10, 20));
        }
        mListView.onLoadFinish(dataList);
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 1000);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                setData();
            }
        }, 1000);
    }

    public class MyAdapter extends AfRecyclerAdapter<String, ViewHolder> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public ViewHolder onBindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onUpdateView(ViewHolder holder, String data, int position) {
            holder.tv1.setText(data);
        }
    }

    private static class ViewHolder extends AfViewHolder {

        private TextView tv1;

        public ViewHolder(View itemView) {
            super(itemView);
            // set item selector
            itemView.setBackgroundResource(android.R.drawable.list_selector_background);
            tv1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
