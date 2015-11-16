/*
 * Copyright 2014-2015 ieclipse.cn.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.ieclipse.af.R;

/**
 * 流式布局，支持子view水平及垂直方向的间距（horizontalSpacing and verticalSpacing）, 支持子View
 * margin, 支持子view gravity，支持divider，支持choice mode.
 *
 * @author Jamling
 * @date 2015/7/10.
 */
public class FlowLayout extends ViewGroup {
    private static final int SHOW_DIVIDER_NONE = LinearLayout.SHOW_DIVIDER_NONE;
    
    private static final int SHOW_DIVIDER_BEGINNING = LinearLayout.SHOW_DIVIDER_BEGINNING;
    
    private static final int SHOW_DIVIDER_END = LinearLayout.SHOW_DIVIDER_END;
    
    private static final int SHOW_DIVIDER_MIDDLE = LinearLayout.SHOW_DIVIDER_MIDDLE;
    
    private int mGravity;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mNumColumns = -1;
    private int mMaxChildWidth = 0;
    private int mMaxChildHeight = 0;
    // Horizontal divider width
    private int mDividerWidth = 0;
    private Drawable mDivider;
    private int mDividerPadding;
    private int mShowDividers = SHOW_DIVIDER_MIDDLE;
    
    private float mGridRatio = 0;
    private int mChoiceMode = ListView.CHOICE_MODE_NONE;
    
    private Drawable mVerticalDivider;
    private int mVerticalDividerHeight = 0;
    private int mShowVerticalDivider = SHOW_DIVIDER_MIDDLE;
    private int mVerticalDividerPadding = 0;
    
    private boolean mHasVisibleChild;
    private final List<List<View>> mLines = new ArrayList<List<View>>();
    private final List<Integer> mLineHeights = new ArrayList<Integer>();
    private final List<Rect> mDividersPos = new ArrayList<Rect>();
    private final List<Rect> mVerticalDividerPos = new ArrayList<Rect>();
    
    public FlowLayout(Context context) {
        this(context, null);
    }
    
    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        if (attrs == null) {
            return;
        }
        int[] and = { android.R.attr.gravity, android.R.attr.horizontalSpacing,
                android.R.attr.verticalSpacing, android.R.attr.numColumns,
                android.R.attr.choiceMode, android.R.attr.divider,
                android.R.attr.dividerHeight, android.R.attr.showDividers,
                android.R.attr.dividerPadding };
        int[] app = R.styleable.FlowLayout;
        
        int[] attr = new int[and.length + app.length];
        System.arraycopy(app, 0, attr, 0, app.length);
        System.arraycopy(and, 0, attr, app.length, and.length);
        
        TypedArray a = context.obtainStyledAttributes(attrs, attr);
        
