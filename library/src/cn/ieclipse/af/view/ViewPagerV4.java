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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.viewpager.widget.ViewPager;
import cn.ieclipse.af.R;

public class ViewPagerV4 extends ViewPager {

    private boolean disableWipe = false;
    private float mRatio;
    private boolean enableClickToCurrent;

    public ViewPagerV4(Context context) {
        super(context);
    }

    public ViewPagerV4(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerV4);
            disableWipe = a.getBoolean(R.styleable.ViewPagerV4_af_disableWipe, false);
            mRatio = a.getFloat(R.styleable.ViewPagerV4_af_ratio, 0);
            a.recycle();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return !disableWipe && super.onTouchEvent(e);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        return !disableWipe && super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (enableClickToCurrent) {
            boolean f = getParent() instanceof AutoPlayView;
            f = f && getAdapter() instanceof AutoPlayView.LoopPagerAdapter;
            if (ev.getAction() == MotionEvent.ACTION_UP && !f) {
                View view = viewOfClickOnScreen(ev);
                if (view != null) {
                    int count = getChildCount();
                    int index = indexOfChild(view);
                    int current = getCurrentItem();
                    if (current != index) {
                        setCurrentItem(current);
                    }
                }
            }
        }
        return super.dispatchTouchEvent(ev);
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

    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.getLocationOnScreen(location);

            int minX = location[0];
            int minY = getTop();

            int maxX = location[0] + v.getWidth();
            int maxY = getBottom();

            float x = ev.getX();
            float y = ev.getY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }

    public void setRatio(float ratio) {
        this.mRatio = ratio;
    }

    public float getRatio() {
        return mRatio;
    }

    /**
     * Enable click non-current item to set to current item.
     * <p>
     * Note: If parent is {@link cn.ieclipse.af.view.AutoPlayView} and the adapter is
     * {@link cn.ieclipse.af.view.AutoPlayView.LoopPagerAdapter}, ignore this feature.
     * </p>
     *
     * @param enableClickToCurrent whether enable or not
     *
     * @since 3.0.1
     */
    public void setEnableClickToCurrent(boolean enableClickToCurrent) {
        this.enableClickToCurrent = enableClickToCurrent;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int width = widthSize;
        if (mRatio > 0 && widthMode != MeasureSpec.UNSPECIFIED) {
            int h = (int) (width / mRatio + 0.5f);
            int hSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, hSpec);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /**
     * Enable set {@link #setOffscreenPageLimit(int)} param to 0
     * <p>
     * Note: If param less than 0, the {@link androidx.core.view.ViewPager} will set limit to default 1.
     * Use reflection to make set limit 0
     * </p>
     *
     * @since 3.0.1
     */
    private void setNoCache() {
        // TODO
        if (getOffscreenPageLimit() == 0) {
            return;
        }
        try {
            Field[] fs = getClass().getSuperclass().getDeclaredFields();
            Field f = getClass().getSuperclass().getDeclaredField("mOffscreenPageLimit");
            f.setAccessible(true);
            f.set(this, 0);
            Method m = ViewPager.class.getDeclaredMethod("populate");
            m.setAccessible(true);
            m.invoke(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
