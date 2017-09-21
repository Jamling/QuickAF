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

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;

/**
 * <p>
 * Another implements of badge view. In fact, this is not a really
 * {@link android.view.View}, but you can consider it as a
 * {@link android.widget.TextView} with a round rectangle color background.
 * </p>
 * <b><em>Note.</em></b>
 * If badge count less than 10 or {@link #badgeStyle} set to
 * {@link #STYLE_BACKGROUND} the background will display as circle
 *
 * @author Jamling
 * @date 2016年1月28日
 */
public class BadgeView2 {

    private int badgeCount = 0;

    /**
     * badge background.
     */
    private Drawable badgeBackground;
    /**
     * badge padding.
     */
    private Rect padding;
    /**
     * text layout
     */
    private StaticLayout badgeLayout;
    /**
     * text paint
     */
    private TextPaint badgePaint;
    private int badgeStyle = STYLE_BOTH;
    /**
     * Show badge text
     */
    public static final int STYLE_TEXT = 1;
    /**
     * Show badge background
     */
    public static final int STYLE_BACKGROUND = 2;
    /**
     * Show badge text and background
     */
    public static final int STYLE_BOTH = STYLE_TEXT | STYLE_BACKGROUND;

    private View targetView;

    /**
     * Construct badge view
     *
     * @param target the target view
     */
    public BadgeView2(View target) {
        this.targetView = target;
        initBadge(target.getContext());
    }

    /**
     * Get count
     *
     * @return count
     */
    public int getBadgeCount() {
        return badgeCount;
    }

    /**
     * Set count
     *
     * @param badgeCount count
     */
    public void setBadgeCount(int badgeCount) {
        if (badgeCount < 0) {
            // throw new IllegalArgumentException(
            // "badge count must be a posotive number");
        }
        if (this.badgeCount != badgeCount) {
            this.badgeCount = badgeCount;
            badgeLayout = new StaticLayout(getBadgeText(), badgePaint, getBadgeTextWidth(),
                StaticLayout.Alignment.ALIGN_NORMAL, 1f, 0f, false);
            requestLayout();
        }
    }

    /**
     * Set badge view display style
     *
     * @param badgeStyle combined value of {@link #STYLE_BACKGROUND}, {@link #STYLE_TEXT}
     *
     * @see BadgeView2#STYLE_BOTH
     * @see BadgeView2#STYLE_BACKGROUND
     * @see BadgeView2#STYLE_TEXT
     */
    public void setBadgeStyle(int badgeStyle) {
        if (this.badgeStyle != badgeStyle) {
            this.badgeStyle = badgeStyle;
            requestLayout();
        }
    }

    /**
     * Whether draw badge view or not, if count less than 0, will not draw
     *
     * @return true if draw the badge
     */
    private boolean willNotDrawBadge() {
        return badgeCount <= 0;
    }

    /**
     * <p>
     * Set shape drawable as background. If set
     * {@link BadgeView2#STYLE_BACKGROUND} style the background will displayed
     * as circle with assigned radius.
     * </p>
     * <p>
     * <b><em>Note </em></b>
     * If badge count less than 10 or {@link #badgeStyle} set to
     * {@link #STYLE_BACKGROUND} the background will display as circle
     * </p>
     * If radius assigned, set a default horizontal padding of radius also.
     *
     * @param radius  the radius of circle background under
     *                {@link BadgeView2#STYLE_BACKGROUND} style, px unit
     * @param bgColor background color, ARGB format
     */
    public void setBadgeBackground(int radius, int bgColor) {
        float[] radiusArray = new float[]{radius, radius, radius, radius, radius, radius, radius, radius};
        padding = new Rect(radius, 0, radius, 0);
        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ShapeDrawable bgDrawable = new ShapeDrawable(roundRect);
        bgDrawable.getPaint().setColor(bgColor);
        bgDrawable.setPadding(padding);
        this.badgeBackground = bgDrawable;
        requestLayout();
    }

    /**
     * Set badge background, may be a .9.png, to get well display effect, you
     * may call {@link BadgeView2#setBadgePadding(int, int, int, int)} to set
     * paddings
     *
     * @param d background drawable
     *
     * @see BadgeView2#setBadgePadding(int, int, int, int)
     */
    public void setBadgeBackground(Drawable d) {
        if (this.badgeBackground != d) {
            badgeBackground = d;
            requestLayout();
        }
    }

    /**
     * Set badge paddings
     *
     * @param left   left padding, px unit
     * @param top    top padding, px unit
     * @param right  right padding, px unit
     * @param bottom bottom padding, px unit
     */
    public void setBadgePadding(int left, int top, int right, int bottom) {
        padding.set(left, top, right, bottom);
        if (badgeBackground instanceof ShapeDrawable) {
            ((ShapeDrawable) badgeBackground).setPadding(left, top, right, bottom);
        }
        requestLayout();
    }

