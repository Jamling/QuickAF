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
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RefreshRecyclerView;

public class GridRecyclerActivity extends BaseActivity {
    
    private RefreshRecyclerView mAfRecycleView;
    private MyAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_grid_recycler;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("GridRecyclerView(Legacy)");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mAfRecycleView = (RefreshRecyclerView) view.findViewById(R.id.recycler);
        mAdapter = new MyAdapter(this);
        mAfRecycleView.setMode(RefreshRecyclerView.REFRESH_MODE_BOTTOM);
        mAfRecycleView.setAdapter(mAdapter);

        // 设置header
        TextView headView = (TextView) View.inflate(this, android.R.layout.simple_list_item_1, null);
        headView.setGravity(Gravity.CENTER);
        headView.setBackgroundResource(android.R.color.holo_green_dark);
        headView.setText("i am header");
        mAdapter.setHeaderView(headView);

        mAdapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AfRecyclerAdapter adapter, View view, int position) {
                boolean hashead = mAdapter.getHeaderView() != null;
                int pos = position - mAdapter.getHeaderCount();
//                if (hashead) {
//                    // 有headview时，data position需要-1
//                    pos = position - 1;
//                }
//                else {
//                    pos = position;
//                }
                DialogUtils.showToast(mAfRecycleView.getContext(),
                    "data " + "position=" + (pos) + " \nview position=" + position +
                        " \nitem=" + mAdapter.getItem(position));

                // mAdapter.updateItem(pos,"update item "+ (pos));
                // 需要重写item的equals()方法
                // mAfRecycleView.updateItem(item);
            }
        });
        // set data
        setData();
        setListener();
    }

    // 竖直的listview
    public void listV(View view) {
        mAfRecycleView.setLinearLayoutManager(LinearLayout.VERTICAL);
    }

    // 水平的listview
    public void listH(View view) {
        mAfRecycleView.setLinearLayoutManager(LinearLayout.HORIZONTAL);
    }

    // gridview
    public void grid(View view) {
        mAfRecycleView.setGridLayoutManager(2);
    }

    // 竖直的staggeredGrid
    public void staggeredGridV(View view) {
        mAfRecycleView.setStaggeredGridLayoutManager(4, LinearLayout.VERTICAL);
    }

    // 水平的的staggeredGrid
    public void staggeredGridH(View view) {
        mAfRecycleView.setStaggeredGridLayoutManager(4, LinearLayout.HORIZONTAL);
    }

    // 设置监听
    private void setListener() {
        mAfRecycleView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAfRecycleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                mAfRecycleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                    }
                }, 2000);
            }
        });
    }

    private void addData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(RandomUtils.genGBK(5, 30));
        }
        mAfRecycleView.onLoadFinish(dataList);
    }

    private void setData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add(RandomUtils.genGBK(10, 20));
        }
        mAfRecycleView.onLoadFinish(dataList);
//        mAfRecycleView.onLoadFailure();
    }

    public class MyAdapter extends AfRecyclerAdapter<String> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public int getFootLayout() {
            // 设置footview
            return R.layout.sample_recyclerview_bottom_view;
        }

        @Override
        public void onUpdateView(RecyclerView.ViewHolder holder, String data, int position) {
            holder.itemView.setBackgroundResource(android.R.drawable.list_selector_background);
            ((TextView)holder.itemView).setText(data);
        }
    }

//    private static class ViewHolder extends AfViewHolder {
//
//        private TextView tv1;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            // set item selector
//            itemView.setBackgroundResource(android.R.drawable.list_selector_background);
//            tv1 = (TextView) itemView.findViewById(android.R.id.text1);
//        }
//    }

    public static void go(Context context) {
        Intent intent = new Intent(context, GridRecyclerActivity.class);
        context.startActivity(intent);
    }
}
