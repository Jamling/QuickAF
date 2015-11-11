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

import java.util.List;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/29.
 */
public abstract class AfRecyclerAdapter<T, VH extends AfViewHolder> extends RecyclerView.Adapter {

    private AfDataHolder<T> mDataHolder = new AfDataHolder<>();

    //public abstract int getLayout();

    public abstract void onUpdateView(VH holder, int position);

    public T getItem(int position) {
        return mDataHolder.getItem(position);
    }

    public void setDataList(List<T> list) {
        mDataHolder.setDataList(list);
    }

//    @Override
//    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
//        // create a new view
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(getLayout(), parent, false);
//        // set the view's size, margins, paddings and layout parameters
//        return getViewHolder();
//    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final VH holder = (VH) viewHolder;
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                holder.onItemClick(holder.itemView, holder.getLayoutPosition());
            }
        });
        onUpdateView(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDataHolder.getCount();
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View itemView, int position);
    }
}
