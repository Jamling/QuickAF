/*
 * Copyright (C) 2015-2016 QuickAF
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
import android.view.View;
import android.widget.CompoundButton;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.RandomUtils;
import cn.ieclipse.af.view.ColumnLayout;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/12/25.
 */
public class ColumnLayoutActivity extends SampleBaseActivity {
    private ColumnLayout mGrid;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_column_layout;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        setTitle("TextGridView");
        mGrid = (ColumnLayout) view.findViewById(R.id.grid);
        mGrid.setTextSize(AppUtils.sp2px(this, 14));// 14sp
        mGrid.setTextColor(0xffff0000);
        chk1.setChecked(true);
        chk2.setChecked(true);
        chk3.setChecked(true);
    }
    
    private void initGrid2() {
        int columns = RandomUtils.genInt(1, 4);
        // mGrid2.setWeights(new int[] { 0, 1, 0, 1 });
        mGrid.clear();
        mGrid.setNumColumns(columns);
        int sum = 0;
        int size = RandomUtils.genInt(5);
        for (int i = 0; i < size; i++) {
            int tmp = RandomUtils.genInt(1, columns);
            String[] ss = new String[tmp];
            for (int k = 0; k < tmp; k++) {
                ss[k] = RandomUtils.genGBK(2, 8);
            }
            mGrid.addRow(ss);
        }
    }
    
    private void addRow() {
        int tmp = RandomUtils.genInt(mGrid.getNumColumns() + 1);
        String[] ss = new String[tmp];
        for (int k = 0; k < tmp; k++) {
            ss[k] = RandomUtils.genGBK(2, 5);
        }
        mGrid.addRow(ss);
    }
    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn1) {
            initGrid2();
        }
        else if (v.getId() == R.id.btn2) {
            addRow();
        }
    }
    
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chk1) {
            int f = mGrid.getShowHorizontalBorder();
            int f2 = mGrid.getShowVerticalBorder();
            if (isChecked) {
                f |= (ColumnLayout.SHOW_DIVIDER_BEGINNING
                        | ColumnLayout.SHOW_DIVIDER_END);
                f2 |= (ColumnLayout.SHOW_DIVIDER_BEGINNING
                        | ColumnLayout.SHOW_DIVIDER_END);
            }
            else {
                f ^= (ColumnLayout.SHOW_DIVIDER_BEGINNING
                        | ColumnLayout.SHOW_DIVIDER_END);
                f2 ^= (ColumnLayout.SHOW_DIVIDER_BEGINNING
                        | ColumnLayout.SHOW_DIVIDER_END);
            }
            mGrid.setShowHorizontalBorder(f);
            mGrid.setShowVerticalBorder(f2);
        }
        else if (buttonView == chk2) {
            int f = mGrid.getShowHorizontalBorder();
            if (isChecked) {
                f |= (ColumnLayout.SHOW_DIVIDER_MIDDLE);
            }
            else {
                f ^= (ColumnLayout.SHOW_DIVIDER_MIDDLE);
            }
            mGrid.setShowHorizontalBorder(f);
        }
        else if (buttonView == chk3) {
            int f = mGrid.getShowVerticalBorder();
            if (isChecked) {
                f |= (ColumnLayout.SHOW_DIVIDER_MIDDLE);
            }
            else {
                f ^= (ColumnLayout.SHOW_DIVIDER_MIDDLE);
            }
            mGrid.setShowVerticalBorder(f);
        }
        mGrid.invalidate();
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        if (et1.getEditableText() == s) {
            try {
                mGrid.setHorizontalSpacing(
                        AppUtils.dp2px(this, Integer.parseInt(s.toString())));
                mGrid.requestLayout();
            } catch (Exception e) {
            
            }
        }
        else if (et2.getEditableText() == s) {
            try {
                mGrid.setVerticalSpacing(
                        AppUtils.dp2px(this, Integer.parseInt(s.toString())));
                mGrid.requestLayout();
            } catch (Exception e) {
            
            }
        }
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before,
            int count) {
        // TODO Auto-generated method stub
        
    }
}
