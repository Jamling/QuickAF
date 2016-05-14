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
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Left drawable and text or hint center horizontal EditText.
 * <pre>
 *     <em>Sample:</em>
 *     android:drawableLeft=""
 *     android:gravity="left"
 * </pre>
 * <p>
 *     <em>Note:</em> to align your drawable and text center, the gravity must be left horizontal
 * </p>
 *
 * @author Jamling
 * @date 2015/7/15.
 */
public class DrawableCenterEditText extends EditText {
    public DrawableCenterEditText(Context context) {
        super(context);
    }
    
    public DrawableCenterEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public DrawableCenterEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @TargetApi(22)
    public DrawableCenterEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
            if (drawableLeft != null) {
                float textWidth = getPaint().measureText(getText().toString());
                if (textWidth == 0) {
                    textWidth = getPaint().measureText(getHint().toString());
                }
                int drawablePadding = getCompoundDrawablePadding();
                int drawableWidth = 0;
                drawableWidth = drawableLeft.getIntrinsicWidth();
                float bodyWidth = textWidth + drawableWidth + drawablePadding;
                float middle = (getMeasuredWidth() - bodyWidth) / 2;
                if (middle > 0) {
                    canvas.translate(middle, 0);
                }
            }
        }
        super.onDraw(canvas);
    }
}
