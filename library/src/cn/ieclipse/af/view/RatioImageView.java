/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import cn.ieclipse.af.R;

/**
 * RatioImageView can display image as a grid base on fix ratio. If the ratio is
 * 1 (default), so the ImageView looks like a square ImageView.
 * 
 * @author Jamling
 * 
 */
public class RatioImageView extends ImageView {
    
    private float mRatio = 1.0f;
    
    /**
     * @param context
     */
    public RatioImageView(Context context) {
        this(context, null);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RatioImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mRatio = a.getFloat(R.styleable.RatioImageView_af_ratio, 0);
            a.recycle();
        }
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
    }

    public float getRatio() {
        return mRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        if (mRatio > 0) {
            setMeasuredDimension(width, (int) (width / mRatio));
        }
    }
}
