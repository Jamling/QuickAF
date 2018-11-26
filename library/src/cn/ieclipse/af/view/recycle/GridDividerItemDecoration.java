package cn.ieclipse.af.view.recycle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import cn.ieclipse.af.volley.Controller;

/**
 * https://github.com/burgessjp/QuickDevLib/blob/master/library/src/main/java/me/solidev/library/ui/recyclerview
 * /GridDividerItemDecoration.java
 *
 * @author _SOLID, Jamling
 */
public class GridDividerItemDecoration extends ListDividerItemDecoration {
    /**
     * Creates a divider {@link androidx.recyclerview.widget.RecyclerView.ItemDecoration} that can be used with a
     * {@link androidx.recyclerview.widget.LinearLayoutManager}.
     *
     * @param context     Current context, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public GridDividerItemDecoration(Context context, int orientation) {
        super(context, orientation);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            getItemOffsets(outRect, view, parent, state, (StaggeredGridLayoutManager) parent.getLayoutManager());
            return;
        }
        else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            int itemPosition = parent.getChildAdapterPosition(view); // item position
            int spanCount = RecyclerHelper.getSpanCount(parent);
            int childCount = parent.getAdapter().getItemCount();
            boolean isLastColumn = isLastColumn(parent, itemPosition, spanCount, childCount);

            int left;
            int right;
            int bottom;
            int eachWidth = (spanCount - 1) * getDividerDrawableWidth() / spanCount;// avg divider
            int dl = getDividerDrawableWidth() - eachWidth;// divider offset

            left = itemPosition % spanCount * dl;
            right = eachWidth - left;
            bottom = getDividerDrawableHeight();
            //Log.e("zzz", "itemPosition:" + itemPosition + " |left:" + left + " right:" + right + " bottom:" + bottom);
//        if(isLastRow(parent, position, spanCount, itemCount)) {
//            bottom = 0;
//        }
            if (isLastColumn) {
                right = 0;
            }
            outRect.set(left, 0, right, bottom);
        }
    }

    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state,
                               StaggeredGridLayoutManager sglm) {
        int itemPosition = parent.getChildAdapterPosition(view); // item position
        int spanCount = RecyclerHelper.getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        if (isLastRow(parent, itemPosition, spanCount, childCount)) // 如果是最后一行，则不需要绘制底部
        {
            outRect.set(0, 0, getDividerDrawableWidth(), 0);
        }
        else if (isLastColumn(parent, itemPosition, spanCount, childCount))
        // 如果是最后一列，则不需要绘制右边
        {
            outRect.set(0, 0, 0, getDividerDrawableHeight());
        }
        else {
            outRect.set(0, 0, getDividerDrawableWidth(), getDividerDrawableHeight());
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        draw(c, parent);
    }

    //绘制横向 item 分割线
    private void draw(Canvas canvas, RecyclerView parent) {
        int childSize = parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            //画水平分隔线
            int left = child.getLeft();
            int right = child.getRight();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + getDividerHeight();

            if (getDivider() != null) {
                getDivider().setBounds(left, top, right, bottom);
                if (!isLastRow(parent, i, RecyclerHelper.getSpanCount(parent), parent.getAdapter().getItemCount())) {
                    getDivider().draw(canvas);
                }
            }
            //画垂直分割线
            top = child.getTop();
            bottom = child.getBottom() + getDividerDrawableHeight();
            left = child.getRight() + layoutParams.rightMargin;
            right = left + getDividerDrawableHeight();
            if (isLastRow(parent, i, RecyclerHelper.getSpanCount(parent), parent.getAdapter().getItemCount())) {
                bottom = bottom - getDividerDrawableHeight();
            }
            if (getDivider() != null) {
                getDivider().setBounds(left, top, right, bottom);
                getDivider().draw(canvas);
            }
        }
    }

    protected boolean isLastColumn(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true;
            }
        }
        else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            }
            else {
                childCount = childCount - childCount % spanCount;
                if (pos >= childCount) {// 如果是最后一列，则不需要绘制右边
                    return true;
                }
            }
        }
        return false;
    }

    private int getGridRows(RecyclerView parent) {
        int spanCount = RecyclerHelper.getSpanCount(parent);
        int count = parent.getAdapter() == null ? 0 : parent.getAdapter().getItemCount();
        int rows = count / spanCount;
        if (count % spanCount > 0) {
            rows++;
        }
        return rows;
    }

    protected boolean isLastRow(RecyclerView parent, int position, int spanCount, int itemCount) {
        if (itemCount < 0) {
            return true;
        }
        int index = itemCount - 1;
        if (parent.getLayoutManager() instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) parent.getLayoutManager();
            while (index >= 0 && itemCount < spanCount + index) {
                index -= glm.getSpanSizeLookup().getSpanSize(index);
            }
            Controller.log(String.format("%d/%d %d %d", position, itemCount, index, spanCount));
            return position >= index;
        }
        else {
            return position + spanCount >= itemCount;
        }
    }
}