/*
 * Copyright (C) 2006-2015 li.jamling@gmail.com rights reserved
 */
package cn.ieclipse.af.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPager的抽象Adapter
 *
 * @Author lijiaming2
 * @Date 2015/5/14
 */
public abstract class AfPagerAdapter<T> extends PagerAdapter {

    protected Context mContext;
    protected List<T> mDataList;


    public AfPagerAdapter(Context context) {
        this.mContext = context;
    }

    public abstract int getLayout();

    public abstract void onUpdateView(View convertView, int position);

    // 以下方法均已实现
    public void setDataList(List<T> list) {
        if (list == null) {
            this.mDataList = new ArrayList<T>(0);
        } else {
            this.mDataList = list;
        }
        if (this.mDataList == null) {
            this.mDataList = new ArrayList<T>(0);
        }
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        }
    }

    public T getItem(int position) {
        if (mDataList == null) {
            return null;
        }
        return mDataList.get(position);
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(container.getContext(), getLayout(), null);
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        onUpdateView(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
