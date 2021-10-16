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
package cn.ieclipse.af.adapter.delegate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;

import cn.ieclipse.af.adapter.AfRecyclerAdapter;
import cn.ieclipse.af.adapter.AfViewHolder;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class AdapterDelegate<T> implements Comparable<AdapterDelegate> {
    protected int viewType;
    private LayoutInflater layoutInflater;
    private AfRecyclerAdapter<T> adapter;
    private RecyclerView.ViewHolder viewHolder;

    public AdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    public AdapterDelegate() {
        this(0);
    }

    void setViewType(int viewType) {
        this.viewType = viewType;
    }

    int getViewType() {
        return viewType;
    }

    void setAdapter(AfRecyclerAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public AfRecyclerAdapter<T> getAdapter() {
        return adapter;
    }

    public RecyclerView.ViewHolder getViewHolder() {
        return viewHolder;
    }

    public boolean isForViewType(T info, int position) {
        return true;
    }

    public Class<? extends RecyclerView.ViewHolder> getViewHolderClass() {
        return AfViewHolder.class;
    }

    public abstract int getLayout();

    public abstract void onUpdateView(RecyclerView.ViewHolder holder, T info, int position);

    public void onBindViewHolder(T info, int position, RecyclerView.ViewHolder holder) {
        onUpdateView(holder, info, position);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        int layout = getLayout();
        View view = null;
        if (layout > 0) {
            view = layoutInflater.inflate(layout, parent, false);
        }
        viewHolder = instanceViewHolder(view);
        if (viewHolder != null && viewHolder instanceof AfViewHolder) {
            ((AfViewHolder) viewHolder).setAdapter(getAdapter());
        }
        return viewHolder;
    }

    /**
     * Instance ViewHolder with reflection, you can override to new concrete ViewHolder
     *
     * @param itemView item view of ViewHolder
     * @return ViewHolder
     * @see #onCreateViewHolder(android.view.ViewGroup)
     * @see #getViewHolderClass()
     * @since 3.0.1
     */
    protected RecyclerView.ViewHolder instanceViewHolder(View itemView) {
        Class<?> cls = getViewHolderClass();
        if (cls != null) {
            try {
                Constructor c = cls.getConstructor(View.class);
                c.setAccessible(true);
                return (RecyclerView.ViewHolder) c.newInstance(itemView);
            } catch (Exception e) {
                String msg = String.format("Can't instance ViewHolder(%s) in %s, is it an assessable (public/static) "
                    + "class? \nPlease see more info in https://github.com/Jamling/QuickAF/issues/41\n root cause "
                    + "message: %s", cls, getClass(), e.getMessage());
                throw new IllegalAccessError(msg);
            }
        }
        return null;
    }

    @Override
    public int compareTo(AdapterDelegate delegate) {
        return viewType - delegate.viewType;
    }
}
