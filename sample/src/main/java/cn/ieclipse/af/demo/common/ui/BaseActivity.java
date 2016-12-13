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
package cn.ieclipse.af.demo.common.ui;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.ieclipse.af.app.AfActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.KeyboardUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author Harry
 */
public abstract class BaseActivity extends AfActivity implements View.OnClickListener {
    protected TextView mTitleLeftView;
    protected TextView mTitleTextView;

    @Override
    public void onClick(View v) {
        if (v == mTitleLeftView) { // default back.
            finish();
        }
    }

    protected void initIntent(Bundle bundle) {

    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        setWindowBackground(android.R.color.white);
        setImmersiveMode(true);
    }

    protected void initContentView(View view) {

    }

    protected void initHeaderView() {
        mTitleLeftView = (TextView) View.inflate(this, R.layout.title_left_tv, null);
        mTitleTextView = (TextView) View.inflate(this, R.layout.title_middle_tv, null);

        mTitleBar.setLeft(mTitleLeftView);
        mTitleBar.setMiddle(mTitleTextView);

        int padding = AppUtils.dp2px(this, 8);
        mTitleBar.setPadding(padding, 0, padding, 0);
        if(!isOverlay()){
            mTitleBar.setBackgroundColor(getStatusBarColor());
            // mTitleBar.setBottomDrawable(AppUtils.getColor(this, R.color.divider));
        }
        setOnClickListener(mTitleLeftView);

    }

    protected void initData() {

    }

    protected void initBottomView() {

    }

    @Deprecated
    protected ImageView createRightIcon(int icon) {
        ImageView iv = (ImageView) View.inflate(this, R.layout.title_right_iv, null);
        if (icon > 0) {
            iv.setImageResource(icon);
        }
        return iv;
    }

    protected ImageView createRightIcon(int icon, boolean add){
        ImageView iv = createRightIcon(icon);
        if (add){
            mTitleBar.addRight(iv);
        }
        setOnClickListener(iv);
        return iv;
    }

    @Deprecated
    protected TextView createRightText(String text) {
        TextView tv = (TextView) View.inflate(this, R.layout.title_right_tv, null);
        tv.setText(text);
        return tv;
    }

    protected TextView createRightText(String text, boolean add) {
        TextView tv = createRightText(text);
        if (add){
            mTitleBar.addRight(tv);
        }
        setOnClickListener(tv);
        return tv;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardUtils.autoHideSoftInput(this, ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected int getStatusBarColor() {
        return AppUtils.getColor(this, R.color.colorPrimary);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void setTitle(CharSequence title) {
        //super.setTitle(title);
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        //super.setTitle(titleId);
        if (mTitleTextView != null) {
            mTitleTextView.setText(titleId);
        }
    }

    protected DialogFragment mLoadingDialog;

    public void showLoadingDialog() {
        showLoadingDialog(getString(R.string.common_loading));
    }

    public void showLoadingDialog(final String message) {
        hideLoadingDialog();
        mLoadingDialog = DialogUtils.showProgress(this, android.R.style.Widget_Holo_Light_ProgressBar_Large, message,
            null);
    }

    public void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismissAllowingStateLoss();
        }
    }

    public void toastError(RestError error){
        hideLoadingDialog();
        VolleyUtils.toastError(this, error);
    }

    public void startFragment(String fragmentClass){
        Intent intent = new Intent(fragmentClass);
        startFragment(intent);
    }

    public void startFragment(Intent intent){
        FragmentActivity.startFragment(this, intent);
    }

    /**
     * Example of starting nested fragment from another fragment: Fragment
     * newFragment = ManagerTagFragment.newInstance(tag.getMac()); TagsActivity
     * tAct = (TagsActivity)getActivity(); tAct.pushFragments(newFragment, true,
     * true, null);
     */
    public void pushFragments(Fragment fragment, boolean shouldAnimate,
                              boolean shouldAdd, String tag) {
        FragmentManager manager = getFragmentManager();// getFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        if (shouldAnimate) {
            // ft.setCustomAnimations(R.animator.fragment_slide_left_enter,
            // R.animator.fragment_slide_left_exit,
            // R.animator.fragment_slide_right_enter,
            // R.animator.fragment_slide_right_exit);
        }
        ft.replace(R.id.main_content, fragment, tag);
        // ft.attach(fragment);

        if (shouldAdd) {
            /*
             * here you can create named backstack for realize another logic.
             * ft.addToBackStack("name of your backstack");
             */
            ft.addToBackStack(null);
        } else {
            /*
             * and remove named backstack:
             * manager.popBackStack("name of your backstack",
             * FragmentManager.POP_BACK_STACK_INCLUSIVE); or remove whole:
             * manager.popBackStack(null,
             * FragmentManager.POP_BACK_STACK_INCLUSIVE);
             */
            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        ft.commit();
    }

    //protected static Intent create(Context context, Object... params);
}
