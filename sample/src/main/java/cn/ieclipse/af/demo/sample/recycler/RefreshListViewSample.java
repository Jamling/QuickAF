/*
 * Copyright (C) 2015-2016 HongTu
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
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.view.refresh.RefreshListViewHelper;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshListViewSample extends SampleBaseFragment implements RefreshLayout.OnRefreshListener,
    NewsController.NewsListener {
    protected RefreshLayout mRefreshLayout;
    protected RefreshListViewHelper mRefreshHelper;
    protected ListView mListView;
    protected RefreshScrollViewSample.NewsAdapter mAdapter;

    private NewsController controller = new NewsController(this);

    @Override
    protected int getContentLayout() {
        return R.layout.sample_refresh_list_view;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        // mRefreshLayout.registerDetector(ListView.class, new RefreshListViewDetector());
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setMode(RefreshLayout.REFRESH_MODE_BOTH);
        mListView = (ListView) mRefreshLayout.findViewById(R.id.listView);
        mListView.setBackgroundResource(R.color.bg_main);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        mRefreshHelper = new RefreshListViewHelper(mRefreshLayout);
        mAdapter = new RefreshScrollViewSample.NewsAdapter();
        mRefreshHelper.setAdapter(mAdapter);

        chk1.setEnabled(false);
        chk2.setEnabled(false);
        chk3.setChecked(mRefreshLayout.isAutoLoad());
        chk5.setChecked(mRefreshHelper.isKeepLoaded());
        load(false);
    }

    @Override
    public void onRefresh() {
        load(false);
    }

    @Override
    public void onLoadMore() {
        load(false);
    }

    protected void load(boolean needCache) {
        NewsController.NewsRequest req = new NewsController.NewsRequest();
        req.page = mRefreshHelper.getCurrentPage();
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
            mRefreshLayout.setAutoLoad(isChecked);
        }
        else if (chk4 == buttonView) {
            controller.setLazyLoad(isChecked);
        }
        else if (chk5 == buttonView) {
            mRefreshHelper.setKeepLoaded(isChecked);
        }
    }

    @Override
    public void onLoadNewsFailure(RestError error) {
        mRefreshHelper.onLoadFailure(error);
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
        mRefreshHelper.onLoadFinish(out);
    }
}
