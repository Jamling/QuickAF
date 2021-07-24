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

import cn.ieclipse.af.demo.sample.ButtonListFragment;

/**
 * Description
 *
 * @author Jamling
 */
public class TabAppFragment extends ButtonListFragment {
    @Override
    public CharSequence getTitle() {
        return "App struct";
    }

    @Override
    protected Class[] getActivities() {
        return new Class[]{
            SampleAfActivityActivity.class, AfActivityImmersiveSample.class, LoadingActivitySample.class, FileChooserSample.class,
            H5Sample.class, AfFragmentSample.class, RadioTabSample.class, ImageBrowserSample.class, AfDownloadSample
            .class, SearchSample.class
        };
    }
}
