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
package cn.ieclipse.af.demo.common.ui;

import android.os.Build;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.BaseFragmentAdapter;
import cn.ieclipse.af.view.PagerSlidingTabStrip;

/**
 * Base fragment holding fragments in ViewPager
 *
 * @author Jamling
 */
public abstract class BasePagerTabFragment extends BaseFragment {
    protected PagerSlidingTabStrip mTopView;
    protected ViewPager mViewPager;
    protected BaseFragmentAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.common_tab_strip;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mTopView = (PagerSlidingTabStrip) view.findViewById(R.id.top_view);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager2);
        mViewPager.setOffscreenPageLimit(5);

        FragmentManager fragmentManager = null;
        if (Build.VERSION.SDK_INT >= 17) {
            fragmentManager = getChildFragmentManager();
        }
        else {
            fragmentManager = getFragmentManager();
        }

        mAdapter = new BaseFragmentAdapter(fragmentManager);
    }

    @Override
    protected void initData() {
        super.initData();
        // add fragments
        addFragments();
        mViewPager.setAdapter(mAdapter);
        mTopView.setViewPager(mViewPager);
    }

    protected abstract void addFragments();
}
