/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.widget.EditText;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016/3/16.
 */
public class SimpleSearchView {
    private EditText mEt;

    public SimpleSearchView(Context context, int gravity) {
        if ((Gravity.HORIZONTAL_GRAVITY_MASK & gravity) == Gravity.CENTER_HORIZONTAL) {
            mEt = new DrawableCenterEditText(context);
        }
        else {
            mEt = new EditText(context);
        }
        mEt.setGravity(Gravity.VERTICAL_GRAVITY_MASK & gravity);

        mEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (listener != null){
                    listener.afterTextChanged(s);
                }
            }
        });
    }

    public EditText getWidget(){
        return mEt;
    }

    public interface TextChangeListener{
        void afterTextChanged(Editable s);
    }

    private TextChangeListener listener;

    public void setListener(TextChangeListener listener) {
        this.listener = listener;
    }
}
