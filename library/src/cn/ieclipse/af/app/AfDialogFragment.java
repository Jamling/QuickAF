/*
 * Copyright (C) 2015-2016 QuickAF
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
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Abstract dialog fragment to build complex dialog.
 *
 * @author Jamling
 */
public abstract class AfDialogFragment<DialogListener> extends DialogFragment implements View.OnClickListener,
    DialogInterface.OnClickListener {
    protected DialogListener listener;
    private boolean isAlert = false;
    private boolean canceledOnTouchOutside = true;

    public static final String EXTRA_ALERT = "AlertDialog";
    public static final String EXTRA_CANCEL_OUTSIDE = "CanceledOnTouchOutside";

    public void setDialogListener(DialogListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            initIntent(bundle);
        }
        if (isAlert) {
            return null;
        }
        View view = inflater.inflate(getContentLayout(), container, false);
        initContentView(view);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;
        if (isAlert) {
            dialog = createAlertBuilder().create();
        }
        else {
            dialog = super.onCreateDialog(savedInstanceState);
        }
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
        return dialog;
    }

    protected void onCreateAlertDialog(AlertDialog.Builder builder) {
        builder.setPositiveButton(android.R.string.ok, this);
        builder.setNegativeButton(android.R.string.cancel, this);
    }

    protected void initContentView(View view) {

    }

    protected abstract int getContentLayout();

    @Override
    public void onClick(View v) {

    }

    protected void onOkPressed() {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            onOkPressed();
        }
    }

    public void setAlert(boolean alert) {
        isAlert = alert;
    }

    public void setCanceledOnTouchOutside(boolean cancelable) {
        canceledOnTouchOutside = cancelable;
    }

    protected void initIntent(Bundle bundle) {
        isAlert = bundle.getBoolean(EXTRA_ALERT);
        canceledOnTouchOutside = bundle.getBoolean(EXTRA_CANCEL_OUTSIDE, canceledOnTouchOutside);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_ALERT, isAlert);
        outState.putBoolean(EXTRA_CANCEL_OUTSIDE, canceledOnTouchOutside);
        super.onSaveInstanceState(outState);
    }

    private AlertDialog.Builder createAlertBuilder() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), getTheme());
        View view = View.inflate(getActivity(), getContentLayout(), null);
        initContentView(view);
        builder.setView(view);
        onCreateAlertDialog(builder);
        return builder;
    }

    public void show(FragmentManager manager, boolean shouldAdd) {
        String tag = getClass().getName();
        FragmentTransaction ft = manager.beginTransaction();
        Fragment prev = manager.findFragmentByTag(tag);
        if (prev != null) {
            ft.remove(prev);
        }
        if (shouldAdd) {
            ft.addToBackStack(null);
        }
        else {
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        show(ft, tag);
    }

    protected void setOnClickListener(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.setOnClickListener(this);
                }
            }
        }
    }

    public interface DefaultDialogListener {
        void onDialogResult(AfDialogFragment dialogFragment, Bundle bundle);
    }

    public interface CancelDialogListner {
        void onCancel(AfDialogFragment dialogFragment);
    }
}
