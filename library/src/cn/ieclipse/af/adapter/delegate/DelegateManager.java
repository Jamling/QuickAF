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

import java.util.ArrayList;
import java.util.List;

/**
 * <em>Composite is superior to inherit</em>. refer to {@link https://github.com/sockeqwe/AdapterDelegates/}
 *
 * @author Jamling
 */
public class DelegateManager<T> {
    private final List<AdapterDelegate> mDelegates;
    private int count;
    private int headerCount;
    private int footerCount;

    private static final int MAX_HEADER_COUNT = 2;

    public DelegateManager() {
        mDelegates = new ArrayList<>();
    }

    public void addDelegate(int viewType, AdapterDelegate delegate) {
        delegate.setViewType(viewType);
        mDelegates.add(viewType, delegate);
        count++;
        if (delegate.getViewType() < 0) {
            if (delegate.getViewType() < -MAX_HEADER_COUNT) {
                footerCount++;
            }
            else {
                headerCount++;
                if (headerCount > MAX_HEADER_COUNT) {
                    throw new IllegalStateException("exceed the max header count(" + MAX_HEADER_COUNT + ")");
                }
            }
        }
    }

    public int getHeaderCount() {
        return headerCount;
    }

    public int getFooterCount() {
        return footerCount;
    }

    public int getCount() {
        return count;
    }

    public AdapterDelegate<T> getDelegateForViewType(int viewType) {
        for (int i = 0; i < count; i++) {
            AdapterDelegate delegate = mDelegates.get(i);
            boolean match = delegate.getViewType() == viewType;
            if (match) {
                return delegate;
            }
        }
        return null;
    }

    public int getItemViewType(T info, int position) {
        for (int i = 0; i < count; i++) {
            AdapterDelegate delegate = mDelegates.get(i);
            boolean match = (delegate.isForViewType(info, position));
            if (match) {
                return delegate.getViewType();
            }
        }
        return 0;
    }

    public void onBindViewHolder(T info, int position, RecyclerView.ViewHolder holder) {
        int viewType = holder.getItemViewType();
        AdapterDelegate delegate = getDelegateForViewType(viewType);
        if (delegate != null) {
            delegate.onBindViewHolder(info, position, holder);
        }
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDelegate<T> delegate = getDelegateForViewType(viewType);
        if (delegate == null) {
            throw new NullPointerException("No AdapterDelegate added for ViewType " + viewType);
        }

        RecyclerView.ViewHolder vh = delegate.onCreateViewHolder(parent);
        if (vh == null) {
            throw new NullPointerException(
                "ViewHolder returned from AdapterDelegate " + delegate + " for ViewType =" + viewType + " is null!");
        }
        return vh;
    }
}
