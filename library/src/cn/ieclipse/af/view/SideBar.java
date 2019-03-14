/*
 * Copyright 2014-2016 QuickAF
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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * 快速滑动筛选view可用于listview，recyclerview，默认A-Z，支持自定义
 *
 * @author wangjian
 */
public class SideBar extends View {

    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public String[] letters
        = {"☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
        "U", "V", "W", "X", "Y", "Z", "#"};

    private int[] attrs = {android.R.attr.textColor, android.R.attr.textColorHighlight, android.R.attr.textSize};
    private int mNormalTextColor = Color.BLACK;
    private int mFocusTextColor = Color.BLUE;
    private int mTextSize = 12;
    private int mFocusBg;

    private TextView mTextDialog;
    private Context mContext;

    private int mChooseItem = -1;// 选中
    private Paint paint = new Paint();

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideBar(Context context) {
        this(context, null);
    }

    @SuppressLint("ResourceType")
    private void init(Context context, AttributeSet attributeSet) {
        TypedArray a = context.obtainStyledAttributes(attributeSet, attrs);
        try {
            mNormalTextColor = a.getColor(0, mNormalTextColor);
            mFocusTextColor = a.getColor(1, mFocusTextColor);
            mTextSize = a.getInt(2, mTextSize);
        } finally {
            a.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / letters.length;// 获取每一个字母的高度

        for (int i = 0; i < letters.length; i++) {
            paint.setColor(mNormalTextColor);
            paint.setTypeface(Typeface.DEFAULT);
            paint.setAntiAlias(true);
            paint.setTextSize(dip2px(mContext, mTextSize));
            // 选中的状态
            if (i == mChooseItem) {
                paint.setColor(mFocusTextColor);
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(letters[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(letters[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = mChooseItem;
        final int c = (int) (y / getHeight() * letters.length);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    setBackground(new ColorDrawable(0x00000000));
                }
                else {
                    setBackgroundDrawable(new ColorDrawable(0x00000000));
                }
                mChooseItem = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                if (mFocusBg > 0) {
                    setBackgroundResource(mFocusBg);
                }

                if (oldChoose != c) {
                    if (c >= 0 && c < letters.length) {
                        if (onTouchingLetterChangedListener != null) {
                            onTouchingLetterChangedListener.onTouchingLetterChanged(letters[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(letters[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        mChooseItem = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    /**
     * 自定义右侧滑动列表，默认A-Z
     *
     * @param letters
     */
    public void setLetters(String... letters) {
        if (letters != null && letters.length > 0) {
            this.letters = letters;
            invalidate();
        }
    }

    /**
     * 字体大小
     *
     * @param textSize unit dp
     */
    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        invalidate();
    }

    /**
     * 设置slidebar触摸时的背景颜色
     *
     * @param resid
     */
    public void setFocusBarBackground(int resid) {
        this.mFocusBg = resid;
    }

    /**
     * 字体normal颜色
     *
     * @param normalTextColor the color resources id or Color.parseColor(ARGB)
     * @param focusTextColor  the color resources id or Color.parseColor(ARGB)
     */
    public void setTextColor(int normalTextColor, int focusTextColor) {
        if (normalTextColor > 0) {
            this.mNormalTextColor = normalTextColor;
        }
        if (focusTextColor > 0) {
            this.mFocusTextColor = focusTextColor;
        }
        invalidate();
    }

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
