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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Description
 *
 * @author Jamling
 */
public class GridDividerItemDecoration extends ListDividerItemDecoration {
    /**
     * Creates a divider {@link android.support.v7.widget.RecyclerView.ItemDecoration} that can be used with a
     * {@link android.support.v7.widget.LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public GridDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            drawHorizontal(c, parent);
            drawVertical(c, parent);
        }
    }

    private boolean mNeedLeftSpacing = false;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof GridLayoutManager) {

            GridLayoutManager glm = (GridLayoutManager) parent.getLayoutManager();
            int position = parent.getChildAdapterPosition(view); // item position
            int spanCount = glm.getSpanCount();
            int column = position % spanCount; // item column
            int row = position / spanCount;
            int hspacing = getDividerDrawableWidth();
            int vspacing = getDividerDrawableHeight();
            int frameWidth = (int) ((parent.getWidth() - (float) hspacing * (spanCount - 1)) / spanCount);//
            int padding = parent.getWidth() / spanCount - frameWidth;
            if (position % spanCount == 0) {
                outRect.left = 0;
                outRect.right = padding;
                mNeedLeftSpacing = true;
            }
            else if ((position + 1) % spanCount == 0) {
                mNeedLeftSpacing = false;
                outRect.right = 0;
                outRect.left = padding;
            }
            else if (mNeedLeftSpacing) {
                mNeedLeftSpacing = false;
                outRect.left = hspacing - padding;
                if ((position + 2) % spanCount == 0) {
                    outRect.right = hspacing - padding;
                }
                else {
                    outRect.right = hspacing / 2;
                }
            }
            else if ((position + 2) % spanCount == 0) {
                mNeedLeftSpacing = false;
                outRect.left = hspacing / 2;
                outRect.right = hspacing - padding;
            }
            else {
                mNeedLeftSpacing = false;
                outRect.left = hspacing / 2;
                outRect.right = hspacing / 2;
            }
            return;
        }
    }
}
