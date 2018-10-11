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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Method;

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
    private View contentView;

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
        if (contentView == null) {
            contentView = inflater.inflate(getContentLayout(), container, false);
            initContentView(contentView);
        }
        return contentView;
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

    /**
     * Set content view for dialog
     *
     * @param contentView root content view
     *
     * @since 2.1.1
     */
    public void setContentView(View contentView) {
        this.contentView = contentView;
    }

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
        if (Build.VERSION.SDK_INT >= 17 && manager.isDestroyed()) {
            return;
        }
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
            try {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (IllegalStateException e) {
                // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
                try {
                    showAllowingStateLossReflect(manager, tag);
                    return;
                } catch (Exception ex) {

                }
            }
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        try {
            show(ft, tag);
        } catch (IllegalStateException e) {
            // java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
        } catch (Exception e) {
            // TODO
        }
    }

    private void showAllowingStateLossReflect(FragmentManager manager, String tag) throws Exception {
        if (tag == null) {
            tag = getClass().getName();
        }
        Method m = DialogFragment.class.getDeclaredMethod("showAllowingStateLoss", FragmentManager.class, String.class);
        m.invoke(this, manager, tag);
    }

    /**
     * Show {@link android.app.DialogFragment} to a fragment
     *
     * @param fragment {@link android.app.Fragment}
     * @param showAdd  add to {@link android.app.FragmentManager.BackStackEntry } or not, default false
     *
     * @since 2.1.1
     */
    public void show(Fragment fragment, boolean showAdd) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            show(fragment.getChildFragmentManager(), showAdd);
        }
        else {
            show(fragment.getFragmentManager(), showAdd);
        }
    }

    /**
     * Show {@link android.app.DialogFragment} to an activity
     *
     * @param activity {@link android.app.Activity}
     * @param showAdd  add to {@link android.app.FragmentManager.BackStackEntry } or not, default false
     *
     * @since 2.1.1
     */
    public void show(Activity activity, boolean showAdd) {
        show(activity.getFragmentManager(), showAdd);
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

    /**
     * New a simple dialog fragment
     *
     * @param view content view
     *
     * @return a simple {@link cn.ieclipse.af.app.AfDialogFragment}
     * @since 2.1.1
     */
    public static AfDialogFragment newSimple(View view) {
        AfDialogFragment fragment = new AfDialogFragment.Simple();
        fragment.setContentView(view);
        return fragment;
    }

    public interface DefaultDialogListener {
        void onDialogResult(AfDialogFragment dialogFragment, Bundle bundle);
    }

    public interface CancelDialogListner {
        void onCancel(AfDialogFragment dialogFragment);
    }

    /**
     * A simple {@link cn.ieclipse.af.app.AfDialogFragment}
     *
     * @since 2.1.1
     */
    public static class Simple extends AfDialogFragment<AfDialogFragment.DefaultDialogListener> {

        @Override
        protected int getContentLayout() {
            return 0;
        }
    }
}
