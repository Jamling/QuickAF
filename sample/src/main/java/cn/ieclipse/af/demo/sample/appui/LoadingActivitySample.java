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
package cn.ieclipse.af.demo.sample.appui;

import android.view.View;

import cn.ieclipse.af.demo.R;
import cn.ieclipse.af.demo.common.ui.LoadingActivity;
import cn.ieclipse.af.util.SharedPrefsUtils;

/**
 * Description
 *
 * @author Jamling
 */
public class LoadingActivitySample extends LoadingActivity {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_loading;
    }

    private View mGuideView;
    private View btnRemove;

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        setTitle("Loading/Tip");
        btnRemove = createRightText("Reset", true);
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
        mLoadingView = View.inflate(this, R.layout.sample_layout_loading, null);
        mGuideView = View.inflate(this, R.layout.sample_layout_guide, null);
        mGuideView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                hideGuideView(v);
            }
        });
        showLoadingView(false);
        finishLoad();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn1) {
            showLoadingView(true);
            finishLoad();
        }
        else if (v.getId() == R.id.btn2) {
            showGuideView(mGuideView, "show_loading_guide");
        }
        else if (v == btnRemove) {
            SharedPrefsUtils.remove("show_loading_guide");
        }
        super.onClick(v);
    }

    private void finishLoad(){
        mTitleBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideLoadingView();
            }
        }, 2000);
    }
}
