package cn.ieclipse.af.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import cn.ieclipse.af.util.AppUtils;

public class TopTabView extends HorizontalScrollView {
    private final PageListener pageListener = new PageListener();
    private ViewPager pager;
    private final LinearLayout tabsContainer;
    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private final Rect indicatorRect;
    private LinearLayout.LayoutParams defaultTabLayoutParams;

    private int scrollOffset = 10;
    private int lastScrollX = 0;
    private int tabSelectColor = android.R.color.white;
    private int tabNormalColor = android.R.color.black;
    private int tabTextSize = 14;

    private Drawable indicator;
    private final TextDrawable[] drawables;
    private Drawable left_edge;
    private Drawable right_edge;

    public TopTabView(Context context) {
        this(context, null);
    }

    public TopTabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopTabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        drawables = new TextDrawable[3];
        int i = 0;
        while (i < drawables.length) {
            drawables[i] = new TextDrawable(getContext());
            i++;
        }

        indicatorRect = new Rect();

        setFillViewport(true);
        setWillNotDraw(false);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_VERTICAL;

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(params);
        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }


    /**
     * 设置item是否有固定大小
     *
     * @param isFixed true 一屏幕显示四个，false 自适应
     */
    public void setItemFixedSize(boolean isFixed) {
        int width = 0;
        if (isFixed) {
            width = AppUtils.getScreenWidth(getContext()) / 4;
        } else {
            width = LayoutParams.WRAP_CONTENT;
        }
        defaultTabLayoutParams = new LinearLayout.LayoutParams(width, LayoutParams.MATCH_PARENT);
    }

    /**
     * 绘制高亮区域作为滑动分页指示器
     *
     * @param indicator
     */
    public void setIndicator(Drawable indicator) {
        this.indicator = indicator;
    }

    /**
     * 绑定与CategoryTabStrip控件对应的ViewPager控件，实现联动
     */
    public void setViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    /**
     * 当附加在ViewPager适配器上的数据发生变化时,应该调用该方法通知CategoryTabStrip刷新数据
     */
    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {
            addTab(i, pager.getAdapter().getPageTitle(i).toString());
        }
    }

    /**
     * 设置tab选中字体颜色
     *
     * @param color
     */
    public void setTabSelectColor(int color) {
        this.tabSelectColor = color;
    }

    /**
     * 设置tab中字体大小 unit DIP
     *
     * @param size
     */
    public void setTabTextSize(int size) {
        this.tabTextSize = size;
    }

    /**
     * 设置tab未选中字体颜色
     *
     * @param color
     */
    public void setTabNormalColor(int color) {
        this.tabNormalColor = color;
    }

    private void addTab(final int position, String title) {
        ViewGroup tab = getItemView(title);
        tab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 设置false防止多页切换出现闪烁现象
                pager.setCurrentItem(position, false);
            }
        });

        tabsContainer.addView(tab, position, defaultTabLayoutParams);
    }

    private ViewGroup getItemView(String title) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        frameLayout.setPadding(AppUtils.dp2px(getContext(), 5), AppUtils.dp2px(getContext(), 3), AppUtils.dp2px(
            getContext(), 5), AppUtils.dp2px(getContext(), 3));

        TextView itemtext = new TextView(getContext());
        itemtext.setText(title);
        itemtext.setGravity(Gravity.CENTER);
        itemtext.setSingleLine(true);
        itemtext.setFocusable(true);
        itemtext.setTextColor(getResources().getColor(tabNormalColor));
        itemtext.setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
        itemtext.setPadding(AppUtils.dp2px(getContext(), 8), AppUtils.dp2px(getContext(), 2), AppUtils.dp2px(
            getContext(), 8), AppUtils.dp2px(getContext(), 2));
        frameLayout.addView(itemtext);
        return frameLayout;
    }

    // 计算滑动过程中矩形高亮区域的上下左右位置
    private void calculateIndicatorRect(Rect rect) {
        if (tabsContainer != null && tabsContainer.getChildCount() > 0) {
            ViewGroup currentTab = (ViewGroup) tabsContainer.getChildAt(currentPosition);
            TextView category_text = (TextView) currentTab.getChildAt(0);

            float left = (float) (currentTab.getLeft() + category_text.getLeft());
            float width = ((float) category_text.getWidth()) + left;

            if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
                ViewGroup nextTab = (ViewGroup) tabsContainer.getChildAt(currentPosition + 1);
                TextView next_category_text = (TextView) nextTab.getChildAt(0);

                float next_left = (float) (nextTab.getLeft() + next_category_text.getLeft());
                left = left * (1.0f - currentPositionOffset) + next_left * currentPositionOffset;
                width = width * (1.0f - currentPositionOffset) + currentPositionOffset * (
                    ((float) next_category_text.getWidth()) + next_left);
            }

            int top = currentTab.getTop() + currentTab.getPaddingTop() + category_text.getTop() + category_text
                .getPaddingTop() + (category_text.getHeight() - category_text.getMeasuredHeight())
                + getPaddingTop();
            //top
            rect.set(((int) left) + getPaddingLeft(), top, ((int) width) + getPaddingLeft(),
                currentTab.getTop() + getPaddingTop() + category_text.getTop() + category_text.getHeight());
        }
    }

    // 计算滚动范围
    private int getScrollRange() {
        return getChildCount() > 0 ? Math.max(0, getChildAt(0).getWidth() - getWidth() + getPaddingLeft()
            + getPaddingRight()) : 0;
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }

        calculateIndicatorRect(indicatorRect);

        int newScrollX = lastScrollX;
        if (indicatorRect.left < getScrollX() + scrollOffset) {
            newScrollX = indicatorRect.left - scrollOffset;
        } else if (indicatorRect.right > getScrollX() + getWidth() - scrollOffset) {
            newScrollX = indicatorRect.right - getWidth() + scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        calculateIndicatorRect(indicatorRect);

        if (indicator != null) {
            indicator.setBounds(indicatorRect);
            indicator.draw(canvas);
        }

        int i = 0;
        while (i < tabsContainer.getChildCount()) {
            if (i < currentPosition - 1 || i > currentPosition + 1) {
                i++;
            } else {
                ViewGroup tab = (ViewGroup) tabsContainer.getChildAt(i);
                TextView category_text = (TextView) tab.getChildAt(0);
                if (category_text != null) {
                    TextDrawable textDrawable = drawables[i - currentPosition + 1];
                    int save = canvas.save();
                    calculateIndicatorRect(indicatorRect);
                    canvas.clipRect(indicatorRect);
                    textDrawable.setText(category_text.getText());
                    textDrawable.setTextSize(TypedValue.COMPLEX_UNIT_PX, category_text.getTextSize());
                    textDrawable.setTextColor(getResources().getColor(tabSelectColor));

                    int left = tab.getLeft() + category_text.getLeft()
                        + (category_text.getWidth() - textDrawable.getIntrinsicWidth()) / 2 + getPaddingLeft();

                    int top = tab.getTop() + category_text.getTop() + category_text.getPaddingTop()
                        + (category_text.getHeight() - textDrawable.getIntrinsicHeight()) / 2 + getPaddingTop();

                    textDrawable.setBounds(left, top, textDrawable.getIntrinsicWidth() + left,
                        textDrawable.getIntrinsicHeight() + top);

                    textDrawable.draw(canvas);
                    canvas.restoreToCount(save);
                }
                i++;
            }
        }

        i = canvas.save();
        int top = getScrollX();
        int height = getHeight();
        int width = getWidth();
        canvas.translate((float) top, 0.0f);
//        if (left_edge == null || top <= 0) {
//            if (right_edge == null || top >= getScrollRange()) {
//                canvas.restoreToCount(i);
//            }
//            else {
//                right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0, width, height);
//                right_edge.draw(canvas);
//                canvas.restoreToCount(i);
//            }
//        }
//        else {
//            left_edge.setBounds(0, 0, left_edge.getIntrinsicWidth(), height);
//            left_edge.draw(canvas);
//        }
//
//        if (right_edge == null || top >= getScrollRange()) {
//            canvas.restoreToCount(i);
//        }
//        else {
//            right_edge.setBounds(width - right_edge.getIntrinsicWidth(), 0, width, height);
//            right_edge.draw(canvas);
//            canvas.restoreToCount(i);
//        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                if (pager.getCurrentItem() == 0) {
                    // 滑动到最左边
                    scrollTo(0, 0);
                } else if (pager.getCurrentItem() == tabCount - 1) {
                    // 滑动到最右边
                    scrollTo(getScrollRange(), 0);
                } else {
                    scrollToChild(pager.getCurrentItem(), 0);
                }
            }
        }

        @Override
        public void onPageSelected(int position) {

        }
    }
}
