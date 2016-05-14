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

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.widget.Toast;
import cn.ieclipse.af.app.AlertDialogFragment;
import cn.ieclipse.af.app.ProgressDialogFragment;
import cn.ieclipse.af.app.ProgressDialogFragment.ProgressInterceptor;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015/10/30.
 */
public final class DialogUtils {
    private DialogUtils() {
    }
    
    /**
     * Show a toast with long time
     *
     * @param context {@link android.content.Context}
     * @param msg     toast text
     *
     * @see android.widget.Toast#makeText(android.content.Context, CharSequence, int)
     */
    public static void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context context, int res){
        DialogUtils.showToast(context, context.getResources().getString(res));
    }
    
    private static void attachDialog(Activity context,
            DialogFragment fragment) {
        FragmentTransaction ft = context.getFragmentManager()
                .beginTransaction();
        Fragment prev = context.getFragmentManager()
                .findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragment.show(ft, "dialog");
    }
    
    /**
     * Custom AlertDialog with full parameters.
     *
     * @param context Activity context
     * @param icon icon res id see {@link android.R.drawable#ic_dialog_alert}
     * @param title dialog title
     * @param message dialog message
     * @param interceptor dialog display interceptor see {@link AlertDialogFragment.AlertInterceptor}
     * @param listeners dialog button onclick listeners see {@link android.content.DialogInterface.OnClickListener}
     */
    public static AlertDialogFragment showAlert(Activity context, int icon,
            String title, String message,
            AlertDialogFragment.AlertInterceptor interceptor,
            DialogInterface.OnClickListener... listeners) {
        AlertDialogFragment fragment = AlertDialogFragment.newInstance(icon,
                title, message, interceptor, listeners);
                
        attachDialog(context, fragment);
        return fragment;
    }
    
    public static AlertDialogFragment showAlert(Activity context, int icon,
            String title, String message,
            DialogInterface.OnClickListener... listeners) {
        return showAlert(context, icon, title, message, null, listeners);
    }
    
    /**
     * Custom AlertDialog with # {@link AlertDialogFragment.AlertInterceptor}.
     *
     * @param context Activity context
     * @param interceptor dialog display interceptor see {@link AlertDialogFragment.AlertInterceptor}
     */
    public static AlertDialogFragment showAlert(Activity context,
            AlertDialogFragment.AlertInterceptor interceptor) {
        return showAlert(context, 0, null, null, interceptor,
                (DialogInterface.OnClickListener) null);
    }
    
    /**
     * Generate a default dialog button onclick listener, it will do nothing in {@link android.content.DialogInterface.OnClickListener#onClick(DialogInterface, int)}
     *
     * @return empty {@link android.content.DialogInterface}
     */
    public static DialogInterface.OnClickListener defaultOnClick() {
        return new DialogInterface.OnClickListener() {
            
            @Override
            public void onClick(DialogInterface dialog, int which) {
            
            }
        };
    }
    
    public static ProgressDialogFragment showProgress(Activity context,
            int style, ProgressInterceptor interceptor,
            OnCancelListener listener) {
        ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(style, null, interceptor, listener);
        attachDialog(context, fragment);
        return fragment;
    }
    
    public static ProgressDialogFragment showProgress(Activity context, int style, String msg,
                                                      OnCancelListener listener) {
        ProgressDialogFragment fragment = ProgressDialogFragment.newInstance(style, msg, null, listener);
        attachDialog(context, fragment);
        return fragment;
    }
}
