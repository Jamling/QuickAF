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
    private final EditText mEt;

    public SimpleSearchView(Context context, int gravity) {
        if ((Gravity.HORIZONTAL_GRAVITY_MASK & gravity) == Gravity.CENTER_HORIZONTAL) {
            mEt = new DrawableCenterEditText(context);
        } else {
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
                if (listener != null) {
                    listener.afterTextChanged(s);
                }
            }
        });
    }

    public EditText getWidget() {
        return mEt;
    }

    public interface TextChangeListener {
        void afterTextChanged(Editable s);
    }

    private TextChangeListener listener;

    public void setListener(TextChangeListener listener) {
        this.listener = listener;
    }
}
