/*
 * Copyright (C) 2015-2016 QuickAF2
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
import cn.ieclipse.af.demo.common.ui.H5Activity;
import cn.ieclipse.af.demo.sample.SampleBaseFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class H5Sample extends SampleBaseFragment {
    @Override
    protected int getContentLayout() {
        return R.layout.sample_activity_h5;
    }

    @Override
    public void onClick(View v) {
        if (v == btn1) {
            String title = et1.getText().toString();
            String url = et2.getText().toString();
            startActivity(H5Activity.create(getActivity(), url, title));
        }
        super.onClick(v);
    }
}
