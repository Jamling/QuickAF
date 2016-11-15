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
 *
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

/**
 * listening ScrollView vertical scroll instances
 *
 * @author wangjian
 * @date 2016-08-13
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
        if (Build.VERSION.SDK_INT < 23) {
            if (mOnScrollChangeListener != null) {
                mOnScrollChangeListener.onScrollChange(this, l, t, oldl, oldt);
            }
        }
    }

    public boolean isScroll2Bottom() {
        View view = getChildAt(0);
        if (view.getBottom() - (getScrollY() + getHeight()) == 0) {
            return true;
        }
        return false;
    }

    public boolean isScroll2Top() {
        return getScrollY() == 0;
    }

    private OnScrollChangeListener mOnScrollChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.mOnScrollChangeListener = onScrollChangeListener;
    }

    /**
     * Interface definition for a callback to be invoked when the scroll
     * X or Y positions of a view change.
     * <b>Note:</b> Some views handle scrolling independently from View and may
     * have their own separate listeners for scroll-type events. For example,
     * {@link android.widget.ListView ListView} allows clients to register an
     * {@link android.widget.ListView#setOnScrollListener(android.widget.AbsListView.OnScrollListener) AbsListView.OnScrollListener}
     * to listen for changes in list scroll position.
     *
     * @see #setOnScrollChangeListener(View.OnScrollChangeListener)
     */
    public interface OnScrollChangeListener {
        /**
         * Called when the scroll position of a view changes.
         *
         * @param v The view whose scroll position has changed.
         * @param scrollX Current horizontal scroll origin.
         * @param scrollY Current vertical scroll origin.
         * @param oldScrollX Previous horizontal scroll origin.
         * @param oldScrollY Previous vertical scroll origin.
         */
        void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY);
    }
}
