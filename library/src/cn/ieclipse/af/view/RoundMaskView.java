/*
 * Copyright 2014-2017 QuickAF.
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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import cn.ieclipse.af.R;

/**
 * @author Jamling
 * @deprecated use {@link cn.ieclipse.af.view.RoundFrameLayout} to implement round effect instead
 */
public class RoundMaskView extends View {
    public RoundMaskView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundMaskView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RoundMaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RoundMaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private float mBorderWidth = 0;
    private int mBorderColor = Color.WHITE;
    private int mRadius;

    private Bitmap src, dst;
    private Paint paint = new Paint();
    private PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundMaskView);
            mBorderColor = a.getColor(R.styleable.RoundMaskView_af_borderColor, mBorderColor);
            mBorderWidth = a.getDimensionPixelOffset(R.styleable.RoundMaskView_af_borderWidth, (int) mBorderWidth);
            mRadius = a.getDimensionPixelOffset(R.styleable.RoundMaskView_android_radius, mRadius);
            a.recycle();
        }
    }

    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h, RectF rectF, int r) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(-1);
        c.drawRoundRect(rectF, r, r, p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h, int color) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(color);
        c.drawRect(0, 0, w, h, p);
        return bm;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        try {
            src = makeSrc(w, h, mBorderColor);
            dst = makeDst(w, h, new RectF(mBorderWidth, mBorderWidth, w - mBorderWidth, h - mBorderWidth), mRadius);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the border
     *
     * @param color of the border
     * @param width of the border
     */
    public void setBorder(int color, float width) {
        this.mBorderWidth = width;
        this.mBorderColor = color;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (src != null && dst != null) {
            int w = getMeasuredWidth(), h = getMeasuredHeight();
            int sc = canvas.saveLayer(0, 0, w, h, null,
                Canvas.ALL_SAVE_FLAG);
            canvas.drawBitmap(dst, 0, 0, paint);
            paint.setXfermode(mode);
            canvas.drawBitmap(src, 0, 0, paint);
            paint.setXfermode(null);
            canvas.restoreToCount(sc);
        }
    }
}
