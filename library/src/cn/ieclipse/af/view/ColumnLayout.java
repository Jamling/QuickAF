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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * A text column layout.
 * </p>
 * Support
 * <ul>
 * <li><code>android:numColumns</code></li>
 * <li><code>android:horizontalSpacing</code></li>
 * <li><code>android:verticalSpacing</code></li>
 * </ul>
 * in xml layout.
 * <p>
 * <strong>Usage</strong>
 * </p>
 * <pre class="prettyprint">
 * private void initGrid2() {
 * int columns = RandomUtils.genInt(1, 4);
 * // mGrid2.setWeights(new int[] { 0, 1, 0, 1 });
 * mGrid2.clear();
 * mGrid2.setNumColumns(columns);
 * int sum = 0;
 * int size = RandomUtils.genInt(5);
 * for (int i = 0; i &lt; size; i++) {
 * int tmp = RandomUtils.genInt(1, colums);
 * String[] ss = new String[tmp];
 * for (int k = 0; k &lt; tmp; k++) {
 * ss[k] = RandomUtils.genGBK(2, 8);
 * }
 * mGrid2.addRow(ss);
 * }
 * }
 * </pre>
 * <strong>Note</strong> padding top and padding bottom is not supported.
 *
 * @author Jamling
 * @deprecated will deleted in future
 */
public class ColumnLayout extends View {
    
    /**
     * @see android.view.View#View(android.content.Context)
     */
    public ColumnLayout(Context context) {
        super(context);
        init(context, null);
    }
    
