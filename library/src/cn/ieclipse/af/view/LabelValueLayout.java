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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 类/接口描述
 *
 * @author Harry
 * @date 2016年1月11日
 */
public class LabelValueLayout extends View {

    /**
     * @param context
     */
    public LabelValueLayout(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public LabelValueLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public LabelValueLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelValueLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    
    private int mNumColumns = 2;
    private TextPaint textPaint;
    private Paint paint;
    private List<CharSequence[]> rows;
    private List<StaticLayout[]> rowLayouts;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private boolean isNeedOuterBorder = true;
    private boolean isNeedInnerHorBorder = true;

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.density = context.getResources().getDisplayMetrics().density;
        // paint.setCompatibilityScaling();
        int[] attr = {android.R.attr.horizontalSpacing, android.R.attr.verticalSpacing, android.R.attr.numColumns};
        TypedArray a = context.obtainStyledAttributes(attrs, attr);
        try {
            mHorizontalSpacing = a.getDimensionPixelOffset(0, 0);
            mVerticalSpacing = a.getDimensionPixelOffset(1, 0);
        } finally {
            a.recycle();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (rowLayouts != null) {
            int w = 0;
            int h = 0;
            int size = rowLayouts.size();
            for (int i = 0; i < size; i++) {
                
                canvas.translate(0, h);
                if (i == 0) {
                    if (isNeedOuterBorder) {
                        canvas.drawLine(0, 0, getWidth(), 0, paint);
                    }
                    canvas.translate(0, getVerticalSpacing() / 2);
                }
                else if (i > 0) {
                    int y = 0 + getVerticalSpacing() / 2;
                    if (isNeedInnerHorBorder) {
                        canvas.drawLine(0, y, getWidth(), y, paint);
                    }
                    canvas.translate(0, getVerticalSpacing());
                }
                canvas.save();
                StaticLayout[] cs = rowLayouts.get(i);
                if (cs != null) {
                    for (int j = 0; j < cs.length; j++) {
                        canvas.translate(w + getHorizontalSpacing(), 0);
                        cs[j].draw(canvas);
                        if (j == 0 && cs.length > 1) {
                            w = getWidth() - cs[j + 1].getWidth() - getHorizontalSpacing() * 3;
                        }
                        h = Math.max(h, cs[j].getHeight());
                    }
                }
                w = 0;
                canvas.restore();
            }
            if (isNeedOuterBorder) {
                canvas.translate(0, h);
                int y = 0 + getVerticalSpacing() / 2;
                canvas.drawLine(0, y, getWidth(), y, paint);
                canvas.translate(0, getVerticalSpacing());
                canvas.save();
            }
        }
        canvas.restore();
    }
    
    public void addRow(CharSequence... text) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(text);
        requestLayout();
    }
    
    public void clear() {
        if (rows != null) {
            rows.clear();
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentWidth = MeasureSpec.getSize(widthMeasureSpec);
        int width = parentWidth - getPaddingLeft() + getPaddingRight() - getHorizontalSpacing() * (mNumColumns - 1);
        if (rowLayouts == null) {
            rowLayouts = new ArrayList<>();
        }
        else {
            rowLayouts.clear();
        }
        int height = 0;
        if (rows != null) {
            for (CharSequence[] row : rows) {
                if (row != null && row.length > 0) {
                    if (row.length < mNumColumns) {

                    }
                    StaticLayout[] layouts = new StaticLayout[row.length];
                    for (int j = 0; j < row.length; j++) {
                        int w = measureTextWidth(row[j]);
                        layouts[j] = new StaticLayout(row[j], textPaint, w, StaticLayout.Alignment.ALIGN_NORMAL, 1f, 0f,
                            false);
                        if (j == 0) {
                            height += layouts[j].getHeight();
                            height += getVerticalSpacing();
                        }
                    }
                    rowLayouts.add(layouts);
                }
            }
        }
        if (height > 0) {
            int sw = (int) paint.getStrokeWidth();
            height = height + (sw > 0 ? sw : 1);
        }
        
        setMeasuredDimension(parentWidth, height);
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
    
    public void setTextColor(int color) {
        this.textPaint.setColor(color);
    }
    
    public void setTextSize(int size) {
        this.textPaint.setTextSize(size);
    }

    /**
     * 相当于左右padding
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.mVerticalSpacing = verticalSpacing;
    }

    /**
     * 设置边框宽度
     *
     * @param width
     */
    public void setStrokeWidth(float width) {
        this.paint.setStrokeWidth(width);
    }

    /**
     * 设置边框颜色
     */
    public void setBorderColor(int color) {
        this.paint.setColor(color);
    }

    /**
     * 是否设置外边框
     */
    public void setOuterBorder(boolean isNeedOuterBorder) {
        this.isNeedOuterBorder = isNeedOuterBorder;
    }

    /**
     * 是否设置水平内边框
     */
    public void setInnerHorBorder(boolean isNeedInnerHorBorder) {
        this.isNeedInnerHorBorder = isNeedInnerHorBorder;
    }
}
