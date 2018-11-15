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
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import cn.ieclipse.af.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.SharedPrefsUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016年1月5日
 */
public class Preference extends FrameLayout implements OnCheckedChangeListener {
    private String key;
    private boolean persistent = false;
    private CharSequence title;
    private CharSequence summary;
    private int layout;
    private int gravity;
    private Drawable icon;
    private Drawable icon2;

    /**
     * @param context
     */
    public Preference(Context context) {
        this(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public Preference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public Preference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs == null) {
            return;
        }
        int[] ATTRS
            = new int[]{android.R.attr.layout, android.R.attr.icon, android.R.attr.title, android.R.attr.summary,
            android.R.attr.drawableRight, android.R.attr.key, android.R.attr.persistent, android.R.attr.gravity,
            android.R.attr.clickable, android.R.attr.focusable};
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS, defStyleAttr, defStyleRes);
        int i = 0;
        layout = a.getResourceId(i++, 0);
        icon = a.getDrawable(i++);
        title = a.getText(i++);
        summary = a.getText(i++);
        icon2 = a.getDrawable(i++);
        key = a.getString(i++);
        persistent = a.getBoolean(i++, false);
        gravity = a.getInt(i++, gravity);
        setClickable(a.getBoolean(i++, true));
        setFocusable(a.getBoolean(i++, true));

        a.recycle();
        // load attribute
        a = context.obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes);
        if (a.hasValue(R.styleable.Preference_android_icon)) {
            int id = a.getResourceId(R.styleable.Preference_android_icon, 0);
            icon = AppUtils.getDrawable(context, id);
        }
        if (a.hasValue(R.styleable.Preference_android_drawableRight)) {
            // icon2 = a.getDrawable(R.styleable.Preference_android_drawableRight);
            int id = a.getResourceId(R.styleable.Preference_android_drawableRight, 0);
            icon2 = AppCompatResources.getDrawable(context, id);
        }
        if (a.hasValue(R.styleable.Preference_android_key)) {
            key = a.getString(R.styleable.Preference_android_key);
        }
        if (a.hasValue(R.styleable.Preference_android_persistent)) {
            persistent = a.getBoolean(R.styleable.Preference_android_persistent, false);
        }
        a.recycle();
        if (layout > 0) {
            LayoutInflater.from(context).inflate(layout, this, true);
        }
    }

    private TextView mTvTitle;
    private TextView mTvSummary;
    private ImageView mIvArrow;
    private CompoundButton mChk;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mTvTitle = (TextView) findViewById(android.R.id.title);
        mTvSummary = (TextView) findViewById(android.R.id.summary);
        mChk = (CompoundButton) findViewById(android.R.id.checkbox);
        mIvArrow = (ImageView) findViewById(android.R.id.icon2);

        if (persistent) {
            if (TextUtils.isEmpty(key)) {
                throw new IllegalArgumentException(
                    "persistent preference but found empty key, did you forgot set 'android:key' attribute?");
            }
        }
//        if (TextUtils.isEmpty(key)) {
//            key = getClass().getSimpleName() + getId();
//        }

        if (mTvTitle != null) {
            mTvTitle.setText(title);
            if (icon != null) {
                mTvTitle.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            }
            if (gravity > 0) {
                mTvTitle.setGravity(gravity);
            }
        }
        if (mTvSummary != null) {
            mTvSummary.setText(summary);
            if (icon2 != null && mIvArrow == null) {
                mTvSummary.setCompoundDrawablesWithIntrinsicBounds(null, null, icon2, null);
            }
        }

        if (mIvArrow != null && icon2 != null) {
            mIvArrow.setImageDrawable(icon2);
        }

        if (mChk != null) {
            if (persistent) {
                mChk.setChecked(getBoolean());
            }
            mChk.setOnCheckedChangeListener(this);
        }
    }

    private boolean getBoolean() {
        boolean ret = false;
        try {
            ret = SharedPrefsUtils.getBoolean(key, false);
        } catch (Exception e) {

        }
        return ret;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (persistent) {
            SharedPrefsUtils.putBoolean(key, isChecked);
        }
        if (mOnChangeListener != null) {
            mOnChangeListener.onPreferenceChange(this, isChecked);
        }
    }

    @Override
    public boolean performClick() {
        if (mChk != null) {
            mChk.performClick();
        }
        return super.performClick();
    }

    public TextView getTitleWidget() {
        return mTvTitle;
    }

    public TextView getSummaryWidget() {
        return mTvSummary;
    }

    /**
     * ImageView which id is <code>android:id/icon2</code>
     * @return
     * @since 3.0.1
     */
    public ImageView getArrowWidget() {
        return mIvArrow;
    }

    public CompoundButton getCheckWidget() {
        return mChk;
    }

    private OnPreferenceChangeListener mOnChangeListener;

    /**
     * Sets the callback to be invoked when this Preference is changed by the
     * user (but before the internal state has been updated).
     *
     * @param onPreferenceChangeListener The callback to be invoked.
     */
    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        mOnChangeListener = onPreferenceChangeListener;
    }

    /**
     * Interface definition for a callback to be invoked when the value of this
     * {@link Preference} has been changed by the user and is about to be set
     * and/or persisted. This gives the client a chance to prevent setting
     * and/or persisting the value.
     */
    public interface OnPreferenceChangeListener {
        /**
         * Called when a Preference has been changed by the user. This is called
         * before the state of the Preference is about to be updated and before
         * the state is persisted.
         *
         * @param preference The changed Preference.
         * @param newValue   The new value of the Preference.
         *
         * @return True to update the state of the Preference with the new
         * value.
         */
        boolean onPreferenceChange(Preference preference, Object newValue);
    }
}
