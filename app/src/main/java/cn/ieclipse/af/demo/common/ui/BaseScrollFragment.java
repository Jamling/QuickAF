package cn.ieclipse.af.demo.common.ui;

import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.view.refresh.EmptyView;
import cn.ieclipse.af.view.refresh.RefreshLayout;
import cn.ieclipse.af.volley.RestError;

/**
 * Base Fragment with refreshable scroll view
 *
 * @author Jamling
 */

public class BaseScrollFragment extends BaseFragment implements RefreshLayout.OnRefreshListener {
    protected RefreshLayout mRefreshLayout;

    @Override
    protected int getContentLayout() {
        return R.layout.base_refresh_scroll_view;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mRefreshLayout = (RefreshLayout) view.findViewById(R.id.refresh);
        mRefreshLayout.setOnRefreshListener(this);
        mRefreshLayout.setMode(RefreshLayout.REFRESH_MODE_TOP);
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
    }

    protected void onRefreshCompleted() {
        mRefreshLayout.onRefreshComplete();
        mRefreshLayout.hideEmptyView();
    }

    protected void onRefreshFailure(RestError error) {
        mRefreshLayout.getEmptyView().showErrorLayout(null);
        mRefreshLayout.getEmptyView().setDesc(EmptyView.LAYER_ERROR, VolleyUtils.getError(getActivity(), error));
        mRefreshLayout.onRefreshComplete();
        mRefreshLayout.showEmptyView();
    }
}
