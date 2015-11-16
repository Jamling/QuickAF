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
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import cn.ieclipse.af.demo.BaseActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.FlowLayout;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月12日
 *       
 */
public class FlowLayoutActivity extends BaseActivity
        implements OnItemSelectedListener {
    FlowLayout fl1;
    FlowLayout fl2;
    
    Spinner hspn;
    Spinner vspn;
    Spinner choice;
    Spinner hdivShow;
    Spinner vdivShow;
    
    int hg = Gravity.CENTER_HORIZONTAL;
    int vg = Gravity.CENTER_VERTICAL;
    
    int[] hgs = { Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    int[] cs = { ListView.CHOICE_MODE_NONE, ListView.CHOICE_MODE_SINGLE,
            ListView.CHOICE_MODE_MULTIPLE };
    int[] divs = { LinearLayout.SHOW_DIVIDER_NONE,
            LinearLayout.SHOW_DIVIDER_BEGINNING,
            LinearLayout.SHOW_DIVIDER_MIDDLE, LinearLayout.SHOW_DIVIDER_END };
            
    @Override
    protected int getContentLayout() {
        return R.layout.activity_flowlayout;
    }
    
    @Override
    protected void initContentView() {
        super.initContentView();
        fl1 = (FlowLayout) findViewById(R.id.flowlayout1);
        fl2 = (FlowLayout) findViewById(R.id.flowlayout2);
        hspn = (Spinner) findViewById(R.id.spn1);
        vspn = (Spinner) findViewById(R.id.spn2);
        choice = (Spinner) findViewById(R.id.spn3);
        hdivShow = (Spinner) findViewById(R.id.spn4);
        vdivShow = (Spinner) findViewById(R.id.spn5);
        
        setListener(hspn, vspn, choice, hdivShow, vdivShow);
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
            fl1.setGravity(hg | vg);
            fl2.setGravity(hg | vg);
        }
        else if (vspn == parent) {
            vg = vgs[pos];
            fl1.setGravity(hg | vg);
            fl2.setGravity(hg | vg);
        }
        else if (hdivShow == parent) {
            int f = divs[pos];
            if (pos == 0) {
                fl1.setShowDividers(f);
                fl2.setShowDividers(f);
            }
            else {
                fl1.setShowDividers(fl1.getShowDividers() | f);
                fl2.setShowDividers(fl2.getShowDividers() | f);
            }
        }
        else if (vdivShow == parent) {
            int f = divs[pos];
            if (pos == 0) {
                fl1.setShowVerticalDividers(f);
                fl2.setShowVerticalDividers(f);
            }
            else {
                fl1.setShowVerticalDividers(fl1.getShowDividers() | f);
                fl2.setShowVerticalDividers(fl2.getShowDividers() | f);
            }
        }
        
        else if (choice == parent) {
            int f = divs[pos];
            fl1.setChoiceMode(f);
            fl2.setChoiceMode(f);
        }
    }
    
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub
        
    }
    
    public static void forward(Context context) {
        Intent intent = new Intent(context, FlowLayoutActivity.class);
        startActivity(intent, null, context, 0);
    }
}