    /**
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet)
     */
    public ColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    /**
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet, int)
     */
    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    /**
     * @see android.view.View#View(android.content.Context, android.util.AttributeSet, int, int)
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    
    public static final int SHOW_DIVIDER_NONE = LinearLayout.SHOW_DIVIDER_NONE;
    
    public static final int SHOW_DIVIDER_BEGINNING = LinearLayout.SHOW_DIVIDER_BEGINNING;
    
    public static final int SHOW_DIVIDER_END = LinearLayout.SHOW_DIVIDER_END;
    
    public static final int SHOW_DIVIDER_MIDDLE = LinearLayout.SHOW_DIVIDER_MIDDLE;
    
    private int mVerticalDividerHeight = 1;
    private int mShowVerticalDivider = SHOW_DIVIDER_NONE;
    private int mVerticalDividerPadding = 0;
    
    private int mDividerWidth = 1;
    private int mDividerPadding;
    private int mShowDividers = SHOW_DIVIDER_NONE;

    private int[] weights = null;
    private TextPaint textPaint;
    private Paint paint;
    private List<CharSequence[]> rows;
    private List<StaticLayout[]> rowLayouts;
    private List<Integer> rowHeights;
    private int[] columnWidths;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mNumColumns = 1;
    private int mGravity = Gravity.NO_GRAVITY;
    
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setStrokeWidth(1);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = context.getResources().getDisplayMetrics().density;
        int[] attr
            = {android.R.attr.horizontalSpacing, android.R.attr.verticalSpacing, android.R.attr.numColumns, android.R
            .attr.gravity};
        TypedArray a = context.obtainStyledAttributes(attrs, attr);
        try {
            mHorizontalSpacing = a.getDimensionPixelOffset(0, 0);
            mVerticalSpacing = a.getDimensionPixelOffset(1, 0);
            mNumColumns = a.getInt(2, mNumColumns);
            mGravity = a.getInt(3, mGravity);
        } finally {
            a.recycle();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        drawBorder(canvas);
        if (rowLayouts != null) {
            int w = getPaddingLeft();
            int h = 0;
            
            int size = rowLayouts.size();
            for (int i = 0; i < size; i++) {
                
                canvas.translate(0, h);
                if (i == 0) {
                    // vertical gravity : center
                    canvas.translate(0, getVerticalSpacing() / 2);
                }
                else if (i > 0) {
                    if (showVBorderMiddle()) {
                        int y = 0 + getVerticalSpacing() / 2;
                        canvas.drawLine(0, y, getWidth(), y, paint);
                    }
                    canvas.translate(0, getVerticalSpacing());
                }
                canvas.save();
                StaticLayout[] cs = rowLayouts.get(i);
                int rh = rowHeights.get(i);
                if (cs != null) {
                    canvas.translate(w, 0);
                    for (int j = 0; j < cs.length; j++) {
                        if (j > 0) {
                            int x = 0 + getHorizontalSpacing() / 2;
                            if (showHBorderMiddle()) {
                                canvas.drawLine(x, -getVerticalSpacing() / 2, x, rh + getVerticalSpacing() / 2, paint);
                            }
                            canvas.translate(getHorizontalSpacing(), 0);
                        }
                        if (isVerticalCenter()) {
                            int offy = (rh - cs[j].getHeight()) >> 1;
                            canvas.save();
                            canvas.translate(0, offy);
                            cs[j].draw(canvas);
                            canvas.restore();
                        }
                        else {
                            cs[j].draw(canvas);
                        }
                        canvas.translate(cs[j].getWidth(), 0);
                    }
                }
                h = rh;
                w = getPaddingLeft();
                canvas.restore();
            }
        }
        canvas.restore();
    }
    
    private void drawBorder(Canvas canvas) {
        float r = getMeasuredWidth() - paint.getStrokeWidth();
        float b = getMeasuredHeight() - paint.getStrokeWidth();
        if (showHBorderBegin()) {
            // left line
            canvas.drawLine(0, 0, 0, getMeasuredHeight(), paint);
        }
        if (showHBorderEnd()) {
            // right line
            canvas.drawLine(r, 0, r, getMeasuredHeight(), paint);
        }
        if (showVBorderBegin()) {
            // top line
            canvas.drawLine(0, 0, getMeasuredWidth(), 0, paint);
        }
        if (showVBorderEnd()) {
            // bottom line
            canvas.drawLine(0, b, getMeasuredWidth(), b, paint);
        }
    }
    
    /**
     * Add row data
     *
     * @param text row data
     */
    public void addRow(CharSequence... text) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(text);
        requestLayout();
    }

    public void setRows(List<CharSequence[]> texts) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.addAll(texts);
        requestLayout();
    }
    
    /**
     * Clear all data
     */
    public void clear() {
        if (rows != null) {
            rows.clear();
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int width = parentWidth - getPaddingLeft() + getPaddingRight() - getHorizontalSpacing() * (mNumColumns - 1);

        columnWidths = new int[mNumColumns];
        if (getWeights() == null) {
            for (int i = 0; i < mNumColumns; i++) {
                columnWidths[i] = getMaxColumnWidth(i);
            }
        }
        else {
            int used = 0;
            int sumWeight = 0;
            for (int i = 0; i < mNumColumns && i < getWeights().length; i++) {
                int weight = getWeights()[i];
                if (weight == 0) {
                    columnWidths[i] = getMaxColumnWidth(i);
                    used += columnWidths[i];
                }
                sumWeight += weight;
            }
            if (sumWeight > 0) {
                int temp = width - used;
                if (temp < 0) {
                    temp = 0;
                }
                for (int i = 0; i < mNumColumns && i < getWeights().length; i++) {
                    int weight = getWeights()[i];
                    if (weight > 0) {
                        columnWidths[i] = weight * temp / sumWeight;
                    }
                }
            }
        }
        if (rowLayouts == null) {
            rowLayouts = new ArrayList<>();
        }
        else {
            rowLayouts.clear();
        }
        if (rowHeights == null) {
            rowHeights = new ArrayList<>();
        }
        else {
            rowHeights.clear();
        }
        int height = 0;
        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                CharSequence[] row = rows.get(i);
                if (row != null && row.length > 0) {
                    if (row.length < mNumColumns) {

                    }
                    StaticLayout[] layouts = new StaticLayout[row.length];
                    int rowHeight = 0;
                    for (int j = 0; j < row.length && j < getNumColumns(); j++) {
                        int w = columnWidths[j];
                        if (/* row.length < mNumColumns && */j == row.length - 1) {
                            w = getLastSpanColumnWidth(parentWidth, j);
                        }
                        layouts[j] = new StaticLayout(row[j], textPaint, w, getAlignment(), 1f, 0f, false);
                        rowHeight = Math.max(rowHeight, layouts[j].getHeight());
                    }
                    height += rowHeight;
                    height += getVerticalSpacing();
                    rowLayouts.add(layouts);
                    rowHeights.add(rowHeight);
                }
            }
        }
        
        setMeasuredDimension(parentWidth, height);
    }

    private StaticLayout.Alignment getAlignment() {
        if (((mGravity & Gravity.HORIZONTAL_GRAVITY_MASK) & Gravity.CENTER_HORIZONTAL) != 0) {
            return StaticLayout.Alignment.ALIGN_CENTER;
        }
        return StaticLayout.Alignment.ALIGN_NORMAL;
    }

    private boolean isVerticalCenter() {
        return ((mGravity & Gravity.VERTICAL_GRAVITY_MASK) & Gravity.CENTER_VERTICAL) != 0;
    }
    
    public int[] getWeights() {
        return weights;
    }
    
    private int getMaxColumnWidth(int index) {
        int ret = 0;
        if (rows != null) {
            for (CharSequence[] row : rows) {
                if (row != null && row.length > index) {
                    ret = Math.max(ret, measureTextWidth(row[index]));
                }
            }
        }
        return ret;
    }
    
    private int getLastSpanColumnWidth(int total, int i) {
        int sum = 0;
        for (int k = 0; k < i; k++) {
            sum += columnWidths[k];
        }
        int last = total - getPaddingLeft() - getPaddingRight() - sum - getHorizontalSpacing() * (i);
        last = Math.max(0, last);
        return last;
    }
    
    private int measureTextWidth(CharSequence text) {
        float w = textPaint.measureText(text, 0, text.length());
        int i = (int) w;
        return w > i ? i + 1 : i;
    }
    
    public int getHorizontalSpacing() {
        // return Math.max(mHorizontalSpacing, getDividerWidth());
        return mHorizontalSpacing;
    }
    
    public int getVerticalSpacing() {
        // return Math.max(mVerticalSpacing, getVerticalDividerHeight());
        return mVerticalSpacing;
    }
    
    public int getNumColumns() {
        return mNumColumns;
    }
    
    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
    }

    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    public void setTextColor(int color) {
        this.textPaint.setColor(color);
    }
    
    public void setTextSize(int size) {
        this.textPaint.setTextSize(size);
    }
    
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }
    
    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
    }
    
    public void setWeights(int[] weights) {
        this.weights = weights;
    }
    
    /**
     * Set border color
     *
     * @param color ARGB format color
     */
    public void setBorderColor(int color) {
        this.paint.setColor(color);
    }
    
    /**
     * Set border width
     *
     * @param width border thickness px unit.
     */
    public void setBorderWidth(float width) {
        this.paint.setStrokeWidth(width);
    }
    
    /**
     * Set show horizontal border
     *
     * @param flag combined value of {@link #SHOW_DIVIDER_NONE}, {@link #SHOW_DIVIDER_BEGINNING}, {@link
     *             #SHOW_DIVIDER_MIDDLE},{@link #SHOW_DIVIDER_END}
     *
     * @see android.widget.LinearLayout#setShowDividers(int)
     */
    public void setShowHorizontalBorder(int flag) {
        this.mShowDividers = flag;
    }
    
    public int getShowHorizontalBorder() {
        return this.mShowDividers;
    }

    public int getShowVerticalBorder() {
        return this.mShowVerticalDivider;
    }

    /**
     * Set show vertical border
     *
     * @param flag combined value of {@link #SHOW_DIVIDER_NONE}, {@link #SHOW_DIVIDER_BEGINNING}, {@link
     *             #SHOW_DIVIDER_MIDDLE},{@link #SHOW_DIVIDER_END}
     *
     * @see android.widget.LinearLayout#setShowDividers(int)
     */
    public void setShowVerticalBorder(int flag) {
        this.mShowVerticalDivider = flag;
    }
    
    private boolean showHBorderBegin() {
        return (this.mShowDividers & SHOW_DIVIDER_BEGINNING) == SHOW_DIVIDER_BEGINNING;
    }
    
    private boolean showHBorderMiddle() {
        return (this.mShowDividers & SHOW_DIVIDER_MIDDLE) == SHOW_DIVIDER_MIDDLE;
    }
    
    private boolean showHBorderEnd() {
        return (this.mShowDividers & SHOW_DIVIDER_END) == SHOW_DIVIDER_END;
    }
    
    private boolean showVBorderBegin() {
        return (this.mShowVerticalDivider & SHOW_DIVIDER_BEGINNING) == SHOW_DIVIDER_BEGINNING;
    }
    
    private boolean showVBorderMiddle() {
        return (this.mShowVerticalDivider & SHOW_DIVIDER_MIDDLE) == SHOW_DIVIDER_MIDDLE;
    }
    
    private boolean showVBorderEnd() {
        return (this.mShowVerticalDivider & SHOW_DIVIDER_END) == SHOW_DIVIDER_END;
    }
}
