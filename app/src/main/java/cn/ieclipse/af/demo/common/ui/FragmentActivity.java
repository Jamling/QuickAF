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

    private static final String EXTRA_FRAGMENT = "cn.ieclipse.af.fragment";
    public static final String EXTRA_SHOW_TITLE = "showActivityTitleBar";
    //private Class<?> fragmentClass = null;
    private String fragmentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            fragmentName = getIntent().getAction();
            if (fragmentName == null) {
                finish();
                return;
            }
            Fragment f = Fragment.instantiate(this, fragmentName, getIntent().getExtras());
            getFragmentManager().beginTransaction().add(R.id.content, f).commit();
            if (f instanceof BaseFragment && isShowTitleBar()) {
                setTitle(((BaseFragment) f).getTitle());
            }
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.base_content;
    }

    @Override
    protected void initIntent(Bundle bundle) {
        super.initIntent(bundle);
        setShowTitleBar(bundle.getBoolean(EXTRA_SHOW_TITLE, true));
        //fragmentClass = (Class<?>) bundle.getSerializable(EXTRA_FRAGMENT);
        //bundle.remove(EXTRA_FRAGMENT);
        //fragmentName = getIntent().getAction();
    }

    public static void startFragment(Context context, @NonNull Class<? extends Fragment> fragmentClass) {
        // Intent target = new Intent(fragmentClass.getName());
        Intent intent = new Intent(context, FragmentActivity.class);
        intent.setAction(fragmentClass.getName());
        context.startActivity(intent);
    }

    public static void startFragment(Context context, Intent intent) {
        if (intent.getAction() == null) {
            throw new NullPointerException("can't start fragment with null action");
        }
        intent.setClass(context, FragmentActivity.class);
        context.startActivity(intent);
    }
}
