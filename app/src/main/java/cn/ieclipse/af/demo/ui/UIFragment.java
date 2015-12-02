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
package cn.ieclipse.af.demo.ui;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.ieclipse.af.demo.BaseFragment;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.cview.FlowLayoutActivity;
import cn.ieclipse.af.demo.cview.RoundButtonActivity;
import cn.ieclipse.af.demo.cview.TitleBarActivity;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月12日
 *       
 */
public class UIFragment extends BaseFragment {
    
    ListView mListView;
    
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_cview;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                selectItem(position);
            }
        });
        
        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, getItems()));
    }
    
    private String[] getItems() {
        String[] items = new String[activitys.length];
        for (int i = 0; i < activitys.length; i++) {
            String cname = activitys[i].getSimpleName();
            items[i] = cname.replace("Activity", "");
        }
        return items;
    }
    
    private Class[] activitys = { DialogsActivity.class };
            
    private void selectItem(int position) {
        if (mListView != null) {
            mListView.setItemChecked(position, true);
        }
        
        Class cls = activitys[position];
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }
    
    @Override
    public void onClick(View v) {
        
        super.onClick(v);
    }
}
