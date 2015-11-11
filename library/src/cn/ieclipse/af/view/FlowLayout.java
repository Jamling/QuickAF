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
package cn.ieclipse.af.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 流式布局，支持子view水平及垂直方向的间距（horizontalSpacing and verticalSpacing）, 支持子View
 * margin, 支持子view gravity，支持divider，支持choice mode.
 *
 * @author Jamling
 * @date 2015/7/10.
 */
public class FlowLayout extends LinearLayout {
    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    private int mNumColumns = -1;
    private int mMaxChildWidth = 0;
    private int mDividerHeight = 1;

    private final List<List<View>> mLines = new ArrayList<List<View>>();
    private final List<Integer> mLineHeights = new ArrayList<Integer>();
    private final List<Rect> mDividersPos = new ArrayList<Rect>();

    public FlowLayout(Context context) {
        this(context, null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        if (attrs == null) {
            return;
        }
        int[] attr
            = {android.R.attr.gravity, android.R.attr.horizontalSpacing, android.R.attr.verticalSpacing, android.R
            .attr.numColumns, android.R.attr.dividerHeight, android.R.attr.choiceMode};
        TypedArray a = context.obtainStyledAttributes(attrs, attr);

        try {
            int index = a.getInt(0, -1);
            if (index > 0) {
                setGravity(index);
            }
            mHorizontalSpacing = a.getDimensionPixelOffset(1, 0);
            mVerticalSpacing = a.getDimensionPixelOffset(2, 0);
            mNumColumns = a.getInt(3, 0);
            mDividerHeight = a.getDimensionPixelOffset(4, mDividerHeight);
            mSelectionMode = a.getInt(5, mSelectionMode);
        } finally {
            a.recycle();
        }
    }

    private void initLines() {
        mLines.clear();
        mLineHeights.clear();
        if (getChildCount() > 0) {
            mLines.add(new ArrayList<View>());
            mLineHeights.add(0);
        }
    }

    private List<View> getCurrentLine() {
        int size = mLines.size();
        return mLines.get(size - 1);
    }

    /**
     * Set number of columns, the result will cause FlowLayout likes GridView
     *
     * @param numColumns
     */
    public void setNumColumns(int numColumns) {
        this.mNumColumns = numColumns;
    }

    /**
     * Set horizontal spacing between child view.
     *
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing) {
        this.mHorizontalSpacing = horizontalSpacing;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        assert (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.UNSPECIFIED);
        final int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count = getChildCount();
        int lineHeight = 0;
        int x = getPaddingLeft();
        int y = getPaddingTop();

        initLines();

        if (mNumColumns > 0) {
            mMaxChildWidth = (width - mHorizontalSpacing * (mNumColumns - 1)) / mNumColumns;
        }

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();

                child.measure(getChildWidthMeasureSpec(child, width), getChildHeightMeasureSpec(child, height));
                int cw = child.getMeasuredWidth();
                int ch = child.getMeasuredHeight();

                cw += lp.leftMargin + lp.rightMargin;
                ch += lp.topMargin + lp.bottomMargin;
                lineHeight = Math.max(lineHeight, ch);

                if (mNumColumns > 0) {
                    cw = mMaxChildWidth;
                }
                if (x + cw > width + getPaddingLeft()) {
                    x = getPaddingLeft();
                    y += lineHeight;
                    y += mVerticalSpacing;
                    lineHeight = ch;
                    mLines.add(new ArrayList<View>());
                    mLineHeights.add(lineHeight);
                }

                x += cw + mHorizontalSpacing;
//                if ((getShowDividers() & LinearLayout.SHOW_DIVIDER_MIDDLE) != 0) {
//                    x += getDividerWidthReflect();
//                }
                getCurrentLine().add(child);
                mLineHeights.set(mLines.size() - 1, lineHeight);
            }
        }
        y += getPaddingBottom();

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = y + lineHeight;
        }
        else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (y + lineHeight < height) {
                height = y + lineHeight;
            }
        }
        setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), height);
    }

    private int getChildWidthMeasureSpec(View child, int width) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        // default wrap_content
        int childWidthMeasureSpec;
        int cw;
        if (mNumColumns <= 0) {
            cw = width - lp.leftMargin - lp.rightMargin;
        }
        else {
            cw = mMaxChildWidth - lp.leftMargin - lp.rightMargin;
        }
        if (lp.width > 0) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(lp.width, MeasureSpec.EXACTLY);
        }
        else if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.EXACTLY);
        }
        else {
            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(cw, MeasureSpec.AT_MOST);
        }
        return childWidthMeasureSpec;
    }

    private int getChildHeightMeasureSpec(View child, int height) {
        int childHeightMeasureSpec;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        if (lp.height > 0) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(lp.height, MeasureSpec.EXACTLY);
        }
        else if (lp.height == LayoutParams.MATCH_PARENT) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        }
        else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        return childHeightMeasureSpec;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDividersPos.clear();
        int lineIndex = 0;
        int x = getPaddingLeft();
        int y = getPaddingTop();
        for (List<View> line : mLines) {
            if (line != null && !line.isEmpty()) {

                int maxLineHeight = mLineHeights.get(lineIndex);
                int count = line.size();
                for (int i = 0; i < count; i++) {
                    View child = line.get(i);
                    if (child != null && child.getVisibility() != GONE) {
                        LayoutParams lp = (LayoutParams) child.getLayoutParams();
                        int cw = child.getMeasuredWidth();
                        int sw = cw + lp.leftMargin + lp.rightMargin;
                        int ch = child.getMeasuredHeight();
                        int sh = ch + lp.topMargin + lp.bottomMargin;
                        int offsetY = 0;
                        int offsetX = 0;
                        int gravity = getGravityReflect() & Gravity.VERTICAL_GRAVITY_MASK;
                        if (maxLineHeight > 0) {
                            if (gravity == Gravity.CENTER_VERTICAL) {
                                offsetY = (maxLineHeight - sh) / 2;
                            }
                            else if (gravity == Gravity.BOTTOM) {
                                offsetY = maxLineHeight - sh;
                            }
                        }
                        if (offsetY < 0) {
                            offsetY = 0;
                        }

                        if (mMaxChildWidth > 0) {
                            gravity = getGravityReflect() & Gravity.HORIZONTAL_GRAVITY_MASK;
                            if (gravity == Gravity.CENTER_HORIZONTAL) {
                                offsetX = (mMaxChildWidth - sw) / 2;
                            }
                            else if (gravity == Gravity.RIGHT) {
                                offsetX = mMaxChildWidth - sw;
                            }
                            if (offsetX < 0) {
                                offsetX = 0;
                            }
                        }
                        int xx = x + offsetX + lp.leftMargin;
                        int yy = y + offsetY + lp.topMargin;

                        child.layout(xx, yy, xx + cw, yy + ch);
                        if (mMaxChildWidth > 0) {
                            x += mMaxChildWidth + mHorizontalSpacing;
                        }
                        else {
                            x += sw + mHorizontalSpacing;
                        }
                        if (i < count - 1) {
                            mDividersPos.add(new Rect(x - mHorizontalSpacing, y, x, maxLineHeight + y));
                        }
                        else if (i < mNumColumns - 1) {
                            if ((getShowDividers() & LinearLayout.SHOW_DIVIDER_END) != 0) {
                                mDividersPos.add(new Rect(x - mHorizontalSpacing, y, x, maxLineHeight + y));
                            }
                        }
                    }
                }
                y += maxLineHeight + mVerticalSpacing;
                x = getPaddingLeft();
            }
            lineIndex++;
        }
    }

    private int getGravityReflect() {
        int ret = Gravity.LEFT | Gravity.START;
        try {
            Class<?> clazz = getClass().getSuperclass();
            Field f = clazz.getDeclaredField("mGravity");
            f.setAccessible(true);
            ret = f.getInt(this);
        } catch (Exception e) {

        }
        return ret;
    }

    private int getDividerWidthReflect() {
        int ret = 0;
        try {
            Class<?> clazz = getClass().getSuperclass();
            Method m = clazz.getDeclaredMethod("getDividerWidth");
            ret = (Integer) m.invoke(this);
        } catch (Exception e) {

        }
        return ret;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public Drawable getDividerDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return super.getDividerDrawable();
        }
        else {
            try {
                Class<?> clazz = getClass().getSuperclass();
                Method m = clazz.getDeclaredMethod("getDividerDrawable");
                return (Drawable) m.invoke(this);
            } catch (Exception e) {
                return null;
            }
        }
    }

    void drawVerticalDivider(Canvas canvas, Rect r) {
        Drawable d = getDividerDrawable();
        int w = getDividerWidthReflect();
        if (d instanceof ColorDrawable) {
            w = mDividerHeight;
        }
        int left = (mHorizontalSpacing - w) / 2;
        if (left < 0) {
            left = 0;
            w = mHorizontalSpacing;
        }
        left = r.left + left;
        d.setBounds(left, r.top + getDividerPadding(), left + w, r.bottom - getDividerPadding());
        d.draw(canvas);
    }

    void drawDivider(Canvas canvas) {
        for (Rect p : mDividersPos) {
            drawVerticalDivider(canvas, p);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        Drawable d = getDividerDrawable();
        if (d != null) {
            drawDivider(canvas);
        }
    }

    // -----------> selection
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener = new PassThroughHierarchyChangeListener();
    private int mSelectionMode = ListView.CHOICE_MODE_NONE;

    private boolean isSingleChoice() {
        return mSelectionMode == ListView.CHOICE_MODE_SINGLE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (isSingleChoice()) {
            // checks the appropriate radio button as requested in the XML file
            if (mCheckedId != -1) {
                mProtectFromCheckedChange = true;
                setCheckedStateForView(mCheckedId, true);
                mProtectFromCheckedChange = false;
                setCheckedId(mCheckedId);
            }
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (isSingleChoice()) {
            if (child instanceof CompoundButton) {
                final CompoundButton button = (CompoundButton) child;
                if (button.isChecked()) {
                    mProtectFromCheckedChange = true;
                    if (mCheckedId != -1) {
                        setCheckedStateForView(mCheckedId, false);
                    }
                    mProtectFromCheckedChange = false;
                    setCheckedId(button.getId());
                }
            }
        }

        super.addView(child, index, params);
    }

    /**
     * <p>
     * Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.
     * </p>
     *
     * @param id the unique id of the radio button to select in this group
     *
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(int id) {
        if (isSingleChoice()) {
            // don't even bother
            if (id != -1 && (id == mCheckedId)) {
                return;
            }

            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }

            if (id != -1) {
                setCheckedStateForView(id, true);
            }

            setCheckedId(id);
        }
        else {
            setCheckedStateForView(id, true);
        }
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof CompoundButton) {
            ((CompoundButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>
     * Returns the identifier of the selected radio button in this group. Upon
     * empty selection, the returned value is -1.
     * </p>
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>
     * Clears the selection. When the selection is cleared, no radio button in
     * this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.
     * </p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>
     * Register a callback to be invoked when the checked radio button changes
     * in this group.
     * </p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * <p>
     * A pass-through listener acts upon the events and dispatches them to
     * another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.
     * </p>
     */
    private class PassThroughHierarchyChangeListener implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onChildViewAdded(View parent, View child) {
            if (parent == FlowLayout.this && child instanceof CompoundButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                        child.setId(id);
                    }
                }
                ((CompoundButton) child).setOnCheckedChangeListener(mChildOnCheckedChangeListener);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FlowLayout.this && child instanceof CompoundButton) {
                ((CompoundButton) child).setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isSingleChoice()) {
                // prevents from infinite recursion
                if (mProtectFromCheckedChange) {
                    return;
                }

                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;

                int id = buttonView.getId();
                setCheckedId(id);
            }
            else {
                setCheckedStateForView(buttonView.getId(), isChecked);
            }
        }
    }

    /**
     * @param choiceMode
     *
     * @see android.widget.ListView#setChoiceMode(int)
     */

    public void setChoiceMode(int choiceMode) {
        mSelectionMode = choiceMode;
    }

    public List<View> getCheckedViews() {
        List<View> list = new ArrayList<View>();
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            View c = getChildAt(i);
            if (c instanceof CompoundButton) {
                list.add(c);
            }
        }
        return list;
    }

    /**
     * <p>
     * Interface definition for a callback to be invoked when the checked radio
     * button changed in this group.
     * </p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>
         * Called when the checked radio button has changed. When the selection
         * is cleared, checkedId is -1.
         * </p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(FlowLayout group, int checkedId);
    }

    // ----> adapter support
    /**
     * Should be used by subclasses to listen to changes in the dataset
     */
    private AdapterDataSetObserver mDataSetObserver;

    /**
     * The adapter containing the data to be displayed by this view
     */
    private ListAdapter mAdapter;

    class AdapterDataSetObserver extends DataSetObserver {
        @Override
        public void onChanged() {
            super.onChanged();
            reset();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    }

    private void reset() {
        removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            final View childView = mAdapter.getView(i, null, this);
            addView(childView, new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

    /**
     * 像ListView、GridView一样使用FlowLayout
     *
     * @param adapter
     */
    public void setAdapter(ListAdapter adapter) {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        //清除现有的数据
        removeAllViews();
        mAdapter = adapter;

        if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }
}