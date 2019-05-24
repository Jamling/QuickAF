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
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.graphics.RoundedColorDrawable;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.EncodeUtils;
import cn.ieclipse.af.util.EncryptUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class ChangePwdActivity extends BaseActivity implements ChangePwdController.ChangePwdListener {
    private View mLayoutInput;
    private View mSubmit;
    private EditText mEtPwd;
    private EditText mEtRepeat;
    private ChangePwdController mChangePwdController = new ChangePwdController(this);

    @Override
    protected int getContentLayout() {
        return R.layout.my_activity_change_pwd;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle(R.string.change_pwd_title);


        TextView mLogin = createRightText("登录");
        mTitleBar.addRight(mLogin);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.create(mTitleBar.getContext()));
            }
        });
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mLayoutInput = view.findViewById(R.id.ll_input);
        int radius = AppUtils.dp2px(view.getContext(), 8);
        RoundedColorDrawable bg = new RoundedColorDrawable(AppUtils.getColor(view.getContext(), R.color.white));
        bg.setRadius(radius);
        bg.setBorder(AppUtils.getColor(view.getContext(), R.color.divider), 1);
        bg.applyTo(mLayoutInput);

        mEtPwd = (EditText) view.findViewById(R.id.et_pwd);
        mEtRepeat = (EditText) view.findViewById(R.id.et_repeat);

        mSubmit = view.findViewById(R.id.btn_submit);
        setOnClickListener(mSubmit);
    }

    @Override
    public void onClick(View v) {
        if (v == mSubmit) {
            doSubmit();
            return;
        }
        super.onClick(v);
    }

    protected void doSubmit() {
        String oldpwd = mEtPwd.getText().toString();
        String newpwd = mEtRepeat.getText().toString();
        if (TextUtils.isEmpty(mEtPwd.getText())) {
            DialogUtils.showToast(this, mEtPwd.getHint().toString());
            return;
        }
        if (!newpwd.equals(oldpwd)) {
            DialogUtils.showToast(this, R.string.change_pwd_error_repeat);
            return;
        }
        ChangePwdRequest req = new ChangePwdRequest();
        req.oldpwd = EncryptUtils.encryptMd5(oldpwd);
        req.newpwd = EncryptUtils.encryptMd5(newpwd);
        mChangePwdController.changePwd(req);
    }

    @Override
    public void onChangePwdFailure(RestError error) {
        toastError(error);
    }

    @Override
    public void onChangePwdSuccess(BaseResponse out) {
        DialogUtils.showToast(this, out.message);
        finish();
    }

    public static Intent create(Context context){
        Intent intent = new Intent(context, ChangePwdActivity.class);
        return intent;
    }
}
