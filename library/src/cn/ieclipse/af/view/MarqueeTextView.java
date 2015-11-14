/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Jamling
 * 
 */
public class MarqueeTextView extends TextView {
    
    /**
     * @param context
     */
    public MarqueeTextView(Context context) {
        super(context);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    
    @Override
    public boolean isInEditMode() {
        return true;
    }
    
    @Override
    protected void onFocusChanged(boolean focused, int direction,
            Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
    
    @Override
    public boolean isFocused() {
        return true;
    }
}
