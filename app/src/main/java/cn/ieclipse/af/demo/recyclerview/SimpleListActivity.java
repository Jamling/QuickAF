/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.demo.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.app.BaseActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.util.DialogUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/30.
 */
public class SimpleListActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_rv_simple_list;
    }

    @Override
    protected void initContentView() {
        super.initContentView();
        mRecyclerView = (RecyclerView) findViewById(R.id.listView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        List<String> list = RandomUtils.genGBKList(10, 20);
        mAdapter.setDataList(list);
        mAdapter.notifyDataSetChanged();
    }

    public static void forward(Context context) {
        Intent intent = new Intent(context, SimpleListActivity.class);
        startActivity(intent, null, context, 0);
    }

    private static class MyAdapter extends AfRecyclerAdapter<String, SimpleListActivity.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,
                false);
            return new ViewHolder(v);
        }

        @Override
        public void onUpdateView(final SimpleListActivity.ViewHolder holder, int position) {
            String info = getItem(position);
            holder.tv1.setText(info);
            holder.itemView.setTag(info);
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    DialogUtils.showToast(holder.itemView.getContext(), String.format("onLongClick pos=%d, info=%s",
                        holder.getAdapterPosition(), holder.itemView.getTag()));
                    return true;
                }
            });
        }
    }

    private static class ViewHolder extends AfViewHolder {

        private TextView tv1;

        public ViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(android.R.id.text1);
        }

        @Override
        public void onItemClick(View itemView, int position) {
            super.onItemClick(itemView, position);
            DialogUtils.showToast(itemView.getContext(), String.format("onClick pos=%d, info=%s", position,
                itemView.getTag()));
        }
    }
}
