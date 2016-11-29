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

import android.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.adapter.AfFragmentPagerAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public class BaseFragmentAdapter extends AfFragmentPagerAdapter {

    private List<BaseFragment> fragments;

    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public BaseFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getItem(position).getTitle();
    }

    public void setFragments(List<BaseFragment> fragments) {
        this.fragments = fragments;
    }

    public void setFragments(BaseFragment... fragments) {
        this.fragments = new ArrayList<>();
        if (fragments != null) {
            for (BaseFragment f : fragments) {
                this.fragments.add(f);
            }
        }
    }

    public void addFragment(BaseFragment fragment) {
        if (this.fragments == null) {
            this.fragments = new ArrayList<>();
        }
        this.fragments.add(fragment);
    }
}
