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
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class AfFragmentSample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_af_fragment;
    }

    @Override
    protected void initContentView(View view) {
        super.initContentView(view);
    }

    @Override
    protected void initInitData() {
        super.initInitData();
        // fragment title bar default hidden.
        setShowTitleBar(true);
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(getTitle() + "(fragment)");
    }

    @Override
    public void onClick(View v) {
        if (btn1 == v) {
            getBaseActivity().setOverlay(!getBaseActivity().isOverlay());
        }
        else if (btn2 == v) {
            getBaseActivity().setShowTitleBar(!getBaseActivity().isShowTitleBar());
        }
        else if (btn3 == v) {
            setOverlay(!isOverlay());
        }
        else if (btn4 == v) {
            setShowTitleBar(!isShowTitleBar());
        }
        super.onClick(v);
    }
}
