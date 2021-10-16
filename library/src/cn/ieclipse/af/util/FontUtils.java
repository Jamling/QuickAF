/*
 * (C) Copyright 2011-2013 li.jamling@gmail.com.
 *
 * This software is the property of li.jamling@gmail.com.
 * You have to accept the terms in the license file before use.
 *
 */
package cn.ieclipse.af.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * This class is manage the custom font.
 *
 * @author Jamling
 * @version 1.0
 * @date 2013-08-22
 */
public class FontUtils {

    private FontUtils() {

    }

    /**
     * Change the root view font used assigned font.
     *
     * @param tf font ({@link android.graphics.Typeface})
     * @param vg ViewGroup
     * @see #changeFont(android.graphics.Typeface, android.view.View)
     */
    private static void changeFont(Typeface tf, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            changeFont(tf, v);
        }
    }

    /**
     * Change the root view font used assigned font.
     * <p>
     * All descendant {@link android.widget.TextView} will changed the font
     * </p>
     *
     * @param tf font ({@link android.graphics.Typeface})
     * @param v the root view
     */
    public static void changeFont(Typeface tf, View v) {
        if (v instanceof ViewGroup) {
            changeFont(tf, (ViewGroup) v);
        } else if (v instanceof TextView) {
            ((TextView) v).setTypeface(tf);
        }
    }

    /**
     * Get custom font type face from assets.
     *
     * @param context current task context.
     * @param path font type face path, relative to /res/assets
     * @return created font.
     */
    public static Typeface getFont(Context context, String path) {
        return Typeface.createFromAsset(context.getAssets(), path);
    }

    /**
     * Change view text size.
     *
     * @param size text size, unit px.
     * @param vg root view
     */
    private static void changeSize(float size, ViewGroup vg) {
        for (int i = 0; i < vg.getChildCount(); i++) {
            View v = vg.getChildAt(i);
            changeSize(size, v);
        }
    }

    /**
     * Change view text size.
     * <p>
     * All descendant {@link android.widget.TextView} will changed the font
     * </p>
     *
     * @param size text size, unit px.
     * @param v root view
     */
    public static void changeSize(float size, View v) {
        if (v instanceof ViewGroup) {
            changeSize(size, (ViewGroup) v);
        } else if (v instanceof TextView) {
            ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
        }
    }
}
