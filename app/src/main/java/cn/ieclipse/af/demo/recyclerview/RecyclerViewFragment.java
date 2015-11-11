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
package cn.ieclipse.af.demo.recyclerview;


import android.view.View;

import cn.ieclipse.af.demo.BaseFragment;
import cn.ieclipse.af.demo.R;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/30.
 */
public class RecyclerViewFragment extends BaseFragment {

    private View simpleList;
    private View divider;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        simpleList = view.findViewById(R.id.btn_rv_simple_list);

        setOnClickListener(simpleList);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_rv_simple_list) {
            // simple list
            SimpleListActivity.forward(getActivity());
        }
        super.onClick(v);
    }
}
