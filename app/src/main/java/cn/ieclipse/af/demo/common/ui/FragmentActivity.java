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
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import cn.ieclipse.af.demo.R;

/**
 * A common activity to wrap a fragment (with empty constructor).
 *
 * @author Jamling
 */
public class FragmentActivity extends BaseActivity {

    public static final String EXTRA_SHOW_TITLE = "showActivityTitleBar";
    private String fragmentName;
    protected Fragment fragment;
    private boolean initHeaderView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            fragmentName = getIntent().getAction();
            if (fragmentName == null) {
                finish();
                return;
            }
            fragment = Fragment.instantiate(this, fragmentName, getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
            if (fragment instanceof BaseFragment && isShowTitleBar()) {
                setTitle(((BaseFragment) fragment).getTitle());
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.base_content;
    }

    @Override
    public void setShowTitleBar(boolean showTitleBar) {
        super.setShowTitleBar(showTitleBar);
        if (showTitleBar && mTitleBar != null && !initHeaderView) {
            initHeaderView = true;
            initHeaderView();
            if (fragment != null && fragment instanceof BaseFragment) {
                setTitle(((BaseFragment) fragment).getTitle());
            }
        }
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        setShowTitleBar(bundle.getBoolean(EXTRA_SHOW_TITLE, false));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EXTRA_SHOW_TITLE, isShowTitleBar());
        super.onSaveInstanceState(outState);
    }

    public static Intent create(Context context, @NonNull Class<? extends Fragment> fragmentClass,
                                boolean showTitleBar) {
        Intent intent = new Intent(context, FragmentActivity.class);
        intent.setAction(fragmentClass.getName());
        intent.putExtra(EXTRA_SHOW_TITLE, showTitleBar);
        return intent;
    }
}
