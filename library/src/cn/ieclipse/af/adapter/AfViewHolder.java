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

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * // Provide a reference to the views for each data item
 * // Complex data items may need more than one view per item, and
 * // you provide access to all the views for a data item in a view holder
 *
 * @author Jamling
 */
public class AfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private View mItemView;
    private AfRecyclerAdapter.OnItemClickListener mOnClickListener;
    private AfRecyclerAdapter.OnItemLongClickListener mOnLongClickListener;

    public AfViewHolder(View itemView) {
        super(itemView);
        this.mItemView = itemView;
    }

    public void setOnClickListener(AfRecyclerAdapter.OnItemClickListener listener) {
        this.mOnClickListener = listener;
        mItemView.setOnClickListener(this);
    }

    public void setOnLongClickListener(AfRecyclerAdapter.OnItemLongClickListener listener) {
        this.mOnLongClickListener = listener;
        mItemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onItemLongClick(v, getAdapterPosition());
        }
        return true;
    }
}
