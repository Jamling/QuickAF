package cn.ieclipse.af.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Description
 *
 * @author Jamling
 */

public class BadgeView3 extends AppCompatTextView {
    protected BadgeView2 badge;

    public BadgeView3(Context context) {
        super(context);
        badge = new BadgeView2(this);
    }

    public int getBadgeCount() {
        return badge.getBadgeCount();
    }

    public void setBadgeCount(int badgeCount) {
        badge.setBadgeCount(badgeCount);
    }

    public void incrementBadgeCount(int increment) {
        int count = getBadgeCount();
        setBadgeCount(increment + count);
    }

    public void decrementBadgeCount(int decrement) {
        incrementBadgeCount(-decrement);
    }

    public void setBadgeStyle(int badgeStyle) {
        badge.setBadgeStyle(badgeStyle);
    }

    public void setBadgeBackground(int radius, int bgColor) {
        badge.setBadgeBackground(radius, bgColor);
    }

    public void setBadgeBackground(Drawable d) {
        badge.setBadgeBackground(d);
    }

    public void setBadgePadding(int left, int top, int right, int bottom) {
        badge.setBadgePadding(left, top, right, bottom);
    }

    @Override
    public void setTextColor(int color) {
        badge.setTextColor(color);
    }

    public void setTextSize(int textSize) {
        badge.setTextSize(textSize);
    }

    public void setMax(int max, CharSequence maxText) {
        badge.setMax(max, maxText);
    }

    public void setBadgeGravity(int gravity) {
        if (getParent() == null) {
            throw new IllegalStateException("Please call setTargetView() first");
        }
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        params.gravity = gravity;
        setLayoutParams(params);
    }

    public int getBadgeGravity() {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) getLayoutParams();
        return params.gravity;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(badge.getMeasuredWidth(), badge.getMeasureHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        badge.draw(canvas);
    }

    /*
     * Attach the BadgeView to the target view
     *
     * @param target the view to attach the BadgeView
     */
    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);
        } else if (target.getParent() instanceof ViewGroup) {
            // use a new Framelayout container for adding badge
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

            badgeContainer.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            badgeContainer.addView(target);

            badgeContainer.addView(this);
            badgeContainer.setId(target.getId());
            target.setId(-1);
            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }
    }
}
