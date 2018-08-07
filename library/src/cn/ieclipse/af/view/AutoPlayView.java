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
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.R;
import cn.ieclipse.af.adapter.AfPagerAdapter;
import cn.ieclipse.af.common.Logger;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;

/**
 * An auto play container.
 * <p>
 * AutoPlayView default child views
 * </p>
 * <ol>
 * <li>{@linkplain android.support.v4.view.ViewPager ViewPager}</li>
 * <li>Indicator layout (if has), it's a horizontal {@linkplain android.widget.LinearLayout LinearLayout}</li>
 * <li>Indicator text widget (if has) to show "current/total" text</li>
 * </ol>
 * <p>
 * You can call {@link #setIndicatorItemLayout(int)} to set indicator item layout and call {@link
 * #setIndicatorItemPadding(int)} to set item padding. the page indicator will changed dynamically.
 * </p>
 *
 * @author Jamling
 * @date 2015/7/15.
 */
public class AutoPlayView extends FrameLayout implements View.OnTouchListener {

    public AutoPlayView(Context context) {
        this(context, null);
    }

    public AutoPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AutoPlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private float mRatio;
    private boolean mLoop = true;
    private boolean mSmoothScroll = true;
    private boolean mPlaying;
    private long mInterval = 5000;
    private boolean mAutoStart;
    private ViewPager mViewPager;
    private LinearLayout mIndicatorLayout;
    private TextView mIndicatorTv;
    private int mIndicatorItemLayout;
    private int mIndicatorItemPadding;
    private int mIndicatorItemSize;
    private int mIndicatorColor;
    private int mIndicatorSelectedColor;
    private int mIndicatorBorderColor;
    private int mIndicatorBorderWidth;
    private boolean mIndicatorAlwaysShow = false;
    private int mPosition;// real position
    public static Logger mLogger = Logger.getLogger(AutoPlayView.class);

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                int current = mViewPager.getCurrentItem();
                int size = mViewPager.getAdapter() == null ? 0 : mViewPager.getAdapter().getCount();
                if (current + 1 < size) {// can next
                    mLogger.v(String.format("from %d to %d", current, current + 1));
                    mViewPager.setCurrentItem(current + 1, mSmoothScroll);
                }
                else if (current + 1 == size && (mLoop && !isAdapterLoop())) {
                    mLogger.v(String.format("from %d to %d", current, 0));
                    mViewPager.setCurrentItem(0, mSmoothScroll);
                }
                if (mPlaying) {
                    mHandler.sendEmptyMessageDelayed(0, mInterval);
                }
            }
            else if (msg.what == 1) {
                mViewPager.setCurrentItem(1, false);
            }
        }
    };

    private void init(Context context, AttributeSet attrs) {
        // mViewPager = new ViewPager(context);
        // addView(mViewPager);
        this.mIndicatorColor = AppUtils.getColor(context, android.R.color.darker_gray);
        this.mIndicatorSelectedColor = AppUtils.getColor(context, android.R.color.holo_blue_dark);
        this.mIndicatorItemSize = AppUtils.dp2px(context, 8);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AutoPlayView);
            mRatio = a.getFloat(R.styleable.AutoPlayView_af_ratio, 0f);
            a.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mIndicatorItemPadding = AppUtils.dp2px(getContext(), 4); // default 4
        // dip
        int size = getChildCount();
        for (int i = 0; i < size; i++) {
            if (getChildAt(i) instanceof ViewPager) {
                mViewPager = (ViewPager) getChildAt(i);
                mViewPager.setFadingEdgeLength(0);
                addOnPageChangedListener(mPageIndicatorListener);
                if (mAutoStart) {
                    start();
                }
                break;
            }
        }
        if (mViewPager != null) {
            // 监听viewpager的触摸事件
            mViewPager.setOnTouchListener(this);
        }
        // default the second layout is indicator layout
        if (size > 1) {
            View v = getChildAt(1);
            if (v instanceof LinearLayout) {
                mIndicatorLayout = (LinearLayout) v;
            }
        }
        // default the 3rd widget is indicator text view
        if (size > 2) {
            View v = getChildAt(2);
            if (v instanceof TextView) {
                mIndicatorTv = (TextView) v;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (isAdapterLoop() && event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mPosition == getCount() - 1) {
                mLogger.v(String.format("onTouch from %d to %d", mPosition, 1));
                mViewPager.setCurrentItem(1, false);
            } else if (mPosition == -1 ) {
                mLogger.v(String.format("onTouch from %d to %d", mPosition, getCount() - 2));
                mViewPager.setCurrentItem(getCount() - 2, false);
            }
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_SCROLL:
                stop();
                break;
            case MotionEvent.ACTION_UP:
                // add if to fix the issue of auto start after motion event
                if (mAutoStart) {
                    start();
                }
                else {

                }
                break;
        }
        return false;
    }

    public void setAutoStart(boolean autoStart) {
        this.mAutoStart = autoStart;
    }

    public void setLoop(boolean loop) {
        if (mLoop != loop) {
            this.mLoop = loop;
            setAdapterLoop(mLoop);
        }
    }

    /**
     * 开启循环播放
     */
    public void start() {
        if (!mPlaying) {
            mPlaying = true;
            mHandler.sendEmptyMessageDelayed(0, mInterval);
        }
        init();
    }

    /**
     * 停止循环播放
     */
    public void stop() {
        mPlaying = false;
        mHandler.removeMessages(0);
    }

    private void init() {
        if (getCount() > 0) {
            if (isAdapterLoop()) {
                mViewPager.setCurrentItem(mPosition + 1, false);
                return;
            }
            if (mPosition > 0) {// 非初次开启从当前位置开启
                mViewPager.setCurrentItem(mPosition, false);
            }
            else {// 初次开启从0位置循环
                mViewPager.setCurrentItem(0, false);
            }
        }
    }

    private void addOnPageChangedListener(ViewPager.OnPageChangeListener listener) {
        if (listener != null) {
            mViewPager.addOnPageChangeListener(listener);
        }
    }

    /**
     * Set a PagerAdapter that will supply views for this pager as needed.
     *
     * @param adapter adapter to use
     *
     * @see android.support.v4.view.ViewPager#setAdapter(android.support.v4.view.PagerAdapter)
     */
    public void setAdapter(PagerAdapter adapter) {
        if (mViewPager != null) {
            mViewPager.setAdapter(adapter);
            setAdapterLoop(mLoop);
        }
        initIndicatorLayout();
    }

    /**
     * Set adapter data, use {@link android.support.v4.view.PagerAdapter#notifyDataSetChanged()}
     * If you want to update UI force, please use <code>
     * setAdapterData(list, true);
     * </code>
     *
     * @param list data
     *
     * @see #setAdapterData(java.util.List, boolean)
     * @since 2.1.1
     */
    public void setAdapterData(List list) {
        setAdapterData(list, false);
    }

    /**
     * Set adapter data and force refresh UI.
     * <p>Note {@link android.support.v4.view.PagerAdapter#notifyDataSetChanged()} will not update UI, please use
     * {@link cn.ieclipse.af.adapter.AfPagerAdapter#notifyDataSetChanged(boolean)} to update UI</p>
     *
     * @param list  data
     * @param force whether force update adapter
     *
     * @since 3.0.1
     */
    public void setAdapterData(List list, boolean force) {
        if (getViewPager() != null) {
            PagerAdapter adapter = getViewPager().getAdapter();
            if (adapter != null) {
                if (adapter instanceof AfPagerAdapter) {
                    ((AfPagerAdapter) adapter).setDataList(list);
                    ((AfPagerAdapter) adapter).notifyDataSetChanged(force);
                }
                else {
                    adapter.notifyDataSetChanged();
                }
                initIndicatorLayout();
            }
        }
    }

    /**
     * 设置一屏有多个元素
     * 推荐使用 {@link cn.ieclipse.af.view.ViewPagerV4}
     *
     * @param margin 元素之间的距离
     *
     * @since 3.0.1
     */
    public void setMultiItemsInViewPager(int margin) {
        if (mViewPager != null) {
            // 3.0以上使用软加速
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }
            // 父和子都需要取消clipChildren(android:clipChildren="false")
            setClipChildren(false);
            mViewPager.setClipChildren(false);
            // 委托ViewPager处理事件
            setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return mViewPager.dispatchTouchEvent(event);
                }
            });
            // 设置item与item之间的间距
            if (margin > 0) {
                mViewPager.setPageMargin(margin);
            }
        }
    }

    /**
     * Set layout resource of the page indicator item
     *
     * @param layoutId xml layout id
     */
    public void setIndicatorItemLayout(int layoutId) {
        this.mIndicatorItemLayout = layoutId;
    }

    public void setIndicatorItemColor(int normalColor, int selectedColor) {
        this.mIndicatorColor = normalColor;
        this.mIndicatorSelectedColor = selectedColor;
    }

    public void setIndicatorItemBorder(int color, int width) {
        this.mIndicatorBorderColor = color;
        this.mIndicatorBorderWidth = width;
    }

    /**
     * Set page indicator layout
     *
     * @param layout indicator layout
     */
    public void setIndicatorLayout(LinearLayout layout) {
        if (layout != null) {
            mIndicatorLayout = layout;
        }
    }

    /**
     * Set page indicator text widget
     *
     * @param tv TextView widget
     */
    public void setIndicatorTextView(TextView tv) {
        if (tv != null) {
            mIndicatorTv = tv;
        }
    }

    public void setIndicatorItemPadding(int padding) {
        if (padding > 0) {
            this.mIndicatorItemPadding = padding;
        }
    }

    public void setIndicatorItemSize(int size) {
        if (size > 0) {
            this.mIndicatorItemSize = size;
        }
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public TextView getIndicatorText() {
        return mIndicatorTv;
    }

    public int getCurrent() {
        return mViewPager.getCurrentItem();
    }

    public int getCount() {
        if (mViewPager.getAdapter() != null) {
//            if (mViewPager.getAdapter() instanceof LoopPagerAdapter) {
//                LoopPagerAdapter aa = (LoopPagerAdapter) mViewPager.getAdapter();
//                return aa.getRealCount();
//            }
            return mViewPager.getAdapter().getCount();
        }
        return 0;
    }

    /**
     * 设置图片循环切换时间
     *
     * @param interval auto play interval time, micro seconds unit
     */
    public void setInterval(long interval) {
        if (interval > 0) {
            this.mInterval = interval;
        }
    }

    /**
     * Set whether always show indicator or not, default is false (when count is less than 2, the indicator is hidden).
     *
     * @param show show
     *
     * @since 2.1.1
     */
    public void setIndicatorAlwaysShow(boolean show) {
        this.mIndicatorAlwaysShow = show;
    }

    /**
     * Initialize the indicator layout, it will generate indicator item view dynamically.
     * <p>
     * Please call the method after your pager adapter changed.
     * </p>
     */
    public void initIndicatorLayout() {
        if (mIndicatorLayout != null) {
            if (mIndicatorLayout.getChildCount() > 0) {
                mIndicatorLayout.removeAllViews();
            }
            int count = getCount();
            if (isAdapterLoop()) {
                count = count - 2;
            }
            if (count <= 1 && !mIndicatorAlwaysShow) {
                return;
            }
            for (int i = 0; i < count; i++) {
                View item = getIndicatorItemView(i);
                ViewGroup.LayoutParams params = item.getLayoutParams();
                if (item.getLayoutParams() == null) {
                    params = new LinearLayout.LayoutParams(mIndicatorItemSize, mIndicatorItemSize);
                }
                if (i > 0 && params instanceof MarginLayoutParams) {
                    ((MarginLayoutParams) params).leftMargin = mIndicatorItemPadding;
                }
                mIndicatorLayout.addView(item, params);
            }
            updateIndicatorItem(mPosition, getCurrent());
        }
    }

    /**
     * Set indicator visible or not.
     *
     * @param visibility see [{@link android.view.View#VISIBLE}|{@link android.view.View#INVISIBLE
     *                   }|{@link android.view.View#GONE}]
     *
     * @since 2.1.1
     */
    public void setIndicatorVisibility(int visibility) {
        if (mIndicatorLayout != null) {
            mIndicatorLayout.setVisibility(visibility);
        }
        if (mIndicatorTv != null) {
            mIndicatorTv.setVisibility(visibility);
        }
    }

    protected View getIndicatorItemView(int position) {
        if (mIndicatorItemLayout > 0) {
            View v = LayoutInflater.from(getContext()).inflate(mIndicatorItemLayout, mIndicatorLayout, false);
            // View.inflate(getContext(), mIndicatorItemLayout, null);
            return v;
        }
        View v = new View(getContext());
        RoundedColorDrawable bg = new RoundedColorDrawable(mIndicatorColor);
        bg.setCircle(true);
        bg.setBorder(mIndicatorBorderColor, mIndicatorBorderWidth);
        bg.addStateColor(android.R.attr.state_selected, mIndicatorSelectedColor);
        bg.addStateColor(android.R.attr.state_activated, mIndicatorSelectedColor);
        bg.applyTo(v);
        return v;
    }

    /**
     * 改变小点
     */
    protected void updateIndicatorItem(int oldPosition, int newPosition) {
        if (mIndicatorLayout != null) {
            View old = null;
            View current = null;
            if (oldPosition < mIndicatorLayout.getChildCount()) {
                old = mIndicatorLayout.getChildAt(oldPosition);
            }
            if (newPosition < mIndicatorLayout.getChildCount()) {
                current = mIndicatorLayout.getChildAt(newPosition);
            }
            if (old != null) {
                old.setSelected(false);
                old.setActivated(false);
            }
            if (current != null) {
                current.setSelected(true);
                current.setActivated(true);
            }
        }
    }

    /**
     * 更新当前项数字
     *
     * @param current current count range: [1, count], start with 1, end with total count
     * @param count   total count
     *
     * @since 3.0.1
     */
    protected void updateIndicatorText(int current, int count) {
        if (mIndicatorTv != null) {
            mIndicatorTv.setText(current + "/" + count);
        }
    }

    /**
     * Set the height/width aspect ratio
     *
     * @param ratio height/width
     */
    public void setRatio(float ratio) {
        this.mRatio = ratio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mRatio > 0) {
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int h = (int) (width * mRatio);
            int hms = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
            super.onMeasure(widthMeasureSpec, hms);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mAutoStart) {
            if (!mPlaying) {
                start();
            }
        }
    }

    // adapter loop
    public void setAdapterLoop(boolean loop) {
        if (getViewPager() != null && getViewPager().getAdapter() != null) {
            if (getViewPager().getAdapter() instanceof LoopPagerAdapter) {
                LoopPagerAdapter adapter = ((LoopPagerAdapter) getViewPager().getAdapter());
                adapter.setLoop(loop);
                if (isAdapterLoop()) {
                    if (mLoopListener == null) {
                        mLoopListener = new LoopOnPageChangeListener(getViewPager());
                    }
                    getViewPager().addOnPageChangeListener(mLoopListener);
                }
                else if (mLoopListener != null) {
                    getViewPager().removeOnPageChangeListener(mLoopListener);
                }
            }
        }
    }

    /**
     * Whether the adapter is {@link cn.ieclipse.af.view.AutoPlayView.LoopPagerAdapter} and whether
     * {@link cn.ieclipse.af.view.AutoPlayView.LoopPagerAdapter#isLoop()}
     *
     * @return true if {@link cn.ieclipse.af.view.AutoPlayView.LoopPagerAdapter} is loop.
     */
    public boolean isAdapterLoop() {
        if (getViewPager().getAdapter() instanceof LoopPagerAdapter) {
            return ((LoopPagerAdapter) getViewPager().getAdapter()).isLoop();
        }
        return false;
    }

    private ViewPager.OnPageChangeListener mPageIndicatorListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            int count = getCount();
            int current = getCurrent();
            if (isAdapterLoop()) {
                if (current == count - 1) {
                    current = 0;
                }
                else {
                    current = getCurrent() - 1;
                }
                count = count - 2;
            }
            // 显示数字指示器
            if (mIndicatorTv != null) {
                updateIndicatorText(current + 1, count);
            }
            // 显示图片指示器
            if (mIndicatorLayout != null) {
                if (getCount() > 0) {
                    // 更新小点点颜色
                    updateIndicatorItem(mPosition, current);
                }
                else {
                    mIndicatorLayout.setVisibility(View.GONE);
                }
            }
            mPosition = current;
        }
    };

    private LoopOnPageChangeListener mLoopListener;

    /**
     * The internal loop listener when {@link #setLoop(boolean)} true
     *
     * @since 2.1.1
     */
    public class LoopOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        private ViewPager mViewPager;

        public LoopOnPageChangeListener(ViewPager viewPager) {
            this.mViewPager = viewPager;
        }

        @Override
        public void onPageSelected(int position) {
            mLogger.v(String.format("onPageSelected %d", position));
            doLoop(position);
        }

        private void doLoop(int position) {
            PagerAdapter adapter = mViewPager.getAdapter();
            if (adapter instanceof LoopPagerAdapter) {
                LoopPagerAdapter aa = (LoopPagerAdapter) adapter;
                boolean loop = aa.isLoop();
                if (loop) {
                    int old = mPosition;
                    int count = adapter.getCount();
                    mLogger.v(String.format("doLoop: %d-%d", old, position));
                    //mViewPager.removeOnPageChangeListener(this);
                    if (position == 0) {
                        mViewPager.setCurrentItem(count - 2, false);
                    }
                    else if (position + 1 == count) {
                        // mViewPager.setCurrentItem(1, false);
                        mHandler.sendEmptyMessageDelayed(1, 100);
                    }
                    //mViewPager.addOnPageChangeListener(this);
                }
            }
        }
    }

    /**
     * Loop {@link android.support.v4.view.PagerAdapter}
     *
     * @param <T> data parameter type
     *
     * @since 2.1.1
     */
    public static abstract class LoopPagerAdapter<T> extends AfPagerAdapter<T> {
        private boolean mLoop = true;

        @Override
        public void setDataList(List<T> list) {
            // super.setDataList(list);
            mDataList = new ArrayList<>();
            if (list != null) {
                mDataList.addAll(list);
            }
            if (mLoop) {
                setLoopInternal();
            }
        }

        /**
         * Return the real actual count for data list
         *
         * @return real count
         * @since 2.1.1
         */
        public int getRealCount() {
            if (mLoop && getCount() > 0) {
                return getCount() - 2;
            }
            return getCount();
        }

        public int getRealPosition(int position) {
            if (mLoop && getCount() > 0) {
                return position - 1;
            }
            return position;
        }

        /**
         * Set whether the adapter is playing loop
         *
         * @param loop loop
         *
         * @see cn.ieclipse.af.view.AutoPlayView
         * @see #getRealCount()
         * @since 2.1.1
         */
        public void setLoop(boolean loop) {
            if (getCount() == 0) {
                return;
            }
            if (mLoop != loop) {
                mLoop = loop;
                setLoopInternal();
                notifyDataSetChanged();
            }
        }

        /**
         * Return whether loop option
         *
         * @return loop
         * @see #setLoop(boolean)
         * @since 2.1.1
         */
        public boolean isLoop() {
            return mLoop;
        }

        private void setLoopInternal() {
            int count = getCount();
            if (count < 1) {
                return;
            }
            if (mLoop) {
                T first = mDataList.get(0);
                T last = mDataList.get(mDataList.size() - 1);
                mDataList.add(0, last);
                mDataList.add(first);
            }
            else {
                mDataList.remove(mDataList.size() - 1);
                mDataList.remove(0);
            }
        }
    }
}
