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

import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.FlowLayout;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月12日
 *       
 */
public class FlowLayoutActivity extends SampleBaseActivity {
    
    int hg = Gravity.CENTER_HORIZONTAL;
    int vg = Gravity.CENTER_VERTICAL;
    
    int[] hgs = { Gravity.LEFT, Gravity.CENTER_HORIZONTAL, Gravity.RIGHT };
    int[] vgs = { Gravity.TOP, Gravity.CENTER_VERTICAL, Gravity.BOTTOM };
    int[] cs = { ListView.CHOICE_MODE_NONE, ListView.CHOICE_MODE_SINGLE,
            ListView.CHOICE_MODE_MULTIPLE };
    int[] divs = { LinearLayout.SHOW_DIVIDER_NONE,
            LinearLayout.SHOW_DIVIDER_BEGINNING,
            LinearLayout.SHOW_DIVIDER_MIDDLE, LinearLayout.SHOW_DIVIDER_END };

    
    private ImageView mRefresh;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_flowlayout;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mRefresh = new ImageView(this);
        mRefresh.setImageResource(android.R.drawable.ic_menu_rotate);
        mTitleBar.addRight(mRefresh);
        setOnClickListener(mRefresh);
        setTitle("FlowLayout");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        fl2.setOnCheckedChangeListener(new FlowLayout.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(FlowLayout group, int checkedId) {
                tv1.setText("OnCheckedChangeListener: " + fl2.getCheckedValues());
            }
        });
        fl2.setOnCheckedChangeListener2(new FlowLayout.OnCheckedChangeListener2() {
            @Override
            public void onCheckedChanged(FlowLayout group, CompoundButton buttonView, int checkedId) {
                tv1.setText("OnCheckedChangeListener2: " + fl2.getCheckedValues());
            }
        });
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            float r = Float.parseFloat(et1.getText().toString());
            fl3.setGridRatio(r);
        } catch (Exception e) {

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
            long id) {
        int pos = parent.getSelectedItemPosition();
        if (spn1 == parent) {
            hg = hgs[pos];
            fl1.setGravity(hg | vg);
            fl2.setGravity(hg | vg);
        }
        else if (spn2 == parent) {
            vg = vgs[pos];
            fl1.setGravity(hg | vg);
            fl2.setGravity(hg | vg);
        }
        else if (spn4 == parent) {
            int f = divs[pos];
            if (pos == 0) {
                fl1.setShowHorizontalDividers(f);
                fl2.setShowHorizontalDividers(f);
            }
            else {
                fl1.setShowHorizontalDividers(fl1.getShowHorizontalDividers() | f);
                fl2.setShowHorizontalDividers(fl2.getShowHorizontalDividers() | f);
            }
        }
        else if (spn5 == parent) {
            int f = divs[pos];
            if (pos == 0) {
                fl1.setShowVerticalDividers(f);
                fl2.setShowVerticalDividers(f);
            }
            else {
                fl1.setShowVerticalDividers(fl1.getShowVerticalDividers() | f);
                fl2.setShowVerticalDividers(fl2.getShowVerticalDividers() | f);
            }
        }
        
        else if (spn3 == parent) {
            int f = cs[pos];
            fl1.setChoiceMode(f);
            fl2.setChoiceMode(f);
        }
    }
    
    @Override
    public void onClick(View v) {
        if (v == mRefresh) {
            MyAdapter adapter = new MyAdapter();
            adapter.setDataList(RandomUtils.genGBKList(10, 5));
            fl3.setAdapter(adapter);
            return;
        }
        super.onClick(v);
    }
    
    private static class MyAdapter extends AfBaseAdapter<String> {
        
        @Override
        public int getLayout() {
            return 0;
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                CheckBox cb = new CheckBox(parent.getContext());
                convertView = cb;
            }
            onUpdateView(convertView, position);
            return convertView;
        }
        
        @Override
        public void onUpdateView(View convertView, int position) {
            CheckBox cb = (CheckBox) convertView;
            cb.setText(getItem(position));
        }
    }
}
