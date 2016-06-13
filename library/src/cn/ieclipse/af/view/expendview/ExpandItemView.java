/*
 * Copyright 2014-2016 QuickAF
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
package cn.ieclipse.af.view.expendview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * ExpandItemView继承后实现自定义功能
 *
 * @author wangjian
 * @date 2016/4/5 .
 */
public abstract class ExpandItemView extends FrameLayout {

    /**
     * 显示在toggleButton的标题文字
     */
    public String mTitle;

    protected LayoutInflater mLayoutInflater;

    protected ExpandableView mExpandableView;

    protected OnPopupItemClickListener mOnPopupItemClickListener;

    public ExpandItemView(ExpandableView expandableView) {
        this(expandableView, null, null);
    }

    protected abstract int getContentLayout();

    public ExpandItemView(ExpandableView expandableView, String title, OnPopupItemClickListener listener) {
        super(expandableView.getContext(), null);
        this.mExpandableView = expandableView;
        this.mOnPopupItemClickListener = listener;

        mLayoutInflater = LayoutInflater.from(getContext());
        int layoutId = getContentLayout();
        if (layoutId > 0) {
            mLayoutInflater.inflate(getContentLayout(), this, true);
        }
        setTitle(title);
        initContentView(this);
    }

    public void initContentView(View view) {

    }

    public String getTitle() {
        return mTitle == null ? "" : mTitle;
    }

    /**
     * 设置tab title
     *
     * @param mTitle
     */
    protected void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    /**
     * 累加子类的高度作为自身的高度
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int cCount = getChildCount();

        int desireWidth = MeasureSpec.getSize(widthMeasureSpec);
        int desireHeight = 0;
        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desireHeight += child.getMeasuredHeight();
        }
        setMeasuredDimension(desireWidth, desireHeight);
    }

    public abstract void clearChoice();

    /**
     * 自定义回调
     */
    public interface OnPopupItemClickListener {
        void onExpandPopupItemClick(ExpandItemView expandItemView, int position);
    }
}
