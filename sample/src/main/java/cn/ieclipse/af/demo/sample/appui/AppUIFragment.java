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
package cn.ieclipse.af.demo.sample.appui;

import android.app.FragmentManager;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.demo.common.BaseFragmentAdapter;
import cn.ieclipse.af.demo.sample.cview.CustomViewFragment;
import cn.ieclipse.af.demo.sample.recycler.RecyclerFragment;
import cn.ieclipse.af.demo.sample.utils.TabUtils;
import cn.ieclipse.af.view.PagerSlidingTabStrip;

/**
 * Description
 *
 * @author Jamling
 */
public class AppUIFragment extends BaseFragment {
    private PagerSlidingTabStrip mTopView;
    private ViewPager mViewPager;
    private BaseFragmentAdapter mAdapter;
    private TextView mRightView;

    @Override
    protected int getContentLayout() {
        return R.layout.main_tab;
    }

    @Override
    public CharSequence getTitle() {
        return "UI samples";
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleLeftView.setVisibility(View.INVISIBLE);
        mTitleTextView.setText(getTitle());
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mTopView = (PagerSlidingTabStrip) view.findViewById(R.id.top_view);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager2);
        mViewPager.setOffscreenPageLimit(5);
    }

    @Override
    protected void initData() {
        super.initData();
        FragmentManager fragmentManager = null;
        if (Build.VERSION.SDK_INT >= 17) {
            fragmentManager = getChildFragmentManager();
        }
        else {
            fragmentManager = getFragmentManager();
        }

        mAdapter = new BaseFragmentAdapter(fragmentManager);
        mAdapter.setFragments(new TabAppFragment(), new CustomViewFragment(), new RecyclerFragment(), new TabUtils(),
            new TabTabFragment());
        mViewPager.setAdapter(mAdapter);
        mTopView.setViewPager(mViewPager);
    }
}
