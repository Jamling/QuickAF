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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

import cn.ieclipse.af.R;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.ViewUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月19日
 */
public class RoundButton extends AppCompatButton {

    /**
     * @param context
     */
    public RoundButton(Context context) {
        super(context);
        init(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public RoundButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private RoundedColorDrawable mRoundBg;
    private int mRadius;
    private int borderWidth;
    private int borderColor;
    private int corners;
    private boolean useSystemBackground = false;

    private void init(Context context, AttributeSet attrs) {
        Drawable bg = getBackground();
        if (bg == null) {
            bg = new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent));
        }

        if (bg instanceof StateListDrawable) {
            // TODO support StateListDrawable
            // mRoundBg = new RoundedColorDrawable(radius, color);
//            StateListDrawable out = new StateListDrawable();
//            StateListDrawable sld = (StateListDrawable) bg;
//            int[] st = sld.getState();
//            for (int i = 0; i < st.length; i++) {
//                sld.selectDrawable(i);
//                sld.getCurrent();
//                
//            }
        } else if (bg instanceof ColorDrawable) {
            mRoundBg = RoundedColorDrawable.fromColorDrawable((ColorDrawable) bg);
        }
        if (attrs != null) {
            int[] attr = R.styleable.RoundButton;
            TypedArray a = context.obtainStyledAttributes(attrs, attr);
            mRadius = a.getDimensionPixelOffset(R.styleable.RoundButton_android_radius, mRadius);
            borderColor = a.getColor(R.styleable.RoundButton_af_borderColor, Color.TRANSPARENT);
            borderWidth = a.getDimensionPixelOffset(R.styleable.RoundButton_af_borderWidth, borderWidth);
            corners = a.getInt(R.styleable.RoundButton_af_corners, 0);
            useSystemBackground = a.getBoolean(R.styleable.RoundButton_af_useSystemBackground, false);
            if (corners > 0) {
                setRadius(mRadius, corners);
            } else {
                setRadius(mRadius);
            }
            setBorder(borderColor, borderWidth);
//            if (a.hasValue(R.styleable.RoundButton_pressedBgColor)) {
//                setStateBgColor(new int[]{android.R.attr.state_pressed},
//                    a.getColor(R.styleable.RoundButton_pressedBgColor, Color.TRANSPARENT), borderColor);
//            }
//            if (a.hasValue(R.styleable.RoundButton_checkedBgColor)) {
//                setStateBgColor(new int[]{android.R.attr.state_checked},
//                    a.getColor(R.styleable.RoundButton_checkedBgColor, Color.TRANSPARENT), borderColor);
//            }
//            if (a.hasValue(R.styleable.RoundButton_selectedBgColor)) {
//                setStateBgColor(new int[]{android.R.attr.state_selected},
//                    a.getColor(R.styleable.RoundButton_selectedBgColor, Color.TRANSPARENT), borderColor);
//            }
            a.recycle();
        }
        if (!useSystemBackground) {
            ViewUtils.setBackground(this, mRoundBg);
        }
    }

    public void setRadius(int radius) {
        if (mRoundBg != null) {
            mRoundBg.setRadius(radius);
        }
    }

    public void setRadius(int radius, int corners) {
        if (mRoundBg != null) {
            mRoundBg.setRadius(radius, corners);
        }
    }

    public void setBorder(int color, int width) {
        if (mRoundBg != null) {
            mRoundBg.setBorder(color, width);
        }
    }

    public void setRoundBackground(Drawable background) {
        if (background instanceof ColorDrawable) {
            mRoundBg = RoundedColorDrawable.fromColorDrawable((ColorDrawable) background);
            mRoundBg.setRadius(mRadius);
            ViewUtils.setBackground(this, mRoundBg);
        }
    }

    public void setRoundBackgroundColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.setColor(color);
        } else {
            setRoundBackground(new ColorDrawable(color));
        }
    }

    public RoundButton addStateBgColor(int[] stateSet, int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(stateSet, color);
        }
        return this;
    }

    public RoundButton setPressedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_pressed, color);
        }
        return this;
    }

    public RoundButton setCheckedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_checked, color);
        }
        return this;
    }

    public RoundButton setSelectedBgColor(int color) {
        if (mRoundBg != null) {
            mRoundBg.addStateColor(android.R.attr.state_selected, color);
        }
        return this;
    }

    public RoundedColorDrawable getRoundBg() {
        return mRoundBg;
    }

    public void apply() {
        if (mRoundBg != null) {
            mRoundBg.applyTo(this);
        }
    }
}
