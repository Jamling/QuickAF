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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.style.ImageSpan;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/7/15.
 */
public class CenterImageSpan extends ImageSpan {
    
    public CenterImageSpan(Context context, Bitmap b) {
        super(context, b);
    }
    
    public CenterImageSpan(Context context, Bitmap b, int verticalAlignment) {
        super(context, b, verticalAlignment);
    }
    
    public CenterImageSpan(Drawable d) {
        super(d);
    }
    
    public CenterImageSpan(Drawable d, int verticalAlignment) {
        super(d, verticalAlignment);
    }
    
    public CenterImageSpan(Drawable d, String source) {
        super(d, source);
    }
    
    public CenterImageSpan(Drawable d, String source, int verticalAlignment) {
        super(d, source, verticalAlignment);
    }
    
    public CenterImageSpan(Context context, Uri uri) {
        super(context, uri);
    }
    
    public CenterImageSpan(Context context, int resourceId) {
        super(context, resourceId);
    }
    
    public CenterImageSpan(Context context, Uri uri, int verticalAlignment) {
        super(context, uri, verticalAlignment);
    }
    
    public CenterImageSpan(Context context, int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
    }
    
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom,
            Paint paint) {
        Drawable b = getDrawable();
        canvas.save();
        int transY = 0;
        transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
        
        canvas.translate(x, transY);
        b.draw(canvas);
        canvas.restore();
    }
    
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        Drawable d = getDrawable();
        Rect rect = d.getBounds();
        if (fm != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
            
            fm.ascent = -bottom;
            fm.top = -bottom;
            fm.bottom = top;
            fm.descent = top;
        }
        return rect.right;
    }
}
