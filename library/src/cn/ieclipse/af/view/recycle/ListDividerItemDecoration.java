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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Enhanced {@link androidx.recyclerview.widget.DividerItemDecoration} support padding, color, height
 *
 * @author Jamling
 */
public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerColor;
    private int mDividerPadding;
    private int mPaddingStart;
    private int mPaddingEnd;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    protected int mOrientation;

    private final Rect mBounds = new Rect();

    /**
     * Creates a divider {@link androidx.recyclerview.widget.RecyclerView.ItemDecoration} that can be used with a {@link
     * androidx.recyclerview.widget.LinearLayoutManager}.
     *
     * @param context Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public ListDividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        if (a.hasValue(0)) {
            mDivider = a.getDrawable(0);
        }
        a.recycle();
        setOrientation(orientation);
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

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        if (mDivider == drawable) {
            return;
        }
        if (drawable instanceof StateListDrawable) {
            mDivider = drawable.getCurrent();
        } else {
            mDivider = drawable;
        }
    }

    public Drawable getDivider() {
        return mDivider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }

        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    protected void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int left;
        int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right, parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        left += mPaddingStart;
        right -= mPaddingEnd;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            final int bottom = mBounds.bottom + Math.round(child.getTranslationY());
            final int top = bottom - getDividerDrawableHeight();
            mDivider.setBounds(left, top, right, bottom);
            if (i < childCount - 1) {
                mDivider.draw(canvas);
            } else if (showEnd()) {
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    protected void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        int top;
        int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        top += mPaddingStart;
        bottom -= mPaddingEnd;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(child.getTranslationX());
            final int left = right - getDividerDrawableWidth();
            mDivider.setBounds(left, top, right, bottom);
            if (i < childCount - 1) {
                mDivider.draw(canvas);
            } else if (showEnd()) {
                mDivider.draw(canvas);
            }
        }
        canvas.restore();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            outRect.set(0, 0, 0, getDividerDrawableHeight());
        } else {
            outRect.set(0, 0, getDividerDrawableWidth(), 0);
        }
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
    }

    public int getDividerHeight() {
        return mDividerHeight;
    }

    public void setDividerColor(int color) {
        this.mDividerColor = color;
        if (mDivider != null && mDivider instanceof ColorDrawable) {
            ((ColorDrawable) mDivider).setColor(color);
        } else {
            // setDrawable(AppUtils.tintDrawable(mDivider, color));
            setDrawable(new ColorDrawable(color));
        }
    }

    public void setPaddingStart(int paddingStart) {
        this.mPaddingStart = paddingStart;
    }

    public void setPaddingEnd(int paddingEnd) {
        this.mPaddingEnd = paddingEnd;
    }

    protected int getDividerDrawableHeight() {
        return Math.max(mDivider.getIntrinsicHeight(), mDividerHeight);
    }

    protected int getDividerDrawableWidth() {
        return Math.max(mDivider.getIntrinsicWidth(), mDividerHeight);
    }

    public static final int BEGINNING = 1;
    public static final int MIDDLE = 2;
    public static final int END = 4;

    private int mDividerShow = MIDDLE;

    public void setDividerShow(int show) {
        this.mDividerShow = show;
    }

    protected boolean showBeginning() {
        return (mDividerShow & BEGINNING) > 0;
    }

    protected boolean showEnd() {
        return (mDividerShow & END) > 0;
    }

    protected boolean showMiddle() {
        return (mDividerShow & MIDDLE) > 0;
    }
}
