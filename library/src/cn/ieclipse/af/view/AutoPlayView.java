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

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * An auto play container.
 *
 * @author Jamling
 * @date 2015/7/15.
 */
public class AutoPlayView extends FrameLayout {
    
    public AutoPlayView(Context context) {
        this(context, null);
    }
    
    public AutoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    
    public AutoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    
    private boolean mLoop = true;
    private boolean mSmoothScroll = true;
    private boolean mPlaying;
    private long mInterval = 5000;
    private boolean mAutoStart;
    private ViewPager mViewPager;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int current = mViewPager.getCurrentItem();
            int size = mViewPager.getAdapter() == null ? 0 : mViewPager.getAdapter().getCount();
            if (current + 1 < size) {// can next
                mViewPager.setCurrentItem(++current, mSmoothScroll);
            }
            else if (current + 1 == size && mLoop) {
                mViewPager.setCurrentItem(0, mSmoothScroll);
            }
            if (mPlaying) {
                mHandler.sendEmptyMessageDelayed(0, mInterval);
            }
        }
    };
    
    private void init(Context context, AttributeSet attrs) {
        // mViewPager = new ViewPager(context);
        // addView(mViewPager);
    }
    
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            if (getChildAt(i) instanceof ViewPager) {
                mViewPager = (ViewPager) getChildAt(i);
                mViewPager.setFadingEdgeLength(0);
                if (mAutoStart) {
                    start();
                }
                break;
            }
        }
    }
    
    public void start() {
        if (!mPlaying) {
            mPlaying = true;
            mHandler.sendEmptyMessageDelayed(0, mInterval);
        }
        if (getCount() > 0) {
            mViewPager.setCurrentItem(0);
        }
    }
    
    public void stop() {
        mPlaying = false;
        mHandler.removeMessages(0);
    }
    
    public void addOnPageChangedListener(ViewPager.OnPageChangeListener listener) {
        if (listener != null) {
            mViewPager.addOnPageChangeListener(listener);
        }
    }
    
    public void setAdapter(PagerAdapter adapter) {
        if (adapter != null) {
            mViewPager.setAdapter(adapter);
        }
    }
    
    public ViewPager getViewPager() {
        return mViewPager;
    }
    
    public int getCurrent() {
        return mViewPager.getCurrentItem();
    }
    
    public int getCount() {
        if (mViewPager.getAdapter() != null) {
            return mViewPager.getAdapter().getCount();
        }
        return 0;
    }
    
    public void setInterval(long interval) {
        this.mInterval = interval;
        if (this.mInterval < 0) {
            this.mInterval = 0;
        }
    }
}
