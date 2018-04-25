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
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * // Provide a reference to the views for each data item
 * // Complex data items may need more than one view per item, and
 * // you provide access to all the views for a data item in a view holder
 *
 * @author Jamling
 */
public class AfViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private AfRecyclerAdapter adapter;
    private AfRecyclerAdapter.OnItemClickListener mOnClickListener;
    private AfRecyclerAdapter.OnItemLongClickListener mOnLongClickListener;

    public AfViewHolder(View view) {
        super(view);
        if (!hasOnClickListener(view)) {
            itemView.setOnClickListener(this);
        }
        if (!hasOnLongClickListener(view)) {
            itemView.setOnLongClickListener(this);
        }
//        if (itemView.getBackground() == null) {
//            itemView.setBackgroundResource(android.R.drawable.list_selector_background);
//        }
    }

    public Context getContext() {
        return itemView.getContext();
    }

    public void setAdapter(AfRecyclerAdapter adapter) {
        this.adapter = adapter;
    }

    public AfRecyclerAdapter getAdapter() {
        return adapter;
    }

    public void setOnClickListener(AfRecyclerAdapter.OnItemClickListener listener) {
        this.mOnClickListener = listener;
        itemView.setOnClickListener(this);
    }

    public void setOnLongClickListener(AfRecyclerAdapter.OnItemLongClickListener listener) {
        this.mOnLongClickListener = listener;
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (mOnClickListener != null) {
            mOnClickListener.onItemClick(getAdapter(), v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnLongClickListener != null) {
            mOnLongClickListener.onItemLongClick(getAdapter(), v, getLayoutPosition());
        }
        return true;
    }

    private boolean hasOnClickListener(View view) {
        if (Build.VERSION.SDK_INT >= 15) {
            return view.hasOnClickListeners();
        }
        try {
            Field f = View.class.getDeclaredField("mListenerInfo");
            f.setAccessible(true);
            Object li = f.get(view);
            if (li != null) {
                f = li.getClass().getDeclaredField("mOnClickListener");
                f.setAccessible(true);
                Object l = f.get(li);
                return l != null;
            }
        } catch (Exception e) {
            Log.e("AfViewHolder", "can't detect OnClickListener under API 15", e);
        }
        return false;
    }

    private boolean hasOnLongClickListener(View view) {
        try {
            Field f = View.class.getDeclaredField("mListenerInfo");
            f.setAccessible(true);
            Object li = f.get(view);
            if (li != null) {
                f = li.getClass().getDeclaredField("mOnLongClickListener");
                f.setAccessible(true);
                Object l = f.get(li);
                return l != null;
            }
        } catch (Exception e) {
            Log.e("AfViewHolder", "can't detect OnLongClickListener", e);
        }
        return false;
    }
}
