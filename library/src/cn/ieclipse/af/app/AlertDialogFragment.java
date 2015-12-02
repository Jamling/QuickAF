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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月26日
 *       
 */
public class AlertDialogFragment extends DialogFragment {
    
    private DialogInterface.OnClickListener[] mListeners;
    private AlertInterceptor mInterceptor;
    
    public static AlertDialogFragment newInstance(int icon, String title,
            String msg, AlertInterceptor interceptor,
            DialogInterface.OnClickListener... listeners) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("icon", icon);
        args.putString("title", title);
        args.putString("msg", msg);
        frag.setArguments(args);
        frag.setListeners(listeners);
        frag.setInterceptor(interceptor);
        return frag;
    }
    
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title");
        int icon = args.getInt("icon");
        String msg = args.getString("msg");
        
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        
        if (icon > 0) {
            builder.setIcon(icon);
        }
        if (title != null) {
            builder.setTitle(title);
        }
        if (msg != null) {
            builder.setMessage(msg);
        }
        
        if (mListeners != null) {
            DialogInterface.OnClickListener l;
            int index = 0;
            if (mListeners.length > index) {
                l = mListeners[0];
                if (l != null) {
                    builder.setPositiveButton(android.R.string.ok, l);
                }
            }
            index++;
            
            if (mListeners.length > index) {
                l = mListeners[0];
                if (l != null) {
                    builder.setNegativeButton(android.R.string.cancel, l);
                }
            }
        }
        
        if (mInterceptor != null) {
            mInterceptor.onCreated(builder);
        }
        return builder.create();
    }
    
    public void setListeners(DialogInterface.OnClickListener... listeners) {
        this.mListeners = listeners;
    }
    
    public void setInterceptor(AlertInterceptor interceptor) {
        this.mInterceptor = interceptor;
    }
    
    public interface AlertInterceptor {
        void onCreated(AlertDialog.Builder builder);
    }
}
