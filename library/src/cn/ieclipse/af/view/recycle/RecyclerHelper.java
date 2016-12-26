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

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.StaggeredGridLayoutManager;

/**
 * Description
 *
 * @author Jamling
 */
public final class RecyclerHelper {
    private RecyclerView recyclerView;

    /**
     * 当前的ItemDecoration
     */
    private RecyclerView.ItemDecoration mItemDecoration;
    /**
     * 默认list分隔线
     */
    private ListItemDecoration mListDecoration;
    /**
     * 默认流式分隔线
     */
    private ItemOffsetDecoration mOffsetDecoration;

    /**
     * 默认分隔线颜色
     */
    private int mDividerColor;
    /**
     * 默认分隔线高度
     */
    private int mDividerHeight = 1;

    /**
     * 分割线的宽度（网格布局有效）
     */
    private int mOffPadding = 1;
    private int mVerticalPadding = mOffPadding;
    private int mHorizontalPadding = mOffPadding;

    private DividerItemDecoration mDividerDecoration;

    public void setRecyclerView(RecyclerView recyclerView) {
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
     * 设置AfRecycleView默认样式
     *
     * @param orientation {@link android.support.v7.widget.LinearLayoutManager#HORIZONTAL} or
     *                    {@link android.support.v7.widget.LinearLayoutManager#VERTICAL}
     */
    public void setLinearLayoutManager(int orientation) {
        LinearLayoutManager manager = new LinearLayoutManager(getRecyclerView().getContext());
        manager.setOrientation(orientation);
        getRecyclerView().setLayoutManager(manager);
    }

    public void setGridLayoutManager(int column) {
        final GridLayoutManager gridManager = new GridLayoutManager(getRecyclerView().getContext(), column);
        getRecyclerView().setLayoutManager(gridManager);
    }

    public void setStaggeredGridLayoutManager(int column, int orientation) {
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(column, orientation);
        getRecyclerView().setLayoutManager(manager);
    }

    public void setItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (mItemDecoration != null) {
            getRecyclerView().removeItemDecoration(mItemDecoration);
        }
        mItemDecoration = itemDecoration;
        getRecyclerView().addItemDecoration(mItemDecoration);
        getRecyclerView().invalidateItemDecorations();
    }

    public void setDividerHeight(int height) {
        if (mItemDecoration instanceof ListDividerItemDecoration) {
            ((ListDividerItemDecoration) mItemDecoration).setDividerHeight(height);
            getRecyclerView().invalidateItemDecorations();
        }
    }

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

    public void setSpanSizeLookup() {
        RecyclerView.LayoutManager lm = getRecyclerView().getLayoutManager();
        if (lm instanceof GridLayoutManager) {
            ((GridLayoutManager) lm).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return 0;
                }
            });
        }
        else if (lm instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) lm).setSpanCount(0);
        }
    }
}
