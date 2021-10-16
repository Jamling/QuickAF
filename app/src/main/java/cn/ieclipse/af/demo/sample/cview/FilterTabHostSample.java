/*
 * Copyright (C) 2015-2017 QuickAF
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.view.SimpleFilterTabView;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.FilterTabHost;
import cn.ieclipse.af.view.FilterTabView;

/**
 * Description
 *
 * @author Jamling
 */
public class FilterTabHostSample extends SampleBaseFragment {

    private FilterTabHost mFilterLayout;
    private ArrayList<FilterTabView> mFilterViews;
    private final String[] mItem1
        = {"Item1_0", "Item1_1", "Item1_2", "Item1_3", "Item1_4", "Item1_5", "Item1_6", "Item1_7", "Item1_8",
        "Item1_9", "Item1_10", "Item1_11", "Item1_12"};
    private final String[] mItem2 = {"Item2_0", "Item2_1", "Item2_2"};
    private final String[] mItem3 = {"Item3_0", "Item1_3", "Item3_2", "Item3_3", "Item3_4"};
    private final String[] mItem4 = {};

    private View mReset;
    @Override
    protected int getContentLayout() {
        return R.layout.sample_filter_tab;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mReset = createRightText("Reset", true);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mFilterLayout = (FilterTabHost) view.findViewById(R.id.filter_layout);
        mFilterLayout.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mFilterViews = new ArrayList<>();

        FilterTabView.OnPopupItemClickListener listener = new FilterTabView.OnPopupItemClickListener() {
            @Override
            public void onPopupItemClick(FilterTabView parent, View view, int position) {
                if (parent instanceof SimpleFilterTabView) {
                    if (position >= 0) {
                        mFilterLayout.setTabChecked(parent, true);
                        parent.setSelected(true);
                        String text = ((SimpleFilterTabView) parent).getData().get(position).toString();
                        DialogUtils.showToast(parent.getContext(), text);
                    }
                }
            }
        };

        SimpleFilterTabView<String> tab1 = new SimpleFilterTabView<>(mFilterLayout, "Option1", listener,
            Arrays.asList(mItem1));
        mFilterViews.add(tab1);

        SimpleFilterTabView<String> tab2 = new SimpleFilterTabView<>(mFilterLayout, "Option2", listener,
            Arrays.asList(mItem2));
        mFilterViews.add(tab2);

        SimpleFilterTabView<String> tab3 = new SimpleFilterTabView<>(mFilterLayout, "Option3", listener,
            Arrays.asList(mItem3));
        mFilterViews.add(tab3);
        tab3.setReplaceTitle(true);
        tab3.setShowCount(3);

        SimpleFilterTabView<String> tab4 = new SimpleFilterTabView<>(mFilterLayout, "Option4", listener,
            Arrays.asList(mItem4));
        mFilterViews.add(tab4);

        mFilterLayout.init(mFilterViews);
        mFilterLayout.setAnimationStyle(R.style.anim_slide_top);
    }

    @Override
    protected void initBottomView() {
        super.initBottomView();
        CheckBox btn = new CheckBox(getActivity());
        btn.setText("Dim window?");
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mFilterLayout != null) {
                    mFilterLayout.setDimWindow(isChecked);
                }
            }
        });
        mBottomBar.addView(btn);
    }

    @Override
    public void onClick(View v) {
        if (v == mReset) {
            mFilterLayout.clearAllChoice();
            return;
        }
        super.onClick(v);
    }
}
