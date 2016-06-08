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
package cn.ieclipse.af.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ScrollView;

import cn.ieclipse.af.R;
import cn.ieclipse.af.adapter.AfGridAdapter;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.ViewUtils;

/**
 * This is a table container for table reports.
 * <p>
 * Using step:
 * <ol>
 *     <li>{@link #setColumns(int)}</li>
 *     <li>{@link #setVisibleColumns(int)}</li>
 *     <li>{@link #setFixColumns(int)}</li>
 *     <li>{@link #setHeaderAdapter(android.widget.ListAdapter)} (optional)</li>
 *     <li>{@link #setDataAdapter(cn.ieclipse.af.adapter.AfGridAdapter)}</li>
 * </ol>
 *
 * @author Jamling
 */
public class TableContainer extends LinearLayout {

    private ListAdapter headerAdapter;
    private ListAdapter dataAdapter;
    private int columns;
    private int fixColumns;
    private int visibleColumns;

    private int mHorizontalSpacing;
    private int mVerticalSpacing;
    // Horizontal divider width
    private int mDividerWidth = 0;
    private Drawable mDivider;
    private int mDividerPadding;
    private int mShowDividers = SHOW_DIVIDER_MIDDLE;

    private Drawable mVerticalDivider;
    private int mVerticalDividerHeight = 0;
    private int mShowVerticalDivider = SHOW_DIVIDER_MIDDLE;
    private int mVerticalDividerPadding = 0;

    // edge color
    private int edgeLength = AppUtils.dp2px(getContext(), 6);
    private int edgeColor = Color.BLACK;

    // cross line
    private boolean mCombineX = true;
    private boolean mCombineY = true;

    public TableContainer(Context context) {
        this(context, null);
    }

