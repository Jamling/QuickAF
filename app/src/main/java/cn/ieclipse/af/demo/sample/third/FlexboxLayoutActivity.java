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
package cn.ieclipse.af.demo.sample.third;

import android.view.View;
import android.widget.AdapterView;

import com.google.android.flexbox.AlignContent;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;
import com.google.android.flexbox.JustifyContent;

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

    int[] drs = {FlexDirection.ROW, FlexDirection.ROW_REVERSE, FlexDirection.COLUMN, FlexDirection.COLUMN_REVERSE};
    int[] fws = {FlexWrap.NOWRAP, FlexWrap.WRAP, FlexWrap.WRAP_REVERSE};
    int[] jcs = {JustifyContent.FLEX_START, JustifyContent.FLEX_END, JustifyContent.CENTER, JustifyContent.SPACE_BETWEEN, JustifyContent.SPACE_AROUND, JustifyContent.SPACE_EVENLY};
    int[] ais = {AlignItems.FLEX_START, AlignItems.FLEX_END, AlignItems.CENTER, AlignItems.BASELINE, AlignItems.STRETCH};
    int[] acs = {AlignContent.FLEX_START, AlignContent.FLEX_END, AlignContent.CENTER, AlignContent.SPACE_BETWEEN, AlignContent.SPACE_AROUND, AlignContent.STRETCH};

    @Override
    protected int getContentLayout() {
        return R.layout.sample_3rd_flexboxlayout;
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
        } else if (spn2 == parent) {
            mFl.setFlexWrap(fws[pos]);
        } else if (spn3 == parent) {
            mFl.setJustifyContent(jcs[pos]);
        } else if (spn4 == parent) {
            mFl.setAlignItems(ais[pos]);
        } else if (spn5 == parent) {
            mFl.setAlignContent(acs[pos]);
        }
    }
}
