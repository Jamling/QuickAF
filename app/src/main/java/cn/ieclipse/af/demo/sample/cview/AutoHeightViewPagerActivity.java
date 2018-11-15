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
import android.widget.TextView;

import java.util.Arrays;

import androidx.viewpager.widget.ViewPager;
import cn.ieclipse.af.adapter.AfPagerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2016年2月3日
 */
public class AutoHeightViewPagerActivity extends BaseActivity {
    
    ViewPager vp;
    
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_auto_viewpager;
    }
    
    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        setTitle("AutoHeightXXX demo");

        vp = (ViewPager) view.findViewById(R.id.view_pager);
        vp.setOffscreenPageLimit(2);
        MyAdapter adapter = new MyAdapter();
        adapter.setDataList(Arrays.asList("Page1", "Page2\n I'am second page\nabc", "Page3\n I am 3rd page\nabc\ndef",
                "Page4", "Page5"));
        vp.setAdapter(adapter);
    }
    
    private static class MyAdapter extends AfPagerAdapter<String> {
        
        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }
        
        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(getItem(position));
            tv.setTag(position);
        }
    }
}
