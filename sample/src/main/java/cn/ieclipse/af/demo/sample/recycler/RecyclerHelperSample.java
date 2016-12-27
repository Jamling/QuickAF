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
package cn.ieclipse.af.demo.sample.recycler;

import android.graphics.Color;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.view.recycle.GridSpaceDecoration;
import cn.ieclipse.af.view.recycle.ListDividerItemDecoration;
import cn.ieclipse.af.view.recycle.RecyclerHelper;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class RecyclerHelperSample extends SampleBaseFragment implements NewsController.NewsListener {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_recycler_layout;
    }

    RecyclerView listView;
    RecyclerHelper helper;
    AfRecyclerAdapter<NewsController.NewsInfo> adapter;
    NewsController controller = new NewsController(this);

    DividerItemDecoration mDefaultDivider;
    ListDividerItemDecoration mAfDivider;

    private static final int[] ORIENTATION = {LinearLayoutManager.VERTICAL, LinearLayoutManager.HORIZONTAL};

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        listView = (RecyclerView) view.findViewById(R.id.rv);
        helper = new RecyclerHelper();
        helper.setRecyclerView(listView);

        adapter = new AfRecyclerAdapter<>();
        mDefaultDivider = new DividerItemDecoration(listView.getContext(), getOrientation());
        mAfDivider = new ListDividerItemDecoration(listView.getContext(), getOrientation());
        onItemSelected(spn3, null, 0, 0);

        setLayout();
        listView.setAdapter(adapter);
        chk1.setChecked(true);
        spn5.setSelection(getResources().getStringArray(R.array.sample_round_radius).length - 1);
    }

    private int getOrientation() {
        return ORIENTATION[spn2.getSelectedItemPosition()];
    }

    private int getColor() {
        String c = getResources().getStringArray(R.array.sample_colors)[spn3.getSelectedItemPosition()];
        return Color.parseColor(c);
    }

    private void setLayout() {
        int i = spn1.getSelectedItemPosition();
        adapter.removeDelegate(1);
        if (i == 0) {
            adapter.registerDelegate(1, new RefreshRecyclerSample.NewsDelegate());
            helper.setLinearLayoutManager(getOrientation());
            helper.getRecyclerView().setBackgroundResource(R.color.white);
        }
        else if (i == 1) {
            adapter.registerDelegate(1, new NewsGridDelegate());
            GridSpaceDecoration decoration = new GridSpaceDecoration(getOrientation());
            int r = Integer.parseInt(
                getResources().getStringArray(R.array.sample_round_radius)[spn4.getSelectedItemPosition()]);
            decoration.setSpacing(r);
            helper.setItemDecoration(decoration);
            helper.setGridLayoutManager(3);
            helper.getRecyclerView().setBackgroundColor(getColor());
        }
        else if (i == 2) {
            adapter.registerDelegate(1, new NewsStaggerDelegate());
            GridSpaceDecoration decoration = new GridSpaceDecoration(getOrientation());
            int r = Integer.parseInt(
                getResources().getStringArray(R.array.sample_round_radius)[spn4.getSelectedItemPosition()]);
            decoration.setSpacing(r);
            helper.setItemDecoration(decoration);
            helper.setStaggeredGridLayoutManager(3, getOrientation());
            helper.getRecyclerView().setBackgroundColor(getColor());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getSelectedItemPosition();
        if (parent == spn1 || parent == spn2) {
            setLayout();
        }
        else if (parent == spn3) {
            String c = getResources().getStringArray(R.array.sample_colors)[pos];
            mAfDivider.setDividerColor(Color.parseColor(c));
            helper.setDividerColor(Color.parseColor(c));
            int layout = spn1.getSelectedItemPosition();
            if (layout > 0) {
                helper.getRecyclerView().setBackgroundColor(getColor());
            }
        }
        else if (parent == spn4) {
            int r = Integer.parseInt(getResources().getStringArray(R.array.sample_round_radius)[pos]);
            int h = AppUtils.dp2px(getActivity(), r);

            int layout = spn1.getSelectedItemPosition();
            if (layout > 0) {
                GridSpaceDecoration decoration = (GridSpaceDecoration) helper.getItemDecoration();
                if (decoration != null) {
                    helper.getRecyclerView().setBackgroundColor(getColor());
                    decoration.setSpacing(r);
                    helper.getRecyclerView().invalidateItemDecorations();
                }
            }
            else {
                mAfDivider.setDividerHeight(h);
                helper.setDividerHeight(h);
            }
        }
        else if (parent == spn5) {
            int r = Integer.parseInt(getResources().getStringArray(R.array.sample_round_radius)[pos]);
            int h = AppUtils.dp2px(getActivity(), r);
            mAfDivider.setPaddingStart(h);
            mAfDivider.setPaddingEnd(h);
            helper.setDividerPaddingStart(h);
            helper.setDividerPaddingEnd(h);
        }
        super.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chk1) {
            if (isChecked) {
                helper.setItemDecoration(mDefaultDivider);
            }
            else {
                helper.setItemDecoration(mAfDivider);
            }
        }
        super.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    protected void initData() {
        super.initData();
        load(false);
    }

    private void load(boolean needCache) {
        NewsController.NewsRequest req = new NewsController.NewsRequest();
        controller.loadNews(req, needCache);
    }

    @Override
    public void onLoadNewsFailure(RestError error) {

    }

    @Override
    public void onLoadNewsSuccess(List<NewsController.NewsInfo> out, boolean fromCache) {
        adapter.setDataList(out);
        adapter.notifyDataSetChanged();
    }

    public static class NewsGridDelegate extends RefreshRecyclerSample.NewsDelegate {
        @Override
        public int getLayout() {
            return R.layout.sample_grid_item_news;
        }
    }

    public static class NewsStaggerDelegate extends NewsGridDelegate {
        @Override
        public int getLayout() {
            return R.layout.sample_stagger_item_news;
        }
    }
}
