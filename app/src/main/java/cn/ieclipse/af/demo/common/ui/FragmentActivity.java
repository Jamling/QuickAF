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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
            getSupportFragmentManager().beginTransaction().add(R.id.content, fragment).commit();
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
    public void onClick(View v) {
        if (v == mTitleLeftView) {
            // 如果fragment没有处理back，则默认为finish Activity
            if (fragment instanceof BaseFragment) {
                if (!((BaseFragment) fragment).handleBack()) {
                    finish();
                    return;
                }
            }
        }
        super.onClick(v);
    }

    // onBackPressed 在某些设备上不会调用，需要自己处理Key事件
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handle = super.dispatchKeyEvent(event);
        // TODO 加if
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
//            if (event.getAction() == KeyEvent.ACTION_UP) {
//                onBackPressed();
//            }
//            return true;
//        }
        return handle;
    }

    @Override
    public void onBackPressed() {
        if (fragment instanceof BaseFragment) {
            if (((BaseFragment) fragment).handleBack()) {
                return;
            }
        }
        super.onBackPressed();
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
