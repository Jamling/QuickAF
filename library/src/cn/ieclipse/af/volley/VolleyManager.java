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
package cn.ieclipse.af.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.internal.ConstructorConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

import cn.ieclipse.af.volley.adapter.CollectionsAdapter;
import cn.ieclipse.af.volley.adapter.IntAdapter;
import cn.ieclipse.af.volley.adapter.StringAdapter;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月10日
 */
public final class VolleyManager {
    private RequestQueue mQueue;
    private Context mContext;
    private VolleyConfig mConfig;
    private Gson mGson;
    private static VolleyManager mInstance;
    
    private VolleyManager(Context context, VolleyConfig config) {
        if (config == null) {
            throw new NullPointerException("Null volley config, did you forget initialize the VolleyManager?");
        }
        if (config.getBaseResponseClass() == null) {
            throw new IllegalArgumentException("Base response class is null, please set from VolleyConfig.Builder ");
        }
        if (config.getBaseResponseClass().isInterface()) {
            throw new IllegalArgumentException("Base response class must be a concrete class");
        }
        mConfig = config;
        initJsonPaser();
        mQueue = Volley.newRequestQueue(context, config.getHttpStack());
    }
    
    public static VolleyManager getInstance() {
        return mInstance;
    }
    
    public static void init(Context context, Class<? extends IBaseResponse> baseResponseClass) {
        init(context, new VolleyConfig.Builder().setBaseResponseClass(baseResponseClass).build());
    }
    
    public static void init(Context context, VolleyConfig config) {
        mInstance = new VolleyManager(context, config);
    }

    private void initJsonPaser() {
        GsonBuilder gsonBulder = new GsonBuilder();
        //1.所有String类型null替换为字符串""
        gsonBulder.registerTypeAdapter(String.class, new StringAdapter());
        //1.int类型对float做兼容
        gsonBulder.registerTypeAdapter(int.class, new IntAdapter());
        //2.通过反射获取instanceCreators属性,集合类型为空时返回空集合
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

        mGson = gsonBulder.create();
    }

    public Gson getPaser(){
        return mGson;
    }

    public RequestQueue getQueue() {
        return mQueue;
    }
    
    public static VolleyConfig getConfig() {
        return getInstance().mConfig;
    }
}
