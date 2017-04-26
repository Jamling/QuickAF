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
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.ieclipse.af.R;

/**
 * Base the center point to rotate the text.
 * <pre>
 * String text = getText().toString();
 * if (mPath == null) {
 *     mPath = new Path();
 *     mPath.moveTo(0f, getHeight());
 *     mPath.lineTo(getWidth(), 0);
 * }
 * Resources res = getResources();
 * xOffset = (float) res.getInteger(R.integer.price_note_x);
 * yOffset = (float) res.getInteger(R.integer.price_note_y);
 * canvas.drawTextOnPath(text, mPath, xOffset, yOffset, getPaint());
 * </pre>
 * @deprecated not works well
 * @author Jamling
 */
public class RotateTextView extends TextView {
    
    private float degree;
    
    /**
     * @param context
     */
    public RotateTextView(Context context) {
        this(context, null);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public RotateTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public RotateTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RotateTextView);
            degree = a.getFloat(R.styleable.RotateTextView_af_degree, 0);
            a.recycle();
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(degree, getMeasuredWidth() >> 1, getMeasuredHeight() >> 1);
        double arc = degree * Math.PI / 180;
        double x = -(getMeasuredWidth() >> 1) * (1 - Math.abs(Math.cos(arc)));
        double y = -(getMeasuredWidth() >> 1) * Math.sin(arc);
        canvas.translate((float) x, (float) y);
        super.onDraw(canvas);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (degree != 0) {
            int w = getMeasuredWidth();
            int h = getMeasuredHeight();
            double arc = degree * Math.PI / 180;
            double hh = Math.abs(Math.sin(arc)) * w + Math.abs(Math.cos(arc)) * h;
            double ww = Math.abs(Math.sin(arc)) * h + Math.abs(Math.cos(arc)) * w;
            setMeasuredDimension((int) (ww), (int) (hh));
        }
    }
}
