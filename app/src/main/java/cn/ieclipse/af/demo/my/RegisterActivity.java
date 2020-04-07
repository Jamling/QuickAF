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
package cn.ieclipse.af.demo.my;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.view.CountDownButton;
import cn.ieclipse.af.volley.RestError;
import cn.ieclipse.util.EncryptUtils;
import cn.ieclipse.util.RegexUtils;

/**
 * 注册
 *
 * @author Jamling
 */
public class RegisterActivity extends BaseActivity implements RegisterController.RegisterListener {

    private RegisterController mController;

    private TextView mEtPhone;
    private TextView mEtCode;
    private Button mBtnSubmit;
    private CountDownButton mBtnCode;

    private View mLayoutCode;
    private View mLayoutPwd;
    private TextView mEtPwd;
    private TextView mBtnForgot;

    private TextView mReg;
    private View mBtnReg;
    private View mLayoutInput;

    private String mPhone;

    @Override
    protected int getContentLayout() {
        return R.layout.my_activity_register;
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

        mLayoutCode = view.findViewById(R.id.ll_code);
        mEtPwd = (TextView) view.findViewById(R.id.et_pwd);
        mBtnForgot = (TextView) view.findViewById(R.id.btn_forgot);

        mLayoutInput = view.findViewById(R.id.ll_input);
        mEtPwd = (TextView) view.findViewById(R.id.et_pwd);
        mEtPhone = (TextView) view.findViewById(R.id.et_phone);
        mEtCode = (TextView) view.findViewById(R.id.et_code);
        mBtnSubmit = (Button) view.findViewById(R.id.btn_submit);
        mBtnCode = (CountDownButton) view.findViewById(R.id.btn_code);

        setOnClickListener(mBtnCode, mBtnSubmit, mBtnForgot);

        mLayoutPwd = mEtPwd;
        int radius = AppUtils.dp2px(view.getContext(), 8);
        RoundedColorDrawable bg = new RoundedColorDrawable(AppUtils.getColor(view.getContext(), R.color.white));
        bg.setRadius(radius);
        bg.setBorder(AppUtils.getColor(view.getContext(), R.color.divider), 1);
        bg.applyTo(mLayoutInput);
    }

    @Override
    protected void initData() {
        super.initData();
        mController = new RegisterController(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnCode) {
            if (validatePhone()) {
                mBtnCode.start();
                VerifyCodeRequest req = new VerifyCodeRequest();
                req.phone = mEtPhone.getText().toString();
                mController.getCode(req);
            }
        }
        else if (v == mBtnSubmit) {
            if (validate()) {
                RegisterRequest req = new RegisterRequest();
                req.phone = mPhone;
                req.code = mEtCode.getText().toString();
                req.password = EncryptUtils.encryptMd5(mEtPwd.getText().toString());
                mController.register(req);
            }
        }
        else if (v == mBtnForgot) {
            finish();
        }
        super.onClick(v);
    }

    private boolean validatePhone() {
        if (TextUtils.isEmpty(mEtPhone.getText())) {
            DialogUtils.showToast(this, mEtPhone.getHint());
            return false;
        }
        mPhone = mEtPhone.getText().toString();
        if (!RegexUtils.isMobilePhoneNumber(mPhone)) {
            DialogUtils.showToast(this, R.string.reg_hint_phone);
            return false;
        }
        return true;
    }

    private boolean validate() {
        if (!validatePhone()) {
            return false;
        }
        if (TextUtils.isEmpty(mEtCode.getText())) {
            DialogUtils.showToast(this, mEtCode.getHint());
            return false;
        }
        if (TextUtils.isEmpty(mEtPwd.getText())) {
            DialogUtils.showToast(this, mEtPwd.getHint());
            return false;
        }

        String pwd = mEtPwd.getText().toString();
        if (pwd.length() < 6 || pwd.length() > 16) {
            DialogUtils.showToast(this, "密码长度不正确");
            return false;
        }
        return true;
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
        toastError(error);
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
