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

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年7月7日
 */
public abstract class AfBaseAdapter<T> extends BaseAdapter {
    public static final int CHECK_NONE = 0x00;
    public static final int CHECK_TOP = 0x01;
    public static final int CHECK_TAIL = 0x02;
    public static final int CHECK_BOTH = 0x03;
    //    protected Context mContext;
    protected List<T> mDataList;
    protected int mDataCheck = CHECK_NONE;

//    public AbBaseAdapter(Context context) {
//        this.mContext = context;
//    }

    public abstract int getLayout();

    public abstract void onUpdateView(View convertView, int position);

    public void setDataCheck(int checkMode) {
        this.mDataCheck = checkMode;
    }

    public int getDataCheck() {
        return mDataCheck;
    }

    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        } else {
            checkDataList();
        }
    }

    public void addAll(List<T> list) {
        checkDataList();
        if (list != null) {
            if ((mDataCheck & CHECK_TAIL) == CHECK_TAIL) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    T t = list.get(i);
                    int idx = mDataList.indexOf(t);
                    if (idx >= 0) {
                        mDataList.set(idx, t);
                    } else {
                        mDataList.add(t);
                    }
                }
            } else {
                mDataList.addAll(list);
            }
        }
    }

    public void addAll2Top(List<T> list) {
        checkDataList();
        if (list != null) {
            if ((mDataCheck & CHECK_TOP) == CHECK_TOP) {
                int size = list.size();
                for (int i = size - 1; i > 0; i--) {
                    T t = list.get(i);
                    int idx = mDataList.indexOf(t);
                    if (idx >= 0) {
                        mDataList.set(idx, t);
                    } else {
                        mDataList.add(0, t);
                    }
                }
            } else {
                mDataList.addAll(0, list);
            }
        }
    }

    public void add2Top(T t) {
        checkDataList();
        if (t != null) {
            if ((mDataCheck & CHECK_TOP) == CHECK_TOP) {
                int idx = mDataList.indexOf(t);
                if (idx >= 0) {
                    mDataList.set(idx, t);
                    return;
                }
            }
            mDataList.add(0, t);
        }
    }

    public void add(T t) {
        checkDataList();
        if (t != null) {
            if ((mDataCheck & CHECK_TAIL) == CHECK_TAIL) {
                int idx = mDataList.indexOf(t);
                if (idx >= 0) {
                    mDataList.set(idx, t);
                    return;
                }
            }
            mDataList.add(t);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(parent.getContext(), getLayout(), null);
        }
        onUpdateView(convertView, position);
        return convertView;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public T getItem(int pos) {
        if (pos >= 0 && pos < mDataList.size()) {
            return mDataList.get(pos);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mDataList != null ? mDataList.size() : 0;
    }

    public List<T> getDataList() {
        checkDataList();
        return mDataList;
    }

    public void setDataList(List<T> list) {
        this.mDataList = list;
        checkDataList();
    }

    private void checkDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>(0);
        }
    }
}
