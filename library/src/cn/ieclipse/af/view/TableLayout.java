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
package cn.ieclipse.af.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Description
 *
 * @author Jamling
 */
public class TableLayout extends FlowLayout {
    public TableLayout(Context context) {
        super(context);
    }

    public TableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private int visibleColumns;
    private int hiddenColumns;
    private int rows;
    //private int[] rowHeights;
    //private int defaultRowHeight = AppUtils.dp2px(getContext(), 40);
    //private int screenWidth = AppUtils.getScreenWidth(getContext());

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int count = getChildCount();
        int lineHeight = 0;
        initLines();

        mHasVisibleChild = false;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                mHasVisibleChild = true;
                break;
            }
        }

        // horizontal beginning divider
        if (mHasVisibleChild && (getShowHorizontalDividers() & SHOW_DIVIDER_BEGINNING) != 0) {
            left += getDividerWidth();
            width -= getDividerWidth();
        }
        // horizontal end divider
        if (mHasVisibleChild && (getShowHorizontalDividers() & SHOW_DIVIDER_END) != 0) {
            width -= getDividerWidth();
            left += getDividerWidth();
        }
        // vertical beginning divider
        if (mHasVisibleChild && (getShowVerticalDividers() & SHOW_DIVIDER_BEGINNING) != 0) {
            top += getVerticalDividerHeight();
        }

        int y = top;
        int x = left;

        if (widthMode == MeasureSpec.UNSPECIFIED) {
            mMaxChildWidth = 0;
        }
        else if (widthMode == MeasureSpec.AT_MOST) {
//            if (width > 0) {
//                mMaxChildWidth = getVisibleColumns() <= 0 ? width
//                                                          : (width - getHorizontalSpacing() * (getVisibleColumns()
// - 1))
//                                     / getVisibleColumns();
//            }
        }
        else if (widthMode == MeasureSpec.EXACTLY) {
            mMaxChildWidth = getVisibleColumns() <= 0 ? width
                                                      : (width - getHorizontalSpacing() * (getVisibleColumns() - 1))
                                 / getVisibleColumns();
        }
//        int cw = getVisibleColumns() <= 0 ? width : (width - getHorizontalSpacing() * (getVisibleColumns() - 1))
//            / getVisibleColumns();
//        mMaxChildWidth = cw;
        // mMaxChildHeight = defaultRowHeight;
        // System.out.println("count:" + count + ", col:" + getNumColumns());
        boolean line1 = true;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                child.measure(getChildWidthMeasureSpec(child, width, 0), getChildHeightMeasureSpec(child, height));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();
                cw += lp.leftMargin + lp.rightMargin;
                ch += lp.topMargin + lp.bottomMargin;
                lineHeight = Math.max(lineHeight, ch);
                lineHeight = Math.max(lineHeight, mMaxChildHeight);
                if (i % getNumColumns() == 0 && i > 0) {
                    // y += lineHeight;
                    y += getVerticalSpacing();

                    lineHeight = ch;
                    mLines.add(new ArrayList<View>());
                    mLineHeights.add(lineHeight);
                    // System.out.println("lineHeight:" + mLineHeights);
                    line1 = false;
                }
                if (line1) {
                    x += cw;
                }
                getCurrentLine().add(child);
                mLineHeights.set(mLines.size() - 1, lineHeight);
            }
        }//System.out.println("line height " + mLineHeights);

        if (mHasVisibleChild && (getShowVerticalDividers() & SHOW_DIVIDER_END) != 0) {
            y += getVerticalDividerHeight();
        }
        y += getPaddingBottom();

        for (int t : mLineHeights) {
            y += t;
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = y;
        }
        else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (y < height) {
                height = y;
            }
        }
        rows = mLines.size();

        int measuredHeight = Math.max(0, height);
        int measuredWidth = width;
        if (widthMode == MeasureSpec.UNSPECIFIED || widthMode == MeasureSpec.AT_MOST) {
            measuredWidth = x + (getNumColumns() - 1) * getHorizontalSpacing() + getPaddingRight();
            if (measuredWidth < 0) {
                measuredWidth = 0;
            }
        }
        else {
            measuredWidth += getPaddingLeft() + getPaddingRight();
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    protected int getChildWidthMeasureSpec(View child, int width, int rest) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        // default wrap_content
        int childWidthMeasureSpec;
        int cw;
        if (mMaxChildWidth > 0) {
            cw = mMaxChildWidth - lp.leftMargin - lp.rightMargin;
        }
        else {
            cw = width - lp.leftMargin - lp.rightMargin;
        }
        //
        if (lp.width > 0) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
        }
        else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.AT_MOST);
        }
        else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        return childWidthMeasureSpec;
    }

    public int getVisibleColumns() {
        if (visibleColumns <= 0) {
            visibleColumns = getNumColumns();
        }
        return visibleColumns;
    }

    public void setVisibleColumns(int visibleColumns) {
        if (visibleColumns < 0 || visibleColumns > getNumColumns()) {
            throw new IllegalArgumentException(
                "invalid hidden columns :" + visibleColumns + ", current columns is : " + getNumColumns());
        }
        this.visibleColumns = visibleColumns;
    }

    @Override
    public int getNumColumns() {
        return super.getNumColumns() - getHiddenColumns();
    }

    public void setHiddenColumns(int hiddenColumns) {
        if (hiddenColumns < 0 || hiddenColumns >= getNumColumns()) {
            throw new IllegalArgumentException(
                "invalid hidden columns :" + hiddenColumns + ", current columns is : " + getNumColumns());
        }
        this.hiddenColumns = hiddenColumns;
    }

    public int getHiddenColumns() {
        return hiddenColumns;
    }

    public int getRows() {
        return rows;
    }

    public void clear() {
        removeAllViews();
    }

    public void reset() {
        clear();
        this.visibleColumns = 0;
        this.hiddenColumns = 0;
        setNumColumns(0);
    }
}
