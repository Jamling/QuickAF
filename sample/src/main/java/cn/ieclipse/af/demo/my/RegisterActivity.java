/*
 * Copyright (C) 20015 MaiNaEr All rights reserved
 */
package cn.ieclipse.af.demo.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 注册
 *
 * @author Jamling
 */
public class RegisterActivity extends BaseActivity implements RegisterController.RegisterListener {

    private RegisterController mController;

    @Override
    protected int getContentLayout() {
        return 0;//R.layout.activity_register;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(R.string.reg_title);
        mTitleTextView.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
    }

    @Override
    protected void initData() {
        super.initData();
        mController = new RegisterController(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private void autoLogin() {
        LoginRequest req = new LoginRequest();
        //req.password = MD5.getMessageDigest(mEtPwd.getText().toString().getBytes());
        //req.phone = mEtPhone.getText().toString().trim();
        mController.login(req);
    }

    @Override
    public void onCodeSuccess(BaseResponse out) {
        DialogUtils.showToast(this, R.string.reg_code_sent);
    }

    @Override
    public void onRegisterSuccess(RegisterResponse out) {
        hideLoadingDialog();
        DialogUtils.showToast(this, R.string.reg_ok);

    }

    @Override
    public void onRegisterFail(RestError error) {
        hideLoadingDialog();
        toastError(error);
    }

    @Override
    public void onCodeFail(RestError error) {

    }

    @Override
    public void onLoginFail(RestError error) {
        hideLoadingDialog();
        toastError(error);
        finish();
    }

    @Override
    public void onLoginSuccess(LoginResponse out) {
        hideLoadingDialog();
        //MainaerConfig.onLogin(this, out);
        //goHome();
        goLogin();
    }

    public static void go(Context context, int code) {
        Intent intent = new Intent(context, RegisterActivity.class);
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, code);
        }
        else {
            context.startActivity(intent);
        }
    }

    private void goHome() {
        // Intent intent = new Intent(this, HomeActivity.class);
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // startActivity(intent);
    }

    private void goLogin() {
        setResult(RESULT_OK);
        finish();
    }
}
