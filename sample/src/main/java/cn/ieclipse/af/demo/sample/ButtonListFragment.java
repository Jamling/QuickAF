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
package cn.ieclipse.af.demo.sample;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseFragment;

/**
 * 类/接口描述
 * 
 * @author Jamling
 *       
 */
public class ButtonListFragment extends BaseFragment {
    
    protected  ListView mListView;
    protected  Class[] activitys;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_button_list_fragment;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        activitys = getActivities();

        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        
        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_activated_1,
                android.R.id.text1, getItems()) {
            @Override
            public View getView(int position, View convertView,
                    ViewGroup parent) {
                TextView tv = (TextView) super.getView(position, convertView,
                        parent);
                tv.setTextColor(getResources()
                        .getColorStateList(R.color.fg_main_bottom_selector));
                return tv;
            }
        });
    }

    protected Class[] getActivities(){
        return new Class[]{};
    }
    
    protected String[] getItems() {
        String[] items = new String[activitys.length];
        for (int i = 0; i < activitys.length; i++) {
            String name = activitys[i].getSimpleName();
            items[i] = name.replace("Activity", "");
        }
        return items;
    }

    private void selectItem(int position) {
        if (mListView != null) {
            mListView.setItemChecked(position, true);
            mListView.setSelection(position);
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
