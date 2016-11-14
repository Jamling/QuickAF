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

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import cn.ieclipse.af.util.BitmapUtil;

/**
 * Description
 *
 * @author Jamling
 */
public class RoundedDrawable extends Drawable {

    protected Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    boolean mIsCircle = false;

    protected final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    protected BitmapShader bitmapShader;
    protected float radius;
    protected Bitmap bitmap;
    protected RectF fillRect;

    public RoundedDrawable(float radius) {
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        this.radius = radius;
    }

    public RoundedDrawable(Drawable drawable) {
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeJoin(Paint.Join.ROUND);
        strokePaint.setStrokeCap(Paint.Cap.ROUND);
        setDrawable(drawable);
    }

    public void setDrawable(Drawable drawable) {
        if (drawable == null) {
            return;
        }
        if (bitmap != null) {
            bitmap.recycle();
        }
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }
        else {
            bitmap = BitmapUtil.getBitmapFromDrawable(drawable);
        }
        if (bitmap == null) {
            return;
        }
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(bitmapShader);
    }

    public void setCircle(boolean circle){
        this.mIsCircle = circle;
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fillRect = new RectF(bounds);
        fillRect.inset(mBorderWidth, mBorderWidth);

        computeBitmapShaderSize();
        computeRadius();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawRoundRect(fillRect, radius, radius, paint);
        if (mIsCircle) {
            canvas.drawCircle(bounds.width() / 2, bounds.height() / 2, radius, strokePaint);
        }
        else {
            canvas.drawRoundRect(new RectF(getBounds()), radius, radius, strokePaint);
        }
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    /**
     * 计算Bitmap shader 大小
     */
    public void computeBitmapShaderSize() {
        Rect bounds = getBounds();
        if (bounds == null) {
            return;
        }
        if (bitmap == null || bitmapShader == null) {
            return;
        }
        // 选择缩放比较多的缩放，这样图片就不会有图片拉伸失衡
        Matrix matrix = new Matrix();
        float scaleX = (fillRect.width()) / (float) bitmap.getWidth();
        float scaleY = (fillRect.height()) / (float) bitmap.getHeight();
        float scale = scaleX > scaleY ? scaleX : scaleY;
        matrix.postScale(scale, scale);
        bitmapShader.setLocalMatrix(matrix);
    }

    /**
     * 计算半径的大小
     */
    public void computeRadius() {
        Rect bounds = getBounds();
        if (bounds == null) {
            return;
        }
//        float w = bounds.width() - mBorderWidth * 2;
//        float h = bounds.height() - mBorderWidth * 2;
        if (mIsCircle) {
            radius = Math.min(fillRect.width(), fillRect.height()) / 2;
        }
        else {

        }
    }

    /**
     * Sets the border
     *
     * @param color color of the border
     * @param width width of the border
     */
    public void setBorder(int color, float width) {
        if (mBorderColor != color || mBorderWidth != width) {
            mBorderColor = color;
            mBorderWidth = width;
            strokePaint.setColor(mBorderColor);
            strokePaint.setStrokeWidth(mBorderWidth);
            invalidateSelf();
        }
    }
}