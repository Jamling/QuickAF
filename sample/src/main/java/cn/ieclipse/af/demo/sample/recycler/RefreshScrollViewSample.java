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

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.PagerBaseAdapter;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshListViewHelper;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshScrollViewSample extends SampleBaseFragment implements RefreshLayout.OnRefreshListener,
    NewsController.NewsListener {
    RefreshLayout refreshLayout;
    RefreshListViewHelper helper;
    ListView listView;
    NewsAdapter adapter;
    NewsController controller = new NewsController(this);

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_refresh_sv;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);

        listView = (ListView) refreshLayout.findViewById(R.id.listView);
        adapter = new NewsAdapter();
        // listView.setAdapter(adapter);

        helper = new RefreshListViewHelper(refreshLayout);
        helper.setListView(listView);
        helper.setAdapter(adapter);

        chk1.setEnabled(false);
        chk2.setEnabled(false);
        chk3.setChecked(refreshLayout.isAutoLoad());
        chk5.setChecked(helper.isKeepLoaded());

        load();
    }

    protected void load() {
        NewsController.NewsRequest req = new NewsController.NewsRequest();
        req.page = helper.getCurrentPage();//adapter.getPage();
        controller.loadNews(req, false);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (chk1 == buttonView) {
//            if (isChecked) {
//                adapter.registerDelegate(-2, new HeaderDelegate());
//            }
//            else {
//                adapter.removeDelegate(-2);
//            }
//            adapter.notifyDataSetChanged();
        }
        else if (chk2 == buttonView) {
//            if (isChecked) {
//                adapter.registerDelegate(-3, new AppFooterLoadingDelegate<NewsController.NewsInfo>(refreshLayout));
//            }
//            else {
//                adapter.removeDelegate(-3);
//            }
//            adapter.notifyDataSetChanged();
        }
        else if (chk3 == buttonView) {
            refreshLayout.setAutoLoad(isChecked);
        }
        else if (chk4 == buttonView) {
            controller.setLazyLoad(isChecked);
        }
        else if (chk5 == buttonView) {
            helper.setKeepLoaded(isChecked);
        }
    }

    @Override
    public void onRefresh() {
        load();
    }

    @Override
    public void onLoadMore() {
        load();
    }

    @Override
    public void onLoadNewsFailure(RestError error) {
//        if (refreshLayout.getEmptyView() != null) {
//            refreshLayout.getEmptyView().showErrorLayout(error);
//        }
//        if (!refreshLayout.isLoadMore()) {
//            if (refreshLayout.getFooterView() != null) {
//                refreshLayout.getFooterView().setError(error);
//            }
//        }
//        refreshLayout.onRefreshComplete();
        helper.onLoadFailure(error);
    }

    @Override
    public void onLoadNewsSuccess(List<NewsController.NewsInfo> out, boolean fromCache) {
        int loadResult = spn1.getSelectedItemPosition();
        if (loadResult == 1) {
            out = null;
        }
        else if (loadResult == 2) {
            throw new NullPointerException("Mock error!");
        }
//        if (adapter.getPage() <= 1) {
//            adapter.setDataList(out);
//        }
//        else {
//            adapter.addAll(out);
//        }
//        adapter.notifyDataSetChanged();
//        refreshLayout.hideEmptyView();
//        refreshLayout.onRefreshComplete();
        helper.onLoadFinish(out);
    }

    public static class NewsAdapter extends PagerBaseAdapter<NewsController.NewsInfo> {

        @Override
        public int getLayout() {
            return R.layout.sample_list_item_news;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            Log.e("QuickAF", "onUpdateView " + position);
            NewsListItem item = (NewsListItem) convertView;
            item.setInfo(getItem(position));
        }
    }
}
