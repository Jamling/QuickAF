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

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import java.util.ArrayList;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.common.ui.BaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class RadioTabSample extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_radio_tab;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        setTitle("RadioGroup+Fragment");
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        mRadioGroup.setOnCheckedChangeListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mFragments.add(createTab(Fragment1.class));
        mFragments.add(createTab(Fragment2.class));
        mFragments.add(createTab(Fragment3.class));

        switchTab(0);
    }

    // all tag fragments
    private ArrayList<Fragment> mFragments = new ArrayList<>(3);
    // current tab
    private Fragment mCurrentFragment;

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int count = group.getChildCount();
        for (int i = 0; i < count; i++) {
            if (group.getChildAt(i).getId() == checkedId) {
                // mViewPager.setCurrentItem(i, true);
                switchTab(i);
                break;
            }
        }
    }

    private void switchTab(int index) {
        Fragment fragment = mFragments.get(index);
        if (fragment != null) {
            FragmentManager manager = getFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            if (mCurrentFragment == null) {
                ft.replace(R.id.main_content, fragment, fragment.getClass().getSimpleName()).commit();
            }
            else if (fragment.isAdded()) {
                ft.hide(mCurrentFragment).show(fragment).commit();
            }
            else {
                ft.hide(mCurrentFragment).add(R.id.main_content, fragment, fragment.getClass().getSimpleName())
                    .commit();
            }
            mCurrentFragment = fragment;
            //onTabChanged(fragment, index);
        }
    }

    private Fragment createTab(Class clazz) {
        Fragment f = getFragmentManager().findFragmentByTag(clazz.getName());
        if (f == null) {
            f = Fragment.instantiate(this, clazz.getName(), null);
        }
        return f;
    }

    public static class Fragment1 extends BaseFragment {

        @Override
        protected int getContentLayout() {
            return R.layout.sample_activity_flowlayout;
        }

        @Override
        protected void initInitData() {
            super.initInitData();
            setTrimMode(true);
        }
    }

    public static class Fragment2 extends BaseFragment {
        @Override
        protected void initInitData() {
            super.initInitData();
            setTrimMode(true);
        }

        @Override
        protected int getContentLayout() {
            return R.layout.sample_fragment_aorm;
        }
    }

    public static class Fragment3 extends BaseFragment {
        @Override
        protected void initInitData() {
            super.initInitData();
            setTrimMode(true);
        }

        @Override
        protected int getContentLayout() {
            return R.layout.my_fragment;
        }
    }
}
