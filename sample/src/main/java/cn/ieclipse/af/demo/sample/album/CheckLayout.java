/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.sample.album;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.FrameLayout;

import cn.ieclipse.af.demo.R;

/**
 * 类/接口描述
 *
 * @author Harry
 * @date 2015/11/17.
 */
public class CheckLayout extends FrameLayout implements Checkable {

    public CheckBox mChk;
    public boolean mChecked = false;

    public CheckLayout(Context context) {
        super(context);
    }

    public CheckLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        mChk.setChecked(checked);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChk = (CheckBox) findViewById(R.id.iv_chk);
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
