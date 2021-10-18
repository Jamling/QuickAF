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
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

/**
 * Fragment used to show alert dialog, it's suggested to use {@link android.app.DialogFragment} to show dialogs
 *
 * @author Jamling
 * @see cn.ieclipse.af.util.DialogUtils
 */
public class AlertDialogFragment extends DialogFragment {

    private DialogInterface.OnClickListener[] mListeners;
    private AlertInterceptor mInterceptor;

    public static AlertDialogFragment newInstance(int icon, CharSequence title, CharSequence msg,
        AlertInterceptor interceptor, DialogInterface.OnClickListener... listeners) {
        AlertDialogFragment frag = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putInt("icon", icon);
        args.putCharSequence("title", title);
        args.putCharSequence("msg", msg);
        frag.setArguments(args);
        frag.setListeners(listeners);
        frag.setInterceptor(interceptor);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        CharSequence title = args.getCharSequence("title");
        int icon = args.getInt("icon");
        CharSequence msg = args.getCharSequence("msg");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());

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
                l = mListeners[index];
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
