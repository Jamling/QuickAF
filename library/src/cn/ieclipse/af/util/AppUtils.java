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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;

import androidx.annotation.AnyRes;
import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;

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
        } else {
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

    public static int dimen2px(Context context, @DimenRes int dimenId) {
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
    public static int getColor(Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(colorId, context.getTheme());
        } else {
            return context.getResources().getColor(colorId);
        }
    }

    /**
     * Get color state list.
     *
     * @param context Context
     * @param colorId color state list defined in colors.xml
     * @return color state list
     * @see android.content.res.Resources#getColorStateList(int)
     */
    public static ColorStateList getColorStateList(Context context, @ColorRes int colorId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColorStateList(colorId, context.getTheme());
        } else {
            return context.getResources().getColorStateList(colorId);
        }
    }

    public static Drawable getDrawable(Context context, @DrawableRes int drawableId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(drawableId);
        } else {
            try {
                Drawable d = AppCompatResources.getDrawable(context, drawableId);
                return d;
            } catch (Resources.NotFoundException e) {
                return VectorDrawableCompat.create(context.getResources(), drawableId, context.getTheme());
            }
            // return context.getResources().getDrawable(drawableId);
        }
    }

    public static Drawable tintDrawable(Drawable drawable, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTint(color);
            return drawable;
        } else {
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, color);
            return wrappedDrawable;
        }
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable.setTintList(colors);
            return drawable;
        } else {
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTintList(wrappedDrawable, colors);
            return wrappedDrawable;
        }
    }

    public static Drawable tintDrawable(Context context, @DrawableRes int drawableId, @ColorRes int colorId) {
        int color = AppUtils.getColor(context, colorId);
        Drawable d = AppUtils.getDrawable(context, drawableId);
        return AppUtils.tintDrawable(d, color);
    }

    public static Drawable tintDrawableStateList(Context context, @DrawableRes int drawableId,
        @ColorRes int colorStateListId) {
        ColorStateList colorStateList = AppUtils.getColorStateList(context, colorStateListId);
        Drawable d = AppUtils.getDrawable(context, drawableId);
        return AppUtils.tintDrawable(d, colorStateList);
    }

    public static String getRes(Context context, @AnyRes int resId) {
        return String.format("android.resource://%s/%d", context.getPackageName(), resId);
    }

    public static String getRes(Context context, String type, String name) {
        return String.format("android.resource://%s/%s/%s", context.getPackageName(), type, name);
    }

    public static ActivityManager.MemoryInfo getMemory(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return info;
    }

    public static boolean hasNavigationBar(Context context) {
        boolean isHave = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            isHave = rs.getBoolean(id);
        } else {
            try {
                Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
                Method m = systemPropertiesClass.getMethod("get", String.class);
                String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
                if ("1".equals(navBarOverride)) {
                    isHave = false;
                } else if ("0".equals(navBarOverride)) {
                    isHave = true;
                }
            } catch (Exception e) {
                Log.w("TAG", e);
            }
        }

        return isHave;
    }

    /**
     * @param context
     * @return
     * @deprecated 如果是全面屏，则此方法无效
     */
    public static boolean isNavigationBarShow(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !menu && !back;
        }
    }

    public static int getNavigationBarHeight(Context context) {
        if (context instanceof Activity) {
            if (!isNavigationBarShow((Activity) context)) {
                return 0;
            }
        }
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        int height = context.getResources().getDimensionPixelSize(resourceId);
        return height;
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
            } else {
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

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getSDKVersion() {
        return Build.VERSION.RELEASE;
    }

    /**
     * Get string type meta-data for application node in AndroidManifest.xml
     *
     * @param context context
     * @param name meta-data name attribute
     * @param defaultValue default value
     * @return meta-data value
     * @since 3.0.1
     */
    public static String getAppMetaData(Context context, String name, @Nullable String defaultValue) {
        try {
            ApplicationInfo info = context.getPackageManager()
                .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(name, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static ApplicationInfo getAppInfo(Context context, @Nullable String packageName) {
        try {
            ApplicationInfo info = context.getPackageManager()
                .getApplicationInfo(packageName == null ? context.getPackageName() : packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return info;
        } catch (NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Get app version info.
     *
     * @param context Context
     * @return app current version name
     */
    public static String getAppVersion(Context context) {
        PackageInfo packInfo = AppUtils.getPackageInfo(context);
        if (packInfo != null) {
            return packInfo.versionName;
        }
        return null;
    }

    public static PackageInfo getPackageInfo(Context context) {
        return AppUtils.getPackageInfo(context, context.getPackageName());
    }

    /**
     * Get PackageInfo of assigned package
     *
     * @param context {@link android.content.Context}
     * @param packageName package name
     * @return PackageInfo
     * @since 3.0.1
     */
    public static PackageInfo getPackageInfo(Context context, String packageName) {
        // Get packagemanager
        PackageManager packageManager = context.getPackageManager();
        // 0 means get version info
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(packageName, 0);
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
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(activity.getApplicationContext(), activity.getClass()));
        // 发送广播
        activity.sendBroadcast(shortcut);
    }

    /**
     * Need {@link android.Manifest.permission#READ_PHONE_STATE} permission
     *
     * @param context context
     * @param imei default IMEI
     * @return imei
     */
    @SuppressLint("MissingPermission")
    public static String getImei(Context context, String imei) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            imei = telephonyManager.getDeviceId();
        } catch (Exception e) {
        }
        return imei;
    }

    /**
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> list = packageManager.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES);
        if (list != null) {
            for (ApplicationInfo info : list) {
                if (info.packageName.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     *
     * @param context 上下文
     * @return if application is in background return true, otherwise return false
     * @deprecated Fail from Android L
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager.RunningAppProcessInfo processInfo = am.getRunningAppProcesses().get(0);
            return context.getPackageName().equals(processInfo.pkgList[0]);
        }
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            return topActivity != null && !topActivity.getPackageName().equals(context.getPackageName());
        }
        return false;
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file APK文件
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
     * @param file APK文件uri
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
     * @param context 上下文
     * @param packageName 包名
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static void executeJs(WebView webView, String js, ValueCallback callback) {
        if (webView != null && js != null) {
            if (!js.startsWith("javascript:")) {
                js = "javascript:" + js;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(js, callback);
                } else {
                    webView.loadUrl(js);
                }
            }
        }
    }
}
