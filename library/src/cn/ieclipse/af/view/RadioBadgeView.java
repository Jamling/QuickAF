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
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016年1月28日
 */
public class RadioBadgeView extends AppCompatRadioButton {

    /**
     * @param context
     */
    public RadioBadgeView(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public RadioBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RadioBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initBadge(context, attrs);
    }

    private BadgeView2 badgeView;

    private void initBadge(Context context, AttributeSet attrs) {
        badgeView = new BadgeView2(this);
    }

    public BadgeView2 getBadgeView() {
        return badgeView;
    }

    public int getBadgeCount() {
        return badgeView.getBadgeCount();
    }

    public void setBadgeCount(int count) {
        badgeView.setBadgeCount(count);
    }

    public void incrementBadgeCount(int increment) {
        int count = getBadgeCount();
        setBadgeCount(increment + count);
    }

    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBadge(canvas);
    }

    private void drawBadge(Canvas canvas) {
        Point p = getBadgePosition();
        canvas.save();
        canvas.translate(p.x, p.y);
        badgeView.draw(canvas);
        canvas.restore();
    }

    /**
     * Calculate the badge view position, default is the top-right of the radio button with a top drawable.
     *
     * @return the position of badge view relative to target
     */
    protected Point getBadgePosition() {
        int offw = getMeasuredWidth() >> 1;
        int offh = getMeasuredHeight() >> 1;
        // 注意这里不考虑复杂的布局
        Drawable d = getCompoundDrawables()[1]; // top drawable;
        if (d != null) {
            offw += d.getIntrinsicWidth() >> 1;
            offh -= (getCompoundDrawablePadding() + d.getIntrinsicHeight()
                + (getPaint().descent() - getPaint().ascent())) / 2;
            offh -= badgeView.getMeasureHeight() / 2;
            if (offh < 0) {
                offh = 0;
            }
        }
        return new Point(offw, offh);
    }
}
