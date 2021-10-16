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

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * 基于设置RecyclerView背景色的另类分隔线方式。
 *
 * @author Jamling
 * @deprecated
 */
public class GridSpaceDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    protected int mOrientation;

    /**
     * Creates a divider {@link androidx.recyclerview.widget.RecyclerView.ItemDecoration} that can be used with a {@link
     * androidx.recyclerview.widget.LinearLayoutManager}.
     *
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public GridSpaceDecoration(int orientation) {
        setOrientation(orientation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
    }

    /**
     * Sets the orientation for this divider. This should be called if {@link androidx.recyclerview.widget.LinearLayoutManager}
     * changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL) {
            throw new IllegalArgumentException("Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    private boolean mNeedLeftSpacing = false;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = 0;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) parent.getLayoutManager();
            spanCount = glm.getSpanCount();
        } else if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) parent.getLayoutManager();
            spanCount = sglm.getSpanCount();
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column
            int row = position / spanCount;
            if (column != 0) {
                outRect.left = getHorizontalSpacing();
            }
            if (row != 0) {
                outRect.top = getVerticalSpacing();
            }
            return;
        }

        int position = parent.getChildAdapterPosition(view); // item position

        int hspacing = getHorizontalSpacing();
        int vspacing = getVerticalSpacing();
        int frameWidth = (int) ((parent.getWidth() - (float) hspacing * (spanCount - 1)) / spanCount);//
        int padding = parent.getWidth() / spanCount - frameWidth;
        if (position >= spanCount) {
            outRect.top = vspacing;
        }
        if (position % spanCount == 0) {
            outRect.left = 0;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((position + 1) % spanCount == 0) {
            mNeedLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false;
            outRect.left = hspacing - padding;
            if ((position + 2) % spanCount == 0) {
                outRect.right = hspacing - padding;
            } else {
                outRect.right = hspacing / 2;
            }
        } else if ((position + 2) % spanCount == 0) {
            mNeedLeftSpacing = false;
            outRect.left = hspacing / 2;
            outRect.right = hspacing - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = hspacing / 2;
            outRect.right = hspacing / 2;
        }
    }

    public int getHorizontalSpacing() {
        return mHorizontalSpacing;
    }

    public int getVerticalSpacing() {
        return mVerticalSpacing;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
    }

    public void setSpacing(int spacing) {
        setHorizontalSpacing(spacing);
        setVerticalSpacing(spacing);
    }
}
