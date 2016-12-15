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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RefreshRecyclerView;
import cn.ieclipse.af.view.recycle.SwipeMenuLayout;
import cn.ieclipse.af.view.recycle.SwipeMenuRecyclerView;

/**
 * list item显示左右滑动显示菜单
 */
public class SwipeMenuRecyclerActivity extends BaseActivity {
    
    private RefreshRecyclerView mAfRecycleView;
    private SwipeMenuRecyclerView mSwipeMenuRecyclerView;
    private MyAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_swipe_menu_recycler;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("SwipeMenuRecyclerView");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mAfRecycleView = (RefreshRecyclerView) view.findViewById(R.id.recycler);
        mSwipeMenuRecyclerView = (SwipeMenuRecyclerView) mAfRecycleView.getRecyclerView();
        // set adapter
        mAdapter = new MyAdapter(this);
        mAfRecycleView.setAdapter(mAdapter);
        mAfRecycleView.setMode(RefreshRecyclerView.REFRESH_MODE_NONE);
        // set data
        setData();
    }

    // 滑动菜单显示左
    public void swipeRight(View view) {
        mSwipeMenuRecyclerView.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_LEFT);
    }

    // 滑动菜单显示右
    public void swipeLeft(View view) {
        mSwipeMenuRecyclerView.setSwipeDirection(SwipeMenuRecyclerView.DIRECTION_RIGHT);
    }

    private void setData() {
        ArrayList<MyItem> dataList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            MyItem item = new MyItem();
            item.id = i;
            item.text = RandomUtils.genGBK(10, 20);
            dataList.add(item);
        }
        mAfRecycleView.onLoadFinish(dataList);
    }

    public class MyAdapter extends AfRecyclerAdapter<MyItem> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return R.layout.sample_swipe_list_item;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 0) {
                return new MyViewHolder(onCreateItemView(parent, viewType));
            }
            return super.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onUpdateView(final RecyclerView.ViewHolder holder2, MyItem data, final int position) {
            final MyViewHolder holder = (MyViewHolder) holder2;
            holder.tv.setText(data.text);
            holder.btDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 有删除单条数据此处使用以下position否则会出现position混乱
                    int adaposition = holder.getAdapterPosition();
                    // int layoutpos = holder.getLayoutPosition();
                    deleteItem(position);
                }
            });

            holder.btOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showToast(SwipeMenuRecyclerActivity.this, "click open");
                }
            });

            // 如要支持侧滑 必须主内容view的id必须是smContentView
            // 侧滑布局id必须是smMenuView 并且用SwipeMenuLayout包裹着
            // 具体参考本adaper中的布局
            SwipeMenuLayout itemView = (SwipeMenuLayout) holder.itemView;
            /**
             * optional 以下设置可选
             */
            // itemView.setSwipeEnable(swipeEnable);
            itemView.setOpenInterpolator(mSwipeMenuRecyclerView.getOpenInterpolator());
            itemView.setCloseInterpolator(mSwipeMenuRecyclerView.getCloseInterpolator());
        }

    }

    public static class MyItem {
        public int id;
        public String text;

        @Override
        public boolean equals(Object o) {
            if (o instanceof MyItem) {
                MyItem item = (MyItem) o;
                return (item.hashCode()) == this.hashCode();
            }
            return super.equals(o);
        }
    }

    class MyViewHolder extends AfViewHolder {

        TextView tv;
        View btOpen;
        View btDelete;

        public MyViewHolder(View view) {
            super(view);
            view.setBackgroundResource(android.R.drawable.list_selector_background);
            btOpen = itemView.findViewById(R.id.btOpen);
            btDelete = itemView.findViewById(R.id.btDelete);
            tv = (TextView) view.findViewById(R.id.cell_number);
        }
    }

    public static void go(Context context) {
        Intent intent = new Intent(context, SwipeMenuRecyclerActivity.class);
        context.startActivity(intent);
    }
}
