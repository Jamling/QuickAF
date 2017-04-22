package cn.ieclipse.af.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * show popwindow
 *
 * @author wangjian
 * @date 2015/11/11.
 */
public class PopupUtils {

    /**
     * show the popwindow on view's bottom
     *
     * @param context
     * @param clickView   the view click to show popwidow
     * @param contentView the layout view be shown
     */
    public static PopupWindow showPopWindowBottom(Context context, View clickView, View contentView) {
        return showPopWindow(context, clickView, contentView, 0, 0);
    }

    /**
     * show the popwindow on view's bottom
     *
     * @param context
     * @param clickView the view click to show popwidow
     * @param layoutId  the layout resid be shown
     */
    public static PopupWindow showPopWindowBottom(Context context, View clickView, int layoutId) {
        return showPopWindow(context, clickView, null, layoutId, 0);
    }

    /**
     * show the popwindow on view's top
     *
     * @param context
     * @param clickView   the view click to show popwidow
     * @param contentView the layout view be shown
     */
    public static PopupWindow showPopWindowTop(Context context, View clickView, View contentView) {
        return showPopWindow(context, clickView, contentView, 0, 1);
    }

    /**
     * show the popwindow on view's top
     *
     * @param context
     * @param clickView the view click to show popwidow
     * @param layoutId  the layout resid be shown
     */
    public static PopupWindow showPopWindowTop(Context context, View clickView, int layoutId) {
        return showPopWindow(context, clickView, null, layoutId, 1);
    }

    /**
     * show the popwindow on view's left
     *
     * @param context
     * @param clickView   the view click to show popwidow
     * @param contentView the layout view be shown
     */
    public static PopupWindow showPopWindowLeft(Context context, View clickView, View contentView) {
        return showPopWindow(context, clickView, contentView, 0, 2);
    }

    /**
     * show the popwindow on view's left
     *
     * @param context
     * @param clickView the view click to show popwidow
     * @param layoutId  the layout resid be shown
     */
    public static PopupWindow showPopWindowLeft(Context context, View clickView, int layoutId) {
        return showPopWindow(context, clickView, null, layoutId, 2);
    }

    /**
     * show the popwindow on view's right
     *
     * @param context
     * @param clickView   the view click to show popwidow
     * @param contentView the layout view be shown
     */
    public static PopupWindow showPopWindowRight(Context context, View clickView, View contentView) {
        return showPopWindow(context, clickView, contentView, 0, 3);
    }

    /**
     * show the popwindow on view's right
     *
     * @param context
     * @param clickView the view click to show popwidow
     * @param layoutId  the layout resid be shown
     */
    public static PopupWindow showPopWindowRight(Context context, View clickView, int layoutId) {
        return showPopWindow(context, clickView, null, layoutId, 3);
    }

    /**
     * show popwindow on clickview top, bottom ,right or left
     *
     * @param context
     * @param clickView   the view click to show popwidow
     * @param contentView the layout view be shown
     * @param layoutId    the layout resid be shown
     * @param location    0.bottom
     *                    1.top
     *                    2.left
     *                    3.right
     *
     * @return
     */
    private static PopupWindow showPopWindow(Context context, View clickView, View contentView, int layoutId,
                                             int location) {
        PopupWindow mPopupWindows = new PopupWindow(context);
        View layout = null;
        if (contentView != null) {
            layout = contentView;
        }
        else if (layoutId > 0) {
            layout = LayoutInflater.from(context).inflate(layoutId, null);
        }
        else {
            throw new RuntimeException("the contentView should not be null or the layoutId > 0 is request");
        }
        mPopupWindows.setContentView(layout);
        // 在layout中如果view是设置为WRAP_CONTENT
        // 在popview弹出时无法获取宽度(getWidth() =-2)
        // 此时需要重新测量view
        ViewUtils.measureView(layout);
        mPopupWindows.setTouchable(true);
        mPopupWindows.setFocusable(true);
        mPopupWindows.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindows.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindows.setBackgroundDrawable(new BitmapDrawable());

        if (location == 0) {
            // bottom
            mPopupWindows.showAsDropDown(clickView);
        }
        else if (location == 1) {
            // top
            int[] local = new int[2];
            clickView.getLocationOnScreen(local);
            mPopupWindows.showAtLocation(clickView, Gravity.NO_GRAVITY, local[0],
                local[1] - mPopupWindows.getContentView().getMeasuredHeight());
        }
        else if (location == 2) {
            // left
            int[] local = new int[2];
            clickView.getLocationOnScreen(local);
            mPopupWindows.showAtLocation(clickView, Gravity.NO_GRAVITY,
                local[0] - mPopupWindows.getContentView().getMeasuredWidth(), local[1]);
        }
        else if (location == 3) {
            // right
            int[] local = new int[2];
            clickView.getLocationOnScreen(local);
            mPopupWindows.showAtLocation(clickView, Gravity.NO_GRAVITY, local[0] + clickView.getMeasuredWidth(),
                local[1]);
        }
        return mPopupWindows;
    }

    public static void showAsDropDown(PopupWindow popupWindow, View anchorView, int offsetX, int offsetY) {
        if (Build.VERSION.SDK_INT == 24) {
            int[] a = new int[2];
            anchorView.getLocationInWindow(a);
            if (anchorView.getContext() instanceof Activity) {
                View decorView = ((Activity) anchorView.getContext()).getWindow().getDecorView();
                popupWindow.showAtLocation(decorView, Gravity.NO_GRAVITY, offsetX,
                    a[1] + anchorView.getHeight() + offsetY);
                return;
            }
        }
        popupWindow.showAsDropDown(anchorView, offsetX, offsetY);
    }

    /**
     * Set popup modal, default is true
     *
     * @param popupWindow {@link android.widget.PopupWindow}
     * @param modal       true to set modal(default) or false
     */
    public static void setModal(PopupWindow popupWindow, boolean modal) {
        try {
            Method method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, modal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setShowIcon(PopupMenu pm) {
        try {
            Field field = pm.getClass().getDeclaredField("mPopup");
            field.setAccessible(true);
            Object helper = field.get(pm);
            // MenuPopupHelper mHelper = (MenuPopupHelper) field.get(pm);
            // mHelper.setForceShowIcon(true);
            helper.getClass().getDeclaredMethod("setForceShowIcon", boolean.class).invoke(helper, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
