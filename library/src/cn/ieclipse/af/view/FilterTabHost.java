/*
 * Copyright (C) 2015-2017 QuickAF
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

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Checkable;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.R;

/**
 * The FilterTabHost include several tab corresponding to {@link cn.ieclipse.af.view.FilterTabView} used to
 * filtering or sorting list
 *
 * @author Jamling
 */
public class FilterTabHost extends FlowLayout implements View.OnClickListener, PopupWindow.OnDismissListener {

    private static final String TAG_TAB = "FilterTabView.Tab";
    private static final String TAG_PW_BG = "FilterTabView.PW.BG";
    /**
     * Current selected tab item.
     */
    private View mSelectedTab;

    /**
     * The tab item layout resource
     */
    private int mTabLayoutRes;

    /**
     * The {@link cn.ieclipse.af.view.FilterTabView} (as the content view of popup window) collections.
     */
    private List<FilterTabView> mFilterTabViews = new ArrayList<>();
    /**
     * The popup window to show the {@link cn.ieclipse.af.view.FilterTabView}.
     */
    private PopupWindow mPopupWindow;

    /**
     * The {@link android.widget.PopupWindow} content view ({@link cn.ieclipse.af.view.FilterTabView})
     * show/hide animation style.
     */
    private int mAnimationStyle;
    private Animation enterAnimation;
    private Animation exitAnimation;

    private Animator bgEnterAnimator;
    private Animation bgExitAnimator;
    private TransitionDrawable bgTransitionDrawable;

    private boolean mDimWindow;
    private float mDimAlpha = .5f;
    private Point mOffset = new Point(0, 0);
    private ViewGroup mContentWrapper;
    private View mContent;

    public FilterTabHost(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public FilterTabHost(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterTabHost(Context context) {
        this(context, null);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FilterTabHost);
            mTabLayoutRes = array.getResourceId(R.styleable.FilterTabHost_af_tabLayout, mTabLayoutRes);
            array.recycle();
        }
        mOffset.y = getVerticalDividerHeight();
    }

