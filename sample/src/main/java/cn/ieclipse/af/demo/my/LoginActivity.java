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

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.demo.MainActivity;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.EncodeUtil;
import cn.ieclipse.af.view.CountDownButton;
import cn.ieclipse.af.view.RoundButton;
import cn.ieclipse.af.volley.RestError;

public class LoginActivity extends BaseActivity implements LoginController.LoginListener {

    private LoginController mLoginController;
    private int REQ_REGISTER = 0x12;

    private RadioGroup mRgType;
    private TextView mEtPhone;
    private TextView mEtCode;
    private Button mBtnLogin;
    private CountDownButton mBtnCode;

    private View mLayoutCode;
    private View mLayoutPwd;
    private TextView mEtPwd;
    private TextView mBtnReg;
    private TextView mBtnForgot;

    private TextView mReg;
    private View mLayoutInput;

    @Override
    protected int getContentLayout() {
        return R.layout.my_activity_login;
    }
    
    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(R.string.login_title);
        mTitleTextView.setTextColor(getResources().getColor(R.color.white));

        mReg = createRightText(getString(R.string.reg_title));
        mTitleBar.addRight(mReg);
        setOnClickListener(mReg);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mRgType = (RadioGroup) view.findViewById(R.id.rg_type);
        mLayoutCode = view.findViewById(R.id.ll_code);
        mEtPwd = (TextView) view.findViewById(R.id.et_pwd);
        mBtnReg = (TextView) view.findViewById(R.id.btn_reg);
        mBtnForgot = (TextView) view.findViewById(R.id.btn_forgot);

        mLayoutInput = view.findViewById(R.id.ll_input);
        mEtPwd = (TextView) view.findViewById(R.id.et_pwd);
        mEtPhone = (TextView) view.findViewById(R.id.et_phone);
        mEtCode = (TextView) view.findViewById(R.id.et_code);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);
        mBtnCode = (CountDownButton) view.findViewById(R.id.btn_get_code);

        setOnClickListener(mBtnCode, mBtnLogin, mBtnForgot, mBtnReg);

        mLayoutPwd = mEtPwd;
        int radius = AppUtils.dp2px(view.getContext(), 8);
        RoundedColorDrawable bg = new RoundedColorDrawable(AppUtils.getColor(view.getContext(), R.color.white));
        bg.setRadius(radius);
        bg.setBorder(AppUtils.getColor(view.getContext(), R.color.divider), 1);
        bg.applyTo(mLayoutInput);

        ((RoundButton) mBtnLogin).addStateBgColor(new int[]{-android.R.attr.state_enabled},
            AppUtils.getColor(this, R.color.black_999999)).apply();

        ColorStateList csl = new ColorStateList(new int[][]{{android.R.attr.state_enabled}, {}},
            new int[]{0, 0});// the second color is disabled color, the enabled color

        mBtnLogin.setEnabled(false);
        mEtPhone.addTextChangedListener(textWatcher);
        mEtPwd.addTextChangedListener(textWatcher);
    }

//    @Override
//    protected int getStatusBarColor() {
//        return android.R.color.transparent;
//    }

    @Override
    protected void initData() {
        mLoginController = new LoginController(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == mReg || view == mBtnReg) {
            RegisterActivity.go(this, REQ_REGISTER);
        }
        else if (view == mBtnLogin) {
            LoginRequest req = new LoginRequest();
            req.username = mEtPhone.getText().toString().trim();
            req.pwd = mEtPwd.getText().toString();
            req.pwd = EncodeUtil.getMd5(req.pwd);
            mLoginController.login(req);
        }
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
    }

    @Override
    public void onLoginSuccess(LoginResponse out) {
        AppConfig.login(out.token, out.userinfo);
        startActivity(MainActivity.create(this));
        finish();
    }

    @Override
    public void onLoginFailure(RestError error) {
        toastError(error);
    }

    @Override
    public void onLoginCodeSuccess(BaseResponse out) {
        String msg = getString(R.string.reg_code_sent);
        if (out != null && !TextUtils.isEmpty(out.getMessage())) {
            msg = out.getMessage();
        }
        DialogUtils.showToast(this, msg);
    }

    @Override
    public void onLoginCodeFail(RestError error) {
        toastError(error);
    }
    public static Intent create(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            boolean input = !TextUtils.isEmpty(mEtPhone.getText()) && !TextUtils.isEmpty(mEtPwd.getText());
            mBtnLogin.setEnabled(input);
        }
    };
}
