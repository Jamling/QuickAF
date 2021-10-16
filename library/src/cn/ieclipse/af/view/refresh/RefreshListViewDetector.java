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
package cn.ieclipse.af.view.refresh;

import android.widget.AbsListView;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshListViewDetector extends RefreshLayout.RefreshDetector<AbsListView> {

    private final AbsListView.OnScrollListener mOnScrollListener = new AbsListView.OnScrollListener() {
        private boolean mLastItemVisible;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (scrollState == SCROLL_STATE_IDLE) {
                if (mLastItemVisible && getRefresh().isEnableLoadMore()) {
                    getRefresh().loadMore();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            mLastItemVisible = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
        }
    };

    @Override
    public void setEnabled(boolean enable) {
        if (enable) {
            getView().setOnScrollListener(mOnScrollListener);
        } else {
            getView().setOnScrollListener(null);
        }
    }
}
