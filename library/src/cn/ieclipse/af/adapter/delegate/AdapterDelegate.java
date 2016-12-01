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
import android.view.ViewGroup;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class AdapterDelegate<T> {
    protected int viewType;

    public AdapterDelegate(int viewType) {
        this.viewType = viewType;
    }

    public AdapterDelegate() {

    }

    void setViewType(int viewType) {
        this.viewType = viewType;
    }

    int getViewType() {
        return viewType;
    }

    public abstract <T> boolean isForViewType(T info, int position);

    public abstract <T> void onBindViewHolder(T info, int position, RecyclerView.ViewHolder holder);

    public abstract RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent);
}
