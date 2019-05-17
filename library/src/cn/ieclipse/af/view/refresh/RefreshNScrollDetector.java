package cn.ieclipse.af.view.refresh;

import androidx.core.widget.NestedScrollView;

public class RefreshNScrollDetector extends RefreshLayout.RefreshDetector<NestedScrollView> {
    private NestedScrollView.OnScrollChangeListener listener =
            (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (getRefresh().isEnableLoadMore()
                        && (scrollY
                                == getView().getChildAt(0).getMeasuredHeight()
                                        - v.getMeasuredHeight())) {
                    getRefresh().loadMore();
                }
            };

    @Override
    public void setEnabled(boolean enable) {
        if (enable) {
            getView().setOnScrollChangeListener(listener);
        } else {
            getView().setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) null);
        }
    }
}
