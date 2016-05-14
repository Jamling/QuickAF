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
 *
 * #                                                   #
 * #                       _oo0oo_                     #
 * #                      o8888888o                    #
 * #                      88" . "88                    #
 * #                      (| -_- |)                    #
 * #                      0\  =  /0                    #
 * #                    ___/`---'\___                  #
 * #                  .' \\|     |# '.                 #
 * #                 / \\|||  :  |||# \                #
 * #                / _||||| -:- |||||- \              #
 * #               |   | \\\  -  #/ |   |              #
 * #               | \_|  ''\---/''  |_/ |             #
 * #               \  .-\__  '-'  ___/-. /             #
 * #             ___'. .'  /--.--\  `. .'___           #
 * #          ."" '<  `.___\_<|>_/___.' >' "".         #
 * #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
 * #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
 * #     =====`-.____`.___ \_____/___.-`___.-'=====    #
 * #                       `=---='                     #
 * #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
 * #                                                   #
 * #               佛祖保佑         永无BUG             #
 * #                                                   #
 */

package cn.ieclipse.af.demo.sample.recycler.sort;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.CharacterParser;
import cn.ieclipse.af.view.RefreshRecyclerView;
import cn.ieclipse.af.view.SideBar;


/**
 * 类/接口描述
 *
 * @author wangjian
 * @date 2016/5/9 .
 */
public class SortRecyclerActivity extends BaseActivity {

    private CharacterParser characterParser;
    private SideBar mSideBar;
    private TextView mUserDialog;
    private RefreshRecyclerView recyclerView;

    StickyRecyclerHeadersDecoration headersDecor;
    ContactModel contactModel;
    MyAdapter adapter;


    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_recycler_sort;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        characterParser = CharacterParser.getInstance();
        mSideBar = (SideBar) findViewById(R.id.contact_sidebar);
        mUserDialog = (TextView) findViewById(R.id.contact_dialog);
        mSideBar.setTextView(mUserDialog);
        mSideBar.setFocusBarBackground(R.drawable.sidebar_background);

        recyclerView = (RefreshRecyclerView) findViewById(R.id.contact_member);
        recyclerView.setRefreshEnable(false);
        adapter = new MyAdapter(this);

        headersDecor = new StickyRecyclerHeadersDecoration(adapter);
        recyclerView.getRefreshView().addItemDecoration(headersDecor);
//        recyclerView.getRefreshView().addItemDecoration(new DividerDecoration(this));

        Gson gson = new GsonBuilder().create();
        contactModel = gson.fromJson(DataList.tempData, ContactModel.class);
        recyclerView.setAdapter(adapter);
        onLoadList(contactModel.lipro);

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    recyclerView.getRefreshView().scrollToPosition(position);
                }
            }
        });

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
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


    public class MyAdapter extends AfRecyclerAdapter<ContactModel.HouseItemInfo, ViewHolder> implements
        StickyRecyclerHeadersAdapter {

        public MyAdapter(Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public ViewHolder onBindViewHolder(View view) {
            return new ViewHolder(view);
        }

        @Override
        public void onUpdateView(ViewHolder holder, ContactModel.HouseItemInfo data, int position) {
            holder.tv1.setText(data.title);
        }

        // 实现以下方法,支持浮动头部
        @Override
        public long getHeaderId(int position) {
            return getItem(position).getSpelling().charAt(0);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent,
                false);
            view.setBackgroundResource(R.color.black_cccccc);
            return new RecyclerView.ViewHolder(view) {
            };
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            String showValue = String.valueOf(getItem(position).getSpelling().charAt(0));
            textView.setText(showValue);
        }

        public int getPositionForSection(char section) {
            for (int i = 0; i < getItemCount(); i++) {
                String sortStr = getDataList().get(i).getSpelling();
                char firstChar = sortStr.toUpperCase().charAt(0);
                if (firstChar == section) {
                    return i;
                }
            }
            return -1;

        }
    }

    private static class ViewHolder extends AfViewHolder {

        private TextView tv1;

        public ViewHolder(View itemView) {
            super(itemView);
            // set item selector
            itemView.setBackgroundResource(android.R.drawable.list_selector_background);
            tv1 = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
