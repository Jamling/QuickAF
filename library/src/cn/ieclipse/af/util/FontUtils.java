/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com. 
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.util;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This class is manage the custom font.
 * 
 * @author Jamling
 * @version 1.0
 * @created 2013-08-22
 * 
 */
public class FontUtils {
    
    private FontUtils() {
        
    }
    
    /**
     * Change the root view font used assigned font.
     * <ul>
     * support view include
     * <li>TextView</li>
     * <li>Button</li>
     * <li>EditText</li>
     * </ul>
     * 
     * @param tf
     * @param vg
     */
    public static void changeFont(Typeface tf, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            changeFont(tf, v);
        }
    }
    
    public static void changeFont(Typeface tf, View v) {
        if (v instanceof ViewGroup) {
            changeFont(tf, (ViewGroup) v);
        }
        else if (v instanceof TextView) {
            ((TextView) v).setTypeface(tf);
        }
        else if (v instanceof Button) {
            ((Button) v).setTypeface(tf);
        }
        else if (v instanceof EditText) {
            ((EditText) v).setTypeface(tf);
        }
    }
    
    /**
     * Get custom font type face from assets.
     * 
     * 
     * @param activity
     *            current task activity context.
     * @param path
     *            font type face path, relative to /res/assets
     * @return created font.
     */
    public static Typeface getFont(Activity activity, String path) {
        return Typeface.createFromAsset(activity.getAssets(), path);
    }
    
    /**
     * Get custom font type face from assets.
     * 
     * 
     * @param context
     *            current task context.
     * @param path
     *            font type face path, relative to /res/assets
     * @return created font.
     */
    public static Typeface getFont(Application context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }
    
    /**
     * Change view text size.
     * 
     * @param size
     *            text size unit px.
     * @param vg
     *            root view
     */
    public static void changeSize(float size, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            changeSize(size, v);
        }
    }
    
    public static void changeSize(float size, View v) {
        if (v instanceof ViewGroup) {
            changeSize(size, (ViewGroup) v);
        }
        else if (v instanceof TextView) {
            ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        else if (v instanceof Button) {
            ((Button) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        else if (v instanceof EditText) {
            ((EditText) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
        else if (v instanceof ViewGroup) {
            changeSize(size, (ViewGroup) v);
        }
    }
}
