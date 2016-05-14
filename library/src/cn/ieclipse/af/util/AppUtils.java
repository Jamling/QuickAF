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
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

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
    
    public static int sp2px(Context context, int sp) {
        DisplayMetrics dm = getDisplayMetrics(context);
        return (int) (sp * dm.scaledDensity + .5);
    }

    /**
     * Get color
     *
     * @param context Context
     * @param colorId color defined in colors.xml
     * @return color
     * @see android.content.res.Resources#getColor(int)
     */
    public static int getColor(Context context, int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorId, context.getTheme());
        }
        else {
            return context.getResources().getColor(colorId);
        }
    }

    public static Drawable getDrawable(Context context, int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(drawableId);
        }
        else {
            return context.getResources().getDrawable(drawableId);
        }
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
    
    public static String getModel() {
        return Build.MODEL;
    }
    
    public static String getSDKVersion() {
        return Build.VERSION.RELEASE;
    }
    
    /**
     * Get app version info.
     *
     * @param context
     * @return
     */
    public static String getAppVersion(Context context) {
        PackageInfo packInfo = getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionName;
        }
        return null;
    }
    
    public static PackageInfo getPackageInfo(Context context) {
        // Get packagemanager
        PackageManager packageManager = context.getPackageManager();
        // 0 means get version info
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            // mLogger.w("get version failed");
        }
        return packInfo;
    }
    
    /**
     * @param activity
     * @param bitmap
     * @param nameId
     * @see android.Manifest.permission#INSTALL_SHORTCUT
     */
    public static void createShortCut(Activity activity, Bitmap bitmap, int nameId) {

        Intent shortcut = new Intent(Intent.ACTION_CREATE_SHORTCUT);
        // 不允许重复创建
        shortcut.putExtra("duplicate", false);
        // 需要现实的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, activity.getString(nameId));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(activity.getApplicationContext(), 0);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
            new Intent(activity.getApplicationContext(), activity.getClass()));
        // 发送广播
        activity.sendBroadcast(shortcut);
    }

    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        return imei;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context 上下文
     * @return if application is in background return true, otherwise return
     * false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件uri
     */
    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 卸载apk
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }
}
