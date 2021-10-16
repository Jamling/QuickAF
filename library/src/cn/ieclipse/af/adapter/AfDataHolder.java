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

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter data holder.
 * <p>
 * The data holder will check your data when the check mode set, if the {@link #equals(Object)} of data object return
 * true, the data regards as has been exists in the holder, and will not add to the holder.
 * </p>
 * <ul>
 * <li>{@link #CHECK_NONE} <strong>(default)</strong>: do not check
 * </li>
 * <li>{@link #CHECK_TOP}: check data whether exist when add data to holder top
 * </li>
 * <li>{@link #CHECK_TAIL}: check data whether exist when add data to holder
 * tail
 * </li>
 * <li>{@link #CHECK_BOTH}: check data whether exist when add data to both top
 * and tail
 * </li>
 * </ul>
 *
 * @author Jamling
 */
public class AfDataHolder<T> {
    /**
     * Default mode, don't check when add data
     */
    public static final int CHECK_NONE = 0x00;
    /**
     * Check when add data to top
     */
    public static final int CHECK_TOP = 0x01;
    /**
     * Check when add data to tail
     */
    public static final int CHECK_TAIL = 0x02;
    /**
     * Check when add data (both from top and tail)
     */
    public static final int CHECK_BOTH = 0x03;

    protected int mDataCheck = CHECK_NONE;
    protected List<T> mDataList;
    protected int mSize = 0;

    /**
     * Set data check mode, combined value of {@link #CHECK_NONE}
     *
     * @param checkMode data check mode
     */
    public void setDataCheck(int checkMode) {
        this.mDataCheck = checkMode;
    }

    /**
     * Get data check mode
     *
     * @return check mode
     * @see #setDataCheck(int)
     */
    public int getDataCheck() {
        return mDataCheck;
    }

    /**
     * Clear the data, the holder will be empty
     */
    public void clear() {
        if (mDataList != null) {
            mDataList.clear();
        } else {
            checkDataList();
        }
        mSize = 0;
    }

    /**
     * Add data to tail
     *
     * @param list data collection
     */
    public void addAll(List<T> list) {
        checkDataList();
        if (list != null) {
            if ((mDataCheck & CHECK_TAIL) == CHECK_TAIL) {
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    T t = list.get(i);
                    int idx = getIndexOf(t);
                    if (idx >= 0) {
                        mDataList.set(idx, t);
                    } else {
                        mDataList.add(t);
                    }
                }
            } else {
                mDataList.addAll(list);
            }
            mSize = mDataList.size();
        }
    }

    /**
     * Add data to top
     *
     * @param list data collection
     */
    public void addAll2Top(List<T> list) {
        checkDataList();
        if (list != null) {
            if ((mDataCheck & CHECK_TOP) == CHECK_TOP) {
                int size = list.size();
                for (int i = size - 1; i > 0; i--) {
                    T t = list.get(i);
                    int idx = getIndexOf(t);
                    if (idx >= 0) {
                        mDataList.set(idx, t);
                    } else {
                        mDataList.add(0, t);
                    }
                }
            } else {
                mDataList.addAll(0, list);
            }
            mSize = mDataList.size();
        }
    }

    /**
     * Add to top
     *
     * @param t data object
     */
    public void add2Top(T t) {
        checkDataList();
        if (t != null) {
            if ((mDataCheck & CHECK_TOP) == CHECK_TOP) {
                int idx = getIndexOf(t);
                if (idx >= 0) {
                    mDataList.set(idx, t);
                    return;
                }
            }
            mDataList.add(0, t);
            mSize += 1;
        }
    }

    /**
     * Adds the specified object at the end of this holder
     *
     * @param t data object
     */
    public void add(T t) {
        checkDataList();
        if (t != null) {
            if ((mDataCheck & CHECK_TAIL) == CHECK_TAIL) {
                int idx = getIndexOf(t);
                if (idx >= 0) {
                    mDataList.set(idx, t);
                    return;
                }
            }
            mDataList.add(t);
            mSize += 1;
        }
    }

    public int getIndexOf(T t) {
        if (mDataList == null || mDataList.isEmpty()) {
            return -1;
        }
        return mDataList.indexOf(t);
    }

    /**
     * Remove specified position object data
     *
     * @param position data position of this holder
     * @return the removed object
     */
    public T remove(int position) {
        T ret = null;
        checkDataList();
        if (position >= 0 && position < mSize) {
            ret = mDataList.remove(position);
            mSize -= 1;
        }
        return ret;
    }

    /**
     * Get this holder size
     *
     * @return count of data
     */
    public int getCount() {
        return mSize;
    }

    /**
     * Return the item of position
     *
     * @param position data position
     * @return null if position out of range
     */
    public T getItem(int position) {
        if (position >= 0 && position < mSize) {
            return mDataList.get(position);
        }
        return null;
    }

    /**
     * Get the holder List
     *
     * @return List
     */
    public List<T> getDataList() {
        checkDataList();
        return mDataList;
    }

    /**
     * Set List data to this holder
     *
     * @param list data list
     */
    public void setDataList(List<T> list) {
        this.mDataList = list;
        checkDataList();
        mSize = mDataList.size();
    }

    private void checkDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>(0);
        }
    }
}
