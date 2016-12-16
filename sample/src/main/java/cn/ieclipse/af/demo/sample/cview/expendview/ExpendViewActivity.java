/*
 * Copyright 2014-2016 QuickAF
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

package cn.ieclipse.af.demo.sample.cview.expendview;

import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.expendview.ExpandItemView;
import cn.ieclipse.af.view.expendview.ExpandableView;
import cn.ieclipse.af.view.expendview.SimpleExpandItemView;

public class ExpendViewActivity extends BaseActivity {
    private ExpandableView mExpandableView;
    private ArrayList<ExpandItemView> mExpandleItemViews;
    private String[] mItem1
        = {"Item1_0", "Item1_1", "Item1_2", "Item1_3", "Item1_4", "Item1_5", "Item1_6", "Item1_7", "Item1_8",
        "Item1_9"};
    private String[] mItem2 = {"Item2_0", "Item2_1", "Item2_2"};
    private String[] mItem3 = {"Item3_0", "Item1_3", "Item3_2", "Item3_3", "Item3_4"};
    private String[] mItem4 = {};

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_expand_view;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        setTitle("ExpandView");
        mExpandableView = (ExpandableView) findViewById(R.id.expandview);

        // mExpandableView.setTabItemId(R.layout.toggle_button);
        mExpandableView.setPopAnimationStyle(R.style.PopupWindowAnimation);

        mExpandleItemViews = new ArrayList<>();

        mExpandleItemViews.add(new MyExpandItemView(mExpandableView, "item1", Arrays.asList(mItem1),
            new ExpandItemView.OnPopupItemClickListener() {
                @Override
                public void onExpandPopupItemClick(ExpandItemView expandItemView, int position) {
                    if (position > 0) {
                        DialogUtils.showToast(expandItemView.getContext(), mItem1[position]);
                    }
                    else {

                    }
                }
            }));

        mExpandleItemViews.add(new SimpleExpandItemView<String>(mExpandableView, "item2", Arrays.asList(mItem2),
            new ExpandItemView.OnPopupItemClickListener() {
                @Override
                public void onExpandPopupItemClick(ExpandItemView expandItemView, int position) {
                    if (position > 0) {
                        DialogUtils.showToast(expandItemView.getContext(), mItem2[position]);
                    }
                    else {

                    }
                }
            }));

        mExpandleItemViews.add(new MyExpandItemView(mExpandableView, "item3", Arrays.asList(mItem3),
            new ExpandItemView.OnPopupItemClickListener() {
                @Override
                public void onExpandPopupItemClick(ExpandItemView expandItemView, int position) {
                    if (position > 0) {
                        DialogUtils.showToast(expandItemView.getContext(), mItem3[position]);
                    }
                    else {

                    }
                }
            }));

        mExpandleItemViews.add(new MyExpandItemView(mExpandableView, "item4", Arrays.asList(mItem4),
            new ExpandItemView.OnPopupItemClickListener() {
                @Override
                public void onExpandPopupItemClick(ExpandItemView expandItemView, int position) {
                    if (position > 0) {
                        DialogUtils.showToast(expandItemView.getContext(), mItem4[position]);
                    }
                    else {

                    }
                }
            }));
        mExpandableView.initViews(mExpandleItemViews);

    }
}
