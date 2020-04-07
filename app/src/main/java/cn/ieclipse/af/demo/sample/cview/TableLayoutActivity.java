/*
 * Copyright (C) 2015-2016 reservoir
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

import android.content.Context;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.ieclipse.af.adapter.AfBaseAdapter;
import cn.ieclipse.af.adapter.AfGridAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseActivity;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.TableContainer;
import cn.ieclipse.af.view.TableLayout;
import cn.ieclipse.util.RandomUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class TableLayoutActivity extends SampleBaseActivity {
    private TableLayout mGrid;
    private ImageView mRefresh;
    private TableContainer mTable2;

    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_table_layout;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("TableLayout demo");
        mRefresh = createRightIcon(android.R.drawable.ic_menu_rotate);
        mTitleBar.addRight(mRefresh);
        setOnClickListener(mRefresh);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mGrid = (TableLayout) view.findViewById(R.id.tl);
        mTable2 = (TableContainer) view.findViewById(R.id.table_container);

//        int flag = LinearLayout.SHOW_DIVIDER_BEGINNING | LinearLayout.SHOW_DIVIDER_MIDDLE
//            | LinearLayout.SHOW_DIVIDER_END;
//        mTable.setShowHorizontalDividers(flag);
//        mTable.setShowVerticalDividers(flag);
//        mTable.setHorizontalDividerDrawable(new ColorDrawable(0xffff0000));
//        mTable.setVerticalDivider(new ColorDrawable(0xff0000ff));
//        mTable.setVerticalDividerHeight(4);
//        mTable.setHorizontalDividerWidth(4);

    }

    @Override
    protected void initData() {
        super.initData();
        mockData1();
        mockData2();
    }

    MyAdapter adapter;
    private int cols = 0;
    private int visibleCols = 0;

    private void mockData1() {
        cols = RandomUtils.genInt(2, 4);
        mGrid.setNumColumns(cols);

        // set data
        int len = (RandomUtils.genInt(1, 3));
        List<String[]> list = new ArrayList<>(len);

        String[] ss = new String[cols];
        for (int i = 0; i < len; i++) {
            ss = new String[cols];
            for (int j = 0; j < cols; j++) {
                ss[j] = String.format("C[%s,%s]", i, (j % cols));
            }
            list.add(ss);
        }
        MyAdapter adapter = new MyAdapter(this);
        adapter.setDataList(list);
        mGrid.setAdapter(adapter);
    }

    private void mockTh() {

    }

    private void mockData2() {
        mTable2.reset();
        mTable2.setColumns(cols);
        visibleCols = RandomUtils.genInt(cols+1);
        mTable2.setVisibleColumns(visibleCols);
        int fix = 0;
        try {
            fix = Integer.parseInt(et1.getText().toString());
        } catch (Exception e) {

        }
        fix = Math.min(fix, cols - 1);
        //fix = Math.min(1, fix);
        mTable2.setFixColumns(fix);

        // set header
        String[] ss = new String[cols];
        for (int j = 0; j < cols; j++) {
            ss[j] = ("TH " + j);
        }
        HeaderAdapter ha = new HeaderAdapter();
        ha.setDataList(Arrays.asList(ss));
        mTable2.setHeaderAdapter(ha);

        // set data
        int len = (RandomUtils.genInt(1, 20));
        List<String[]> list = new ArrayList<>(len);

        for (int i = 0; i < len; i++) {
            ss = new String[cols];
            for (int j = 0; j < cols; j++) {
                ss[j] = String.format("TD[%s,%s]", i, (j % cols));
            }
            list.add(ss);
        }

        tv1.setText(String.format("Advance TableLayout\nTotal columns:%d, Fix columns: %d, visible columns: %d, rows: %d", cols, fix, visibleCols, len));

        MyAdapter adapter = new MyAdapter(this);
        adapter.setDataList(list);
        mTable2.setDataAdapter(adapter);

    }

    @Override
    public void onClick(View v) {
        if (v == mRefresh) {
            mockData1();
            mockData2();
        }
        super.onClick(v);
    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            int skip = Integer.parseInt(s.toString());
//                    if (adapter != null) {
//                        //adapter.setSkipColumns(skip > adapter.getColumnCount() ? 0 : skip);
//                        //mGrid.setNumColumns(cols - adapter.getSkipColumns());
//                        mGrid.setHiddenColumns(skip);
//                        adapter.setSkipColumns(mGrid.getHiddenColumns());
//                    }
            mTable2.setFixColumns(skip);
            mockData2();
        } catch (Exception e) {
            DialogUtils.showToast(mTable2.getContext(), e.toString());
        }
    }

    private class HeaderAdapter extends AfBaseAdapter<String> {
        int w;

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public void onUpdateView(View convertView, int position) {
            if (w <= 0) {
                w = AppUtils.getScreenWidth(convertView.getContext()) - AppUtils.dimen2px(convertView.getContext(),
                    R.dimen.activity_horizontal_margin) * 2;
            }
            convertView.setLayoutParams(
                new ViewGroup.LayoutParams(mTable2.getColumnWidth(w), ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setBackgroundColor(AppUtils.getColor(tv.getContext(), R.color.colorPrimaryDark));
            tv.setTextColor(0xffffffff);
            tv.setText(getItem(position));
        }
    }

    private class MyAdapter extends AfGridAdapter<String[], String> {

        Context context;
        int w;

        private MyAdapter(Context context) {
            this.context = context;
            if (w <= 0) {
                w = AppUtils.getScreenWidth(context) - AppUtils.dimen2px(context, R.dimen.activity_horizontal_margin)
                    * 2;
            }
        }

        @Override
        public int getLayout() {
            return android.R.layout.simple_list_item_1;
        }

        @Override
        public int getRowCount() {
            return getDataList().size();
        }

        @Override
        public int getColumnCount() {
            return getRowCount() > 0 ? getDataList().get(0).length : 0;
        }

        @Override
        public void onUpdateView(View convertView, int row, int column) {
            convertView.setLayoutParams(
                new ViewGroup.LayoutParams(mTable2.getColumnWidth(w), ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
            if (row % 2 == 0) {
                convertView.setBackgroundColor(AppUtils.getColor(context, android.R.color.darker_gray));
            }
            tv.setTextColor(0xff000000);
            tv.setText(getItem(row, column));
        }

        @Override
        public String getItem(int row, int col) {
            return getDataList().get(row)[col];
        }
    }
}
