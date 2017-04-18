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
package cn.ieclipse.af.view.recycle;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public final class RecyclerHelper {
    
    /**
     * Same to {@link android.support.v7.widget.OrientationHelper#HORIZONTAL}
     */
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    /**
     * Same to {@link android.support.v7.widget.OrientationHelper#VERTICAL}
     */
    public static final int VERTICAL = OrientationHelper.VERTICAL;
    
    private RecyclerView recyclerView;
    private RecyclerView.ItemDecoration mItemDecoration;
    
    public RecyclerHelper() {
        
    }
    
    public RecyclerHelper(RecyclerView recyclerView) {
        setRecyclerView(recyclerView);
    }
    
    public void setRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
    
    public RecyclerView getRecyclerView() {
        return recyclerView;
    }
    
    /**
     * @see android.support.v7.widget.SimpleItemAnimator#setSupportsChangeAnimations(boolean)
     */
    public void setSupportsChangeAnimations(boolean supportsChangeAnimations) {
        RecyclerView.ItemAnimator animator = getRecyclerView().getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }
    
    // layout
    
    /**
     * Set the RecyclerView using {@link android.support.v7.widget.LinearLayoutManager}.
     *
     * @param orientation orientation value of {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setLinearLayoutManager(int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(getRecyclerView().getContext());
        manager.setOrientation(orientation);
        getRecyclerView().setLayoutManager(manager);
    }
    
    /**
     * Set the RecyclerView using {@link android.support.v7.widget.GridLayoutManager}.
     *
     * @param column grid column
     */
    public void setGridLayoutManager(int column) {
        final GridLayoutManager gridManager = new GridLayoutManager(getRecyclerView().getContext(), column);
        getRecyclerView().setLayoutManager(gridManager);
    }
    
    /**
     * Set the RecyclerView using {@link android.support.v7.widget.StaggeredGridLayoutManager}
     *
     * @param column      grid column
     * @param orientation orientation value of {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setStaggeredGridLayoutManager(int column, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(column, orientation);
        getRecyclerView().setLayoutManager(manager);
    }
    
    /**
     * Set the {@link android.support.v7.widget.RecyclerView} item decoration
     *
     * @param itemDecoration {@link android.support.v7.widget.RecyclerView.ItemDecoration}
     */
    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mItemDecoration != null) {
            getRecyclerView().removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = itemDecoration;
        getRecyclerView().addItemDecoration(mItemDecoration);
        getRecyclerView().invalidateItemDecorations();
    }
    
    public RecyclerView.ItemDecoration getItemDecoration() {
        return mItemDecoration;
    }
    
    /**
     * Set {@link cn.ieclipse.af.view.recycle.ListDividerItemDecoration} height
     *
     * @param height px height;
     */
    public void setDividerHeight(int height) {
        if (mItemDecoration instanceof ListDividerItemDecoration) {
            ((ListDividerItemDecoration) mItemDecoration).setDividerHeight(height);
            getRecyclerView().invalidateItemDecorations();
        }
    }
    
    /**
     * Set {@link cn.ieclipse.af.view.recycle.ListDividerItemDecoration} color
     *
     * @param color color argb format
     */
    public void setDividerColor(int color) {
        if (mItemDecoration instanceof ListDividerItemDecoration) {
            ((ListDividerItemDecoration) mItemDecoration).setDividerColor(color);
            getRecyclerView().invalidateItemDecorations();
        }
    }
    
    public void setDividerPaddingStart(int start) {
        if (mItemDecoration instanceof ListDividerItemDecoration) {
            ((ListDividerItemDecoration) mItemDecoration).setPaddingStart(start);
            getRecyclerView().invalidateItemDecorations();
        }
    }
    
    public void setDividerPaddingEnd(int end) {
        if (mItemDecoration instanceof ListDividerItemDecoration) {
            ((ListDividerItemDecoration) mItemDecoration).setPaddingStart(end);
            getRecyclerView().invalidateItemDecorations();
        }
    }
    
    /**
     * For #GridLayoutManager set header/footer full row
     */
    public void setSpanSizeLookup() {
        final RecyclerView.LayoutManager lm = getRecyclerView().getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            ((GridLayoutManager) lm).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getRecyclerView().getAdapter() instanceof AfRecyclerAdapter) {
                        int viewType = getRecyclerView().getAdapter().getItemViewType(position);
                        if (viewType < 0) {
                            return ((GridLayoutManager) lm).getSpanCount();
                        }
                        return 1;
                    }
                    return 1;
                }
            });
        }
    }
    
    /**
     * Scroll to recycler position and set the position to top
     *
     * @param position position
     */
    public void scrollToPosition(int position) {
        final RecyclerView.LayoutManager lm = getRecyclerView().getLayoutManager();
        if (lm instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) lm;
            llm.scrollToPositionWithOffset(position, 0);
//            int firt = llm.findFirstVisibleItemPosition();
//            int last = llm.findLastVisibleItemPosition();
//            if (position <= firt) {
//                getRecyclerView().scrollToPosition(position);
//            }
//            else if (position <= last) {
//                View v = getRecyclerView().getChildAt(position - firt);
//                if (llm.getOrientation() == OrientationHelper.VERTICAL) {
//                    int top = v.getTop();
//                    getRecyclerView().scrollBy(0, top);
//                }
//                else {
//                    int left = v.getLeft();
//                    getRecyclerView().scrollTo(left, 0);
//                }
//            }
//            else {
//                getRecyclerView().scrollToPosition(position);
//                // repeat (position <= last)
//                int n = position - llm.findFirstVisibleItemPosition();
//                if (0 <= n && n < getRecyclerView().getChildCount()) {
//                    View v = getRecyclerView().getChildAt(n);
//                    if (llm.getOrientation() == OrientationHelper.VERTICAL) {
//                        int top = v.getTop();
//                        getRecyclerView().scrollBy(0, top);
//                    }
//                    else {
//                        int left = v.getLeft();
//                        getRecyclerView().scrollTo(left, 0);
//                    }
//                }
//            }
        }
    }
}
