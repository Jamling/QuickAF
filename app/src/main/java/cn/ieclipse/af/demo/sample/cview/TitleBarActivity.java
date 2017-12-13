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

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.TitleBar;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月17日
 *       
 */
public class TitleBarActivity extends SampleBaseActivity {
        
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_titlebar;
    }

    TitleBar bar;
    TitleBar bar2;
    int[] hgs = { Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    int hg = Gravity.CENTER_HORIZONTAL;
    int vg = Gravity.CENTER_VERTICAL;
    
    private Context context;

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        bar = mTitleBar;
        setTitle("Title Bar");
        mTitleLeftView.setBackgroundColor(AppUtils.getColor(this, R.color.colorAccent));
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        context = TitleBarActivity.this;

        bar = (TitleBar) findViewById(cn.ieclipse.af.R.id.titleBar);
        bar2 = (TitleBar) findViewById(R.id.titleBar2);

        //bar2 = new TitleBar(context);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        //ll.addView(bar2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        bar2.setBackgroundColor(0xffff9966);
        TextView tv = new TextView(context);
        tv.setText("返回");
        tv.setBackgroundColor(AppUtils.getColor(this, R.color.colorAccent));
        bar2.addLeft(tv);
        
        tv = new TextView(context);
        tv.setText("Title");
        bar2.addMiddle(tv);

    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        int pos = parent.getSelectedItemPosition();
        if (spn1 == parent) {
            hg = hgs[pos];
            bar.setGravity(hg | vg);
            bar2.setGravity(hg | vg);
        }
        else if (spn2 == parent) {
            vg = vgs[pos];
            bar.setGravity(hg | vg);
            bar2.setGravity(hg | vg);
        }
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    
    }
    
    @Override
    public void onClick(View v) {
        String s = RandomUtils.genGBK(1, 2);
        if (v.getId() == R.id.chk1) {
            CheckBox chk = (CheckBox) v;
            int color = chk.isChecked() ? 0xff0000ff : 0xff000000;
            bar.setBottomDrawable(color);
            bar2.setBottomDrawable(color);
        }
        else if (v.getId() == R.id.btn1) {
            TextView btn = new TextView(context);
            btn.setText("left");
            bar.addLeft(btn);
            
            btn = new Button(context);
            btn = (TextView)View.inflate(context, R.layout.common_title_left_tv, null);
            btn.setText("left");
            btn.setBackgroundColor(AppUtils.getColor(this, R.color.colorAccent));
            bar2.addLeft(btn);
        }
        else if (v.getId() == R.id.btn2) {
            TextView btn = new Button(context);
            btn.setText(s);
            //bar.setLeft(btn);
            
            btn = new Button(context);
            btn.setText("Back");
            bar2.setLeft(btn);
        }
        else if (v.getId() == R.id.btn3) {
            View rv = createRightIcon(R.mipmap.search, true);
            rv.setBackgroundColor(AppUtils.getColor(this, R.color.colorAccent));
            //bar.addRight(rv);

            TextView tv = (TextView) bar2.addRight(R.layout.common_title_right_tv);
            tv.setText(s);
            tv.setBackgroundColor(AppUtils.getColor(this, R.color.colorAccent));
            //bar2.addRight(tv);
        }
        else if (v.getId() == R.id.btn4) {
            View rv = View.inflate(this, R.layout.common_title_right_iv, null);
            bar.setRight(rv);

            TextView tv = (TextView) View.inflate(this, R.layout.common_title_right_tv, null);
            tv.setText(s);
            bar2.setRight(tv);
        }
        else if (v.getId() == R.id.btn5){
            TextView tv = (TextView) View.inflate(this, R.layout.common_title_right_tv, null);
            tv.setText(s);
            bar.addMiddle(tv);

             tv = (TextView) View.inflate(this, R.layout.common_title_right_tv, null);
            tv.setText(s);
            bar2.addMiddle(tv);
        }
        else if (v.getId() == R.id.btn6){
            TextView btn = new EditText(context);
            btn.setText(s);
            bar.setMiddle(btn);

            btn = new TextView(context);
            btn.setText(s);
            bar2.setMiddle(btn);
        }
        super.onClick(v);
    }
}
