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

import android.view.View;
import android.widget.TextView;

import cn.ieclipse.af.demo.common.ui.BaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class TabTabFragment extends BaseFragment {

    @Override
    public CharSequence getTitle() {
        return "Tab Sample";
    }

    @Override
    protected int getContentLayout() {
        return android.R.layout.simple_list_item_1;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        TextView tv1 = (TextView) view.findViewById(android.R.id.text1);
        tv1.setText("Hi~ as you see, this fragment is a sliding tab sample~");
    }
}
