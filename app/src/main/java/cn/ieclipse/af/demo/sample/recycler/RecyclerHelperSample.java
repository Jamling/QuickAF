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
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import java.util.Collections;
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

        adapter = new AfRecyclerAdapter();
        adapter.setHasStableIds(false);
        mDefaultDivider = new DividerItemDecoration(listView.getContext(), getOrientation()){
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
//                outRect.left = AppUtils.dp2px(listView.getContext(), 20);
//                outRect.right = AppUtils.dp2px(listView.getContext(), 20);
            }
        };
        mAfDivider = new ListDividerItemDecoration(listView.getContext(), getOrientation());
        onItemSelected(spn3, null, 0, 0);

        setLayout();
        chk1.setChecked(true);
        spn5.setSelection(getResources().getStringArray(R.array.sample_round_radius).length - 1);
    }

    private int getOrientation() {
        return ORIENTATION[spn2.getSelectedItemPosition()];
    }

    private int getColor() {
        String c = getResources().getStringArray(R.array.sample_colors)[spn3.getSelectedItemPosition()];
        //return Color.parseColor(c);
        return 0;
    }

    private void setLayout() {
        int i = spn1.getSelectedItemPosition();
        adapter.removeDelegate(1);
        if (i == 0) {
            adapter.registerDelegate(1, new RefreshRecyclerSample.NewsDelegate());
            helper.setItemDecoration(chk1.isChecked() ? mDefaultDivider : mAfDivider);
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
        listView.setAdapter(adapter);
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
            mDefaultDivider.setDrawable(new ColorDrawable(Color.parseColor(c)));
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
        else if (buttonView == chk2) {
            if (isChecked) {

            }
        }
        super.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    protected void initData() {
        super.initData();
        load(false);
        mItemTouchHelper = new ItemTouchHelper(mTouchCallback);
        mItemTouchHelper.attachToRecyclerView(helper.getRecyclerView());
        adapter.setOnItemLongClickListener(new AfRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AfRecyclerAdapter adapter, View view, int position) {
                RecyclerView.ViewHolder vh = helper.getRecyclerView().getChildViewHolder(view);
                //view.getTag(AfViewHolder.TAG_VIEW_HOLDER);
                mItemTouchHelper.startDrag(vh);
                return true;
            }
        });
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
        // listView.setAdapter(new NewsAdapter(out));
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

        @Override
        public Class<? extends RecyclerView.ViewHolder> getViewHolderClass() {
            return MyViewHolder.class;
        }
    }
    private static class MyViewHolder extends NewsHolder {

        public MyViewHolder(View view) {
            super(view);
        }
    }
    
    public static class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
        List<NewsController.NewsInfo> out;
        public NewsAdapter(List<NewsController.NewsInfo> out){
            this.out = out;
        }
        @Override
        public NewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sample_list_item_news, parent, false);
            NewsHolder holder = new NewsHolder(view);
            return holder;
        }
    
        @Override
        public void onBindViewHolder(NewsHolder holder, int position) {
            holder.setInfo(out.get(position));
        }
    
        @Override
        public int getItemCount() {
            return out == null ? 0 : out.size();
        }
    }

    private ItemTouchHelper mItemTouchHelper;
    private ItemTouchHelper.Callback mTouchCallback = new ItemTouchHelper.Callback() {

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            final int swipeFlags = 0;
            return RecyclerHelper.getMovementFlags(recyclerView, viewHolder);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition != toPosition) {
                Collections.swap(adapter.getDataList(), fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
            }
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //mAdapter.notifyDataSetChanged();
            System.out.println("onSwiped " + viewHolder);
        }

        /**
         * 长按选中Item的时候开始调用
         *
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                viewHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(-1);
        }
    };
}
