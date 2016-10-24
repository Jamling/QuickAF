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
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.api.BasePostRequest;
import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.common.ui.BaseFragment;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.DialogUtils;
import cn.ieclipse.af.util.FileUtil;
import cn.ieclipse.af.view.Preference;
import cn.ieclipse.af.view.RoundButton;
import cn.ieclipse.af.volley.RestError;

/**
 * Description
 *
 * @author Jamling
 */
public class MyFragment extends BaseFragment implements LogoutController.LogoutListener {

    private Preference mLayoutInfo;
    private Preference mLayoutPwd;
    private Preference mLayoutClearCache;
    private RoundButton mLayoutLogout;
    private View mBtnLogin;

    private LogoutController mLogoutController;
    @Override
    protected CharSequence getTitle() {
        return "个人中心";
    }

    @Override
    protected int getContentLayout() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
//        mBtnLogin = createRightText("登录");
//        mTitleBar.addRight(mBtnLogin);
//        setOnClickListener(mBtnLogin);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);

        mBtnLogin = view.findViewById(R.id.iv_profile);
        setOnClickListener(mBtnLogin);

        mLayoutInfo = (Preference) view.findViewById(R.id.layoutInfo);
        mLayoutPwd = (Preference) view.findViewById(R.id.layoutPwd);
        mLayoutClearCache = (Preference) view.findViewById(R.id.layoutClear);
        mLayoutLogout = (RoundButton) view.findViewById(R.id.layoutLogout);
        mLayoutLogout.setPressedBgColor(AppUtils.getColor(view.getContext(), R.color.colorPrimary));

        setOnClickListener(mLayoutInfo, mLayoutPwd, mLayoutClearCache, mLayoutLogout);
    }

    @Override
    protected void initData() {
        super.initData();
        mLogoutController = new LogoutController(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // if (mRefreshCacheSize)
        {
            calcCache();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mLayoutLogout) {
            mLoadingDialog = DialogUtils.showAlert(getActivity(), android.R.drawable.ic_dialog_alert, "退出",
                "确认注销当前账户并退出客户端？",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                }, DialogUtils.defaultOnClick());
        }
        else if (v == mLayoutPwd) {
            startActivity(ChangePwdActivity.create(getActivity()));
        }
        else if (v == mLayoutInfo) {
            startActivity(UserInfoActivity.create(getActivity()));
        }
        else if (v == mLayoutClearCache){
            clearCache();
        }
        else if (v == mBtnLogin) {
            startActivity(LoginActivity.create(getActivity()));
        }
        super.onClick(v);
    }

    private void logout() {
        showLoadingDialog();
        mLogoutController.logout(new BasePostRequest());
        AppConfig.logout();
    }

    @Override
    public void onLogoutSuccess(BaseResponse out) {
        hideLoadingDialog();
        getActivity().finish();
    }

    @Override
    public void onLogoutFailure(RestError error) {
        toastError(error);
        getActivity().finish();
    }

    private long calcSync() {
        long ret = 0l;
        File dir = FileUtil.getExternal(getActivity(), null);
        ret += FileUtil.getFileSize(dir);
        return ret;
    }

    private void calcCache() {
        final Activity mActivity = getActivity();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                return FileUtil.formatFileSize(calcSync());
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mLayoutClearCache.getSummaryWidget().setText((String) o);
            }
        };
        task.execute();
    }

    private void clearCache() {
        final Activity mActivity = getActivity();
        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                // TODO
                return FileUtil.formatFileSize(calcSync());
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(mActivity, "正在清空缓存，请稍候...", Toast.LENGTH_SHORT).show();
                // DialogUtils.showToast(getActivity(), "正在清空缓存，请稍候...");
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                mLayoutClearCache.getSummaryWidget().setText((String) o);
                DialogUtils.showToast(mActivity, "缓存已清空！.");
            }
        };
        task.execute();
    }
}
