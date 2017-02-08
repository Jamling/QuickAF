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
package cn.ieclipse.af.demo.common.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.util.ViewUtils;
import cn.ieclipse.af.view.FilterTabHost;
import cn.ieclipse.af.view.FilterTabView;

/**
 * Description
 *
 * @author Jamling
 */
public class SimpleFilterTabView<T> extends FilterTabView implements AdapterView.OnItemClickListener {

    protected ListView mListView;
    protected List<T> mData;
    protected int mShowCount;
    protected boolean mReplaceTitle;
    protected SimpleAdapter<T> mAdapter;

    public SimpleFilterTabView(FilterTabHost expandableLayout) {
        super(expandableLayout);
    }

    public SimpleFilterTabView(FilterTabHost expandableLayout, CharSequence title, OnPopupItemClickListener listener,
                               List<T> data) {
        super(expandableLayout, title, listener);
        setData(data);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.base_filter_tab;
    }

    @Override
    public void initContentView(View view) {
        super.initContentView(view);
        mListView = (ListView) view.findViewById(android.R.id.list);
        if (mListView != null) {
            mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            mListView.setOnItemClickListener(this);
        }
    }

    public void setData(List<T> data) {
        this.mData = data;
        if (mAdapter == null) {
            mAdapter = new SimpleAdapter<>();
            mAdapter.setDataList(data);
            if (mListView != null) {
                mListView.setAdapter(mAdapter);
            }
        }
        else {
            mAdapter.setDataList(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    public SimpleAdapter<T> getAdapter() {
        return mAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        hideExpandableView();
        if (mOnPopupItemClickListener != null) {
            mOnPopupItemClickListener.onPopupItemClick(this, view, position);
        }
        if (mListView != null) {
            mListView.setSelection(position);
            mListView.setItemChecked(position, true);
        }

        if (mReplaceTitle) {
            T info = (T) parent.getAdapter().getItem(position);
            mExpandableLayout.setSelectTabText(info.toString());
        }
    }

    @Override
    public void clearChoice() {
        if (mListView != null) {
            mListView.clearChoices();
            mListView.setSelection(ListView.INVALID_POSITION);
        }
    }

    public void setShowCount(int count) {
        if (count > 0) {
            mShowCount = count;
        }
    }

    public void setReplaceTitle(boolean replace) {
        this.mReplaceTitle = replace;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setListMaxHeight();
    }

    private void setListMaxHeight() {
        if (mListView != null && mShowCount > 0 && mListView.getAdapter() != null) {
            if (mShowCount < mListView.getAdapter().getCount()) {
                int totalHeight = 0;
                for (int i = 0; i < mShowCount; i++) {
                    View listItem = mListView.getAdapter().getView(i, null, mListView);
                    ViewUtils.measureView(listItem);
                    totalHeight += listItem.getMeasuredHeight();
                }

                ViewGroup.LayoutParams params = mListView.getLayoutParams();
                params.height = totalHeight + (mListView.getDividerHeight() * mShowCount);
                mListView.setLayoutParams(params);
            }
        }
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
