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
package cn.ieclipse.af.common.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import cn.ieclipse.af.app.AfFragment;
import cn.ieclipse.af.common.R;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.KeyboardUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author harry
 */
public abstract class CommonBaseFragment extends AfFragment implements View.OnClickListener {

    protected TextView mTitleLeftView;
    protected TextView mTitleTextView;

    protected abstract int getContentLayout();

    @Override
    public void onClick(View v) {
        if (v == mTitleLeftView) {
            if (!handleBack()) {
                getActivity().finish();
            }
        }
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
    protected void initInitData() {
        super.initInitData();
        if (!getBaseActivity().isShowTitleBar()) {
            setShowTitleBar(true);
        }
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleBar.getConfig().setMinHeight(AppUtils.dimen2px(getContext(), R.dimen.title_bar_height));
        mTitleLeftView = (TextView) mTitleBar.createItem(R.layout.common_title_left_tv);
        mTitleTextView = (TextView) mTitleBar.createItem(R.layout.common_title_middle_tv);

        mTitleBar.setLeft(mTitleLeftView);
        mTitleBar.setMiddle(mTitleTextView);

        int padding = AppUtils.dp2px(getContext(), 8);
        mTitleBar.setPadding(padding, 0, padding, 0);
        if (!isOverlay()) {
            mTitleBar.setBackgroundColor(AppUtils.getColor(getContext(), R.color.colorPrimary));
            // mTitleBar.setBottomDrawable(AppUtils.getColor(this, R.color.divider));
        }
        mTitleTextView.setText(getTitle());
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
        mLoadingDialog = DialogUtils.showProgress(getActivity(), R.style.Widget_AppCompat_ProgressBar,
            message, null);
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
        }
    }

    public void toastError(RestError error) {
        hideLoadingDialog();
    }

    @Deprecated
    protected ImageView createRightIcon(int icon) {
        CommonBaseActivity activity = getBaseActivity();
        return activity.createRightIcon(icon, false);
    }

    protected ImageView createRightIcon(int icon, boolean add) {
        ImageView iv = createRightIcon(icon);
        if (add) {
            mTitleBar.addRight(iv);
        }
        setOnClickListener(iv);
        return iv;
    }

    @Deprecated
    protected TextView createRightText(String text) {
        CommonBaseActivity activity = getBaseActivity();
        return activity.createRightText(text, false);
    }

    protected TextView createRightText(String text, boolean add) {
        TextView tv = createRightText(text);
        if (add) {
            mTitleBar.addRight(tv);
        }
        setOnClickListener(tv);
        return tv;
    }

    protected CommonBaseActivity getBaseActivity() {
        if (getActivity() instanceof CommonBaseActivity) {
            return (CommonBaseActivity) getActivity();
        }
        return null;
    }

    public CharSequence getTitle() {
        if (mTitleTextView != null && !TextUtils.isEmpty(mTitleTextView.getText())) {
            return mTitleTextView.getText();
        }
        return getClass().getSimpleName();
    }

    public void setTitle(CharSequence title) {
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    public boolean handleBack() {
        FragmentManager fm;
        if (Build.VERSION.SDK_INT >= 17) {
            fm = getChildFragmentManager();
            if (fm.popBackStackImmediate()) {
                return true;
            }
        }
        fm = getFragmentManager();
        return fm.popBackStackImmediate();
    }
}
