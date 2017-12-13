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
package cn.ieclipse.af.demo;

import android.app.Application;

import com.android.volley.DefaultRetryPolicy;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.ieclipse.af.volley.VolleyConfig;
import cn.ieclipse.af.volley.VolleyManager;

/**
 * Description
 *
 * @author Jamling
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.logo)
            .showImageOnFail(R.mipmap.logo).cacheOnDisk(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(options)
            .build();
        ImageLoader.getInstance().init(config);
        AppConfig.init(getApplicationContext());

        VolleyConfig vc = new VolleyConfig.Builder().setBaseResponseClass(AppConfig.VOLLEY_RESPONSE_CLASS)
            .setRetryPolicy(new DefaultRetryPolicy(AppConfig.VOLLEY_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)).setDebug(AppConfig.isDebug()).build();
        VolleyManager.init(getApplicationContext(), vc);
    }
}
