package cn.ieclipse.af.util;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.core.app.ActivityCompat;

/**
 * Android 6.0 Runtime permission utils
 *
 * @author Jamling
 */

public class PermissionUtils {

    private static WeakReference<Object> mContext;

    //
//    private static PermissionUtils instance = new PermissionUtils();
//
//    public static PermissionUtils with(Activity activity) {
//        mContext = new WeakReference<Object>(activity);
//        return instance;
//    }
//
    private Object getContext() {
        if (mContext == null) {
            throw new IllegalStateException(
                "context is null! do you forget call " + PermissionUtils.class + ".with" + "()?");
        }
        return mContext.get();
    }

    public boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Object context = getContext();
            List<String> denied = null;
            if (context instanceof Fragment) {
                denied = getDeniedPermissions(((Fragment) context).getContext(), permissions);
            }
//            else if (context instanceof android.support.v4.Fragment){
//
//            }
            else if (context instanceof Activity) {
                denied = getDeniedPermissions((Context) context, permissions);
            }
            return denied == null || denied.isEmpty();
        }
        return true;
    }

    /**
     * Check the app whether has permissons
     *
     * @param context     context
     * @param permissions permissions
     *
     * @return true is granted permission
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> denied = getDeniedPermissions(context, permissions);
            return denied == null || denied.isEmpty();
        }
        return true;
    }

    public static List<String> getDeniedPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            List<String> denied = new ArrayList<>();
            if (permissions != null) {
                for (String p : permissions) {
                    if (!TextUtils.isEmpty(p)) {
                        if (context.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                            denied.add(p);
                        }
                    }
                }
            }
            return denied;
        }
        return null;
    }

//    public static void requestPermissions(android.support.v4.app.Fragment fragment, int code, String... permissions) {
//        if (Build.VERSION.SDK_INT >= 23) {
//            List<String> denied = getDeniedPermissions(fragment.getContext(), permissions);
//            if (denied != null && !denied.isEmpty()) {
//                fragment.requestPermissions(denied.toArray(new String[denied.size()]), code);
//            }
//        }
//    }

    private static void requestPermissionsInternal(Object context, int requestCode, PermissionExplainCallback callback,
                                                   String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            Fragment fragment = null;
            androidx.fragment.app.Fragment fragmentv4 = null;
            Activity activity = null;
            if (context instanceof Fragment) {
                fragment = (Fragment) context;
                activity = fragment.getActivity();
            }
            else if (context instanceof androidx.fragment.app.Fragment) {
                fragmentv4 = (androidx.fragment.app.Fragment) context;
                activity = fragmentv4.getActivity();
            }
            else if (context instanceof Activity) {
                activity = (Activity) context;
            }
            final List<String> denied = getDeniedPermissions(activity, permissions);
            if (denied != null && !denied.isEmpty()) {
                if (callback == null) {
                    if (fragment != null) {
                        fragment.requestPermissions(permissions, requestCode);
                    }
                    else if (fragmentv4 != null) {
                        fragmentv4.requestPermissions(permissions, requestCode);
                    }
                    else {
                        activity.requestPermissions(permissions, requestCode);
                    }
                }
                else {
                    for (int i = 0; i < denied.size(); i++) {
                        String permission = denied.get(i);
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                            callback.onRationale(permission);
                        }
                        else {
                            if (fragment != null) {
                                fragment.requestPermissions(new String[]{permission}, requestCode);
                            }
                            else if (fragmentv4 != null) {
                                fragmentv4.requestPermissions(new String[]{permission}, requestCode);
                            }
                            else {
                                activity.requestPermissions(new String[]{permission}, requestCode);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void requestPermissions(final Activity activity, final int requestCode,
                                          final PermissionExplainCallback callback, final String... permissions) {
        PermissionUtils.requestPermissionsInternal(activity, requestCode, callback, permissions);
    }

    public static void requestPermissions(final Activity activity, final int requestCode, final String... permissions) {
        PermissionUtils.requestPermissionsInternal(activity, requestCode, null, permissions);
    }

    public static void requestPermissions(final Fragment fragment, final int requestCode, final String... permissions) {
        PermissionUtils.requestPermissionsInternal(fragment, requestCode, null, permissions);
    }

    public static void requestPermissions(final androidx.fragment.app.Fragment fragment, final int requestCode,
                                          final String... permissions) {
        PermissionUtils.requestPermissionsInternal(fragment, requestCode, null, permissions);
    }

    public static void requestPermissions(final Fragment fragment, final int requestCode,
                                          final PermissionExplainCallback callback, final String... permissions) {
        PermissionUtils.requestPermissionsInternal(fragment, requestCode, callback, permissions);
    }

    public static void requestPermissions(final androidx.fragment.app.Fragment fragment, final int requestCode,
                                          final PermissionExplainCallback callback, final String... permissions) {
        PermissionUtils.requestPermissionsInternal(fragment, requestCode, callback, permissions);
    }

    /**
     * Call this method in {@link android.app.Activity#onRequestPermissionsResult(int, String[], int[])} or
     * {@link android.app.Fragment#onRequestPermissionsResult(int, String[], int[])}
     *
     * @param permissions  permissions
     * @param grantResults grant results
     *
     * @return all permission granted or not
     * @see android.app.Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean isGraned(String[] permissions, int[] grantResults) {
        boolean ret = true;
        for (int i = 0; i < grantResults.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    /**
     * If {@link androidx.core.app.ActivityCompat}#shouldShowRequestPermissionRationale(android.app.Activity,
     * String) , show an explain of request permission to user.
     *
     * @see ActivityCompat#shouldShowRequestPermissionRationale(android.app.Activity, String)
     */
    public interface PermissionExplainCallback {
        /**
         * Show permission explain
         *
         * @param permission permission
         *
         * @see ActivityCompat#shouldShowRequestPermissionRationale(android.app.Activity, String)
         */
        void onRationale(String permission);
    }
}