        try {
            mGridRatio = a.getFloat(R.styleable.FlowLayout_fl_gridRatio,
                    mGridRatio);
            mChoiceMode = a.getInt(R.styleable.FlowLayout_fl_choiceMode,
                    mChoiceMode);
            mNumColumns = a.getInt(R.styleable.FlowLayout_fl_columns,
                    mNumColumns);
                    
            // vertical
            setVerticalDivider(
                    a.getDrawable(R.styleable.FlowLayout_fl_vDivider));
            mVerticalDividerHeight = a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_vDividerHeight,
                    mVerticalDividerHeight);
            mShowVerticalDivider = a.getInt(
                    R.styleable.FlowLayout_fl_vDividerShow,
                    mShowVerticalDivider);
            mVerticalDividerPadding = a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_vDividerPadding,
                    mVerticalDividerPadding);
            setVerticalSpacing(a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_vSpacing, 0));
            // horizontal
            setDividerDrawable(
                    a.getDrawable(R.styleable.FlowLayout_fl_hDivider));
            mDividerWidth = a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_hDividerWidth, 0);
            mShowDividers = a.getInt(R.styleable.FlowLayout_fl_hDividerShow,
                    mShowDividers);
            mDividerPadding = a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_hDividerPadding, mDividerPadding);
            setHorizontalSpacing(a.getDimensionPixelOffset(
                    R.styleable.FlowLayout_fl_hSpacing, 0));
                    
            // int offset = app.length;
            //
            // int index = a.getInt(offset++, -1);
            // if (index > 0) {
            // setGravity(index);
            // }
            // setHorizontalSpacing(a.getDimensionPixelOffset(offset++, 0));
            // setVerticalSpacing(a.getDimensionPixelOffset(offset++, 0));
            // mNumColumns = a.getInt(offset++, mNumColumns);
            // mSelectionMode = a.getInt(offset++, mSelectionMode);
            // Drawable d = a.getDrawable(offset++);
            // if (d != null) {
            // setDividerDrawable(d);
            // }
            // mDividerWidth = a.getDimensionPixelOffset(offset++,
            // mDividerWidth);
            // mShowDividers = a.getInt(offset++, mShowDividers);
            // mDividerPadding = a.getDimensionPixelOffset(offset++,
            // mDividerPadding);
            setGravity(a.getInt(R.styleable.FlowLayout_android_gravity, 0));
            if (a.hasValue(R.styleable.FlowLayout_android_numColumns)) {
                mNumColumns = a.getInt(
                        R.styleable.FlowLayout_android_numColumns, mNumColumns);
            }
            mSelectionMode = a.getInt(R.styleable.FlowLayout_android_choiceMode,
                    mSelectionMode);
            if (a.hasValue(R.styleable.FlowLayout_android_divider)) {
                setDividerDrawable(
                        a.getDrawable(R.styleable.FlowLayout_android_divider));
            }
            if (a.hasValue(R.styleable.FlowLayout_android_dividerHeight)) {
                mDividerWidth = a.getDimensionPixelOffset(
                        R.styleable.FlowLayout_android_dividerHeight,
                        mDividerWidth);
            }
            if (a.hasValue(R.styleable.FlowLayout_android_showDividers)) {
                mShowDividers = a.getInt(
                        R.styleable.FlowLayout_android_showDividers,
                        mShowDividers);
            }
            if (a.hasValue(R.styleable.FlowLayout_android_dividerPadding)) {
                mDividerPadding = a.getDimensionPixelOffset(
                        R.styleable.FlowLayout_android_dividerPadding,
                        mDividerPadding);
            }
        } finally {
            a.recycle();
        }
    }
    
    private void initLines() {
        mLines.clear();
        mLineHeights.clear();
        if (getChildCount() > 0) {
            mLines.add(new ArrayList<View>());
            mLineHeights.add(0);
        }
    }
    
    private List<View> getCurrentLine() {
        int size = mLines.size();
        return mLines.get(size - 1);
    }
    
    /**
     * Set number of columns, the result will cause FlowLayout likes GridView
     *
     * @param numColumns
     */
    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
    }
    
    /**
     * Set horizontal spacing between child view.
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        if (this.mHorizontalSpacing != horizontalSpacing) {
            this.mHorizontalSpacing = horizontalSpacing;
            requestLayout();
        }
    }
    
    public void setVerticalSpacing(int verticalSpacing) {
        if (this.mVerticalSpacing != verticalSpacing) {
            this.mVerticalSpacing = verticalSpacing;
            requestLayout();
        }
    }
    
    public int getHorizontalSpacing() {
        return Math.max(mHorizontalSpacing, getDividerWidth());
    }
    
    public int getVerticalSpacing() {
        return Math.max(mVerticalSpacing, getVerticalDividerHeight());
    }
    
    public int getShowVerticalDivider() {
        return mShowVerticalDivider;
    }
    
    public int getVerticalDividerHeight() {
        return mVerticalDividerHeight;
    }
    
    public void setVerticalDividerHeight(int verticalDividerHeight) {
        if (getVerticalDivider() != null) {
            this.mVerticalDividerHeight = verticalDividerHeight;
            requestLayout();
        }
        else {
            this.mVerticalDividerHeight = 0;
        }
    }
    
    public int getVerticalDividerPadding() {
        return mVerticalDividerPadding;
    }
    
    public Drawable getVerticalDivider() {
        return mVerticalDivider;
    }
    
    public void setVerticalDivider(Drawable divider) {
        if (divider == mVerticalDivider) {
            return;
        }
        
        this.mVerticalDivider = divider;
        if (divider != null) {
            if (!(divider instanceof ColorDrawable)) {
                mVerticalDividerHeight = divider.getIntrinsicHeight();
            }
        }
        else {
            mVerticalDividerHeight = 0;
        }
        setWillNotDraw(divider == null && getDividerDrawable() != null);
        requestLayout();
    }
    
    /**
     * Set how dividers should be shown between items in this layout
     *
     * @param showDividers
     *            One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *            {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END}, or
     *            {@link #SHOW_DIVIDER_NONE} to show no dividers.
     */
    public void setShowVerticalDividers(int showDividers) {
        if (showDividers != mShowVerticalDivider) {
            requestLayout();
        }
        mShowVerticalDivider = showDividers;
    }
    
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            if ((gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.START;
            }
            
            if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == 0) {
                gravity |= Gravity.TOP;
            }
            
            mGravity = gravity;
            requestLayout();
        }
    }
    
    public int getGravity() {
        return mGravity;
    }
    
    @Deprecated
    private int getGravityReflect() {
        int ret = Gravity.LEFT | Gravity.START;
        try {
            Class<?> clazz = getClass().getSuperclass();
            Field f = clazz.getDeclaredField("mGravity");
            f.setAccessible(true);
            ret = f.getInt(this);
        } catch (Exception e) {
        
        }
        return ret;
    }
    
    public void setDividerDrawable(Drawable divider) {
        if (divider == mDivider) {
            return;
        }
        mDivider = divider;
        if (divider != null) {
            mDividerWidth = divider.getIntrinsicWidth();
        }
        else {
            mDividerWidth = 0;
        }
        setWillNotDraw(divider == null);
        requestLayout();
    }
    
    public int getDividerWidth() {
        return mDividerWidth;
    }
    
    @Deprecated
    private int getDividerWidthReflect() {
        int ret = 0;
        try {
            Class<?> clazz = getClass().getSuperclass();
            Method m = clazz.getDeclaredMethod("getDividerWidth");
            ret = (Integer) m.invoke(this);
        } catch (Exception e) {
        
        }
        return ret;
    }
    
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Drawable getDividerDrawable() {
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        // return super.getDividerDrawable();
        // }
        // else {
        // try {
        // Class<?> clazz = getClass().getSuperclass();
        // Method m = clazz.getDeclaredMethod("getDividerDrawable");
        // return (Drawable) m.invoke(this);
        // } catch (Exception e) {
        // return null;
        // }
        // }
        return mDivider;
    }
    
    public int getShowDividers() {
        return mShowDividers;
    }
    
    public void setShowDividers(int showDividers) {
        if (showDividers != mShowDividers) {
            requestLayout();
        }
        mShowDividers = showDividers;
    }
    
    public int getDividerPadding() {
        return mDividerPadding;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert(MeasureSpec
                .getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
                - getPaddingBottom();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        
        final int count = getChildCount();
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
        if (mHasVisibleChild
                && (getShowDividers() & SHOW_DIVIDER_BEGINNING) != 0) {
            left += getDividerWidth();
            width -= getDividerWidth();
        }
        // horizontal end divider
        if (mHasVisibleChild && (getShowDividers() & SHOW_DIVIDER_END) != 0) {
            width -= getDividerWidth();
        }
        // vertical beginning divider
        if (mHasVisibleChild
                && (getShowVerticalDivider() & SHOW_DIVIDER_BEGINNING) != 0) {
            top += getVerticalDividerHeight();
        }
        
        int x = left;
        int y = top;
        if (mNumColumns > 0) {
            mMaxChildWidth = (width
                    - getHorizontalSpacing() * (mNumColumns - 1)) / mNumColumns;
        }
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                
                child.measure(getChildWidthMeasureSpec(child, width),
                        getChildHeightMeasureSpec(child, height));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();
                
                cw += lp.leftMargin + lp.rightMargin;
                ch += lp.topMargin + lp.bottomMargin;
                lineHeight = Math.max(lineHeight, ch);
                lineHeight = Math.max(lineHeight, mMaxChildHeight);
                
                if (mNumColumns > 0) {
                    cw = mMaxChildWidth;
                }
                if (x + cw > width + left) {
                    x = left;
                    y += lineHeight;
                    y += getVerticalSpacing();
                    lineHeight = ch;
                    mLines.add(new ArrayList<View>());
                    mLineHeights.add(lineHeight);
                }
                
                x += cw + getHorizontalSpacing();
                // if ((getShowDividers() & LinearLayout.SHOW_DIVIDER_MIDDLE) !=
                // 0) {
                // x += getDividerWidthReflect();
                // }
                getCurrentLine().add(child);
                mLineHeights.set(mLines.size() - 1, lineHeight);
            }
        }
        // vertical end divider
        if (mHasVisibleChild
                && (getShowVerticalDivider() & SHOW_DIVIDER_END) != 0) {
            y += getVerticalDividerHeight();
        }
        y += getPaddingBottom();
        
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = y + lineHeight;
        }
        else if (MeasureSpec
                .getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (y + lineHeight < height) {
                height = y + lineHeight;
            }
        }
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(),
                height);
    }
    
    private int getChildWidthMeasureSpec(View child, int width) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        // default wrap_content
        int childWidthMeasureSpec;
        int cw;
        if (mNumColumns <= 0) {
            cw = width - lp.leftMargin - lp.rightMargin;
        }
        else {
            cw = mMaxChildWidth - lp.leftMargin - lp.rightMargin;
        }
        if (lp.width > 0) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
                    MeasureSpec.EXACTLY);
        }
        else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw,
                    MeasureSpec.EXACTLY);
        }
        else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw,
                    MeasureSpec.AT_MOST);
        }
        return childWidthMeasureSpec;
    }
    
    private int getChildHeightMeasureSpec(View child, int height) {
        int childHeightMeasureSpec;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.height > 0) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
                    MeasureSpec.EXACTLY);
        }
        else if (lp.height == LayoutParams.MATCH_PARENT) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.AT_MOST);
        }
        else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        return childHeightMeasureSpec;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDividersPos.clear();
        mVerticalDividerPos.clear();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        if (mHasVisibleChild
                && (getShowDividers() & SHOW_DIVIDER_BEGINNING) != 0) {
            left += getDividerWidth();
        }
        if (mHasVisibleChild
                && (getShowVerticalDivider() & SHOW_DIVIDER_BEGINNING) != 0) {
            top += getVerticalDividerHeight();
        }
        
        int x = left;
        int y = top;
        
        final int lineCount = mLines.size();
        for (int lineIndex = 0; lineIndex < lineCount; lineIndex++) {
            int maxLineHeight = mLineHeights.get(lineIndex);
            List<View> line = mLines.get(lineIndex);
            if (line != null && !line.isEmpty()) {
                if (lineIndex > 0) {
                    if ((mShowVerticalDivider & SHOW_DIVIDER_MIDDLE) != 0) {
                        mVerticalDividerPos
                                .add(new Rect(x, y - getVerticalSpacing(),
                                        x + getMeasuredWidth(), y));
                    }
                }
                int count = line.size();
                for (int i = 0; i < count; i++) {
                    View child = line.get(i);
                    if (child != null && child.getVisibility() != GONE) {
                        LayoutParams lp = (LayoutParams) child
                                .getLayoutParams();
                        int cw = child.getMeasuredWidth();
                        int sw = cw + lp.leftMargin + lp.rightMargin;
                        int ch = child.getMeasuredHeight();
                        int sh = ch + lp.topMargin + lp.bottomMargin;
                        int offsetY = 0;
                        int offsetX = 0;
                        int gravity = getGravity()
                                & Gravity.VERTICAL_GRAVITY_MASK;
                        if (maxLineHeight > 0) {
                            if (gravity == Gravity.CENTER_VERTICAL) {
                                offsetY = (maxLineHeight - sh) / 2;
                            }
                            else if (gravity == Gravity.BOTTOM) {
                                offsetY = maxLineHeight - sh;
                            }
                        }
                        if (offsetY < 0) {
                            offsetY = 0;
                        }
                        
                        if (mMaxChildWidth > 0) {
                            gravity = getGravity()
                                    & Gravity.HORIZONTAL_GRAVITY_MASK;
                            if (gravity == Gravity.CENTER_HORIZONTAL) {
                                offsetX = (mMaxChildWidth - sw) / 2;
                            }
                            else if (gravity == Gravity.RIGHT) {
                                offsetX = mMaxChildWidth - sw;
                            }
                            if (offsetX < 0) {
                                offsetX = 0;
                            }
                        }
                        int xx = x + offsetX + lp.leftMargin;
                        int yy = y + offsetY + lp.topMargin;
                        
                        child.layout(xx, yy, xx + cw, yy + ch);
                        if (mMaxChildWidth > 0) {
                            x += mMaxChildWidth + getHorizontalSpacing();
                        }
                        else {
                            x += sw + getHorizontalSpacing();
                        }
                        if (i < count - 1) {
                            if ((getShowDividers()
                                    & LinearLayout.SHOW_DIVIDER_MIDDLE) != 0) {
                                mDividersPos.add(
                                        new Rect(x - getHorizontalSpacing(), y,
                                                x, maxLineHeight + y));
                            }
                        }
                        else if (i < mNumColumns - 1) {
                            if ((getShowDividers()
                                    & LinearLayout.SHOW_DIVIDER_END) != 0) {
                                mDividersPos.add(
                                        new Rect(x - getHorizontalSpacing(), y,
                                                x, maxLineHeight + y));
                            }
                        }
                    }
                }
                y += maxLineHeight + getVerticalSpacing();
                x = left;
            } // end column for
        } // end row for
          // if (mHasVisibleChild
          // && (mShowVerticalDivider & SHOW_DIVIDER_END) != 0) {
          // mVerticalDividerPos.add(new Rect(x, y, x + getMeasuredWidth(),
          // y + getVerticalDividerHeight()));
          // }
    }
    
    void drawVerticalDivider(Canvas canvas, Rect r) {
        Drawable d = getDividerDrawable();
        int w = getDividerWidth();
        // if (d instanceof ColorDrawable) {
        // w = mDividerWidth;
        // }
        int left = (getHorizontalSpacing() - w) / 2;
        if (left < 0) {
            left = 0;
            w = getHorizontalSpacing();
        }
        left = r.left + left;
        d.setBounds(left, r.top + getDividerPadding(), left + w,
                r.bottom - getDividerPadding());
        d.draw(canvas);
    }
    
    void drawDividerHorizontal(Canvas canvas) {
        if (mHasVisibleChild
                && (getShowDividers() & SHOW_DIVIDER_BEGINNING) != 0) {
            Drawable d = getDividerDrawable();
            d.setBounds(getPaddingLeft(), getPaddingTop(),
                    getPaddingLeft() + getDividerWidth(),
                    getHeight() - getPaddingBottom());
            d.draw(canvas);
        }
        for (Rect p : mDividersPos) {
            drawVerticalDivider(canvas, p);
        }
        if (mHasVisibleChild
                && (getShowDividers() & SHOW_DIVIDER_END) != 0) {
            Drawable d = getDividerDrawable();
            d.setBounds(getWidth() - getPaddingBottom() - getDividerWidth(),
                    getPaddingTop(), getWidth() - getPaddingBottom(),
                    getHeight() - getPaddingBottom());
            d.draw(canvas);
        }
    }
    
    void drawHorizontalDivider(Canvas canvas, Rect r) {
        Drawable d = getVerticalDivider();
        int h = getVerticalDividerHeight();
        // if (d instanceof ColorDrawable) {
        // h = getVerticalDividerHeight();
        // }
        int t = (getVerticalSpacing() - h) / 2;
        if (t < 0) {
            t = 0;
            h = getVerticalSpacing();
        }
        t = r.top + t;
        d.setBounds(r.left + getVerticalDividerPadding(), t,
                r.right - getVerticalDividerPadding(), t + h);
        d.draw(canvas);
    }
    
    void drawDividerVertical(Canvas canvas) {
        if (mHasVisibleChild
                && (mShowVerticalDivider & SHOW_DIVIDER_BEGINNING) != 0) {
            Drawable d = getVerticalDivider();
            d.setBounds(getPaddingLeft() - getVerticalDividerPadding(),
                    getPaddingTop(),
                    getWidth() - getPaddingBottom()
                            - getVerticalDividerPadding(),
                    getPaddingTop() + getVerticalDividerHeight());
            d.draw(canvas);
        }
        for (Rect p : mVerticalDividerPos) {
            drawHorizontalDivider(canvas, p);
        }
        if (mHasVisibleChild
                && (mShowVerticalDivider & SHOW_DIVIDER_END) != 0) {
            Drawable d = getVerticalDivider();
            d.setBounds(getPaddingLeft() - getVerticalDividerPadding(),
                    getHeight() - getPaddingBottom()
                            - getVerticalDividerHeight(),
                    getWidth() - getPaddingBottom()
                            - getVerticalDividerPadding(),
                    getHeight() - getPaddingBottom());
            d.draw(canvas);
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        Drawable d = getDividerDrawable();
        if (d != null) {
            drawDividerHorizontal(canvas);
        }
        
        d = getVerticalDivider();
        if (d != null) {
            drawDividerVertical(canvas);
        }
    }
    
    // -----------> selection
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener = new PassThroughHierarchyChangeListener();
    private int mSelectionMode = ListView.CHOICE_MODE_NONE;
    
    private boolean isSingleChoice() {
        return mSelectionMode == ListView.CHOICE_MODE_SINGLE;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        
        if (isSingleChoice()) {
            // checks the appropriate radio button as requested in the XML file
            if (mCheckedId != -1) {
                mProtectFromCheckedChange = true;
                setCheckedStateForView(mCheckedId, true);
                mProtectFromCheckedChange = false;
                setCheckedId(mCheckedId);
            }
        }
    }
    
    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isSingleChoice()) {
            if (child instanceof CompoundButton) {
                final CompoundButton button = (CompoundButton) child;
                if (button.isChecked()) {
                    mProtectFromCheckedChange = true;
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    mProtectFromCheckedChange = false;
                    setCheckedId(button.getId());
                }
            }
        }
        
        super.addView(child, index, params);
    }
    
    /**
     * <p>
     * Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.
     * </p>
     *
     * @param id
     *            the unique id of the radio button to select in this group
     *            
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(int id) {
        if (isSingleChoice()) {
            // don't even bother
            if (id != -1 && (id == mCheckedId)) {
                return;
            }
            
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            
            if (id != -1) {
                setCheckedStateForView(id, true);
            }
            
            setCheckedId(id);
        }
        else {
            setCheckedStateForView(id, true);
        }
    }
    
    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }
    
    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof CompoundButton) {
            ((CompoundButton) checkedView).setChecked(checked);
        }
    }
    
    /**
     * <p>
     * Returns the identifier of the selected radio button in this group. Upon
     * empty selection, the returned value is -1.
     * </p>
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }
    
    /**
     * <p>
     * Clears the selection. When the selection is cleared, no radio button in
     * this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.
     * </p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }
    
    /**
     * <p>
     * Register a callback to be invoked when the checked radio button changes
     * in this group.
     * </p>
     *
     * @param listener
     *            the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(
            OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }
    
    /**
     * <p>
     * A pass-through listener acts upon the events and dispatches them to
     * another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.
     * </p>
     */
    private class PassThroughHierarchyChangeListener
            implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;
        
        /**
         * {@inheritDoc}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onChildViewAdded(View parent, View child) {
            if (parent == FlowLayout.this && child instanceof CompoundButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                        child.setId(id);
                    }
                }
                ((CompoundButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            }
            
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }
        
        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FlowLayout.this && child instanceof CompoundButton) {
                ((CompoundButton) child).setOnCheckedChangeListener(null);
            }
            
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
    
    private class CheckedStateTracker
            implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            if (isSingleChoice()) {
                // prevents from infinite recursion
                if (mProtectFromCheckedChange) {
                    return;
                }
                
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                
                int id = buttonView.getId();
                setCheckedId(id);
            }
            else {
                setCheckedStateForView(buttonView.getId(), isChecked);
            }
        }
    }
    
    /**
     * @param choiceMode
     *            
     * @see android.widget.ListView#setChoiceMode(int)
     */
    
    public void setChoiceMode(int choiceMode) {
        mSelectionMode = choiceMode;
    }
    
    public List<View> getCheckedViews() {
        List<View> list = new ArrayList<View>();
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View c = getChildAt(i);
            if (c instanceof CompoundButton) {
                list.add(c);
            }
        }
        return list;
    }
    
    /**
     * <p>
     * Interface definition for a callback to be invoked when the checked radio
     * button changed in this group.
     * </p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>
         * Called when the checked radio button has changed. When the selection
         * is cleared, checkedId is -1.
         * </p>
         *
         * @param group
         *            the group in which the checked radio button has changed
         * @param checkedId
         *            the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(FlowLayout group, int checkedId);
    }
    
    // ----> adapter support
    /**
     * Should be used by subclasses to listen to changes in the dataset
     */
    private AdapterDataSetObserver mDataSetObserver;
    
    /**
     * The adapter containing the data to be displayed by this view
     */
    private ListAdapter mAdapter;
    
    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            reset();
        }
        
        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }
    
    private void reset() {
        removeAllViews();
        
        for (int i = 0; i < mAdapter.getCount(); i++) {
            final View childView = mAdapter.getView(i, null, this);
            addView(childView, new MarginLayoutParams(LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT));
        }
    }
    
    /**
     * 像ListView、GridView一样使用FlowLayout
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        
        // 清除现有的数据
        removeAllViews();
        mAdapter = adapter;
        
        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }
    
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }
    
    /**
     * Returns a set of layout parameters with a width of
     * {@link android.view.ViewGroup.LayoutParams#MATCH_PARENT} and a height of
     * {@link android.view.ViewGroup.LayoutParams#WRAP_CONTENT} when the
     * layout's orientation is {@link #VERTICAL}. When the orientation is
     * {@link #HORIZONTAL}, the width is set to
     * {@link LayoutParams#WRAP_CONTENT} and the height to
     * {@link LayoutParams#WRAP_CONTENT}.
     */
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }
    
    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }
    
    // Override to allow type-checking of LayoutParams.
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LinearLayout.LayoutParams;
    }
    
    /**
     * Per-child layout information associated with ViewLinearLayout.
     * 
     * @attr ref android.R.styleable#LinearLayout_Layout_layout_weight
     * @attr ref android.R.styleable#LinearLayout_Layout_layout_gravity
     */
    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            
        }
        
        /**
         * {@inheritDoc}
         */
        public LayoutParams(int width, int height) {
            super(width, height);
        }
        
        /**
         * Creates a new set of layout parameters with the specified width,
         * height and weight.
         *
         * @param width
         *            the width, either {@link #MATCH_PARENT},
         *            {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param height
         *            the height, either {@link #MATCH_PARENT},
         *            {@link #WRAP_CONTENT} or a fixed size in pixels
         * @param weight
         *            the weight
         */
        public LayoutParams(int width, int height, float weight) {
            super(width, height);
        }
        
        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }
        
        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.MarginLayoutParams source) {
            super(source);
        }
        
        /**
         * Copy constructor. Clones the width, height, margin values, weight,
         * and gravity of the source.
         *
         * @param source
         *            The layout params to copy from.
         */
        public LayoutParams(LayoutParams source) {
            super(source);
        }
    }
}