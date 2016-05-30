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

import android.view.View;
import android.widget.AdapterView;

import com.google.android.flexbox.FlexboxLayout;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;

/**
 * 类/接口描述
 *
 * @author Harry
 * @date 2016年5月16日
 */
public class FlexboxLayoutActivity extends SampleBaseActivity {

    private FlexboxLayout mFl;

    int[] drs
        = {FlexboxLayout.FLEX_DIRECTION_ROW, FlexboxLayout.FLEX_DIRECTION_ROW_REVERSE, FlexboxLayout
        .FLEX_DIRECTION_COLUMN, FlexboxLayout.FLEX_DIRECTION_COLUMN_REVERSE};

    int[] fws = {FlexboxLayout.FLEX_WRAP_NOWRAP, FlexboxLayout.FLEX_WRAP_WRAP, FlexboxLayout.FLEX_WRAP_WRAP_REVERSE};

    int[] jcs
        = {FlexboxLayout.JUSTIFY_CONTENT_FLEX_START, FlexboxLayout.JUSTIFY_CONTENT_FLEX_END, FlexboxLayout
        .JUSTIFY_CONTENT_CENTER, FlexboxLayout.JUSTIFY_CONTENT_SPACE_BETWEEN, FlexboxLayout
        .JUSTIFY_CONTENT_SPACE_AROUND};
    int[] ais
        = {FlexboxLayout.ALIGN_ITEMS_FLEX_START, FlexboxLayout.ALIGN_ITEMS_FLEX_END, FlexboxLayout
        .ALIGN_ITEMS_CENTER, FlexboxLayout.ALIGN_ITEMS_BASELINE, FlexboxLayout.ALIGN_ITEMS_STRETCH};
    int[] acs
        = {FlexboxLayout.ALIGN_CONTENT_FLEX_START, FlexboxLayout.ALIGN_CONTENT_FLEX_END, FlexboxLayout
        .ALIGN_CONTENT_CENTER, FlexboxLayout.ALIGN_CONTENT_SPACE_BETWEEN, FlexboxLayout.ALIGN_CONTENT_SPACE_AROUND,
        FlexboxLayout.ALIGN_CONTENT_STRETCH};

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_flexboxlayout;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("FlexboxLayout");
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mFl = (FlexboxLayout) view.findViewById(R.id.content);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getSelectedItemPosition();
        if (spn1 == parent) {
            mFl.setFlexDirection(drs[pos]);
        }
        else if (spn2 == parent) {
            mFl.setFlexWrap(fws[pos]);
        }
        else if (spn3 == parent) {
            mFl.setJustifyContent(jcs[pos]);
        }
        else if (spn4 == parent) {
            mFl.setAlignItems(ais[pos]);
        }
        else if (spn5 == parent) {
            mFl.setAlignContent(acs[pos]);
        }
    }
}
