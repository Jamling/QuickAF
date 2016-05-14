/*
 * Copyright (c) 2015-present, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */

package cn.ieclipse.af.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.View;

import java.util.Arrays;

import cn.ieclipse.af.util.ViewUtils;

public class RoundedColorDrawable extends Drawable {
    final float[] mRadii = new float[8];
    final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    boolean mIsCircle = false;
    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    final Path mPath = new Path();
    private int mColor = Color.TRANSPARENT;
    private final RectF mTempRect = new RectF();
    
    /**
     * Creates a RoundedColorDrawable.
     *
     * @param color
     *            of the drawable
     */
    public RoundedColorDrawable(int color) {
        setColor(color);
    }
    
    /**
     * Creates a new instance of RoundedColorDrawable from the given
     * ColorDrawable.
     * 
     * @param colorDrawable
     *            color drawable to extract the color from
     * @return a new RoundedColorDrawable
     */
    public static RoundedColorDrawable fromColorDrawable(
            ColorDrawable colorDrawable) {
        return new RoundedColorDrawable(colorDrawable.getColor());
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
    public RoundedColorDrawable(float[] radii, int color) {
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
    public RoundedColorDrawable(float radius, int color) {
        this(color);
        setRadius(radius);
    }
    
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        updatePath();
    }
    
    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(mPath, mPaint);
        if (mBorderWidth != 0) {
            mPaint.setColor(mBorderColor);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mBorderWidth);
            canvas.drawPath(mPath, mPaint);
        }
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
     * Sets the color.
     * 
     * @param color ARGB color
     */
    public void setColor(int color) {
        if (mColor != color) {
            mColor = color;
            invalidateSelf();
        }
    }
    
    /**
     * Gets the color.
     * 
     * @return color
     */
    public int getColor() {
        return mColor;
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
    
    /**
     * Setting a color filter on a ColorDrawable has no effect. This has been
     * inspired by Android ColorDrawable.
     *
     * @param colorFilter
     *            Ignore.
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
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
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;        
    }

    private StateListDrawable sld;
    public RoundedColorDrawable setStateColor(int[][] stateSets, int[] colors){
        if (stateSets != null && colors != null) {
            int len = Math.min(stateSets.length, colors.length);
            for (int i = 0; i < len; i++) {
                RoundedColorDrawable self = new RoundedColorDrawable(mRadii, colors[i]);
                self.setBorder(mBorderColor, mBorderWidth);
                sld.addState(stateSets[i], self);
            }
        }
        return this;
    }

    public RoundedColorDrawable addStateColor(int[] stateSet, int color, int borderColor){
        if (sld == null) {
            sld = new StateListDrawable();
        }
        RoundedColorDrawable self = new RoundedColorDrawable(mRadii, color);
        self.setBorder(borderColor > 0 ? borderColor : mBorderColor, mBorderWidth);
        sld.addState(stateSet, self);
        return this;
    }

    public RoundedColorDrawable addStateColor(int[] stateSet, int color){
        return addStateColor(stateSet, color, 0);
    }

    public RoundedColorDrawable addStateColor(int state, int color){
        return addStateColor(new int[]{state}, color);
    }

    public void applyTo(View view) {
        if (sld != null) {
            sld.addState(new int[]{}, this);
        }
        ViewUtils.setBackground(view, sld == null ? this : this.sld);
    }

}
