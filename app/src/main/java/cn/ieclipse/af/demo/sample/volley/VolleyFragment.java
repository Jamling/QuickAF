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
package cn.ieclipse.af.demo.sample.volley;

import android.view.View;

import cn.ieclipse.af.demo.sample.ButtonListFragment;
import cn.ieclipse.af.demo.sample.volley.upload.UploadActivity;
import cn.ieclipse.af.demo.sample.volley.upload.UploadActivity2;
import cn.ieclipse.af.demo.sample.volley.weather.WeatherActivity;

/**
 * Description
 *
 * @author Jamling
 */
public class VolleyFragment extends ButtonListFragment {
    @Override
    public CharSequence getTitle() {
        return "Volley sample";
    }

    @Override
    protected void initHeaderView() {
        super.initHeaderView();
        mTitleTextView.setText(getTitle());
        mTitleLeftView.setVisibility(View.INVISIBLE);

        mTitleBar.setVisibility(View.GONE);
    }

    @Override
    protected Class[] getActivities() {
        return new Class[]{WeatherActivity.class, UploadActivity.class, UploadActivity2.class, AdapterDemoActivity
            .class};
    }

    @Override
    protected String[] getItems() {
        return super.getItems();
    }
}
