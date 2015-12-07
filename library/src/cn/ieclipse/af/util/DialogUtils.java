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
    
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
        // ft.commitAllowingStateLoss();
        
    }
    
    /**
     * Custom AlertDialog with full parameters.
     * 
     * @param context
     * @param icon
     * @param title
     * @param message
     * @param interceptor
     * @param listeners
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
     * @param context
     * @param interceptor
     */
    public static AlertDialogFragment showAlert(Activity context,
            AlertDialogFragment.AlertInterceptor interceptor) {
        return showAlert(context, 0, null, null, interceptor,
                (DialogInterface.OnClickListener) null);
    }
    
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
        ProgressDialogFragment fragment = ProgressDialogFragment
                .newInstance(style, interceptor, listener);
        attachDialog(context, fragment);
        return fragment;
    }
    
    public static ProgressDialogFragment showProgress(Activity context,
            int style, OnCancelListener listener) {
        ProgressDialogFragment fragment = ProgressDialogFragment
                .newInstance(style, null, listener);
        attachDialog(context, fragment);
        return fragment;
    }
}
