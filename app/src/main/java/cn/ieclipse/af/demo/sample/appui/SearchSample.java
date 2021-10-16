package cn.ieclipse.af.demo.sample.appui;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.demo.common.ui.H5Activity;
import cn.ieclipse.af.demo.common.view.SearchLayout;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.demo.sample.recycler.NewsController;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.PopupUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */

public class SearchSample extends SampleBaseFragment {

    SampleSearchPopupWindow searchPopupWindow;
    SearchLayout searchLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_search;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        searchLayout = (SearchLayout) view.findViewById(R.id.sample_search);
        searchPopupWindow = new SampleSearchPopupWindow(searchLayout);
        searchPopupWindow.setAnchor(mTitleBar);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btn1) {
//            View view = View.inflate(v.getContext(), R.layout.sample_activity_datepicker, null);
//            view.setBackgroundColor(-1);
//            PopupWindow mPopupWindows = new PopupWindow(context);
//            mPopupWindows.setContentView(view);
//            mPopupWindows.setTouchable(true);
//            mPopupWindows.setFocusable(true);
//            mPopupWindows.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
//            mPopupWindows.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
//            mPopupWindows.setBackgroundDrawable(new BitmapDrawable());
//            mPopupWindows.showAtLocation(mTitleBar, Gravity.NO_GRAVITY, 0, 0);
            //searchPopupWindow.show(mTitleBar, view);
        }
    }

    public static class SampleSearchPopupWindow extends
        SearchLayout.SearchPopupWindow<NewsController.NewsInfo> implements NewsController.NewsListener {

        private final NewsController mNewsController = new NewsController(this);

        public SampleSearchPopupWindow(SearchLayout searchLayout) {
            super(searchLayout);
        }

        public void show(View parent, View content){
            if (content != null) {
                mPopupWindows.setContentView(content);
            }
            //mPopupWindows.showAtLocation(parent, Gravity.NO_GRAVITY, 0, 0);
            PopupUtils.showAsDropDown(mPopupWindows, parent, 0, 0);
        }

        @Override
        protected void initData() {
            mAdapter = new MySearchAdapter();
            mListView.setAdapter(mAdapter);
            addHotTags();
            super.loadSearchHistory();
        }

        @Override
        public void search(NewsController.NewsInfo info, String keyword) {
            super.search(info, keyword);
            if (info != null) {
                load(info.title);
            }
            else {
                load(keyword);
            }
        }

        @Override
        protected void load(String keyword) {
            NewsController.NewsRequest req = new NewsController.NewsRequest();
            req.word = keyword;
            mNewsController.loadNews(req, false);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NewsController.NewsInfo info = (NewsController.NewsInfo) mAdapter.getItem(position);
            context.startActivity(H5Activity.create(context, info.url, info.title));
            addSearchHistory(info);
            cancelSearch();
        }

        /**
         * 添加热门搜索标贴
         */
        private void addHotTags() {
            mFlHotTags.removeAllViews();
            List<NewsController.NewsInfo> list = loadSearchHistory();
            if (list != null) {
                for (NewsController.NewsInfo info : list) {
                    TextView tv = generateHotTag();
                    tv.setText(info.title);
                    tv.setTag(info);
                    mFlHotTags.addView(tv);
                }
            }
        }

        protected TextView generateHotTag() {
            TextView tv = new TextView(context);
            int p = AppUtils.dp2px(context, 6);
            tv.setPadding(p << 1, p, p << 1, p);
            tv.setTextSize(12);
            RoundedColorDrawable bg = new RoundedColorDrawable(p << 1, 0);
            bg.setBorder(AppUtils.getColor(context, R.color.colorPrimary), AppUtils.dp2px(context, 1)).applyTo(tv);
            return tv;
        }

        @Override
        public void onLoadNewsSuccess(List<NewsController.NewsInfo> out, boolean fromCache) {
            mAdapter.setDataList(out);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onLoadNewsFailure(RestError error) {
            VolleyUtils.showError(mEmptyText, error);
        }

        private class MySearchAdapter extends SearchLayout.SearchAdapter<NewsController.NewsInfo> {
            @Override
            protected String getDisplayName(NewsController.NewsInfo info) {
                return info.title;
            }
        }
    }
}
