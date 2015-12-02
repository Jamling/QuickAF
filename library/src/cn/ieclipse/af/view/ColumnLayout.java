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
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月25日
 *       
 */
public class ColumnLayout extends ViewGroup {
    
    /**
     * @param context
     */
    public ColumnLayout(Context context) {
        super(context);
        init(context, null);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public ColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyleAttr
     * @param defStyleRes
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ColumnLayout(Context context, AttributeSet attrs, int defStyleAttr,
            int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs) {
        
    }
    
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        
    }
    
}
