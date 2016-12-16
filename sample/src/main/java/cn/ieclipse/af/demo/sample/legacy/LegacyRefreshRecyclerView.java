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

package cn.ieclipse.af.demo.sample.legacy;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.H5Activity;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.demo.sample.recycler.NewsController;
import cn.ieclipse.af.demo.sample.recycler.NewsListItem;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RefreshRecyclerView;
import cn.ieclipse.af.volley.RestError;

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
public class LegacyRefreshRecyclerView extends SampleBaseFragment implements NewsController.NewsListener {
    
    RefreshRecyclerView mAfRecycleView;
    private MyAdapter mAdapter;
    private int loadResult;
    NewsController controller = new NewsController(this);

    @Override
    protected int getContentLayout() {
        return R.layout.sample_legacy_refresh_recycler;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText("RecyclerView(Legacy)");
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
        mAdapter = new MyAdapter(getActivity());

        mAfRecycleView.setAdapter(mAdapter);
        setListener();

        chk2.setEnabled(false);
        chk5.setEnabled(false);
        chk3.setChecked(mAfRecycleView.isAutoLoad());
        // set data
        // setData();
        load(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chk1 == buttonView) {
            if (isChecked) {
                addHead(null);
            }
            else {
                removeHead(null);
            }
        }
        else if (chk2 == buttonView) {

        }
        else if (chk3 == buttonView) {

        }
        else if (chk3 == buttonView) {
            mAfRecycleView.setAutoLoad(isChecked);
        }
        else if (chk4 == buttonView) {
            controller.setLazyLoad(isChecked);
        }
        else if (chk5 == buttonView) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent == spn1) {
            loadResult = position;
        }
    }

    private void load(boolean needCache) {
        NewsController.NewsRequest req = new NewsController.NewsRequest();
        req.page = mAfRecycleView.getCurrentPage();
        controller.loadNews(req, needCache);
    }

    @Override
    public void onLoadNewsFailure(RestError error) {
        mAfRecycleView.onLoadFailure();
    }

    @Override
    public void onLoadNewsSuccess(List<NewsController.NewsInfo> out, boolean fromCache) {
        if (loadResult == 1) {
            mAfRecycleView.onLoadFinish(null);
        }
        else if (loadResult == 2) {
            throw new NullPointerException("Mock error!");
        }
        else {
            mAfRecycleView.onLoadFinish(out);
        }
    }

    public void addHead(View view) {
        // 设置header
        TextView headView = (TextView) View.inflate(getActivity(), android.R.layout.simple_list_item_1, null);
        headView.setGravity(Gravity.CENTER);
        headView.setBackgroundResource(android.R.color.holo_green_dark);
        headView.setText("i am header");
        mAdapter.setHeaderView(headView);
        mAdapter.notifyDataSetChanged();

        mAdapter.getHeaderView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showToast(getActivity(), "click head view");
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
                load(false);
            }

            @Override
            public void onLoadMore() {
                load(false);
            }
        });

        // 点击事件
        mAdapter.setOnItemClickListener(new AfRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AfRecyclerAdapter adapter, View view, int position) {
                // don't wonder header or footer.
                NewsController.NewsInfo info = mAdapter.getItem(position);
                // not recommended to use data position.
                int dataPosition = position - mAdapter.getHeaderCount();
                DialogUtils.showToast(getActivity(),
                    "data " + "position=" + dataPosition + " \nview position=" + position +
                        " \nitem=" + info);
                startActivity(H5Activity.create(getActivity(), info.url, info.title));
            }
        });

        // 长按事件
        mAdapter.setOnItemLongClickListener(new AfRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(AfRecyclerAdapter adapter, View view, int position) {
                mAfRecycleView.deleteItem(mAdapter.getItem(position));
            }
        });
    }

    public class MyAdapter extends AfRecyclerAdapter<NewsController.NewsInfo> {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            // return android.R.layout.simple_list_item_1;
            return R.layout.sample_list_item_news;
        }

        @Override
        public int getFootLayout() {
            return R.layout.sample_recyclerview_bottom_view;
        }

        @Override
        public void onUpdateView(RecyclerView.ViewHolder holder, NewsController.NewsInfo data, int position) {
            //holder.itemView.setBackgroundResource(android.R.drawable.list_selector_background);
            //((TextView) holder.itemView).setText(data.title);
            ((NewsListItem) holder.itemView).setInfo(data);
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
}
