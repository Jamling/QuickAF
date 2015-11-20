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
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import cn.ieclipse.af.demo.BaseActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.RoundButton;
import cn.ieclipse.af.view.TitleBar;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月17日
 *       
 */
public class RoundButtonActivity extends BaseActivity
        implements OnItemSelectedListener {
        
    @Override
    protected int getContentLayout() {
        return R.layout.activity_round_button;
    }
    
    Spinner hspn;
    Spinner vspn;
    TitleBar bar;
    int[] hgs = { Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    int hg = Gravity.CENTER_HORIZONTAL;
    int vg = Gravity.CENTER_VERTICAL;
    
    RoundButton btn;
    View btn2;
    
    EditText et1;
    EditText et2;
    
    @Override
    protected void initContentView() {
        super.initContentView();
        
        btn = (RoundButton) findViewById(R.id.btn1);
        
        hspn = (Spinner) findViewById(R.id.spn1);
        vspn = (Spinner) findViewById(R.id.spn2);
        setListener(hspn, vspn);
        
        et1 = (EditText) findViewById(R.id.et_text);
        et2 = (EditText) findViewById(R.id.et_text2);
        btn2 = findViewById(R.id.btn2);
        setOnClickListener(btn2);
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
            String c = getResources().getStringArray(R.array.colors)[pos];
            btn.setRoundBackgroundColor(Color.parseColor(c));
        }
        else if (vspn == parent) {
            int r = Integer.parseInt(
                    getResources().getStringArray(R.array.round_radius)[pos]);
            int radius = AppUtils.dp2px(this, r);
            btn.setRadius(radius);
        }
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    
    }
    
    @Override
    public void onClick(View v) {
        if (v == btn2) {
            String s1 = et1.getText().toString();
            String s2 = et2.getText().toString();
            try {
                btn.setBorder(Color.parseColor(s1), Integer.parseInt(s2));
            } catch (Exception e) {
            
            }
        }
        super.onClick(v);
    }
    
    public static void forward(Context context) {
        Intent intent = new Intent(context, RoundButtonActivity.class);
        startActivity(intent, null, context, 0);
    }
}
