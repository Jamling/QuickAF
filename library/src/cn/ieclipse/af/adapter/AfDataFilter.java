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
package cn.ieclipse.af.adapter;

import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

/**
 * Filter for adapter
 *
 * @author Jamling
 */
public abstract class AfDataFilter<T> extends Filter {
    private AfBaseAdapter<T> baseAdapter;
    private AfRecyclerAdapter<T> recyclerAdapter;
    protected List<T> mResultList;
    protected List<T> mSourceList;

    public void setAdapter(AfBaseAdapter<T> adapter) {
        this.baseAdapter = adapter;
    }

    public void setAdapter(AfRecyclerAdapter<T> adapter) {
        this.recyclerAdapter = adapter;
    }

    public void setSourceList(List<T> sourceList) {
        this.mSourceList = sourceList;
    }

    public List<T> getSourceList() {
        return mSourceList;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if (mResultList == null) {
            mResultList = new ArrayList<>();
        }
        else {
            mResultList.clear();
        }
        if (mSourceList != null) {
            doFilter(charSequence);
        }
        results.values = mResultList;
        return results;
    }

    /**
     * Do filter process, add data from {@link #mSourceList} to {@link #mResultList}
     *
     * @param charSequence inputted text
     */
    protected abstract void doFilter(CharSequence charSequence);

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
        List<T> list = (List<T>) filterResults.values;
        if (baseAdapter != null) {
            baseAdapter.setDataList(list);
            baseAdapter.notifyDataSetChanged();
        }
        if (recyclerAdapter != null) {
            recyclerAdapter.setDataList(list);
        }
    }
}
