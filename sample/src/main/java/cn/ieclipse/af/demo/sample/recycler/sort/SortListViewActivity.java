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

package cn.ieclipse.af.demo.sample.recycler.sort;

import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.CharacterParser;
import cn.ieclipse.af.view.SideBar;


/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/5/9 .
 */
public class SortListViewActivity extends BaseActivity {

    private CharacterParser characterParser;
    private SideBar mSideBar;
    private TextView mUserDialog;
    private ListView listview;

    ContactModel contactModel;
    AtAdapter adapter;


    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_listview_sort;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        characterParser = CharacterParser.getInstance();
        mSideBar = (SideBar) findViewById(R.id.contact_sidebar);
        mUserDialog = (TextView) findViewById(R.id.contact_dialog);
        mSideBar.setTextView(mUserDialog);
        mSideBar.setFocusBarBackground(R.drawable.sidebar_background);

        listview = (ListView) findViewById(R.id.contact_member);
        adapter = new AtAdapter();

        Gson gson = new GsonBuilder().create();
        contactModel = gson.fromJson(DataList.tempData, ContactModel.class);
        listview.setAdapter(adapter);
        onLoadList(contactModel.lipro);

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listview.setSelection(position);
                }
            }
        });

    }

    public void onLoadList(List<ContactModel.HouseItemInfo> hosueList) {
        if (hosueList != null && !hosueList.isEmpty()) {
            for (ContactModel.HouseItemInfo at : hosueList) {
                if (at.title.length() > 0) {
                    // 解析出对应的拼音
                    String a = characterParser.getSelling(at.title).toUpperCase(Locale.US);
                    at.setSpelling(a);
                    // 获得第一个字符
                    if (a.length() > 1) {
                        a = a.substring(0, 1);
                    }
                    // 匹配A-Z就设置进去
                    if (a.matches("[A-Z]")) {
                        at.setInitial(a);
                    }
                }
            }
            Collections.sort(hosueList);
            adapter.clear();
            adapter.setDataList(hosueList);
        }
    }


    private class AtAdapter extends AfBaseAdapter<ContactModel.HouseItemInfo> implements SectionIndexer,
        Filterable {

        @Override
        public int getLayout() {
            return R.layout.sample_house_select_list_item;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            Holder holder = (Holder) convertView.getTag();
            if (holder == null) {
                holder = new Holder();
                holder.tvLab = (TextView) convertView.findViewById(R.id.tv_index_lab);
                holder.ivCheckStatus = (ImageView) convertView.findViewById(R.id.iv_check);
                holder.tvHouseName = (TextView) convertView.findViewById(R.id.tv_house_name);
                convertView.setTag(holder);
            }

            ContactModel.HouseItemInfo at = getItem(position);
            holder.tvHouseName.setText(at.title);

            int section = getSectionForPosition(position);
            if (position == getPositionForSection(section)) {
                holder.tvLab.setVisibility(View.VISIBLE);
                holder.tvLab.setText(at.getInitial());
            }
            else {
                holder.tvLab.setVisibility(View.GONE);
            }
        }

        @Override
        public Object[] getSections() {
            return null;
        }

        @Override
        public int getPositionForSection(int sectionIndex) {
            for (int i = 0; i < getCount(); i++) {
                String sortStr = getDataList().get(i).getInitial();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == sectionIndex) {
                    return i;
                }
            }

            return -1;
        }

        @Override
        public int getSectionForPosition(int position) {
            ContactModel.HouseItemInfo at = getDataList().get(position);
            if (at != null) {
                return at.getInitial().charAt(0);
            }
            return -1;
        }

        @Override
        public boolean hasStableIds() {
            //支持listview.getCheckedItemIds()不为空
            return true;
        }

        @Override
        public Filter getFilter() {
            return null;
        }
    }

    private static class Holder {
        private TextView tvLab;
        private TextView tvHouseName;
        private ImageView ivCheckStatus;
    }

}
