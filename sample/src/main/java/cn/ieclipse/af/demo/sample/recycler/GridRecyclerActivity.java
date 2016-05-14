/*
 * Copyright 2014-2016 QuickAF
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
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RefreshRecyclerView;

public class GridRecyclerActivity extends BaseActivity {
    
    RefreshRecyclerView mAfRecycleView;
    private MyAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_grid_recycler;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("GridRecyclerView");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mAfRecycleView = (RefreshRecyclerView) view.findViewById(R.id.recycler);
        mAdapter = new MyAdapter(this);
        mAfRecycleView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                DialogUtils.showToast(GridRecyclerActivity.this, mAdapter.getItem(position));
            }
        });

        // set data
        setData();
    }

    public void listV(View view) {
        mAfRecycleView.setLinearLayoutManager(LinearLayout.VERTICAL);
    }

    public void listH(View view) {
        mAfRecycleView.setLinearLayoutManager(LinearLayout.HORIZONTAL);
    }

    public void grid(View view) {
        mAfRecycleView.setGridLayoutManager(2);
    }

    public void staggeredGridV(View view) {
        mAfRecycleView.setStaggeredGridLayoutManager(4, LinearLayout.VERTICAL);
    }

    public void staggeredGridH(View view) {
        mAfRecycleView.setStaggeredGridLayoutManager(4, LinearLayout.HORIZONTAL);
    }

    private void setData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 60; i++) {
            dataList.add(RandomUtils.genGBK(10, 20));
        }
        mAfRecycleView.onLoadFinish(dataList);
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

    public static void go(Context context) {
        Intent intent = new Intent(context, GridRecyclerActivity.class);
        context.startActivity(intent);
    }
}
