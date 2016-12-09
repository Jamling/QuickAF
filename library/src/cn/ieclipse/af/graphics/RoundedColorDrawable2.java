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
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;

import java.util.Arrays;

/**
 * Description
 *
 * @deprecated the class works not well, please use {@link cn.ieclipse.af.graphics.RoundedColorDrawable}
 * @author Jamling
 */
public class RoundedColorDrawable2 extends ColorDrawable {

    final float[] mRadii = new float[8];
    final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    boolean mIsCircle = false;
    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    final Path mPath = new Path();
    private int mColor = Color.TRANSPARENT;
    private final RectF mTempRect = new RectF();

    public RoundedColorDrawable2(int color) {
        super(color);
    }
    /**
     * Creates a new instance of RoundedColorDrawable.
     *
     * @param radii
     *            Each corner receive two radius values [X, Y]. The corners are
     *            ordered top-left, top-right, bottom-right, bottom-left.
     * @param color
     *            of the drawable
     */
    public RoundedColorDrawable2(float[] radii, int color) {
        this(color);
        setRadii(radii);
    }

    /**
     * Creates a new instance of RoundedColorDrawable.
     *
     * @param radius
     *            of the corners in pixels
     * @param color
     *            of the drawable
     */
    public RoundedColorDrawable2(float radius, int color) {
        this(color);
        setRadius(radius);
    }

    /**
     * Sets whether to round as circle.
     *
     * @param isCircle
     *            whether or not to round as circle
     */
    public void setCircle(boolean isCircle) {
        mIsCircle = isCircle;
        updatePath();
        invalidateSelf();
    }

    /**
     * Sets the rounding radii.
     *
     * @param radii
     *            Each corner receive two radius values [X, Y]. The corners are
     *            ordered top-left, top-right, bottom-right, bottom-left
     */
    public void setRadii(float[] radii) {
        if (radii == null) {
            Arrays.fill(mRadii, 0);
        }
        else {
            assert(radii.length == 8);
            System.arraycopy(radii, 0, mRadii, 0, 8);
        }
        updatePath();
        invalidateSelf();
    }

    /**
     * Sets the rounding radius.
     *
     * @param radius
     */
    public void setRadius(float radius) {
        assert(radius >= 0);
        Arrays.fill(mRadii, radius);
        updatePath();
        invalidateSelf();
    }

    /**
     * Sets the border
     *
     * @param color
     *            of the border
     * @param width
     *            of the border
     */
    public void setBorder(int color, float width) {
        if (mBorderColor != color) {
            mBorderColor = color;
            invalidateSelf();
        }

        if (mBorderWidth != width) {
            mBorderWidth = width;
            updatePath();
            invalidateSelf();
        }
    }

    private void updatePath() {
        mPath.reset();
        mTempRect.set(getBounds());
        mTempRect.inset(mBorderWidth / 2, mBorderWidth / 2);
        if (mIsCircle) {
            float radius = Math.min(mTempRect.width(), mTempRect.height()) / 2;
            mPath.addCircle(mTempRect.centerX(), mTempRect.centerY(), radius,
                Path.Direction.CW);
        }
        else {
            mPath.addRoundRect(mTempRect, mRadii, Path.Direction.CW);
        }
        mTempRect.inset(-mBorderWidth / 2, -mBorderWidth / 2);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updatePath();
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.clipPath(mPath);
        // super.draw(canvas);
        mPaint.setColor(getColor());
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
        if (mBorderWidth != 0) {
            mPaint.setColor(mBorderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            canvas.drawPath(mPath, mPaint);
        }
    }
}