    /**
     * Initialize with the {@link cn.ieclipse.af.view.FilterTabView} collections
     *
     * @param filterTabViews {@link cn.ieclipse.af.view.FilterTabView} collections
     */
    public void init(List<FilterTabView> filterTabViews) {
        if (!mFilterTabViews.isEmpty()) {
            mFilterTabViews.clear();
        }
        setNumColumns(filterTabViews.size());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int i = 0; i < filterTabViews.size(); i++) {
            FilterTabView filterTabView = filterTabViews.get(i);
            mFilterTabViews.add(filterTabView);

            // 设置tab
            if (mTabLayoutRes > 0) {
                View tabItem = inflater.inflate(mTabLayoutRes, this, false);
                addChild(tabItem, filterTabView.getTitle());
            }
        }
    }

    public void addTab(FilterTabView tabView, View tabItem) {
        mFilterTabViews.add(tabView);
        addChild(tabItem, tabView.getTitle());
        setNumColumns(mFilterTabViews.size());
    }

    protected void addChild(View tabItem, CharSequence title) {
        tabItem.setTag(TAG_TAB);
        tabItem.setOnClickListener(this);
        setTabText(tabItem, title);
        addView(tabItem);
    }

    /**
     * Set the text of tab
     *
     * @param view tab view/layout
     * @param text text string
     */
    protected void setTabText(View view, CharSequence text) {
        if (view != null && view instanceof TextView) {
            ((TextView) view).setText(text);
        }
    }

    @Override
    public void onClick(View view) {
        Object tag = view.getTag();
        if (TAG_TAB.equals(tag)) {
            mSelectedTab = view;
            showPopup();
        }
        else if (TAG_PW_BG.equals(tag)) {
            hidePopup();
        }
    }

    public void setTabLayout(int layoutId) {
        this.mTabLayoutRes = layoutId;
    }

    public void setAnimationStyle(int style) {
        this.mAnimationStyle = style;
        if (mAnimationStyle > 0) {
            int[] attrs = new int[]{android.R.attr.windowEnterAnimation, android.R.attr.windowExitAnimation};
            TypedArray array = getContext().obtainStyledAttributes(mAnimationStyle, attrs);
            int i = 0;
            if (array.hasValue(i)) {
                int ani = array.getResourceId(i, android.R.anim.fade_in);
                enterAnimation = AnimationUtils.loadAnimation(getContext(), ani);
            }
            i++;
            if (array.hasValue(i)) {
                int ani = array.getResourceId(i, android.R.anim.fade_out);
                exitAnimation = AnimationUtils.loadAnimation(getContext(), ani);
            }
            array.recycle();
        }

        if (enterAnimation != null) {
            bgEnterAnimator = ValueAnimator.ofInt(255, (int) (255 * mDimAlpha));
            bgEnterAnimator.setDuration(enterAnimation.getDuration());
        }
        if (exitAnimation != null) {
            bgExitAnimator = new AlphaAnimation(1, 0);
            bgExitAnimator.setDuration(exitAnimation.getDuration());
        }
    }

    protected void setTabChecked(boolean checked) {
        if (mSelectedTab != null && mSelectedTab instanceof Checkable) {
            ((Checkable) mSelectedTab).setChecked(checked);
        }
    }

    protected void setWindowAlpha(float alpha) {
        if (mDimWindow && getContext() instanceof Activity) {
            Window window = ((Activity) getContext()).getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.alpha = alpha;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(lp);
        }
    }

    /**
     * Set dim window or not, if dim
     *
     * @param dim true or false (default)
     */
    public void setDimWindow(boolean dim) {
        this.mDimWindow = dim;
    }

    /**
     * Set dim alpha when {@link #setDimWindow(boolean)} set true
     *
     * @param alpha from 0-1
     */
    public void setDimAlpha(float alpha) {
        this.mDimAlpha = alpha;
    }

    public void setPopupOffset(int x, int y) {
        mOffset.x = x;
        mOffset.y = y;
    }

    @Override
    public void onDismiss() {
        setTabChecked(false);
        setWindowAlpha(1f);
    }

    protected void initPopupWindow(View contentView) {
        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT);
        /** 监听popupWindow的收缩*/
        mPopupWindow.setOnDismissListener(this);
        // mPopupWindow.setAnimationStyle(0);

        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        if (mDimWindow) {
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        }
        else {
            int c2 = (int) (mDimAlpha * 0xff);
            c2 = c2 << 24;
            bgTransitionDrawable = new TransitionDrawable(
                new Drawable[]{new ColorDrawable(0x0), new ColorDrawable(c2)});
            mPopupWindow.setBackgroundDrawable(bgTransitionDrawable);
        }
        // PopupUtils.setModal(mPopupWindow, false);
    }

    /**
     * 显示popupWindow
     */
    private void showPopup() {
        int index = 0;
        if (mFilterTabViews.isEmpty()) {
            return;
        }
        if (mSelectedTab != null) {
            for (int i = 0; i < getChildCount(); i++) {
                if (mSelectedTab == getChildAt(i)) {
                    index = i;
                    break;
                }
            }
        }
        // TODO stop previous animation?
        View content = mFilterTabViews.get(index);
        if (content.getBackground() == null) {
            content.setBackgroundColor(-1);
        }
        ViewGroup wrapper = (ViewGroup) content.getParent();
        if (wrapper == null) {
            wrapper = new RelativeLayout(getContext());
            RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
            wrapper.addView(content, rl);
            wrapper.setTag(TAG_PW_BG);
            wrapper.setOnClickListener(this);
        }
        if (null == mPopupWindow) {
            initPopupWindow(wrapper);
        }
        else {
            mPopupWindow.setContentView(wrapper);
        }

        if (mPopupWindow.isShowing()) {
            hidePopup();
        }
        else {
            mContent = content;
            mContentWrapper = wrapper;
            setWindowAlpha(mDimAlpha);
            mPopupWindow.update();
            mPopupWindow.showAsDropDown(mSelectedTab, mOffset.x, mOffset.y);
            if (enterAnimation != null) {
                content.startAnimation(enterAnimation);
                bgTransitionDrawable.startTransition((int) enterAnimation.getDuration());
            }
        }
    }

    /**
     * 隐藏PopupWindow，并且重置Tab状态
     */
    public void hidePopup() {
        if (exitAnimation != null) {
            bgTransitionDrawable.reverseTransition((int) enterAnimation.getDuration());
            if (mContent != null) {
                mContent.startAnimation(exitAnimation);
                exitAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (mPopupWindow != null) {
                            mPopupWindow.dismiss();
                        }
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        }
        else {
            if (mPopupWindow != null) {
                mPopupWindow.dismiss();
            }
        }
    }

    public void setSelectTabText(CharSequence text) {
        if (null != mSelectedTab) {
            setTabText(mSelectedTab, text);
        }
    }

    public void clearChoice(int index) {
        if (index >= 0 && index < mFilterTabViews.size()) {
            mFilterTabViews.get(index).clearChoice();
        }
    }

    public void clearAllChoice() {
        int size = mFilterTabViews.size();
        for (int i = 0; i < size; i++) {
            mFilterTabViews.get(i).clearChoice();
        }
    }
}