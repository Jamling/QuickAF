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
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RefreshRecyclerView;

//  ┏┓　　  ┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃━━━神兽保佑无BUG！
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//   ┃　　　┃
//   ┃　　　┃
//   ┃　　　┗━━━┓
//   ┃　　　　　　　┣┓
//   ┃　　　　　　　┏┛
//   ┗┓┓┏━┳┓┏┛
//     ┃┫┫　┃┫┫
//     ┗┻┛　┗┻┛
public class RefreshRecyclerViewActivity extends BaseActivity {
    
    RefreshRecyclerView mAfRecycleView;
    private MyAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_recycler;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("RecyclerView");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mAfRecycleView = (RefreshRecyclerView) view.findViewById(R.id.recycler);
        // 设置下拉进度条切换颜色默认黑色
        mAfRecycleView.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark);
        // 设置支持的刷新方向
        mAfRecycleView.setMode(RefreshRecyclerView.REFRESH_MODE_BOTH);
        // set adapter
        mAdapter = new MyAdapter(this);

        mAfRecycleView.setAdapter(mAdapter);
        setListener();

        // set data
        setData();
    }

    public void addHead(View view) {
        // 设置header
        TextView headView = (TextView) View.inflate(this, android.R.layout.simple_list_item_1, null);
        headView.setGravity(Gravity.CENTER);
        headView.setBackgroundResource(android.R.color.holo_green_dark);
        headView.setText("i am header");
        mAdapter.setHeaderView(headView);
        mAdapter.notifyDataSetChanged();

        mAdapter.getHeaderView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showToast(RefreshRecyclerViewActivity.this, "click head view");
            }
        });
    }

    public void removeHead(View view) {
        mAdapter.setHeaderView(null);
        mAdapter.notifyDataSetChanged();
    }

    public void clear(View view) {
        mAdapter.clear();
    }

    public void refresh(View view) {
        mAfRecycleView.refresh();
    }

    private void setData() {
        mAfRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }
                mAfRecycleView.onLoadFinish(dataList);
//                 模拟加载空数据
//                mAfRecycleView.onLoadFinish(null);
                // 模拟加载失败
                // mAfRecycleView.onLoadFailure();
            }
        }, 2000);
    }

    private void addData() {
        mAfRecycleView.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<String> dataList = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    dataList.add(RandomUtils.genGBK(10, 20));
                }

                mAfRecycleView.onLoadFinish(dataList);
                // 模拟加载失败
//                mAfRecycleView.onLoadFailure();
            }
        }, 2000);
    }

    // 设置监听
    private void setListener() {
        mAfRecycleView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setData();
            }

            @Override
            public void onLoadMore() {
                addData();
            }
        });

        // 点击事件
        mAdapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean hashead = mAdapter.getHeaderView() != null;
                String item = "";
                int pos = 0;
                if (hashead) {
                    // 有headview时，data position需要-1
                    pos = position - 1;
                }
                else {
                    pos = position;
                }
                DialogUtils.showToast(RefreshRecyclerViewActivity.this,
                    "data " + "position=" + (pos) + " \nview position=" + position +
                        " \nitem=" + mAdapter.getItem(pos));

                // mAdapter.updateItem(pos,"update item "+ (pos));
                // 需要重写item的equals()方法
                // mAfRecycleView.updateItem(item);
            }
        });

        // 长按事件
        mAdapter.setOnItemLongClickListener(new AfRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 有headview时，data position需要-1
                // mAdapter.deleteItem(position -1);
                // 需要重写item的equals()方法
                boolean hashead = mAdapter.getHeaderView() != null;
                int pos = 0;
                if (hashead) {
                    // 有headview时，position需要-1
                    pos = position - 1;
                }
                else {
                    pos = position;
                }

                mAfRecycleView.deleteItem(mAdapter.getItem(pos));
            }
        });
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
        public int getFootLayout() {
            return R.layout.sample_recyclerview_bottom_view;
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
