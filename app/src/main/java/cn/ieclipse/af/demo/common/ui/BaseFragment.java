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

import cn.ieclipse.af.common.ui.CommonBaseFragment;
import cn.ieclipse.af.demo.common.api.VolleyUtils;
import cn.ieclipse.af.volley.RestError;

/**
 * 类/接口描述
 *
 * @author harry
 */
public abstract class BaseFragment extends CommonBaseFragment {
    @Override
    public void toastError(RestError error) {
        super.toastError(error);
        VolleyUtils.toastError(getActivity(), error);
    }

    protected BaseActivity getBaseActivity() {
        if (getActivity() instanceof BaseActivity) {
            return (BaseActivity) getActivity();
        }
        return null;
    }

    public void startFragment(Class<? extends Fragment> fragmentClass) {
        getBaseActivity().startFragment(fragmentClass);
    }
}
