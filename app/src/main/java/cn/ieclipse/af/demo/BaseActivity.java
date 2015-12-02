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
package cn.ieclipse.af.demo;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import cn.ieclipse.af.app.AfActivity;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月12日
 *       
 */
public abstract class BaseActivity extends AfActivity {
    
    private TextView mLeft;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleBar.setBackgroundColor(0xff9966);
        mLeft = new TextView(this);
        mLeft.setText("关闭");
        Drawable d = getResources().getDrawable(android.R.drawable.ic_menu_close_clear_cancel);
        mLeft.setCompoundDrawablesWithIntrinsicBounds(d, null, null, null);
        mLeft.setGravity(Gravity.CENTER_VERTICAL);
        mTitleBar.setLeft(mLeft);
        setOnClickListener(mLeft);
        // mTitleBar.setLayoutGravity(Gravity.CENTER);
    }
    
    @Override
    public void onClick(View v) {
        if (v == mLeft) {
            finish();
        }
        super.onClick(v);
    }
    
}
