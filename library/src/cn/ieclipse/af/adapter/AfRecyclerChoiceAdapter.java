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

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.widget.Checkable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description
 *
 * @author Jamling
 */
public abstract class AfRecyclerChoiceAdapter<T> extends AfRecyclerAdapter<T> {
    /**
     * Normal list that does not indicate choices
     */
    public static final int CHOICE_MODE_NONE = 0;

    /**
     * The list allows up to one choice
     */
    public static final int CHOICE_MODE_SINGLE = 1;

    /**
     * The list allows multiple choices
     */
    public static final int CHOICE_MODE_MULTIPLE = 2;

    private SparseBooleanArray mSelections = new SparseBooleanArray();
    private int mCheckedItemCount;
    private int mChoiceMode = CHOICE_MODE_NONE;
    private Map<Integer, WeakReference<RecyclerView.ViewHolder>> mHolderMap;

    protected List<T> mCheckedItems;
    protected T mCheckedItem;

    public AfRecyclerChoiceAdapter(Context context) {
        super(context);
    }

    /**
     * Defines the choice behavior for the List. By default, Lists do not have any choice behavior
     * ({@link #CHOICE_MODE_NONE}). By setting the choiceMode to {@link #CHOICE_MODE_SINGLE}, the
     * List allows up to one item to  be in a chosen state. By setting the choiceMode to
     * {@link #CHOICE_MODE_MULTIPLE}, the list allows any number of items to be chosen.
     *
     * @param choiceMode One of {@link #CHOICE_MODE_NONE}, {@link #CHOICE_MODE_SINGLE}, or
     *                   {@link #CHOICE_MODE_MULTIPLE}
     */
    public void setChoiceMode(int choiceMode) {
        mChoiceMode = choiceMode;
        if (mChoiceMode != CHOICE_MODE_NONE) {
            if (mSelections == null) {
                mSelections = new SparseBooleanArray(0);
            }
        }
    }

    /**
     * Sets the checked state of the specified position. The is only valid if
     * the choice mode has been set to {@link #CHOICE_MODE_SINGLE} or
     * {@link #CHOICE_MODE_MULTIPLE}.
     *
     * @param position The item whose checked state is to be checked
     * @param value    The new checked state for the item
     */
    public void setItemChecked(int position, boolean value) {
        if (mChoiceMode == CHOICE_MODE_NONE) {
            return;
        }

        if (mChoiceMode == CHOICE_MODE_SINGLE) {
            clearChoices();
            mSelections.put(position, value);
        }
        else if (mChoiceMode == CHOICE_MODE_MULTIPLE) {
            mSelections.put(position, value);
        }
        updateCheckedHolder(position, value);
        T obj = getItem(position);

//        // save checked data
//        if (mCheckedItems == null) {
//            mCheckedItems = new ArrayList<>();
//        }
//
//        if (value) {
//            if (!mCheckedItems.contains(obj)) {
//                mCheckedItems.add(obj);
//                mCheckedItemCount++;
//            }
//        }
//        else {
//            if (mCheckedItems.remove(obj)) {
//                mCheckedItemCount--;
//            }
//        }

        mCheckedItem = obj;
    }

    public void toggleItemChecked(int position) {
        if (mChoiceMode == CHOICE_MODE_NONE) {
            return;
        }
        boolean old = mSelections.get(position);
        setItemChecked(position, !old);
    }

    /**
     * Returns the number of items currently selected. This will only be valid
     * if the choice mode is not {@link #CHOICE_MODE_NONE} (default).
     * <p>To determine the specific items that are currently selected, use one of
     * the <code>getChecked*</code> methods.
     *
     * @return The number of items currently selected
     */
    public int getCheckedItemCount() {
        mCheckedItemCount = 0;
        if (mSelections == null) {
            return mCheckedItemCount;
        }
        else {
            int size = mSelections.size();
            for (int i = 0; i < size; i++) {
                if (mSelections.valueAt(i)) {
                    mCheckedItemCount++;
                }
            }
        }
        return mCheckedItemCount;
    }

    /**
     * Clear any choices previously set
     */
    public void clearChoices() {
        if (mSelections != null) {
            int size = mSelections.size();
            for (int i = 0; i < size; i++) {
                int position = mSelections.keyAt(i);
                setItemChecked(position, false);
            }
            mSelections.clear();
        }

        mCheckedItemCount = 0;
        mCheckedItem = null;
        if (mCheckedItems != null) {
            mCheckedItems.clear();
        }
    }

//    /**
//     * Returns the set of checked items in the list. The result is only valid if
//     * the choice mode has not been set to {@link #CHOICE_MODE_NONE}.
//     *
//     * @return A SparseBooleanArray which will return true for each call to
//     * get(int position) where position is a checked position in the
//     * list and false otherwise, or <code>null</code> if the choice
//     * mode is set to {@link #CHOICE_MODE_NONE}.
//     */
//    public SparseBooleanArray getCheckedItemPositions() {
//        return mSelections;
//    }

    public Integer[] getCheckedPositions() {
        List<Integer> checked = new ArrayList<>();
        if (mSelections != null) {
            int size = mSelections.size();
            for (int i = 0; i < size; i++) {
                int position = mSelections.keyAt(i);
                if (mSelections.valueAt(i)) {
                    checked.add(position);
                }
            }
        }
        return checked.toArray(new Integer[checked.size()]);
    }

    public T getCheckedItem() {
        return mCheckedItem;
    }

    public List<T> getCheckedItems() {
        if (mCheckedItems == null) {
            mCheckedItems = new ArrayList<>(0);
        }
        mCheckedItems.clear();
        for(int i : getCheckedPositions()){
            mCheckedItems.add(getItem(i));
        }
        return mCheckedItems;
    }

    /**
     * Returns the checked state of the specified position. The result is only
     * valid if the choice mode has been set to {@link #CHOICE_MODE_SINGLE}
     * or {@link #CHOICE_MODE_MULTIPLE}.
     *
     * @param position The item whose checked state to return
     *
     * @return The item's checked state or <code>false</code> if choice mode
     * is invalid
     * @see #setChoiceMode(int)
     */
    public boolean isItemChecked(int position) {
        if (mChoiceMode != CHOICE_MODE_NONE && mSelections != null) {
            return mSelections.get(position);
        }

        return false;
    }

    private void updateCheckedHolder(int position, boolean check) {
        if (mHolderMap != null) {
            WeakReference<RecyclerView.ViewHolder> holder = mHolderMap.get(position);
            if (holder != null) {
                RecyclerView.ViewHolder viewHolder = holder.get();
                if (viewHolder != null) {
                    if (viewHolder.itemView instanceof Checkable) {
                        ((Checkable) viewHolder.itemView).setChecked(check);
                    }
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (mHolderMap == null) {
            mHolderMap = new HashMap<>();
        }
        mHolderMap.put(position, new WeakReference<>(viewHolder));
        super.onBindViewHolder(viewHolder, position);
        boolean select = mSelections.get(position);
        if (viewHolder.itemView instanceof Checkable) {
            ((Checkable) viewHolder.itemView).setChecked(select);
        }
    }
}
