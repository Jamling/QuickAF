/*
 * Copyright 2014-2015 ieclipse.cn.
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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.ieclipse.af.R;
import cn.ieclipse.af.util.AppUtils;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月17日
 *       
 */
public class TitleBar extends LinearLayout {
    
    /**
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    private LinearLayout mLeftContainer;
    private LinearLayout mMiddleContainer;
    private LinearLayout mRightContainer;
    
    private TextView mTitleTv;
    private TextView mLeftTv;
    
    private int bottomDrawableHeight = 1;
    private Drawable bottomDrawable;
    private int minHeight;
    
    private void init(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        if (getId() == View.NO_ID) {
            setId(R.id.titleBar);
        }
        
        if (attrs != null) {
            int[] and = { android.R.attr.minHeight };
            TypedArray a = context.obtainStyledAttributes(attrs, and);
            minHeight = a.getDimensionPixelOffset(0, 0);
            a.recycle();
        }
        
        if (minHeight <= 0) {
            minHeight = AppUtils.dp2px(context, 48);
        }
        
        if (config == null) {
            config = new Config();
            config.padding = 8;
            config.rightItemPadding = 16;
            config.leftWeight = 1;
            config.middleWeight = 2;
            config.rightWeight = 1;
            config.minHeight = minHeight;
        }
        
        mLeftContainer = new LinearLayout(context);
        mLeftContainer.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        
        mMiddleContainer = new LinearLayout(context);
        mMiddleContainer.setGravity(Gravity.CENTER);
        
        mRightContainer = new LinearLayout(context);
        mRightContainer.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        
        mLeftContainer.setBackgroundColor(0xffcecece);
        mMiddleContainer.setBackgroundColor(0xffff9966);
        mRightContainer.setBackgroundColor(0xffcecece);
        
        addView(mLeftContainer, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        addView(mMiddleContainer, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        addView(mRightContainer, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
                
    }
    
    @Override
    public void setGravity(int gravity) {
        if (mGravity != gravity) {
            mGravity = gravity;
            if (mMiddleContainer != null) {
                mMiddleContainer
                        .setGravity((gravity & Gravity.HORIZONTAL_GRAVITY_MASK)
                                | Gravity.CENTER_VERTICAL);
            }
            requestLayout();
        }
    }
    
    public void addLeft(View view) {
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (mLeftContainer.getChildCount() > 0) {
            p.leftMargin = config.leftItemPadding;
        }
        mLeftContainer.addView(view, p);
        requestLayout();
    }
    
    public void setLeft(View view) {
        mLeftContainer.removeAllViews();
        addLeft(view);
    }
    
    public void addMiddle(View view) {
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (mMiddleContainer.getChildCount() > 0) {
            p.leftMargin = config.middleItemPadding;
        }
        mMiddleContainer.addView(view, p);
        requestLayout();
    }
    
    public void setMiddle(View view) {
        mMiddleContainer.removeAllViews();
        addMiddle(view);
    }
    
    public void addRight(View view) {
        LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        if (mRightContainer.getChildCount() > 0) {
            p.leftMargin = config.rightItemPadding;
        }
        mRightContainer.addView(view, p);
        requestLayout();
    }
    
    public View addRightView(int id){
        View v = View.inflate(getContext(), id, null);
        addRight(v);
        return v;
    }
    
    public void setRight(View view) {
        mRightContainer.removeAllViews();
        addRight(view);
    }
    
    public View getLeftView() {
        if (mLeftContainer.getChildCount() > 0) {
            return mLeftContainer.getChildAt(0);
        }
        return null;
    }
    
    public void setBottomDrawable(Drawable drawable) {
        if (this.bottomDrawable == drawable) {
            return;
        }
        if (drawable != null) {
            if (!(drawable instanceof ColorDrawable)) {
                this.bottomDrawableHeight = drawable.getIntrinsicHeight();
            }
        }
        this.bottomDrawable = drawable;
        setWillNotDraw(getBackground() == null || this.bottomDrawable == null);
        this.bottomDrawable.invalidateSelf();
    }
    
    public void setBottomDrawable(int color) {
        ColorDrawable drawable = new ColorDrawable(color);
        setBottomDrawable(drawable);
    }
    
    private static class Config {
        private int minHeight;
        private float leftWeight;
        private float middleWeight;
        private float rightWeight;
        
        private int leftItemPadding;
        private int middleItemPadding;
        private int rightItemPadding;
        private int padding;
        
        float getSumWeight() {
            return leftWeight + middleWeight + rightWeight;
        }
    }
    
    private Config config;
    private int mGravity = 0;
    
    public int getGravity() {
        return mGravity;
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                - getPaddingRight();
        int height = 0 - getPaddingTop() - getPaddingBottom();
        if (config.minHeight > 0) {
            height = config.minHeight - getPaddingTop() - getPaddingBottom();
        }
        measureLeft(width, height);
        measureRight(width, height);
        measureMiddle(width, height);
        height = Math.max(height, leftHeight);
        height = Math.max(height, middleHeight);
        height = Math.max(height, rightHeight);
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(),
                height + getPaddingTop() + getPaddingRight());
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // layoutLeft(l, t, l + leftWidth, b);
        mLeftContainer.layout(l, t, l + leftWidth, b);
        int x = leftWidth + config.padding;
        mMiddleContainer.layout(x, t, x + middleWidth, b);
        mRightContainer.layout(r - rightWidth, t, r, b);
        // layoutMiddle(l + leftWidth + config.padding, t,
        // l + leftWidth + config.padding + middleWidth, b);
    }
    
    private int leftWidth;
    private int leftHeight;
    
    private void measureLeft(int width, int height) {
        leftWidth = 0;
        leftHeight = 0;
        int maxWidth = (int) (width * config.leftWeight
                / config.getSumWeight());
                
        mLeftContainer.measure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
        leftHeight = Math.max(mLeftContainer.getMeasuredHeight(), leftHeight);
        leftWidth = Math.min(mLeftContainer.getMeasuredWidth(), maxWidth);
    }
    
    private int rightWidth;
    private int rightHeight;
    
    private void measureRight(int width, int height) {
        rightWidth = 0;
        rightHeight = 0;
        int maxWidth = (int) (width * config.rightWeight
                / config.getSumWeight());
        mRightContainer.measure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
        rightHeight = Math.max(rightHeight,
                mRightContainer.getMeasuredHeight());
        rightWidth = Math.min(mRightContainer.getMeasuredWidth(), maxWidth);
        
    }
    
    private int middleWidth;
    private int middleHeight;
    
    private void measureMiddle(int width, int height) {
        middleWidth = 0;
        middleHeight = 0;
        int maxWidth = (int) (width * config.middleWeight
                / config.getSumWeight());
        int gh = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
        if (gh == Gravity.CENTER_HORIZONTAL) {
            leftWidth = Math.max(leftWidth, rightWidth);
            rightWidth = leftWidth;
        }
        maxWidth = width - config.padding * 2 - leftWidth - rightWidth;
        
        mMiddleContainer.measure(
                MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
                
        middleWidth = maxWidth;
    }
    
    private int getChildWidthMeasureSpec(View child, int width) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        // default wrap_content
        int childWidthMeasureSpec;
        int cw = width - lp.leftMargin - lp.rightMargin;
        
        if (lp.width > 0) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width,
                    MeasureSpec.EXACTLY);
        }
        else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw,
                    MeasureSpec.EXACTLY);
        }
        else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw,
                    MeasureSpec.AT_MOST);
        }
        return childWidthMeasureSpec;
    }
    
    private int getChildHeightMeasureSpec(View child, int height) {
        int childHeightMeasureSpec;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int ch = height - lp.topMargin - lp.rightMargin;
        if (lp.height > 0) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height,
                    MeasureSpec.EXACTLY);
        }
        else if (lp.height == LayoutParams.MATCH_PARENT) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
                    MeasureSpec.AT_MOST);
        }
        else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        return childHeightMeasureSpec;
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bottomDrawable != null) {
            bottomDrawable.setBounds(0,
                    getMeasuredHeight() - bottomDrawableHeight,
                    getMeasuredWidth(), getMeasuredHeight());
            bottomDrawable.draw(canvas);
        }
    }

    // ---> adapter
    public void setTitleText(int resId){
        if (getLeftView() instanceof TextView){
            ((TextView) getLeftView()).setText(resId);
        }
    }

    public void setTitleTextForegroundColor(int color){
        if (getLeftView() instanceof TextView){
            ((TextView) getLeftView()).setTextColor(color);
        }
    }

    public void setTitleBarBackground(int color){
        setBackgroundColor(color);
    }

    public void setTitleBarGravity(int g, int x){

    }
}
