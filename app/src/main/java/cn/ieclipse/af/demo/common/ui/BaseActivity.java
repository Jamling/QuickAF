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

import android.app.Fragment;
import android.content.Intent;

import cn.ieclipse.af.common.ui.CommonBaseActivity;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author Harry
 */
public abstract class BaseActivity extends CommonBaseActivity {
    @Override
    public void toastError(RestError error) {
        super.toastError(error);
        VolleyUtils.toastError(this, error);
    }

    /**
     * Start {@link cn.ieclipse.af.demo.common.ui.FragmentActivity} with a {@link android.app.Fragment}
     *
     * @param fragmentClass class of {@link android.app.Fragment}
     *
     * @since 2.1.1
     */
    public void startFragment(Class<? extends Fragment> fragmentClass) {
        Intent intent = FragmentActivity.create(this, fragmentClass, false);
        startActivity(intent);
    }
}
