/**
 * Copyright (C) 2015 FengYunDuoBao All rights reserved
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * listening ScrollView vertical scroll instances
 *
 * @author wangjian
 * @date 2015年8月13日
 */
public class VScrollView extends ScrollView {

    public VScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VScrollView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onVScrollChanged(l, t, oldl, oldt);
        }
    }

    private OnScrollChangeListener mOnScrollChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.mOnScrollChangeListener = onScrollChangeListener;
    }

    public interface OnScrollChangeListener {
        /**
         * This is called in response to an internal scroll in this view (i.e., the
         * view scrolled its own contents). This is typically as a result of
         * {@link #scrollBy(int, int)} or {@link #scrollTo(int, int)} having been
         * called.
         *
         * @param l    Current horizontal scroll origin.
         * @param t    Current vertical scroll origin.
         * @param oldl Previous horizontal scroll origin.
         * @param oldt Previous vertical scroll origin.
         */
        void onVScrollChanged(int l, int t, int oldl, int oldt);
    }
}
