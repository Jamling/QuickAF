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
package cn.ieclipse.af.app;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * /**
 * Fragment used to show progress dialog, it's suggested to use {@link android.app.DialogFragment} to show dialogs
 *
 * @author Jamling
 * @see cn.ieclipse.af.util.DialogUtils
 */
public class ProgressDialogFragment extends DialogFragment {
    private ProgressInterceptor interceptor;
    private OnCancelListener onCancelListener;
    
    public static ProgressDialogFragment newInstance(int style, String msg, ProgressInterceptor interceptor,
                                                     OnCancelListener listener) {
        ProgressDialogFragment frag = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putInt("style", style);
        args.putString("msg", msg);
        frag.setArguments(args);
        frag.setInterceptor(interceptor);
        frag.setOnCancelListener(listener);
        return frag;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        int style = getArguments().getInt("style");
        String msg = getArguments().getString("msg");
        if (style > 0) {
            dialog.setProgressStyle(style);
        }
        if (!TextUtils.isEmpty(msg)) {
            dialog.setMessage(msg);
        }
        if (onCancelListener != null) {
            dialog.setOnCancelListener(onCancelListener);
        }
        if (interceptor != null) {
            interceptor.onCreated(dialog);
        }
        return dialog;
    }
    
    public void setOnCancelListener(OnCancelListener onCancelListener) {
        this.onCancelListener = onCancelListener;
    }
    
    public void setInterceptor(ProgressInterceptor interceptor) {
        this.interceptor = interceptor;
    }
    
    public interface ProgressInterceptor {
        void onCreated(ProgressDialog dialog);
    }
}
