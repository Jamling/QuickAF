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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.sample.volley.adapter.CollectionsAdapter;
import cn.ieclipse.af.demo.sample.volley.adapter.IntAdapter;
import cn.ieclipse.af.demo.sample.volley.adapter.StringAdapter;
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
//        DisplayImageOptions options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.logo)
//            .showImageOnFail(R.mipmap.logo).cacheOnDisk(true).build();
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions
// (options)
//            .build();
//        ImageLoader.getInstance().init(config);
        AppConfig.init(getApplicationContext());

        VolleyConfig vc = new VolleyConfig.Builder().setBaseResponseClass(BaseResponse.class).setRetryPolicy(
            new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)).setJsonParser(getJsonPaser()).build();
        VolleyManager.init(getApplicationContext(), vc);
    }

    private Gson getJsonPaser() {
        GsonBuilder gsonBulder = new GsonBuilder();
        //所有String类型null替换为字符串“”
        gsonBulder.registerTypeAdapter(String.class, new StringAdapter());
        //int类型对float做兼容
        gsonBulder.registerTypeAdapter(int.class, new IntAdapter());
        //通过反射获取instanceCreators属性
        try {
            Class builder = (Class) gsonBulder.getClass();
            Field f = builder.getDeclaredField("instanceCreators");
            f.setAccessible(true);
            //得到此属性的值
            Map<Type, InstanceCreator<?>> val = (Map<Type, InstanceCreator<?>>) f.get(gsonBulder);
            //注册数组的处理器
            gsonBulder.registerTypeAdapterFactory(new CollectionsAdapter(new ConstructorConstructor(val)));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return gsonBulder.create();

    }

}
