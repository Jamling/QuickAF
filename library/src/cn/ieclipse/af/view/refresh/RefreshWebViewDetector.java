package cn.ieclipse.af.view.refresh;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.WebView;

/**
 * {@link android.webkit.WebView} refresh detector
 *
 * @author Jamling
 * @since 2.1.1
 */
public class RefreshWebViewDetector extends RefreshLayout.RefreshDetector<WebView> {

    @RequiresApi(Build.VERSION_CODES.M)
    private View.OnScrollChangeListener mOnScrollChangeListener = new View.OnScrollChangeListener() {
        @Override
        public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            if (getRefresh().isEnableLoadMore()) {
                int ch = (int) (getView().getContentHeight() * getView().getScale());
                int vh = scrollY + v.getHeight();
                if (vh + 2 >= ch) {
                    getRefresh().loadMore();
                }
            }
        }
    };

    @Override
    public void setEnabled(boolean enable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (enable) {
                getView().setOnScrollChangeListener(mOnScrollChangeListener);
            }
            else {
                getView().setOnScrollChangeListener(null);
            }
        }
    }
}
