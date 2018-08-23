/*
 * Copyright (C) 2015-2016 adviser2
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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ToggleButton;

/**
 * Tab item which is the child view of {@link cn.ieclipse.af.view.FilterTabHost}, it can place an image such as arrow
 * after the text.
 *
 * @author Jamling
 * @since 3.0.1
 */
public class FilterTabItem extends ToggleButton {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FilterTabItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public FilterTabItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public FilterTabItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterTabItem(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        calc(canvas);
        super.onDraw(canvas);
    }

    protected void calc(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[2]; // right drawable
            if (drawableLeft != null) {
                CharSequence text = getText();
                if (TextUtils.isEmpty(text)) {
                    text = getHint();
                }
                float textWidth = 0;
                if (!TextUtils.isEmpty(text)) {
                    textWidth = getPaint().measureText(text.toString());
                }
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                float middle = (getMeasuredWidth() - bodyWidth) / 2;
                if (middle > 0) {
                    canvas.translate(-middle, 0);
                }
            }
        }
    }
}
