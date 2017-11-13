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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import cn.ieclipse.af.R;
import cn.ieclipse.af.graphics.RoundedDrawable;

/**
 * Description
 *
 * @author Jamling
 */
public class RoundImageView extends RatioImageView {
    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            mBorderColor = a.getColor(R.styleable.RoundImageView_af_borderColor, Color.TRANSPARENT);
            mBorderWidth = a.getDimensionPixelOffset(R.styleable.RoundImageView_af_borderWidth, (int) mBorderWidth);
            mIsCircle = a.getBoolean(R.styleable.RoundImageView_af_circle, mIsCircle);
            mRadius = a.getDimensionPixelOffset(R.styleable.RoundImageView_android_radius, mRadius);
            a.recycle();
        }
        mInit = true;
        if (getDrawable() != null) {
            setImageDrawable(getDrawable());
        }
    }

    boolean mIsCircle = false;
    float mBorderWidth = 0;
    int mBorderColor = Color.TRANSPARENT;
    int mRadius;
    boolean mInit = false;

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (mInit) {
            updateRoundDrawable();
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        updateRoundDrawable();
    }

    @Override
    public void setImageURI(@Nullable Uri uri) {
        super.setImageURI(uri);
        updateRoundDrawable();
    }

    protected void updateRoundDrawable() {
        Drawable src = getDrawable();
        if (src != null && !(src instanceof RoundedDrawable)) {
            RoundedDrawable target = new RoundedDrawable(mRadius);
            target.setCircle(mIsCircle);
            target.setBorder(mBorderColor, mBorderWidth);
            target.setDrawable(src);
            setImageDrawable(target);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        if (!(getDrawable() instanceof RoundedDrawable)) {
//            RoundedDrawable target = new RoundedDrawable(mRadius);
//            target.setCircle(mIsCircle);
//            target.setBorder(mBorderColor, mBorderWidth);
//            target.setDrawable(getDrawable());
//            super.setImageDrawable(target);
//            return;
//        }
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
