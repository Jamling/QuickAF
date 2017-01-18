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
package cn.ieclipse.af.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.ieclipse.af.adapter.delegate.AdapterDelegate;
import cn.ieclipse.af.adapter.delegate.DelegateManager;
import cn.ieclipse.af.common.Logger;

/**
 * AFRecyclerAdapter for {@link android.support.v7.widget.RecyclerView}
 *
 * @author Jamling
 */
public class AfRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Logger mLogger = Logger.getLogger(getClass());

    public static final int ITEM_VIEW_TYPE_NORMAL = 0;
    public static final int ITEM_VIEW_TYPE_HEADER = -2;
    public static final int ITEM_VIEW_TYPE_FOOTER = -3;

    private AfDataHolder<T> mDataHolder = new AfDataHolder<>();
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;
    private View mHeaderView;
    private View mFooterView;

    private DelegateManager<T> mDelegatesManager;

    /**
     * If old version (no delegate), use this constructor.
     *
     * @param context Context
     *
     * @deprecated from 2.0.0 Use {@link #AfRecyclerAdapter()} instead
     */
    @Deprecated
    public AfRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        setDataCheck(AfDataHolder.CHECK_BOTH);

        mDelegatesManager = new DelegateManager<>(this);
        // 绑定footer view
        bindFooterView();
    }

    /**
     * Use delegate
     *
     * @since 2.0.0
     */
    public AfRecyclerAdapter() {
        setDataCheck(AfDataHolder.CHECK_BOTH);
        setHasStableIds(true);
        mDelegatesManager = new DelegateManager<>(this);
    }

    public void registerDelegate(int viewType, AdapterDelegate delegate) {
        mDelegatesManager.addDelegate(viewType, delegate);
    }

    public void registerDelegate(AdapterDelegate delegate) {
        mDelegatesManager.addDelegate(delegate);
    }

    public void removeDelegate(int viewType) {
        mDelegatesManager.removeDelegate(viewType);
    }

    public T getItem(int position) {
        return mDataHolder.getItem(position - getHeaderCount());
    }

    public void setDataCheck(int checkMode) {
        mDataHolder.setDataCheck(checkMode);
    }

    public int getDataCheck() {
        return mDataHolder.getDataCheck();
    }

    public void setDataList(List<T> list) {
        mDataHolder.setDataList(list);
        //notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataHolder.getDataList();
    }

    public void add(T data) {
        mDataHolder.add(data);
        notifyItemInserted(getHeaderCount() + mDataHolder.getCount());
    }

    public void add2Top(T data) {
        mDataHolder.add2Top(data);
        notifyItemInserted(getHeaderCount());
    }

    public void add2Top(List<T> list) {
        mDataHolder.addAll2Top(list);
        //notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        mDataHolder.addAll(list);
        //notifyDataSetChanged();
    }

    public void clear() {
        mDataHolder.clear();
        //notifyDataSetChanged();
    }

    /**
     * Same as <code>deleteItem(position, true);</code>
     *
     * @param position item position see {@link android.support.v7.widget.RecyclerView.ViewHolder#getAdapterPosition()}
     *
     * @see cn.ieclipse.af.adapter.AfRecyclerAdapter#deleteItem(int, boolean)
     */
    public void deleteItem(int position) {
        deleteItem(position, true);
    }

    /**
     * Remove item, if isLayoutPosition is true (default) means removing the adapter item.
     *
     * @param position         the position of adapter or data
     * @param isLayoutPosition true is the layout/adapter position, false is the data position
     *
     * @deprecated use {@link #deleteItem(int)} instead.
     */
    public void deleteItem(int position, boolean isLayoutPosition) {
        int index = isLayoutPosition ? position - getHeaderCount() : position;
        T obj = mDataHolder.remove(index);
        // 删除后不为空，更新item
        if (mDataHolder.getCount() > 0) {
            if (obj != null) {
                notifyItemRemoved(isLayoutPosition ? position : position + getHeaderCount());
            }
        }
        else {
            // 删除后为空，更新显示empty view
            notifyDataSetChanged();
        }
    }

    public void deleteItem(T info) {
        int idx = getIndexOf(info);
        if (idx >= 0) {
            deleteItem(idx, false);
        }
    }

    public void updateItem(int position){
        // int idx = position - getHeaderCount();
        notifyItemChanged(position);
    }

    public void updateItem(T info) {
        int idx = getIndexOf(info);
        if (idx >= 0) {
            mDataHolder.getDataList().set(idx, info);
            notifyItemChanged(idx + getHeaderCount());
        }
    }

    public int getIndexOf(T t) {
        return mDataHolder.getIndexOf(t);
    }

    @Deprecated
    public int getLayout() {
        return 0;
    }

    @Deprecated
    public void onUpdateView(RecyclerView.ViewHolder holder, T data, int position) {

    }

    @Deprecated
    public int getFootLayout() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mDelegatesManager.getCount() > 0) {
            RecyclerView.ViewHolder holder = mDelegatesManager.onCreateViewHolder(parent, viewType);
            if (viewType >= 0 && holder instanceof AfViewHolder) {
                if (mOnItemClickListener != null) {
                    ((AfViewHolder) holder).setOnClickListener(mOnItemClickListener);
                }

                if (mOnItemLongClickListener != null) {
                    ((AfViewHolder) holder).setOnLongClickListener(mOnItemLongClickListener);
                }
            }
            return holder;
        }
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            mHeaderView.setTag(ITEM_VIEW_TYPE_HEADER);
            return new AfViewHolder(mHeaderView);
        }
        else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            mFooterView.setTag(ITEM_VIEW_TYPE_FOOTER);
            return new AfViewHolder(mFooterView);
        }
        else {
            AfViewHolder vh = new AfViewHolder(onCreateItemView(parent, viewType));
            vh.setAdapter(this);
            return vh;
        }
    }

    @Deprecated
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        View layout = mInflater.inflate(getLayout(), parent, false);
        return layout;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mDelegatesManager.getCount() > 0) {
            mDelegatesManager.onBindViewHolder(getItem(position), position, holder);
            return;
        }
        // 此处不对头部和尾部item做处理
        if (getItemViewType(position) == ITEM_VIEW_TYPE_NORMAL) {
            // 绑定数据
            try {
                // 判断是否有headview，更新position
                // int pos = getHeaderCount() > 0 ? position - getHeaderCount() : position;
                onUpdateView(holder, getItem(position), position);
            } catch (Exception e) {
                mLogger.e("exception onUpdateView", e);
            }
            if (holder instanceof AfViewHolder) {
                // 设置监听
                if (mOnItemClickListener != null) {
                    ((AfViewHolder) holder).setOnClickListener(mOnItemClickListener);
                }

                if (mOnItemLongClickListener != null) {
                    ((AfViewHolder) holder).setOnLongClickListener(mOnItemLongClickListener);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + mDataHolder.getCount() + getFooterCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getDataItemCount() {
        return mDataHolder.getCount();
    }

    public int getHeaderCount() {
        if (mDelegatesManager.getCount() > 0) {
            return mDelegatesManager.getHeaderCount();
        }
        return mHeaderView == null ? 0 : 1;
    }

    public int getFooterCount() {
        if (mDelegatesManager.getCount() > 0) {
            return mDelegatesManager.getFooterCount();
        }
        return mFooterView == null ? 0 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (mDelegatesManager.getCount() > 0) {
            return mDelegatesManager.getItemViewType(getItem(position), position);
        }
        // head默认0位置
        if (getHeaderCount() > 0 && position == 0) {
            return ITEM_VIEW_TYPE_HEADER;
        }
        // footer位置list末尾
        if (getFooterCount() > 0 && position == getItemCount() - 1) {
            return ITEM_VIEW_TYPE_FOOTER;
        }
        return ITEM_VIEW_TYPE_NORMAL;
    }

    public View getHeaderView() {
        return mHeaderView;
    }

    public void setHeaderView(View headerView) {
        this.mHeaderView = headerView;
        if (mHeaderView != null) {
            mHeaderView.setLayoutParams(getHeaderLayoutParams());
        }
    }

    public View getFooterView() {
        return mFooterView;
    }

    public void setFooterView(View footerView) {
        this.mFooterView = footerView;
        if (mFooterView != null) {
            mFooterView.setLayoutParams(getFooterLayoutParams());
        }
        notifyItemChanged(getItemCount());
    }

    /**
     * set footer view
     */
    private void bindFooterView() {
        int footLayout = getFootLayout();
        if (footLayout > 0) {
            View layout = mInflater.inflate(footLayout, null);
            setFooterView(layout);
        }
    }

    protected ViewGroup.LayoutParams getHeaderLayoutParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }

    protected ViewGroup.LayoutParams getFooterLayoutParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        // 防止RefreshRecyclerView中setGridLayoutManager在setAdapter之前调用无效
        setSpanSizeLookup(recyclerView.getLayoutManager());
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // do in sub
//        if (mRecyclerView != null) {
//            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
//            // 设置当StaggeredGridLayoutManager时的header和footer占满一行
//            if (manager instanceof StaggeredGridLayoutManager) {
//                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
//                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
//                    int itemType = holder.getItemViewType();
//                    // itemType 是head或footer时占满一行
//                    if (itemType < 0) {
//                        StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
//                        p.setFullSpan(true);
//                    }
//                }
//            }
//        }
    }

    /**
     * 设置当GridLayoutManager时的header和footer占满一行
     *
     * @param manager
     */
    public void setSpanSizeLookup(final RecyclerView.LayoutManager manager) {
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = (GridLayoutManager) manager;
            if (gridManager.getSpanSizeLookup() instanceof GridLayoutManager.DefaultSpanSizeLookup) {
                // 当itemType是head或footer时占满一行
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        int itemType = getItemViewType(position);
                        int spanCount = gridManager.getSpanCount();
                        if (itemType == AfRecyclerAdapter.ITEM_VIEW_TYPE_FOOTER
                            || itemType == AfRecyclerAdapter.ITEM_VIEW_TYPE_HEADER) {
                            return spanCount;
                        }
                        else {
                            return 1;
                        }
                    }
                });
            }
        }
    }

    //-------------------设置监听-start---------------------//

    /**
     * refer to {@link android.widget.AdapterView.OnItemClickListener}
     */
    public interface OnItemClickListener {
        /**
         * 对item做点击监听
         *
         * @param adapter AfRecyclerAdapter
         * @param view item view
         * @param position position
         */
        void onItemClick(AfRecyclerAdapter adapter, View view, int position);
    }

    public interface OnItemLongClickListener {
        /**
         * 对item做长按监听
         *
         * @param adapter AfRecyclerAdapter
         * @param view item view
         * @param position position
         */
        void onItemLongClick(AfRecyclerAdapter adapter, View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mOnItemLongClickListener = listener;
    }
    //-------------------设置监听-end---------------------//
}
