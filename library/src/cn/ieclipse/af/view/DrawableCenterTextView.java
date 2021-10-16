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
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * /** Left drawable and text or hint center horizontal TextText.
 * <pre>
 *     <em>Sample:</em>
 *     android:drawableLeft=""
 *     android:gravity="left"
 * </pre>
 * <p>
 * <em>Note:</em> to align your drawable and text center, the gravity must be left horizontal
 * </p>
 *
 * @author Jamling
 * @date 2015/7/18.
 */
public class DrawableCenterTextView extends AppCompatTextView {

    public DrawableCenterTextView(Context context) {
        super(context);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        if (drawables != null) {
            Drawable drawableLeft = drawables[0];
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
//                if ((getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.CENTER_HORIZONTAL){
//                    middle =
//                }
                if (middle > 0) {
                    canvas.translate(middle, 0);
                }
            }
        }
        super.onDraw(canvas);
    }
}
