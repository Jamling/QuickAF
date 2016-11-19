/*
 * Copyright (C) 2015-2016 HongTu
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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author Jamling
 */
public class HotImageView extends ImageView implements GestureDetector.OnGestureListener {
    private GestureDetector mGestureDetector;

    public HotImageView(Context context) {
        this(context, null);
    }

    public HotImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(context, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (regions != null && !regions.isEmpty()) {
            int x = getHotX(e.getX());
            int y = getHotY(e.getY());
            System.err.println("x:" + x + ",y:" + y);
            for (int i = 0; i < regions.size(); i++) {
                Rect rect = regions.get(i);
                OnClickListener l = listeners.get(i);
                if (l != null && rect.contains(x, y)) {
                    l.onClick(this);
                    return true;
                }
            }
        }
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
        return false;
    }

    private List<Rect> regions = new ArrayList<>();
    private List<OnClickListener> listeners = new ArrayList<>();

    public void addRegion(Rect rect, OnClickListener listener) {
        if (rect == null) {
            return;
        }
        regions.add(rect);
        listeners.add(listener);
    }

    public int getHotX(float ex) {
        int w = 0;
        if (getDrawable() != null) {
            w = getDrawable().getIntrinsicWidth();
        }
        if (w > 0) {
            return (int) (ex * w / getMeasuredWidth());
        }
        return (int) (ex);
    }

    public int getHotY(float ey) {
        int w = 0;
        if (getDrawable() != null) {
            w = getDrawable().getIntrinsicHeight();
        }
        if (w > 0) {
            return (int) (ey * w / getMeasuredHeight());
        }
        return (int) (ey);
    }
}