    /**
     * Set text color
     *
     * @param color ARGB color
     */
    public void setTextColor(int color) {
        this.badgePaint.setColor(color);
        // invalidate();
    }

    /**
     * Set text size
     *
     * @param textSize text size, px unit
     */
    public void setTextSize(int textSize) {
        this.badgePaint.setTextSize(textSize);
    }

    private void initBadge(Context context) {
        badgePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        badgePaint.density = context.getResources().getDisplayMetrics().density;
        padding = new Rect();
    }

    private int getBadgeTextWidth() {
        if (badgeCount <= 0) {
            return 0;
        }
        CharSequence text = getBadgeText();
        return (int) (badgePaint.measureText(text, 0, text.length()) + .5f);
    }

    @SuppressLint("WrongCall")
    private void requestLayout() {
        onMeasure();
        this.targetView.requestLayout();
    }

    private int measuredWidth;
    private int measureHeight;

    void onMeasure() {
        if (badgeBackground == null) {
            return;
        }
        boolean bg = drawBg();
        boolean fg = drawFg();
        Rect p = new Rect();
        if (badgeBackground.getPadding(p)) {
            padding = p;
        }
        int w = padding.left + padding.right;
        int h = padding.top + padding.bottom;

        if (bg) {
            int bw = badgeBackground.getIntrinsicWidth();
            int bh = badgeBackground.getIntrinsicHeight();
            // draw bg and text.
            if (fg) {
                // draw circle, no padding
                if (badgeCount < 10) {
                    w = Math.max(w, 1 + Math.max(this.badgeLayout.getWidth(), this.badgeLayout.getHeight()));
                    h = w;
                }
                else {
                    w += Math.max(bw, this.badgeLayout.getWidth());
                    h += Math.max(bh, this.badgeLayout.getHeight());
                }
            }
            // draw bg only
            else {
                if (bw > 0 && bh > 0) {
                    w = bw;
                    h = bh;
                }
                else {
                    w = Math.max(w, h);
                    h = w;
                }
            }
        }
        else if (fg) {
            w += this.badgeLayout.getWidth();
            h += this.badgeLayout.getHeight();
        }
        this.measuredWidth = w;
        this.measureHeight = h;
    }

    public int getMeasuredWidth() {
        return measuredWidth;
    }

    public int getMeasureHeight() {
        return measureHeight;
    }

    public void draw(Canvas canvas) {
        // draw bg
        if (willNotDrawBadge()) {
            return;
        }
        if (drawBg()) {
            this.badgeBackground.setBounds(0, 0, getMeasuredWidth(), getMeasureHeight());
            this.badgeBackground.draw(canvas);
        }

        if (drawFg()) {
            canvas.save();
            // if (badgeCount < 10 && drawBg())
            {
                canvas.translate((getMeasuredWidth() - this.badgeLayout.getWidth()) >> 1,
                    (getMeasureHeight() - this.badgeLayout.getHeight() >> 1));
            }
            // else {
            // canvas.translate(padding.left, padding.top);
            // }
            badgeLayout.draw(canvas);
            canvas.restore();
        }
    }

    private boolean drawBg() {
        boolean ret = this.badgeBackground != null && (badgeStyle & STYLE_BACKGROUND) == STYLE_BACKGROUND;
        return ret;
    }

    private boolean drawFg() {
        boolean ret = this.badgeLayout != null && (badgeStyle & STYLE_TEXT) == STYLE_TEXT;
        return ret;
    }

    // add feature
    public static final int MAX_DEFAULT = 99;
    private int max = MAX_DEFAULT;
    private CharSequence maxText = genMaxText();

    private CharSequence getBadgeText() {
        if (badgeCount > max) {
            return maxText;
        }
        return String.valueOf(badgeCount);
    }

    private CharSequence genMaxText() {
        SpannableStringBuilder ssb = new SpannableStringBuilder(String.valueOf(max) + "+");
        RelativeSizeSpan span = new RelativeSizeSpan(.8f);
        // ssb.setSpan(span, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SuperscriptSpan span2 = new SuperscriptSpan();
        // ssb.setSpan(span2, 2, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ssb;
    }

    /**
     * Set maximum count and display text
     *
     * @param max     the maximum count, such as 99
     * @param maxText the display text of maximum, such as 99+ or null to use
     *                default (max+)
     */
    public void setMax(int max, CharSequence maxText) {
        this.max = max;
        this.maxText = maxText;
        if (TextUtils.isEmpty(this.maxText)) {
            this.maxText = genMaxText();
        }
    }
}
