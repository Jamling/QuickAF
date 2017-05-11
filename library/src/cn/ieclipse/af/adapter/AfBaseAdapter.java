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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.common.Logger;

/**
 * BaseAdapter with {@link java.util.List} for {@link android.widget.AdapterView}
 *
 * @author Jamling
 */
public abstract class AfBaseAdapter<T> extends BaseAdapter {
    protected Logger mLogger = Logger.getLogger(getClass());
    private AfDataHolder<T> mDataHolder = new AfDataHolder<>();
    protected List<T> mCheckedItems;
    protected T mCheckedItem;

    public AfBaseAdapter() {
        setDataCheck(AfDataHolder.CHECK_BOTH);
    }

    public abstract int getLayout();

    public abstract void onUpdateView(View convertView, int position);

    public void setDataCheck(int checkMode) {
        mDataHolder.setDataCheck(checkMode);
    }

    public int getDataCheck() {
        return mDataHolder.getDataCheck();
    }

    public void clear() {
        mDataHolder.clear();
    }

    public void addAll(List<T> list) {
        mDataHolder.addAll(list);
    }

    public void addAll2Top(List<T> list) {
        mDataHolder.addAll2Top(list);
    }

    public void add2Top(T t) {
        mDataHolder.add2Top(t);
    }

    public void add2Top(List<T> list) {
        mDataHolder.addAll2Top(list);
    }

    public void add(T t) {
        mDataHolder.add(t);
    }

    public int getIndexOf(T t) {
        return mDataHolder.getIndexOf(t);
    }

    public T deleteItem(int position) {
        return mDataHolder.remove(position);
    }

    public void deleteItem(T t) {
        int index = getIndexOf(t);
        if (index >= 0) {
            deleteItem(index);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null && getLayout() > 0) {
            // convertView = View.inflate(parent.getContext(), getLayout(), parent);
            convertView = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
        }
        try {
            onUpdateView(convertView, position);
        } catch (Exception e) {
            mLogger.e("exception onUpdateView", e);
        }
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public T getItem(int position) {
        return mDataHolder.getItem(position);
    }

    @Override
    public int getCount() {
        return mDataHolder.getCount();
    }

    public List<T> getDataList() {
        return mDataHolder.getDataList();
    }

    public void setDataList(List<T> list) {
        mDataHolder.setDataList(list);
    }

    public void setItemChecked(int position, boolean value) {
        if (mCheckedItems == null) {
            mCheckedItems = new ArrayList<>();
        }
        T obj = getItem(position);
        if (value) {
            if (!mCheckedItems.contains(obj)) {
                mCheckedItems.add(obj);
            }
        }
        else {
            mCheckedItems.remove(obj);
        }

        mCheckedItem = obj;
    }

    public T getCheckedItem() {
        return mCheckedItem;
    }

    public List<T> getCheckedItems() {
        if (mCheckedItems == null) {
            mCheckedItems = new ArrayList<>(0);
        }
        return mCheckedItems;
    }

    public void clearChoices() {
        mCheckedItem = null;
        if (mCheckedItems != null) {
            mCheckedItems.clear();
        }
    }
}