    public TableContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TableContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TableContainer);
        setColumns(a.getInt(R.styleable.TableContainer_fl_columns, columns));
        // vertical
        setVerticalDivider(a.getDrawable(R.styleable.TableContainer_fl_vDivider));
        setVerticalDividerHeight(
            a.getDimensionPixelOffset(R.styleable.TableContainer_fl_vDividerHeight, mVerticalDividerHeight));

        setShowVerticalDividers(a.getInt(R.styleable.TableContainer_fl_vDividerShow, mShowVerticalDivider));
        setVerticalDividerPadding(
            a.getDimensionPixelOffset(R.styleable.TableContainer_fl_vDividerPadding, mVerticalDividerPadding));
        setVerticalSpacing(a.getDimensionPixelOffset(R.styleable.TableContainer_fl_vSpacing, mVerticalDividerPadding));
        // horizontal
        setHorizontalDividerDrawable(a.getDrawable(R.styleable.TableContainer_fl_hDivider));
        setHorizontalDividerWidth(
            a.getDimensionPixelOffset(R.styleable.TableContainer_fl_hDividerWidth, mDividerWidth));
        setShowHorizontalDividers(a.getInt(R.styleable.TableContainer_fl_hDividerShow, mShowDividers));
        setHorizontalDividerPadding(
            a.getDimensionPixelOffset(R.styleable.TableContainer_fl_hDividerPadding, mDividerPadding));
        setHorizontalSpacing(a.getDimensionPixelOffset(R.styleable.TableContainer_fl_hSpacing, mHorizontalSpacing));

        if (a.hasValue(R.styleable.TableContainer_edgeColor)) {
            edgeColor = a.getColor(R.styleable.TableContainer_edgeColor, edgeColor);
            mVScroll.setEdgeColor(edgeColor);
            mHeaderScroll.setEdgeColor(edgeColor);
            mDataScroll.setEdgeColor(edgeColor);
        }
        if (a.hasValue(R.styleable.TableContainer_android_fadingEdgeLength)) {
            edgeLength = a.getDimensionPixelSize(R.styleable.TableContainer_android_fadingEdgeLength, edgeLength);
            mVScroll.setFadingEdgeLength(edgeLength);
            mHeaderScroll.setFadingEdgeLength(edgeLength);
            mDataScroll.setFadingEdgeLength(edgeLength);
        }

        a.recycle();
    }

    public void setHeaderAdapter(ListAdapter adapter) {
        // mTl01.setAdapter(adapter);
        if (adapter != null) {
            setColumns(adapter.getCount());
        }
        this.headerAdapter = adapter;
        mTl00.removeAllViews();
        mTl01.removeAllViews();

        if (fixColumns > 0 && fixColumns <= columns) {
            int maxHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                final View childView = adapter.getView(i, null, this);
//                ViewGroup.LayoutParams lp = childView.getLayoutParams() == null ? new MarginLayoutParams(
//                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) : childView.getLayoutParams();
                ViewUtils.measureView(childView);
                maxHeight = Math.max(maxHeight, childView.getMeasuredHeight());
            }
            for (int i = 0; i < adapter.getCount(); i++) {
                final View childView = adapter.getView(i, null, this);
                ViewGroup.LayoutParams lp = childView.getLayoutParams() == null ? new MarginLayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT) : childView.getLayoutParams();
                lp.height = maxHeight;
                if (i < fixColumns) {
                    mTl00.addView(childView);
                }
                else {
                    mTl01.addView(childView);
                }
            }
        }
        else {
            mTl01.setAdapter(adapter);
        }
    }

    public void setDataAdapter(AfGridAdapter dataAdapter) {

        //  mTl11.setAdapter(dataAdapter);
        this.dataAdapter = dataAdapter;
        mTl10.removeAllViews();
        mTl11.removeAllViews();

        if (headerAdapter != null && !headerAdapter.isEmpty() && mCombineY) {
            if ((mTl00.getShowVerticalDividers() & FlowLayout.SHOW_DIVIDER_END) != 0) {
                mTl00.setShowVerticalDividers(mTl00.getShowVerticalDividers() ^ FlowLayout.SHOW_DIVIDER_END);
            }
            if ((mTl01.getShowVerticalDividers() & FlowLayout.SHOW_DIVIDER_END) != 0) {
                mTl01.setShowVerticalDividers(mTl01.getShowVerticalDividers() ^ FlowLayout.SHOW_DIVIDER_END);
            }
        }

        if (dataAdapter != null) {
            if (headerAdapter == null) {
                setColumns(dataAdapter.getColumnCount());
            }
            if (fixColumns > 0 && fixColumns <= columns) {
                int rows = dataAdapter.getRowCount();
                int cols = dataAdapter.getColumnCount();
                for (int j = 0; j < rows; j++) {
                    View[] temps = new View[cols];
                    int maxHeight = 0;
                    for (int i = 0; i < cols; i++) {
                        ViewGroup parent = i < fixColumns ? mTl10 : mTl11;
                        final View childView = dataAdapter.getView(j, i, null, parent);
                        ViewUtils.measureView(childView);
                        maxHeight = Math.max(maxHeight, childView.getMeasuredHeight());
                        temps[i] = childView;
                    }
                    for (int i = 0; i < cols; i++) {
                        ViewGroup parent = i < fixColumns ? mTl10 : mTl11;
                        final View childView = temps[i];
                        ViewGroup.LayoutParams lp = childView.getLayoutParams();
                        lp.height = maxHeight;
                        parent.addView(childView);
                    }
                    // System.out.println("row " + j + " height: " + maxHeight);
                }
            }
            else {
                mTl11.setAdapter(dataAdapter);
            }
        }
    }

    public void setColumns(int columns) {
        mTl01.setNumColumns(columns);
        mTl11.setNumColumns(columns);

        mTl01.setVisibleColumns(this.visibleColumns);
        mTl11.setVisibleColumns(this.visibleColumns);
        this.columns = columns;
    }

    public void setFixColumns(int columns) {

        if (mCombineX) {
            if ((mTl00.getShowHorizontalDividers() & FlowLayout.SHOW_DIVIDER_END) != 0) {
                mTl00.setShowHorizontalDividers(mTl00.getShowHorizontalDividers() ^ FlowLayout.SHOW_DIVIDER_END);
            }
            // TODO set dynamic horizontal spacing
            if ((mTl10.getShowHorizontalDividers() & FlowLayout.SHOW_DIVIDER_END) != 0) {
                mTl10.setShowHorizontalDividers(mTl10.getShowHorizontalDividers() ^ FlowLayout.SHOW_DIVIDER_END);
            }
        }
        mTl01.setHiddenColumns(columns);
        mTl11.setHiddenColumns(columns);

        mTl00.setNumColumns(columns);
        mTl10.setNumColumns(columns);
        this.fixColumns = columns;
    }

    private void moveColumns() {
        int oldc = mTl11.getNumColumns();
        int oldr = mTl11.getRows();

        int width = 0;
        View[] vs = new View[columns];
        for (int i = 0; i < columns; i++) {
            vs[i] = mTl01.getChildAt(i);
            width += vs[i].getMeasuredWidth() + mTl01.getHorizontalSpacing();
        }

        for (View v : vs) {
            mTl01.removeViewInLayout(v);
            mTl00.addView(v);
        }

        View temp;
        int offset = 0;
        for (int i = 0; i < oldr; i++) {
            for (int j = 0; j < columns; j++) {
                temp = mTl11.getChildAt(i * oldc + j - offset);
                mTl11.removeViewInLayout(temp);
                mTl10.addView(temp);
                offset++;
            }
        }
        mTl00.requestLayout();
        mTl10.requestLayout();
        mTl01.requestLayout();
        mTl11.requestLayout();
    }

    public void setVisibleColumns(int columns) {
        this.visibleColumns = columns;
    }

    public void reset() {
        mTl00.removeAllViews();
        mTl01.removeAllViews();
        mTl10.removeAllViews();
        mTl11.removeAllViews();
        mTl01.setHiddenColumns(0);
        mTl11.setHiddenColumns(0);
    }

    public int getColumnWidth(int displayWidth) {
        int width = displayWidth <= 0 ? getMeasuredWidth() : displayWidth - getPaddingLeft() - getPaddingRight()
            - mTl11.getHorizontalSpacing() * (this.visibleColumns - 1);
        if (this.visibleColumns > 0) {
            return width / visibleColumns;
        }
        return 0;
    }

    public void setCombineHorizontalAxis(boolean combine) {
        this.mCombineX = combine;
    }

    public void setCombineVerticalAxis(boolean combine) {
        this.mCombineY = combine;
    }

    private TableLayout mTl00;
    private TableLayout mTl01;
    private TableLayout mTl10;
    private TableLayout mTl11;
    private VScrollView mVScroll;
    private HScrollView mHeaderScroll;
    private HScrollView mDataScroll;

    public TableLayout getFixHeaderLayout() {
        return mTl00;
    }

    public TableLayout getHeaderLayout() {
        return mTl01;
    }

    public TableLayout getFixDataLayout() {
        return mTl10;
    }

    public TableLayout getDataLayout() {
        return mTl11;
    }

    public ScrollView getVScrollView() {
        return mVScroll;
    }

    // proxy method
    public void setHorizontalSpacing(int horizontalSpacing) {
        getHeaderLayout().setHorizontalSpacing(horizontalSpacing);
        getDataLayout().setHorizontalSpacing(horizontalSpacing);

        getFixHeaderLayout().setHorizontalSpacing(horizontalSpacing);
        getFixDataLayout().setHorizontalSpacing(horizontalSpacing);
    }

    public void setVerticalSpacing(int verticalSpacing) {
        getHeaderLayout().setVerticalSpacing(verticalSpacing);
        getDataLayout().setVerticalSpacing(verticalSpacing);

        getFixHeaderLayout().setVerticalSpacing(verticalSpacing);
        getFixDataLayout().setVerticalSpacing(verticalSpacing);
    }

    public void setVerticalDividerHeight(int verticalDividerHeight) {
        getHeaderLayout().setVerticalDividerHeight(verticalDividerHeight);
        getDataLayout().setVerticalDividerHeight(verticalDividerHeight);

        getFixHeaderLayout().setVerticalDividerHeight(verticalDividerHeight);
        getFixDataLayout().setVerticalDividerHeight(verticalDividerHeight);
    }

    public void setVerticalDividerPadding(int verticalDividerPadding) {
        getHeaderLayout().setVerticalDividerPadding(verticalDividerPadding);
        getDataLayout().setVerticalDividerPadding(verticalDividerPadding);
        getFixHeaderLayout().setVerticalDividerPadding(verticalDividerPadding);
        getFixDataLayout().setVerticalDividerPadding(verticalDividerPadding);
    }

    public void setVerticalDivider(Drawable divider) {
        getHeaderLayout().setVerticalDivider(divider);
        getDataLayout().setVerticalDivider(divider);

        getFixHeaderLayout().setVerticalDivider(divider);
        getFixDataLayout().setVerticalDivider(divider);
    }

    public void setShowVerticalDividers(int showDividers) {
        getHeaderLayout().setShowVerticalDividers(showDividers);
        getDataLayout().setShowVerticalDividers(showDividers);

        getFixHeaderLayout().setShowVerticalDividers(showDividers);
        getFixDataLayout().setShowVerticalDividers(showDividers);
        this.mShowVerticalDivider = showDividers;
    }

    public void setHorizontalDividerDrawable(Drawable divider) {
        getHeaderLayout().setHorizontalDividerDrawable(divider);
        getDataLayout().setHorizontalDividerDrawable(divider);

        getFixHeaderLayout().setHorizontalDividerDrawable(divider);
        getFixDataLayout().setHorizontalDividerDrawable(divider);
    }

    public void setShowHorizontalDividers(int showDividers) {
        getHeaderLayout().setShowHorizontalDividers(showDividers);
        getDataLayout().setShowHorizontalDividers(showDividers);
        getFixHeaderLayout().setShowHorizontalDividers(showDividers);
        getFixDataLayout().setShowHorizontalDividers(showDividers);
        this.mShowDividers = showDividers;
    }

    public void setHorizontalDividerWidth(int width) {
        getHeaderLayout().setHorizontalDividerWidth(width);
        getDataLayout().setHorizontalDividerWidth(width);
        getFixHeaderLayout().setHorizontalDividerWidth(width);
        getFixDataLayout().setHorizontalDividerWidth(width);
    }

    public void setHorizontalDividerPadding(int horizontalDividerPadding) {
        getHeaderLayout().setHorizontalDividerPadding(horizontalDividerPadding);
        getDataLayout().setHorizontalDividerPadding(horizontalDividerPadding);
        getFixHeaderLayout().setHorizontalDividerPadding(horizontalDividerPadding);
        getFixDataLayout().setHorizontalDividerPadding(horizontalDividerPadding);
    }

    // init and  inner classes

    public interface IScroller {
        void onScrollXY(int offsetX, int offsetY);
    }

    public static class HScrollView extends HorizontalScrollView implements IScroller {
        private IScroller scroller;
        public int edgeColor = 0;

        public HScrollView(Context context) {
            super(context);
        }

        public HScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void onScrollXY(int offsetX, int offsetY) {
            scrollTo(offsetX, offsetY);
        }

        public void setScroller(IScroller scroller) {
            this.scroller = scroller;
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            super.onScrollChanged(l, t, oldl, oldt);

            if (scroller != null) {
                scroller.onScrollXY(l, t);
            }
        }

        @Override
        public int getSolidColor() {
            return edgeColor;
        }

        public void setEdgeColor(int edgeColor) {
            this.edgeColor = edgeColor;
        }
    }

    public static class VScrollView extends ScrollView implements IScroller {

        public int edgeColor = 0;

        public VScrollView(Context context) {
            super(context);
        }

        public VScrollView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        public void onScrollXY(int offsetX, int offsetY) {
            // no need
        }

        @Override
        public int getSolidColor() {
            return edgeColor;
        }

        public void setEdgeColor(int edgeColor) {
            this.edgeColor = edgeColor;
        }
    }

    public void initLayout() {
        setOrientation(VERTICAL);
        LayoutParams MW = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams WW = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LayoutParams WM = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams MM = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(HORIZONTAL);
        ll.setLayoutParams(MW);

        mTl00 = new TableLayout(getContext());
        mTl00.setLayoutParams(WW);

        HScrollView hv = new HScrollView(getContext());
        hv.setLayoutParams(MW);
        mTl01 = new TableLayout(getContext());
        mTl01.setLayoutParams(WW);
        hv.addView(mTl01);
        ll.addView(mTl00);
        ll.addView(hv);
        addView(ll);

        // separator
        initSeparator();

        // line2
        VScrollView vv = new VScrollView(getContext());
        vv.setLayoutParams(MW);
        addView(vv);

        LinearLayout ll2 = new LinearLayout(getContext());
        ll2.setOrientation(VERTICAL);
        ll2.setLayoutParams(MW);
        vv.addView(ll2);

        ll = new LinearLayout(getContext());
        ll.setOrientation(HORIZONTAL);
        ll.setLayoutParams(MW);
        ll2.addView(ll);

        mTl10 = new TableLayout(getContext());
        mTl10.setLayoutParams(WW);

        HScrollView hv2 = new HScrollView(getContext());
        hv2.setLayoutParams(MW);
        mTl11 = new TableLayout(getContext());
        mTl11.setLayoutParams(WW);
        hv2.addView(mTl11);
        ll.addView(mTl10);
        ll.addView(hv2);

        hv.setScroller(hv2);
        hv2.setScroller(hv);

        mVScroll = vv;
        mHeaderScroll = hv;
        mDataScroll = hv2;
        initHScrollView(hv);
        initHScrollView(hv2);
        initVScrollView(vv);
    }

    protected void initHScrollView(HScrollView hv) {
//        android:fadingEdgeLength="12dp"
//        android:scrollbarStyle="outsideOverlay"
//        android:scrollbars="none"
//        android:fadingEdge="horizontal"
//        android:requiresFadingEdge="horizontal"
//        android:fadeScrollbars="true"
//        android:background="@color/colorPrimary"
        hv.setFadingEdgeLength(edgeLength);
        hv.setHorizontalScrollBarEnabled(false);
        hv.setScrollbarFadingEnabled(true);
        hv.setHorizontalFadingEdgeEnabled(true);
        hv.setEdgeColor(edgeColor);
    }

    protected void initSeparator() {

    }

    protected void initVScrollView(VScrollView vv) {
        vv.setFadingEdgeLength(edgeLength);
        vv.setVerticalScrollBarEnabled(false);
        vv.setScrollbarFadingEnabled(true);
        vv.setVerticalFadingEdgeEnabled(true);
        vv.setEdgeColor(edgeColor);
    }
}
