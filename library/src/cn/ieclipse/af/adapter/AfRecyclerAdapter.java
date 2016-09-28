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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import cn.ieclipse.af.common.Logger;

/**
 * AFRecyclerAdapter for {@link android.support.v7.widget.RecyclerView}
 *
 * @author Jamling
 */
public abstract class AfRecyclerAdapter<T, VH extends AfViewHolder> extends RecyclerView.Adapter {
    protected Logger mLogger = Logger.getLogger(getClass());

    public static final int ITEM_VIEW_TYPE_NORMAL = 0;
    public static final int ITEM_VIEW_TYPE_HEADER = -2;
    public static final int ITEM_VIEW_TYPE_FOOTER = -3;

    private AfDataHolder<T> mDataHolder = new AfDataHolder<>();
    private LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    private View mHeaderView;
    private View mFootView;

    public AfRecyclerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        setDataCheck(AfDataHolder.CHECK_BOTH);

        // 绑定footer view
        bindFooterView();
    }

    public T getItem(int position) {
        return mDataHolder.getItem(position);
    }

    public void setDataCheck(int checkMode) {
        mDataHolder.setDataCheck(checkMode);
    }

    public void setDataList(List<T> list) {
        mDataHolder.setDataList(list);
        notifyDataSetChanged();
    }

    public List<T> getDataList() {
        return mDataHolder.getDataList();
    }

    public void add(T data) {
        mDataHolder.add(data);
        notifyItemInserted(mDataHolder.getCount());
    }

    public void add2Top(T data) {
        mDataHolder.add2Top(data);
        notifyItemInserted(0);
    }

    public void add2Top(List<T> list) {
        mDataHolder.addAll2Top(list);
        notifyDataSetChanged();
    }

    public void addAll(List<T> list) {
        mDataHolder.addAll(list);
        notifyDataSetChanged();
    }

    public void updateItem(int position, T data) {
        mDataHolder.getDataList().set(position, data);
        notifyItemChanged(position);
    }

    public void clear() {
        mDataHolder.clear();
        notifyDataSetChanged();
    }

    /**
     * 函数里面的传入的参数position，
     * 它是在进行onBind操作时确定的，
     * 在删除单项后，已经出现在画面里的项不会再有调用onBind机会，
     * 这样它保留的position一直是未进行删除操作前的postion值。
     * 所以最好使用holder.getAdapterPosition();
     *
     * @param position
     */
    public void deleteItem(int position) {
        mDataHolder.remove(position);
        // 删除后不为空，更新item
        if (mDataHolder.getCount() > 0) {
            // 根据是否有headview，移除对应的item view
            int delViewPosi;
            if (getHeaderCount() > 0) {
                delViewPosi = position + 1;
            }
            else {
                delViewPosi = position;
            }
            notifyItemRemoved(delViewPosi);
        }
        else {
            // 删除后为空，更新显示empty view
            notifyDataSetChanged();
        }
    }

    public abstract int getLayout();

    public abstract VH onBindViewHolder(View view);

    public abstract void onUpdateView(VH holder, T data, int position);

    public int getFootLayout() {
        return 0;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_HEADER) {
            mHeaderView.setTag(ITEM_VIEW_TYPE_HEADER);
            return (VH) new AfViewHolder(mHeaderView);
        }
        else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            mFootView.setTag(ITEM_VIEW_TYPE_FOOTER);
            return (VH) new AfViewHolder(mFootView);
        }
        else {
            View layout = mInflater.inflate(getLayout(), parent, false);
            return onBindViewHolder(layout);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        // 此处不对头部和尾部item做处理
        if (getItemViewType(position) == ITEM_VIEW_TYPE_NORMAL) {
            final VH holder = (VH) viewHolder;
            // 绑定数据
            try {
                // 判断是否有headview，更新position
                int pos = getHeaderCount() > 0 ? position - 1 : position;
                onUpdateView(holder, getItem(pos), pos);
            } catch (Exception e) {
                mLogger.e("exception onUpdateView", e);
            }
            // 设置监听
            if (mOnItemClickListener != null) {
                holder.setOnClickListener(mOnItemClickListener);
            }

            if (mOnItemLongClickListener != null) {
                holder.setOnLongClickListener(mOnItemLongClickListener);
            }
        }
    }

    @Override
    public int getItemCount() {
        return getHeaderCount() + mDataHolder.getCount() + getFooterCount();
    }

    public int getDataItemCount() {
        return mDataHolder.getCount();
    }

    private int getHeaderCount() {
        return mHeaderView == null ? 0 : 1;
    }

    private int getFooterCount() {
        return mFootView == null ? 0 : 1;
    }

    @Override
    public int getItemViewType(int position) {
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
        if(mHeaderView != null){
            mHeaderView.setLayoutParams(getLayoutParams());
        }
    }

    public View getFootView() {
        return mFootView;
    }

    public void setFootView(View footView) {
        this.mFootView = footView;
        if(mFootView != null){
            mFootView.setLayoutParams(getLayoutParams());
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
            setFootView(layout);
        }
    }

    protected ViewGroup.LayoutParams getLayoutParams() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
        return params;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @Override
    public void onViewAttachedToWindow(final RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (mRecyclerView != null) {
            RecyclerView.LayoutManager manager = mRecyclerView.getLayoutManager();
            if (manager instanceof StaggeredGridLayoutManager) {
                ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
                if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    int itemType = holder.getItemViewType();
                    // itemType 是head或footer时占满一行
                    if (itemType == ITEM_VIEW_TYPE_FOOTER || itemType == ITEM_VIEW_TYPE_HEADER) {
                        StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                        p.setFullSpan(true);
                    }
                }
            }
        }
    }

    //-------------------设置监听-start---------------------//
    public interface OnItemClickListener {
        /**
         * 对item做点击监听，当有headview时，adapter中数据对应的position需要-1
         *
         * @param view
         * @param position
         */
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        /**
         * 对item做长按监听，当有headview时，adapter中数据对应的position需要-1
         *
         * @param view
         * @param position
         */
        void onItemLongClick(View view, int position);
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
