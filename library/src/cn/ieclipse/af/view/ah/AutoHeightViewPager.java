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
package cn.ieclipse.af.view.ah;

import android.content.Context;
import android.graphics.Point;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自动计算高度的ViewPager No implements
 * 
 * @author Jamling
 * @date 2015/9/30.
 */
public class AutoHeightViewPager extends ViewPager {
    public AutoHeightViewPager(Context context) {
        super(context);
    }
    
    public AutoHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int size = getChildCount();
        if (size > 0
                && getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            Point p = mHeights.get(getCurrentItem());
            if (p == null) {
                try {
                    int i = getChildIndex();
                    View child = getChildAt(i);
                    if (child != null) {
                        child.measure(widthMeasureSpec,
                                MeasureSpec.makeMeasureSpec(
                                        MeasureSpec.UNSPECIFIED,
                                        MeasureSpec.UNSPECIFIED));
                        int w = child.getMeasuredWidth();
                        int h = child.getMeasuredHeight();
                        mHeights.put(getCurrentItem(), new Point(w, h));
                        heightMeasureSpec = MeasureSpec.makeMeasureSpec(h,
                                MeasureSpec.EXACTLY);
                        // widthMeasureSpec = MeasureSpec.makeMeasureSpec(w,
                        // MeasureSpec.EXACTLY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(p.y,
                        MeasureSpec.EXACTLY);
                // widthMeasureSpec = MeasureSpec.makeMeasureSpec(p.x,
                // MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    
    private Map<Integer, Point> mHeights = new HashMap<>();
    
    /**
     * Get ViewPager display page
     * 
     * @return the display page index of ViewPager.
     */
    // TODO need to improve
    private int getChildIndex() {
        int idx = getCurrentItem();
        int total = 0;
        if (getAdapter() != null) {
            total = getAdapter().getCount();
        }
        int s = getChildCount();
        
        List<Integer> p = new ArrayList<>();
        List<Integer> n = new ArrayList<>();
        List<Integer> f = new ArrayList<>();
        for (int j = 0; j < getOffscreenPageLimit(); j++) {
            if (idx - (j + 1) >= 0) {
                p.add(idx - j - 1);
            }
            if (idx + j + 1 < total) {
                n.add(idx + j + 1);
            }
        }
        Collections.sort(p);
        f.addAll(p);
        f.add(idx);
        f.addAll(n);
        System.out.println("except:" + f);
        
        int t = f.get(0);
        int off = idx - t;
        
        int ret = 0;
        if (p.size() < f.size()) {
            if (f.size() < s) {
                ret = off;
            }
            else {
                ret = idx - t + (s - f.size());
            }
        }
        else if (p.size() == f.size()) {
            ret = f.indexOf(idx);
        }
        else {
            ret = p.get(p.size() - 1) - idx;
        }
        System.out.println("idx " + ret);
        return ret;
    }
}
