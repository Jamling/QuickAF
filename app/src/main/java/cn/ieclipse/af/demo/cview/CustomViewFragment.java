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
package cn.ieclipse.af.demo.cview;

import android.view.View;
import cn.ieclipse.af.demo.BaseFragment;
import cn.ieclipse.af.demo.R;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月12日
 *       
 */
public class CustomViewFragment extends BaseFragment {
    
    View flowLayout;
    
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_cview;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        flowLayout = view.findViewById(R.id.btn_flowlayout);
        setOnClickListener(flowLayout);
    }
    
    @Override
    public void onClick(View v) {
        if (v == flowLayout) {
            FlowLayoutActivity.forward(getActivity());
        }
        super.onClick(v);
    }
}
