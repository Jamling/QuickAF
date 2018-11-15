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
 */
package cn.ieclipse.af.demo.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * Description
 *
 * @author Jamling
 */
public class IntroViewPager extends ViewPager implements GestureDetector.OnGestureListener {
    private GestureDetector mGestureDetector;

    public IntroViewPager(Context context) {
        this(context, null);
    }

    public IntroViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev) || super.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (getAdapter().getCount() == getCurrentItem() + 1) {
            // if (e1.getX() > e2.getX())
            {
                if (flingListener != null) {
                    flingListener.onFlingNext();
                }
                return true;
            }
        }
        return false;
    }

    public interface FlingListener {
        void onFlingNext();

        void onFlingPrevious();
    }

    private FlingListener flingListener;

    public void setFlingListener(FlingListener flingListener) {
        this.flingListener = flingListener;
    }
}
