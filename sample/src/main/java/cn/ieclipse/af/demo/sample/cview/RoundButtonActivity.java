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
package cn.ieclipse.af.demo.sample.cview;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.CountDownButton;
import cn.ieclipse.af.view.RoundButton;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月17日
 *       
 */
public class RoundButtonActivity extends SampleBaseActivity {
        
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_round_button;
    }
    
    RoundButton myBtn1;
    CountDownButton myBtn2;
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        myBtn1 = (RoundButton) btn1;
        myBtn2 = (CountDownButton) btn2;
        // btn2 text: enable: white; normal: gray
        ColorStateList csl2 = new ColorStateList(
            new int[][]{{android.R.attr.state_pressed}, {android.R.attr.state_enabled}, {}},
            new int[]{Color.RED, Color.GREEN, Color.BLUE});
        //myBtn2.setTextColor(AppUtils.getColorStateList(this, R.color.fg_main_btn_getcode_selector));
        myBtn2.setTotalTime(10000);
        // btn2 bg
        int r = AppUtils.dp2px(this, 4);
        RoundedColorDrawable normal = new RoundedColorDrawable(r, AppUtils.getColor(this, R.color.black_333333));
        normal.addStateColor(new int[]{android.R.attr.state_enabled, android.R.attr.state_window_focused}, AppUtils
            .getColor(this, R.color.colorPrimary)).applyTo(myBtn2);

        RoundButton btn3 = (RoundButton) this.btn3;
        RoundedColorDrawable bg3 = new RoundedColorDrawable(r << 1, 0xff333333);
        bg3.addStateColor(android.R.attr.state_pressed, 0xff3F51B5).applyTo(btn3);

    }

    void changeBorder() {
        try {
            int w = Integer.parseInt(et2.getText().toString());
            int c = Color.parseColor(et1.getText().toString());
            myBtn1.setBorder(c, w);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        int pos = parent.getSelectedItemPosition();
        if (spn1 == parent) {
            String c = getResources().getStringArray(R.array.sample_colors)[pos];
            myBtn1.setRoundBackgroundColor(Color.parseColor(c));
        }
        else if (spn2 == parent) {
            int r = Integer.parseInt(getResources().getStringArray(R.array.sample_round_radius)[pos]);
            int radius = AppUtils.dp2px(this, r);
            myBtn1.setRadius(radius);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
        changeBorder();
    }

    @Override
    public void onClick(View v) {
        if (v == myBtn2) {
            myBtn2.start();
            return;
        }
        super.onClick(v);
    }

}
