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
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import cn.ieclipse.af.R;

/**
 * 仿Launcher中的WorkSapce，可以左右滑动切换屏幕的类
 * 
 * @author jamling, Yao.GUET blog: http://blog.csdn.net/Yao_GUET date:
 *         2011-05-04
 *         
 */
public class ScrollLayout extends ViewGroup {
    
    private static final String TAG = "CScrollLayout";
    private static final int TOUCH_STATE_REST = 0;
    private static final int TOUCH_STATE_SCROLLING = 1;
    private static final int SNAP_VELOCITY = 600;
    private boolean debug = false;
    
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;
    
    private int mCurScreen;
    private int mDefaultScreen = 0;
    
    private int mSnapVelocity;
    private int mTouchState = TOUCH_STATE_REST;
    private int mTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;
    
    private OnScreenChangeListener mScreenChangeListener;
    private ViewPager mViewPager;
    
    public void setOnScreenChangeListener(OnScreenChangeListener listener) {
        this.mScreenChangeListener = listener;
    }
    
    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }
    
    public ScrollLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public ScrollLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCurScreen = mDefaultScreen;
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ScrollLayout);
            debug = a.getBoolean(R.styleable.ScrollLayout_af_debug, false);
            mSnapVelocity = a.getInt(R.styleable.ScrollLayout_af_snapVelocity,
                    SNAP_VELOCITY);
                    
            a.recycle();
        }
        
    }
    
    private void disableWipe(boolean disableWipe) {
        if (mViewPager != null) {
            if (mViewPager instanceof ViewPagerV4) {
                ((ViewPagerV4) mViewPager).setDisableWipe(disableWipe);
            }
            else {
                mViewPager.requestDisallowInterceptTouchEvent(disableWipe);
            }
        }
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (debug) {
            Log.v(TAG, String.format("onLayout changed=%b,l=%d,t=%d,r=%d,b=%d",
                    changed, l, t, r, b));
        }
        if (changed) {
            int childLeft = 0;
            final int childCount = getChildCount();
            
            for (int i = 0; i < childCount; i++) {
                final View childView = getChildAt(i);
                if (childView.getVisibility() != View.GONE) {
                    final int childWidth = childView.getMeasuredWidth();
                    childView.layout(childLeft, 0, childLeft + childWidth,
                            childView.getMeasuredHeight());
                    childLeft += childWidth;
                }
            }
        }
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (debug) {
            Log.v(TAG, "onMeasure");
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (widthMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ScrollLayout only canmCurScreen run at EXACTLY mode!");
        }
        
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (heightMode != MeasureSpec.EXACTLY) {
            throw new IllegalStateException(
                    "ScrollLayout only can run at EXACTLY mode!");
        }
        
        // The children are given the same width and height as the scrollLayout
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
        }
        // Log.e(TAG, "moving to screen "+mCurScreen);
        scrollTo(mCurScreen * width, 0);
    }
    
    /**
     * According to the position of current layout scroll to the destination
     * page.
     */
    public void snapToDestination() {
        final int screenWidth = getWidth();
        final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
        snapToScreen(destScreen);
    }
    
    public void snapToScreen(int whichScreen) {
        // get the valid layout page
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        if (getScrollX() != (whichScreen * getWidth())) {
            
            final int delta = whichScreen * getWidth() - getScrollX();
            mScroller.startScroll(getScrollX(), 0, delta, 0,
                    Math.abs(delta) * 2);
            mCurScreen = whichScreen;
            invalidate(); // Redraw the layout
            
            if (mScreenChangeListener != null) {
                int count = getChildCount();
                mScreenChangeListener.onScreenChange(mCurScreen,
                        mCurScreen < count - 1, mCurScreen > 0);
            }
        }
    }
    
    public void setToScreen(int whichScreen) {
        whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
        mCurScreen = whichScreen;
        scrollTo(whichScreen * getWidth(), 0);
        if (mScreenChangeListener != null) {
            int count = getChildCount();
            mScreenChangeListener.onScreenChange(mCurScreen,
                    mCurScreen < count - 1, mCurScreen > 0);
        }
    }
    
    public int getCurScreen() {
        return mCurScreen;
    }
    
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
        
        final int action = event.getAction();
        final float x = event.getX();
        final float y = event.getY();
        
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (debug) {
                    Log.v(TAG, "event down!");
                }
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                mLastMotionX = x;
                break;
                
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) (mLastMotionX - x);
                mLastMotionX = x;
                
                scrollBy(deltaX, 0);
                break;
                
            case MotionEvent.ACTION_UP:
                if (debug) {
                    Log.v(TAG, "event : up");
                }
                // if (mTouchState == TOUCH_STATE_SCROLLING) {
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity();
                if (debug) {
                    Log.v(TAG, "velocityX:" + velocityX);
                }
                
                if (velocityX > mSnapVelocity) {
                    if (mCurScreen > 0) {
                        // Fling enough to move left
                        if (debug) {
                            Log.v(TAG, "snap left");
                        }
                        snapToScreen(mCurScreen - 1);
                    }
                    else {
                        // TODO
                        disableWipe(false);
                        snapToDestination();
                    }
                }
                else if (velocityX < -mSnapVelocity) {
                    if (mCurScreen < getChildCount() - 1) {
                        // Fling enough to move right
                        if (debug) {
                            Log.v(TAG, "snap right");
                        }
                        snapToScreen(mCurScreen + 1);
                    }
                    else {
                        // TODO
                        disableWipe(false);
                        snapToDestination();
                    }
                }
                else {
                    snapToDestination();
                }
                
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                // }
                mTouchState = TOUCH_STATE_REST;
                break;
            case MotionEvent.ACTION_CANCEL:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        
        return true;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (debug) {
            Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);
        }
        
        final int action = ev.getAction();
        if ((action == MotionEvent.ACTION_MOVE)
                && (mTouchState != TOUCH_STATE_REST)) {
            return true;
        }
        
        final float x = ev.getX();
        final float y = ev.getY();
        
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                final int xDiff = (int) Math.abs(mLastMotionX - x);
                if (xDiff > mTouchSlop) {
                    mTouchState = TOUCH_STATE_SCROLLING;
                    
                }
                break;
                
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
                        : TOUCH_STATE_SCROLLING;
                break;
                
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mTouchState = TOUCH_STATE_REST;
                break;
        }
        
        return mTouchState != TOUCH_STATE_REST;
    }
    
    public static interface OnScreenChangeListener {
        void onScreenChange(int screen, boolean hasNext, boolean hasPreview);
    }
}
