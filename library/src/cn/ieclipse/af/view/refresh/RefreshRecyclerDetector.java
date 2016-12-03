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

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Description
 *
 * @author Jamling
 */
public class RefreshRecyclerDetector extends RefreshLayout.RefreshDetector<RecyclerView> {
    @Override
    public void setEnabled(boolean enable) {
        if (enable) {
            getView().addOnScrollListener(mInnerOnScrollListener);
        }
        else {
            getView().removeOnScrollListener(mInnerOnScrollListener);
        }
    }

    private RecyclerView.OnScrollListener mInnerOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            // 滚动停止时
            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                int orientation = OrientationHelper.VERTICAL;
                if (manager instanceof StaggeredGridLayoutManager) {
                    orientation = ((StaggeredGridLayoutManager) manager).getOrientation();
                }
                else if (manager instanceof LinearLayoutManager) {
                    orientation = ((LinearLayoutManager) manager).getOrientation();
                }

                // 是否允许自动加载
                if (getRefresh().isAutoLoad()) {
                    // 水平滚动加载
                    if (orientation == OrientationHelper.HORIZONTAL) {
                        // if (!recyclerView.canScrollHorizontally(-1)) {
                        //    // scrolled To start
                        // }
                        // else
                        if (!recyclerView.canScrollHorizontally(1)) {
                            // scrolled To end
                            getRefresh().loadMore();
                        }
                    }
                    else {

                        // 竖直滚动加载
                        // if (!recyclerView.canScrollVertically(-1)) {
                        //    // Scrolled To Top
                        // }
                        // else
                        if (!recyclerView.canScrollVertically(1)) {
                            // scrolled To bottom
                            getRefresh().loadMore();
                        }
                    }
                }
            }
        }
    };
}
