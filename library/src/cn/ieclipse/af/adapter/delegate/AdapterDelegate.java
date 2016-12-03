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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
            view = layoutInflater.inflate(getLayout(), parent, false);
        }
        return instanceViewHolder(view);
    }

    private RecyclerView.ViewHolder instanceViewHolder(View itemView) {
        Class<?> cls = getViewHolderClass();
        if (cls != null) {
            try {
                Constructor c = cls.getConstructor(View.class);
                return (RecyclerView.ViewHolder) c.newInstance(itemView);
            } catch (Exception e) {
                throw new NullPointerException("Can't create ViewHolder in " + getClass());
            }
        }
        return null;
    }

    @Override
    public int compareTo(AdapterDelegate delegate) {
        return viewType - delegate.viewType;
    }
}
