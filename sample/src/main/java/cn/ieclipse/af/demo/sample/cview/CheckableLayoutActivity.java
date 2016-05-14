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
package cn.ieclipse.af.demo.sample.cview;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.view.checkable.CheckableFrameLayout;
import cn.ieclipse.af.view.checkable.CheckableLinearLayout;
import cn.ieclipse.af.view.checkable.CheckableRelativeLayout;

/**
 * 类/接口描述
 *
 * @author Jamling
 */
public class CheckableLayoutActivity extends BaseActivity {
    ListView mListView;
    
    @Override
    protected int getContentLayout() {
        return android.R.layout.list_content;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        setTitle("CheckableXXXLayout");
        mListView = (ListView) view.findViewById(android.R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(),
            R.layout.sample_list_item_checkable, android.R.id.text1,
            new String[] {
                "Checkable Layouts: here just a sample of "
                    + CheckableLinearLayout.class.getSimpleName(),
                CheckableLinearLayout.class.getName(),
                CheckableRelativeLayout.class.getName(),
                CheckableFrameLayout.class.getName() }));
    }
}
