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
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ViewPagerV4 extends ViewPager {
    
    private boolean disableWipe = false;
    
    public ViewPagerV4(Context context) {
        super(context);
    }
    
    public ViewPagerV4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ViewPagerV4);
            disableWipe = a.getBoolean(R.styleable.CViewPagerV4_disableWipe,
                    false);
            
            a.recycle();
        }
        
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return disableWipe ? false : super.onTouchEvent(e);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return disableWipe ? false : super.onInterceptTouchEvent(e);
    }
    
    protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
        if (v instanceof ScrollLayout) {
            return true;
        }
        return super.canScroll(v, checkV, dx, x, y);
    }
    
    public void setDisableWipe(boolean disableWipe) {
        this.disableWipe = disableWipe;
    }
}
