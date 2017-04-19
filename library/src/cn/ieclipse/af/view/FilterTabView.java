/*
 * Copyright (C) 2015-2017 QuickAF
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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class FilterTabView extends FrameLayout {
    public FilterTabView(Context context) {
        super(context);
    }
    
    public CharSequence mTitle;

    protected LayoutInflater mLayoutInflater;

    protected FilterTabHost mFilterTabHost;

    protected OnPopupItemClickListener mOnPopupItemClickListener;

    public FilterTabView(FilterTabHost filterTabHost) {
        this(filterTabHost, null);
    }

    public FilterTabView(FilterTabHost filterTabHost, CharSequence title) {
        this(filterTabHost, title, null);
    }

    public FilterTabView(FilterTabHost filterTabHost, CharSequence title, OnPopupItemClickListener listener) {
        super(filterTabHost.getContext(), null);
        this.mFilterTabHost = filterTabHost;
        this.mOnPopupItemClickListener = listener;

        mLayoutInflater = LayoutInflater.from(getContext());
        int layoutId = getContentLayout();
        if (layoutId > 0) {
            mLayoutInflater.inflate(getContentLayout(), this, true);
        }
        setTitle(title);
        initContentView(this);
    }

    protected abstract int getContentLayout();

    public void initContentView(View view) {

    }

    public CharSequence getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    /**
     * Set title
     *
     * @param title title text
     */
    protected void setTitle(CharSequence title) {
        this.mTitle = title;
    }

//    /**
//     * 累加子类的高度作为自身的高度
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int cCount = getChildCount();
//
//        int desireWidth = MeasureSpec.getSize(widthMeasureSpec);
//        int desireHeight = 0;
//        for (int i = 0; i < cCount; i++) {
//            View child = getChildAt(i);
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            desireHeight += child.getMeasuredHeight();
//        }
//        setMeasuredDimension(desireWidth, desireHeight);
//    }

    public void hideExpandableView() {
        mFilterTabHost.hidePopup();
    }

    public void clearChoice() {
        mFilterTabHost.clearChoice(this);
    }

    /**
     * 自定义回调
     */
    public interface OnPopupItemClickListener {
        void onPopupItemClick(FilterTabView parent, View view, int position);
    }
}
