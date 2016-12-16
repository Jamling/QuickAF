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

import cn.ieclipse.af.demo.common.ui.BasePagerTabFragment;
import cn.ieclipse.af.demo.sample.cview.CustomViewFragment;
import cn.ieclipse.af.demo.sample.recycler.RecyclerFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class AppUIFragment extends BasePagerTabFragment {

    @Override
    public CharSequence getTitle() {
        return "UI samples";
    }

    @Override
    protected void addFragments() {
        mAdapter.setFragments(new TabAppFragment(), new CustomViewFragment(), new RecyclerFragment());
    }
}
