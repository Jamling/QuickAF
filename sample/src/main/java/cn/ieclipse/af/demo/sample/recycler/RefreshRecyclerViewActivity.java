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
import cn.ieclipse.af.view.refresh.SwipyRefreshLayoutDirection;

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
        // set empty view
        setEmptyView();

        // 设置下拉进度条切换颜色默认黑色
        mAfRecycleView.setColorSchemeResources(android.R.color.holo_red_dark, android.R.color.holo_blue_bright,
            android.R.color.holo_green_dark);

        // 设置支持的刷新方向
        mAfRecycleView.setRefreshDirection(SwipyRefreshLayoutDirection.BOTH);
        // 设置是否支持滑动到底部自动加载
        //mAfRecycleView.setAutoLoadEnable(true);

        // set adapter
        mAdapter = new MyAdapter(this);

        mAfRecycleView.setAdapter(mAdapter);
        setListener();

        // set data
        setData();
    }

    public void addHead(View view) {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView view1 = (TextView) View.inflate(this, android.R.layout.simple_list_item_1, null);
        view1.setLayoutParams(params);
        addHeadView(view1);
    }

    public void removeHead(View view) {
        addHeadView(null);
    }

    private void addHeadView(TextView view) {
        if (view != null) {
            view.setText("head view");
            // 添加headview
            mAdapter.setHeaderView(view);
            mAdapter.getHeaderView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtils.showToast(RefreshRecyclerViewActivity.this, "click head view");
                }
            });
        }
        else {
            mAdapter.setHeaderView(null);
        }
        mAdapter.notifyDataSetChanged();

        //TextView footView = (TextView)View.inflate(this, android.R.layout.simple_list_item_1, null);
        //footView.setText("foot view");
        // 添加footview
        // mAdapter.setFootView(footView);
    }

    public void clear(View view) {
        mAdapter.clear();
    }

    private void setData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(RandomUtils.genGBK(10, 20));
        }
        mAfRecycleView.onLoadFinish(dataList);
    }

    private void addData() {
        ArrayList<String> dataList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            dataList.add(RandomUtils.genGBK(10, 20));
        }
        mAfRecycleView.onLoadFinish(dataList);
    }

    private void setEmptyView() {
        View loadingview = getLayoutInflater().inflate(R.layout.main_empty_view, null);
        TextView loadTv = (TextView) loadingview.findViewById(R.id.textView);
        loadTv.setText("正在努力加载中...");

        View tipview = getLayoutInflater().inflate(R.layout.main_empty_view, null);
        TextView tipTv = (TextView) tipview.findViewById(R.id.textView);
        tipTv.setText("暂无数据请下拉或点击重试...");

        mAfRecycleView.setEmptyView(loadingview, tipview);

    }

    private void setListener() {
        mAfRecycleView.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAfRecycleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setData();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mAfRecycleView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        addData();
                    }
                }, 1000);
            }
        });

        mAdapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String item = mAdapter.getItem(position - 1);
                DialogUtils.showToast(RefreshRecyclerViewActivity.this,
                    "data " + "position=" + (position - 1) + " \nview position=" + position +
                        " \nitem=" + item);

                //mAdapter.updateItem(position -1,"update item "+ (position -1));
                // 需要重写item的equals()方法
                //mAfRecycleView.updateItem(item);
            }
        });

        mAdapter.setOnItemLongClickListener(new AfRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 有headview时，position需要-1
                // mAdapter.deleteItem(position -1);
                // 需要重写item的equals()方法
                mAfRecycleView.deleteItem(mAdapter.getItem(position - 1));
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
