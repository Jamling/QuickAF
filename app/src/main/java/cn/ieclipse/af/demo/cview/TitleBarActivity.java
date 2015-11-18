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
package cn.ieclipse.af.demo.cview;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import cn.ieclipse.af.demo.BaseActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.TitleBar;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月17日
 *       
 */
public class TitleBarActivity extends BaseActivity
        implements OnItemSelectedListener {
        
    @Override
    protected int getContentLayout() {
        return R.layout.activity_titlebar;
    }
    
    Spinner hspn;
    Spinner vspn;
    TitleBar bar;
    TitleBar bar2;
    int[] hgs = { Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    int hg = Gravity.CENTER_HORIZONTAL;
    int vg = Gravity.CENTER_VERTICAL;
    
    @Override
    protected void initContentView() {
        super.initContentView();
        bar = (TitleBar) findViewById(R.id.titleBar);
        bar.setBackgroundColor(0xffff9966);
        
        bar2 = (TitleBar) findViewById(R.id.titleBar2);
        TextView tv = new TextView(this);
        tv.setText("返回");
        bar.addLeft(tv);
        
        tv = new TextView(this);
        tv.setText("Title");
        bar.addMiddle(tv);
        
        hspn = (Spinner) findViewById(R.id.spn1);
        vspn = (Spinner) findViewById(R.id.spn2);
        setListener(hspn, vspn);
    }
    
    private void setListener(Spinner... spinners) {
        for (Spinner spinner : spinners) {
            spinner.setOnItemSelectedListener(this);
        }
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        int pos = parent.getSelectedItemPosition();
        if (hspn == parent) {
            hg = hgs[pos];
            bar.setGravity(hg | vg);
            bar2.setGravity(hg | vg);
        }
        else if (vspn == parent) {
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
        if (v.getId() == R.id.check1) {
            CheckBox chk = (CheckBox) v;
            int color = chk.isChecked() ? 0xff0000ff : 0x000000;
            bar.setBottomDrawable(color);
            bar2.setBottomDrawable(color);
        }
        else if (v.getId() == R.id.button1) {
            Button btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar.addLeft(btn);
            
            btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar2.addLeft(btn);
        }
        else if (v.getId() == R.id.button2) {
            Button btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar.setLeft(btn);
            
            btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar2.setLeft(btn);
        }
        else if (v.getId() == R.id.button3) {
            TextView btn = new TextView(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar.addRight(btn);
            
            btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar2.addRight(btn);
        }
        else if (v.getId() == R.id.button4) {
            Button btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar.setMiddle(btn);
            
            btn = new Button(this);
            btn.setText(RandomUtils.genGBK(1, 3));
            bar2.setMiddle(btn);
        }
        super.onClick(v);
    }
    
    public static void forward(Context context) {
        Intent intent = new Intent(context, TitleBarActivity.class);
        startActivity(intent, null, context, 0);
    }
}
