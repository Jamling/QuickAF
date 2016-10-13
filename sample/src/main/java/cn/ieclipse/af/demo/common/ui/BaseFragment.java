/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.common.ui;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ieclipse.af.app.AfFragment;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.KeyboardUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author harry
 */
public abstract class BaseFragment extends AfFragment implements View.OnClickListener {

    protected TextView mTitleLeftView;
    protected TextView mTitleTextView;
    protected abstract int getContentLayout();

    @Override
    public void onClick(View v) {

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

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleLeftView = (TextView) View.inflate(mTitleBar.getContext(), R.layout.title_left_tv, null);
        mTitleTextView = (TextView) View.inflate(mTitleBar.getContext(), R.layout.title_middle_tv, null);

        mTitleBar.setLeft(mTitleLeftView);
        mTitleBar.setMiddle(mTitleTextView);

        int padding = AppUtils.dp2px(mTitleBar.getContext(), 8);
        mTitleBar.setPadding(padding, 0, padding, 0);
        //if(!isOverlay())
        {
            mTitleBar.setBackgroundColor(AppUtils.getColor(mTitleBar.getContext(), R.color.colorPrimary));
            mTitleBar.setBottomDrawable(AppUtils.getColor(mTitleBar.getContext(), R.color.divider));
        }
        setOnClickListener(mTitleLeftView);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        KeyboardUtils.autoHideSoftInput(view);
    }

    protected DialogFragment mLoadingDialog;

    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.common_loading));
    }
    public void showLoadingDialog(final String message) {
        hideLoadingDialog();
        mLoadingDialog = DialogUtils.showProgress(getAfActivity(), android.R.style.Widget_Holo_Light_ProgressBar_Large,
            message, null);
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
        }
    }

    public void toastError(RestError error){
        hideLoadingDialog();
        VolleyUtils.toastError(getAfActivity(), error);
    }

    protected ImageView createRightIcon(int icon) {
        BaseActivity activity = getBaseActivity();
        return activity.createRightIcon(icon);
    }

    protected TextView createRightText(String text) {
        BaseActivity activity = getBaseActivity();
        return activity.createRightText(text);
    }

    protected BaseActivity getBaseActivity(){
        if (getActivity() instanceof BaseActivity){
            return (BaseActivity) getActivity();
        }
        return null;
    }

    protected CharSequence getTitle(){
        return getClass().getSimpleName();
    }

    public void startFragment(String fragmentClass){
        getBaseActivity().startFragment(fragmentClass);
    }

    public void startFragment(Intent intent){
        getBaseActivity().startFragment(intent);
    }
}
