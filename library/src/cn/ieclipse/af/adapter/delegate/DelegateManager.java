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

import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import cn.ieclipse.af.adapter.AfRecyclerAdapter;

/**
 * <em>Composite is superior to inherit</em>. refer to
 * <a href="https://github.com/sockeqwe/AdapterDelegates/">AdapterDelegates</a>
 * <p>
 *     Here is a minimized implementation of Adapter delegate.
 * </p>
 * @author Jamling
 */
public class DelegateManager<T> {
    private AfRecyclerAdapter<T> adapter;
    private final List<AdapterDelegate> delegates;
    private int count;
    private int headerCount;
    private int footerCount;

    private static final int MAX_HEADER_COUNT = 2;

    public DelegateManager(@NonNull AfRecyclerAdapter<T> adapter) {
        delegates = new ArrayList<>();
        this.adapter = adapter;
    }

    public void addDelegate(@NonNull AdapterDelegate delegate) {
        delegate.setAdapter(adapter);
        delegates.add(delegate);
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
        Collections.sort(delegates);
    }

    public void addDelegate(int viewType, @NonNull AdapterDelegate delegate) {
        delegate.setViewType(viewType);
        addDelegate(delegate);
    }

    public void removeDelegate(int viewType) {
        AdapterDelegate delegate = getDelegateForViewType(viewType);
        if (delegate != null) {
            if (delegates.remove(delegate)) {
                if (viewType < 0) {
                    if (viewType < -MAX_HEADER_COUNT) {
                        footerCount--;
                    }
                    else {
                        headerCount--;
                    }
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
            AdapterDelegate delegate = delegates.get(i);
            boolean match = delegate.getViewType() == viewType;
            if (match) {
                return delegate;
            }
        }
        return null;
    }

    public int getItemViewType(T info, int position) {
        for (int i = 0; i < count; i++) {
            AdapterDelegate delegate = delegates.get(i);
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
