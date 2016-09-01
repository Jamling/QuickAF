/*
 * Copyright 2014-2016 QuickAF
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

package cn.ieclipse.af.view.expendview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.R;

public class ExpandableView extends LinearLayout implements View.OnClickListener {

    /**
     * 记录选中的ToggleButton
     */
    private ToggleButton mSelectToggleBtn;
    /**
     * 筛选
     */
    private List<View> mToggleButtons = new ArrayList<>();

    private List<ExpandItemView> mItemViews = new ArrayList<>();
    /**
     * 筛选项集合
     */
    private List<View> mPopupViews;
    /**
     * popupwindow展示的宽
     */
    private int mDisplayWidth;
    /**
     * popupwindow展示的高
     */
    private int mDisplayHeight;
    /**
     * 筛选内容用PopupWindow弹出来
     */
    private PopupWindow mPopupWindow;

    /**
     * tab item layout id
     */
    private int mTabResid;
    /**
     * popwindow默认弹出动画
     */
    private int popAnimationStyle;

    public ExpandableView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public ExpandableView(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.expandView);
            mTabResid = array.getResourceId(R.styleable.expandView_tabItemId, mTabResid);
            array.recycle();
        }

        setOrientation(LinearLayout.HORIZONTAL);
        mDisplayWidth = getResources().getDisplayMetrics().widthPixels;
        mDisplayHeight = getResources().getDisplayMetrics().heightPixels;
        mDisplayHeight = (int) (mDisplayHeight * 1.0);
    }

    /**
     * 初始化数据和布局
     * 1.根据筛选项的数量，动态增加上面一排ToggleButton
     * 2.设置每一个ToggleButton的监听事件
     *
     * @param views
     */
    public void initViews(List<ExpandItemView> views) {
        mPopupViews = new ArrayList<>();
        if (!mItemViews.isEmpty()) {
            mItemViews.clear();
        }
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < views.size(); i++) {
            ExpandItemView itemView = views.get(i);
            mItemViews.add(itemView);

            final RelativeLayout tabItemLayout = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
            tabItemLayout.addView(itemView, rl);
            mPopupViews.add(tabItemLayout);
            // 设置tab
            ToggleButton toggleButton = null;
            if (mTabResid > 0) {//R.layout.toggle_button
                toggleButton = (ToggleButton) inflater.inflate(mTabResid, this, false);
            }
            // 设置tab文字
            toggleButton.setText(itemView.getTitle());
            mToggleButtons.add(toggleButton);
            addView(toggleButton);
            toggleButton.setTag(i);

            toggleButton.setOnClickListener(this);
            tabItemLayout.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ToggleButton) {
            mSelectToggleBtn = (ToggleButton) view;
            showPopWindow();
        }
        else if (view instanceof RelativeLayout) {
            hidePopWindow();
        }
    }

    public void setTabItemId(int resid) {
        this.mTabResid = resid;
    }

    /**
     * Set a drawable to be used as a divider between items.
     *
     * @param divider      Drawable that will divide each item.
     * @param showDividers One or more of {@link #SHOW_DIVIDER_BEGINNING},
     *                     {@link #SHOW_DIVIDER_MIDDLE}, or {@link #SHOW_DIVIDER_END},
     *                     or {@link #SHOW_DIVIDER_NONE} to show no dividers.
     */
    public void setDividerDrawable(Drawable divider, int showDividers) {
        setDividerDrawable(divider);
        setShowDividers(showDividers);
    }

    public void setPopAnimationStyle(int style) {
        this.popAnimationStyle = style;
    }

    /**
     * 隐藏popupWindow，并且重置ToggleButton字体颜色
     */
    public void hidePopWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        if (mSelectToggleBtn != null) {
            mSelectToggleBtn.setChecked(false);
        }
    }

    /**
     * 显示popupWindow
     */
    private void showPopWindow() {
        int index = 0;
        if (mPopupViews.isEmpty()) {
            return;
        }
        if (mSelectToggleBtn != null) {
            index = (int) mSelectToggleBtn.getTag();
        }
        if (null == mPopupWindow) {
            mPopupWindow = new PopupWindow(mPopupViews.get(index), mDisplayWidth, mDisplayHeight);
            /** 监听popupWindow的收缩*/
            mPopupWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    if (mSelectToggleBtn != null) {
                        mSelectToggleBtn.setChecked(false);
                    }
                }
            });
            if (popAnimationStyle > 0) {
                mPopupWindow.setAnimationStyle(popAnimationStyle);
            }
            mPopupWindow.setFocusable(true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        }
        else {
            mPopupWindow.setContentView(mPopupViews.get(index));
        }

        if (mPopupWindow.isShowing()) {
            hidePopWindow();
        }
        else {
            mPopupWindow.showAsDropDown(mToggleButtons.get(0), 0, 0);
        }
    }

    public void setSelectItemText(String selectTxt) {
        if (null != mSelectToggleBtn) {
            mSelectToggleBtn.setText(selectTxt);
        }
    }

    public void clearChoice(int index) {
        if (index >= 0 && index < mItemViews.size()) {
            mItemViews.get(index).clearChoice();
        }
    }

    public void clearAllChoice() {
        int size = mItemViews.size();
        for (int i = 0; i < size; i++) {
            mItemViews.get(i).clearChoice();
        }
    }
}
