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
package cn.ieclipse.af.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.lang.reflect.Method;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/29.
 */
public final class AppUtils {
    private AppUtils() {
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        Resources res;
        if (context == null) {
            res = Resources.getSystem();
        }
        else {
            res = context.getResources();
        }
        return res.getDisplayMetrics();
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrics(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrics(context).heightPixels;
    }

    public static int dp2px(Context context, float dip) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return (int) (dip * dm.density + .5);
    }

    public static int dp2px(Context context, int dip) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return (int) (dip * dm.density + .5);
    }

    public static int dimen2px(Context context, int dimenId) {
        return context.getResources().getDimensionPixelOffset(dimenId);
    }

    public static boolean hasVirtualSoftKey(Context context) {
        return ViewConfiguration.get(context).hasPermanentMenuKey();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getSoftKeyBarHeight(Context context) {
        // getRealMetrics is only available with API 17 and +
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            wm.getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight) {
                return realHeight - usableHeight;
            }
            else {
                return 0;
            }
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setPopupWindowModal(PopupWindow popupWindow, boolean modal) {
        try {
            Method method = PopupWindow.class.getDeclaredMethod("setTouchModal", boolean.class);
            method.setAccessible(true);
            method.invoke(popupWindow, modal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
