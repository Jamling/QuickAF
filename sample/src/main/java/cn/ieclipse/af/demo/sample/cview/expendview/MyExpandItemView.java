/*
 * Copyright 2014-2016 QuickAF
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

package cn.ieclipse.af.demo.sample.cview.expendview;

import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.view.expendview.ExpandableView;
import cn.ieclipse.af.view.expendview.SimpleExpandItemView;

public class MyExpandItemView extends SimpleExpandItemView<String> {
    /**
     * 底部按钮
     */
    private Button mBottomBtn;

    public MyExpandItemView(ExpandableView expandableView, String title, List<String> data,
                            OnPopupItemClickListener listener) {
        super(expandableView, title, data, listener);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.sample_expand_item_layout;
    }

    @Override
    public void initContentView(View view) {
        // super.initContentView(view);
        mListView = (ListView) findViewById(R.id.listView);
        mBottomBtn = (Button) findViewById(R.id.btn_all);


        // adapter1.notifyDataSetChanged();

        /**底部按钮的回调*/
        mBottomBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mExpandableView.hidePopWindow();
                mExpandableView.setSelectItemText(mBottomBtn.getText().toString());
                if (mOnPopupItemClickListener != null) {
                    mOnPopupItemClickListener.onExpandPopupItemClick(MyExpandItemView.this, -1);
                }
            }
        });
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void setData(List<String> data) {
        MyAdapter adapter1 = new MyAdapter();
        adapter1.setDataList(data);
        mListView.setAdapter(adapter1);
    }

    class MyAdapter extends AfBaseAdapter<String> {

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(getItem(position));
        }
    }


}
