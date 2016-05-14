/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.ieclipse.af.R;
import cn.ieclipse.af.view.TitleBar;

/**
 * Top Fragment of QuickAF, typically it's a BaseFragment in App module to extend this fragment to customize the app
 * Basic UI/Interaction
 *
 * @author Jamling
 */
public abstract class AfFragment extends Fragment implements View.OnClickListener {

    protected AfActivity mActivity;
    protected TitleBar mTitleBar;
    
    protected abstract int getContentLayout();
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (savedInstanceState != null) {
            bundle = savedInstanceState;
        }
        if (bundle != null) {
            initIntent(bundle);
        }
        View view = inflater.inflate(getContentLayout(), container, false);
        mTitleBar = (TitleBar)view.findViewById(R.id.titleBar);
        if (mTitleBar != null) {
            initHeaderView();
        }
        initContentView(view);
        initData();
        return view;
    }
    
    @Override
    public void onClick(View v) {

    }
    
    protected void setOnClickListener(View... views) {
        if (views != null) {
            for (View view : views) {
                if (view != null) {
                    view.setOnClickListener(this);
                }
            }
        }
    }
    
    protected void initIntent(Bundle bundle) {

    }

    protected void initHeaderView(){

    }
    
    protected void initContentView(View view) {

    }
    
    protected void initData() {

    }
    
    protected AfActivity getAfActivity() {
        Activity context = getActivity();
        if (context == null) {
            if (getView() != null && getView().getContext() instanceof Activity) {
                context = (Activity) getView().getContext();
            }
        }
        if (context instanceof AfActivity) {
            return (AfActivity) context;
        }
        return null;
    }
}
