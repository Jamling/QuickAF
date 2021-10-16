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

import cn.ieclipse.af.R;
import cn.ieclipse.af.util.AppUtils;

/**
 * Layout String[][] as table.
 *
 * @author Jamling
 */
public class TableView extends View {

    public TableView(Context context) {
        this(context, null);
    }

    public TableView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TableView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    public static final int SHOW_DIVIDER_NONE = LinearLayout.SHOW_DIVIDER_NONE;

    public static final int SHOW_DIVIDER_BEGINNING = LinearLayout.SHOW_DIVIDER_BEGINNING;

    public static final int SHOW_DIVIDER_END = LinearLayout.SHOW_DIVIDER_END;

    public static final int SHOW_DIVIDER_MIDDLE = LinearLayout.SHOW_DIVIDER_MIDDLE;

    private final int mVerticalDividerHeight = 1;
    private int mShowVerticalDivider = SHOW_DIVIDER_NONE;
    private final int mVerticalDividerPadding = 0;

    private final int mDividerWidth = 1;
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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableView);
        try {
            mHorizontalSpacing = a.getDimensionPixelOffset(R.styleable.TableView_android_horizontalSpacing, 0);
            mVerticalSpacing = a.getDimensionPixelOffset(R.styleable.TableView_android_verticalSpacing, 0);
            mNumColumns = a.getInt(R.styleable.TableView_android_numColumns, mNumColumns);
            mGravity = a.getInt(R.styleable.TableView_android_gravity, mGravity);
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
            int h = getPaddingTop();

            canvas.translate(0, h);
            int size = rowLayouts.size();
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    // vertical gravity : center
                    //canvas.translate(0, getVerticalSpacing() / 2);
                } else if (i > 0) {
                    if (showVBorderMiddle()) {
                        int y = 0 + getVerticalSpacing() / 2;
                        canvas.drawLine(0, y, getWidth(), y, paint);
                    }
                    canvas.translate(0, getVerticalSpacing());
                }
                StaticLayout[] cs = rowLayouts.get(i);
                int rh = rowHeights.get(i);
                if (cs != null) {
                    canvas.save();
                    canvas.translate(w, 0);
                    for (int j = 0; j < cs.length && j < getNumColumns(); j++) {
                        if (j > 0) {
                            int x = 0 + getHorizontalSpacing() / 2;
                            int y1 = 0 - getVerticalSpacing() / 2;
                            int y2 = rh + getVerticalSpacing() / 2;
                            if (i == 0) {
                                y1 -= getPaddingTop();
                            }
                            if (i + 1 == size) {
                                y2 += getPaddingBottom();
                            }
                            if (showHBorderMiddle()) {
                                canvas.drawLine(x, y1, x, y2, paint);
                            }
                            canvas.translate(getHorizontalSpacing(), 0);
                        }
                        if (isVerticalCenter()) {
                            int offy = (rh - cs[j].getHeight()) >> 1;
                            canvas.save();
                            canvas.translate(0, offy);
                            cs[j].draw(canvas);
                            canvas.restore();
                        } else {
                            cs[j].draw(canvas);
                        }
                        canvas.translate(cs[j].getWidth(), 0);
                    } // end column
                }
                canvas.restore();
                canvas.translate(0, rh);
            }// end row
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

    public void setData(List<CharSequence[]> table) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        int cols = 1;
        if (table != null) {
            for (CharSequence[] row : table) {
                rows.add(row);
                if (row != null) {
                    cols = Math.max(row.length, cols);
                }
            }
        }
        if (this.headers == null && mNumColumns == 0) {
            //setNumColumns(cols);
        }
        requestLayout();
    }

    public void setData(CharSequence[][] table) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        int cols = 1;
        if (table != null) {
            for (CharSequence[] row : table) {
                rows.add(row);
                if (row != null) {
                    cols = Math.max(row.length, cols);
                }
            }
        }
        if (this.headers == null && mNumColumns == 0) {
            //setNumColumns(cols);
        }
        requestLayout();
    }

    private void initColumnWidths() {
        if (columnWidths == null || columnWidths.length != mNumColumns) {
            columnWidths = new int[mNumColumns];
        }
    }

    private CharSequence[] headers;

    public void setHeader(CharSequence... texts) {
        if (texts != null) {
            setNumColumns(texts.length);
            initColumnWidths();
        }
        this.headers = texts;
    }

    /**
     * Clear all data
     */
    public void clear() {
        if (rows != null) {
            rows.clear();
        }
    }

    int defaultColumnWidth;

    public void setDefaultColumnWidth(int px) {
        this.defaultColumnWidth = px;
    }

    public void setDefaultColumnWidth(int px, boolean dip) {
        this.defaultColumnWidth = dip ? AppUtils.dp2px(getContext(), px) : px;
    }

    public void setColumnWidth(int index, int px) {
        if (columnWidths != null) {
            if (index > 0 && index < columnWidths.length) {
                columnWidths[index] = px;
            }
        }
    }

    public void setColumnWidth(int index, int px, boolean dip) {
        int p = dip ? AppUtils.dp2px(getContext(), px) : px;
        setColumnWidth(index, p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int width = parentWidth - (getPaddingLeft() + getPaddingRight()) - getHorizontalSpacing() * (mNumColumns - 1);
        initColumnWidths();
        boolean wc = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED;
        if (getWeights() == null) {
            for (int i = 0; i < mNumColumns; i++) {
                if (wc && defaultColumnWidth > 0) {
                    columnWidths[i] = defaultColumnWidth;
                } else {
                    columnWidths[i] = getMaxColumnWidth(i);
                }
            }
        } else {
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
        } else {
            rowLayouts.clear();
        }
        if (rowHeights == null) {
            rowHeights = new ArrayList<>();
        } else {
            rowHeights.clear();
        }
        int height = getPaddingTop() + getPaddingBottom();
        List<CharSequence[]> rows = new ArrayList<>();
        if (headers != null) {
            rows.add(0, headers);
        }
        if (this.rows != null) {
            rows.addAll(this.rows);
        }

        if (rows != null) {
            for (int i = 0; i < rows.size(); i++) {
                CharSequence[] row = rows.get(i);
                if (row != null && row.length > 0) {
                    if (row.length < mNumColumns) {
                        //
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
                    if (i > 0) {
                        height += getVerticalSpacing();
                    }
                    rowLayouts.add(layouts);
                    rowHeights.add(rowHeight);
                }
            }
        }
        if (wc) {
            parentWidth = 0;
            for (int i = 0; i < columnWidths.length; i++) {
                parentWidth += columnWidths[i];
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

    public void setColumnWidths(int[] columnWidths) {
        this.columnWidths = columnWidths;
    }

    public int[] getColumnWidths() {
        return columnWidths;
    }

    public TextPaint getTextPaint() {
        return textPaint;
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
     * #SHOW_DIVIDER_MIDDLE},{@link #SHOW_DIVIDER_END}
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
     * #SHOW_DIVIDER_MIDDLE},{@link #SHOW_DIVIDER_END}
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
