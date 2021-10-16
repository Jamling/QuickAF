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
package cn.ieclipse.af.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Used for rounded background color span for text
 *
 * @since 3.0.1
 */
public class RoundedColorSpan extends ReplacementSpan {
    private final float[] mRadii = new float[8];
    private int mColor = Color.TRANSPARENT;
    private int mFgColor;
    private boolean mFgColorSet = false;
    private int mWidth;
    private int padding;

    /**
     * Creates a RoundedColorSpan.
     */
    public RoundedColorSpan(int bgColor, int fgColor, int radius) {
        this.mColor = bgColor;
        this.mFgColor = fgColor;
        this.mRadii[0] = radius;
        this.mFgColorSet = true;
    }

    public RoundedColorSpan(int bgColor, int radius) {
        this.mColor = bgColor;
        this.mRadii[0] = radius;
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, @IntRange(from = 0) int start,
        @IntRange(from = 0) int end, @Nullable Paint.FontMetricsInt fontMetricsInt) {
        mWidth = ((int) (paint.measureText(text, start, end) + 0.5 + 2 * padding));
        return mWidth;
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, @IntRange(from = 0) int start,
        @IntRange(from = 0) int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
        int old = paint.getColor();
        paint.setColor(this.mColor);
        paint.setAntiAlias(true);// 设置画笔的锯齿效果
        RectF oval = new RectF(x, y + paint.ascent(), x + mWidth, y + paint.descent());
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        canvas.drawRoundRect(oval, mRadii[0], mRadii[0], paint);//绘制圆角矩形，第二个参数是x半径，第三个参数是y半径
        if (mFgColorSet) {
            paint.setColor(this.mFgColor);
        } else {
            paint.setColor(old);//恢复画笔的文字颜色
        }
        canvas.drawText(text, start, end, x + padding, y, paint);//绘制文字
    }
}
