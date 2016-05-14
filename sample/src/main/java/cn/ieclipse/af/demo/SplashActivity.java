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
package cn.ieclipse.af.demo;

import cn.ieclipse.af.demo.common.ui.BaseActivity;
import cn.ieclipse.af.demo.my.LoginActivity;

/**
 * Description
 *
 * @author Jamling
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected int getContentLayout() {
        return 0;
    }

    @Override
    protected void initWindowFeature() {
        super.initWindowFeature();
        setShowTitleBar(false);
    }

    @Override
    protected void initData() {
        super.initData();

        if (AppConfig.isLogin()) {
            goHome();
            return;
        }

        goHome();
    }

    private void goHome() {
        startActivity(MainActivity.create(this));
        finish();
    }

    private void goLogin() {
        startActivity(LoginActivity.create(this));
        finish();
    }
}
