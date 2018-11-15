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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.ieclipse.af.adapter.AfRecyclerAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public final class RecyclerHelper {
    
    /**
     * Same to {@link OrientationHelper#HORIZONTAL}
     */
    public static final int HORIZONTAL = OrientationHelper.HORIZONTAL;
    /**
     * Same to {@link OrientationHelper#VERTICAL}
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
     * @see SimpleItemAnimator#setSupportsChangeAnimations(boolean)
     */
    public void setSupportsChangeAnimations(boolean supportsChangeAnimations) {
        RecyclerView.ItemAnimator animator = getRecyclerView().getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }
    
    // layout
    
    /**
     * Set the RecyclerView using {@link LinearLayoutManager}.
     *
     * @param orientation orientation value of {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setLinearLayoutManager(int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(getRecyclerView().getContext());
        manager.setOrientation(orientation);
        getRecyclerView().setLayoutManager(manager);
    }
    
    /**
     * Set the RecyclerView using {@link GridLayoutManager}.
     *
     * @param column grid column
     */
    public void setGridLayoutManager(int column) {
        final GridLayoutManager gridManager = new GridLayoutManager(getRecyclerView().getContext(), column);
        getRecyclerView().setLayoutManager(gridManager);
    }
    
    /**
     * Set the RecyclerView using {@link StaggeredGridLayoutManager}
     *
     * @param column      grid column
     * @param orientation orientation value of {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setStaggeredGridLayoutManager(int column, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(column, orientation);
        getRecyclerView().setLayoutManager(manager);
    }
    
    /**
     * Set the {@link RecyclerView} item decoration
     *
     * @param itemDecoration {@link RecyclerView.ItemDecoration}
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
            ((ListDividerItemDecoration) mItemDecoration).setPaddingEnd(end);
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

    /**
     * Common implements of ItemTouchHelper.Callback{@link #getMovementFlags(RecyclerView, RecyclerView.ViewHolder)}
     * @param recyclerView {@link RecyclerView}
     * @param viewHolder {@link RecyclerView.ViewHolder}
     *
     * @return movement flags
     * @since 3.0.1
     */
    public static int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        // linearLayoutManager
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            int orientation = linearLayoutManager.getOrientation();

            int dragFlag = 0;
            int swipeFlag = 0;

            // 为了方便理解，相当于分为横着的ListView和竖着的ListView
            if (orientation == LinearLayoutManager.HORIZONTAL) {// 如果是横向的布局
                swipeFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            else if (orientation == LinearLayoutManager.VERTICAL) {// 如果是竖向的布局，相当于ListView
                dragFlag = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                swipeFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            }
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        // GridLayoutManager
        else if (layoutManager instanceof GridLayoutManager || layoutManager instanceof StaggeredGridLayoutManager) {
            // flag如果值是0，相当于这个功能被关闭
            int dragFlag = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlag = 0;
            // create make
            return makeMovementFlags(dragFlag, swipeFlag);
        }
        return 0;
    }
}
