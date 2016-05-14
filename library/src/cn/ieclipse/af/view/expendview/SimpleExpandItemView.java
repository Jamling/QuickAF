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
package cn.ieclipse.af.view.expendview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;

/**
 * Description
 *
 * @author Jamling
 */
public class SimpleExpandItemView<T> extends ExpandItemView implements AdapterView.OnItemClickListener {

    protected ListView mListView;
    protected List<T> mData;

    public SimpleExpandItemView(ExpandableView expandableView) {
        super(expandableView);
    }

    public SimpleExpandItemView(ExpandableView expandableView, String title, List<T> data,
                                OnPopupItemClickListener listener) {
        super(expandableView, title, listener);
        setData(data);
    }

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    public void initContentView(View view) {
        super.initContentView(view);
        if (getContentLayout() == 0) {
            mListView = new ListView(view.getContext());
            mListView.setFooterDividersEnabled(true);
            addView(mListView,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
        if (mListView != null) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setOnItemClickListener(this);
        }
    }

    public void setData(List<T> data) {
        this.mData = data;
        if (mListView != null) {
            SimpleAdapter adapter = new SimpleAdapter();
            adapter.setDataList(mData);
            mListView.setAdapter(adapter);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mExpandableView.hidePopWindow();
        // T info = (T) parent.getAdapter().getItem(position);
        // mExpandableView.setSelectItemText(info.toString());
        if (mOnPopupItemClickListener != null) {
            mOnPopupItemClickListener.onExpandPopupItemClick(this, position);
        }
        mListView.setSelection(position);
        mListView.setItemChecked(position, true);
    }

    protected static class SimpleAdapter<T> extends AfBaseAdapter<T> {

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_single_choice;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            TextView tv = (TextView) convertView;
            tv.setText(getItem(position).toString());
        }
    }
}
