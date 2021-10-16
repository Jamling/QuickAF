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
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Collections;
import java.util.List;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.KeyboardUtils;
import cn.ieclipse.af.view.recycle.GridDividerItemDecoration;
import cn.ieclipse.af.view.recycle.ListDividerItemDecoration;
import cn.ieclipse.af.view.recycle.RecyclerHelper;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class RecyclerSample extends SampleBaseFragment implements NewsController.NewsListener {
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
    Drawable listDivider;

    private static final int[] ORIENTATION = {LinearLayoutManager.VERTICAL, LinearLayoutManager.HORIZONTAL};

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        listView = view.findViewById(R.id.rv);
        helper = new RecyclerHelper();
        helper.setRecyclerView(listView);

        adapter = new AfRecyclerAdapter();
        adapter.setHasStableIds(false);
        mDefaultDivider = new DividerItemDecoration(listView.getContext(), getOrientation());
        mAfDivider = new ListDividerItemDecoration(listView.getContext(), getOrientation());
        listDivider = mAfDivider.getDivider();

        spn5.setSelection(getResources().getStringArray(R.array.sample_round_radius).length - 1);
        spn5.setSelection(2);

        onItemSelected(spn3, null, 0, 0);

        setLayout();

        et1.setOnFocusChangeListener((v, hasFocus) -> {
            if (!v.hasFocus()) {
                load(false);
            }
        });
        et1.addTextChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardUtils.hideSoftInput(et1);
    }

    private int getOrientation() {
        return ORIENTATION[spn2.getSelectedItemPosition()];
    }

    private int getDColor() {
        String c = getResources().getStringArray(R.array.sample_colors)[spn3.getSelectedItemPosition()];
        return Color.parseColor(c);
    }

    private int getDHeight() {
        int r = Integer.parseInt(
            getResources().getStringArray(R.array.sample_round_radius)[spn4.getSelectedItemPosition()]);
        int h = AppUtils.dp2px(getActivity(), r);
        return h;
    }

    private int getDPadding() {
        int r = Integer.parseInt(
            getResources().getStringArray(R.array.sample_round_radius)[spn5.getSelectedItemPosition()]);
        int h = AppUtils.dp2px(getActivity(), r);
        return h;
    }

    private int getDShow() {
        int i = spn6.getSelectedItemPosition();
        return new int[]{0, ListDividerItemDecoration.BEGINNING, ListDividerItemDecoration.MIDDLE,
            ListDividerItemDecoration.END}[i];
    }

    private void updateD() {
        mAfDivider.setDividerShow(getDShow());
        if (chk1.isChecked()) {
            helper.setDividerColor(getDColor());
        }
        helper.setDividerPaddingStart(getDPadding());
        helper.setDividerPaddingEnd(getDPadding());
        helper.setDividerHeight(getDHeight());
        if (adapter.getItemCount() > 0) {
            helper.getRecyclerView().invalidateItemDecorations();
        }
    }

    private void setLayout() {
        int i = spn1.getSelectedItemPosition();
        adapter.removeDelegate(1);
        if (i == 0) {
            adapter.registerDelegate(1, new RefreshRecyclerSample.NewsDelegate());
            helper.setLinearLayoutManager(getOrientation());

            helper.setItemDecoration(mAfDivider);
            helper.getRecyclerView().setBackgroundResource(R.color.white);
        }
        else if (i == 1) {
            adapter.registerDelegate(1, new NewsGridDelegate());
            helper.setGridLayoutManager(3);
            helper.setItemDecoration(new GridDividerItemDecoration(getContext(), getOrientation()));
            helper.getRecyclerView().setBackgroundResource(R.color.black_alpha_50);
        }
        else if (i == 2) {
            adapter.registerDelegate(1, new NewsStaggerDelegate());
            helper.setStaggeredGridLayoutManager(3, getOrientation());
            helper.setItemDecoration(new GridDividerItemDecoration(getContext(), getOrientation()));
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) helper.getRecyclerView().getLayoutManager();
            sglm.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
            helper.getRecyclerView().setBackgroundResource(R.color.black_alpha_50);
        }
        listView.setAdapter(adapter);
        updateD();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int pos = parent.getSelectedItemPosition();
        if (parent == spn1 || parent == spn2) {
            setLayout();
        }
        else if (parent == spn3) { // color
            int c = getDColor();
            updateD();
//            int layout = spn1.getSelectedItemPosition();
//            if (layout > 0) {
//                helper.getRecyclerView().setBackgroundColor(getColor());
//            }
        }
        else if (parent == spn4) { // height
            int layout = spn1.getSelectedItemPosition();
            updateD();
        }
        else if (parent == spn5) { // padding
            updateD();
        }
        else if (parent == spn6) { // show
            updateD();
        }
        super.onItemSelected(parent, view, position, id);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == chk1) {
            if (!isChecked) {
                mAfDivider.setDrawable(listDivider);
            }
            updateD();
        }
        else if (buttonView == chk2) {
            mItemTouchHelper.attachToRecyclerView(isChecked ? helper.getRecyclerView() : null);
        }
        super.onCheckedChanged(buttonView, isChecked);
    }

    @Override
    public void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
        load(false);
    }

    @Override
    protected void initData() {
        super.initData();
        load(false);
        mItemTouchHelper = new ItemTouchHelper(mTouchCallback);
        // mItemTouchHelper.attachToRecyclerView(helper.getRecyclerView());
        // adapter.setOnItemLongClickListener(mItemLongClickListener);
    }

    private void load(boolean needCache) {
        NewsController.NewsRequest req = new NewsController.NewsRequest();
        try {
            req.num = Integer.parseInt(et1.getText().toString());
        } catch (Exception e) {
            req.num = 3;
        }
        controller.loadNews(req, needCache);
    }

    @Override
    public void onLoadNewsFailure(RestError error) {
        DialogUtils.showToast(getContext(), VolleyUtils.getError(getContext(), error));
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

        @Override
        protected RecyclerView.ViewHolder instanceViewHolder(View itemView) {
            return new MyViewHolder(itemView);
        }
    }

    private static class MyViewHolder extends NewsHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public static class NewsAdapter extends RecyclerView.Adapter<NewsHolder> {
        List<NewsController.NewsInfo> out;

        public NewsAdapter(List<NewsController.NewsInfo> out) {
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

    private final AfRecyclerAdapter.OnItemLongClickListener mItemLongClickListener
        = new AfRecyclerAdapter.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AfRecyclerAdapter adapter, View view, int position) {
            if (chk2.isChecked()) {
                RecyclerView.ViewHolder vh = helper.getRecyclerView().getChildViewHolder(view);
                mItemTouchHelper.startDrag(vh);
                return true;
            }
            return false;
        }
    };
    private ItemTouchHelper mItemTouchHelper;
    private final ItemTouchHelper.Callback mTouchCallback = new ItemTouchHelper.Callback() {

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
