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
        mQueue = Volley.newRequestQueue(context, config.getHttpStack());
    }
    
    static VolleyManager getInstance() {
        return mInstance;
    }
    
    public static void init(Context context, Class<? extends IBaseResponse> baseResponseClass) {
        init(context, new VolleyConfig.Builder().setBaseResponseClass(baseResponseClass).build());
    }
    
    public static void init(Context context, VolleyConfig config) {
        mInstance = new VolleyManager(context, config);
    }
    
    public RequestQueue getQueue() {
        return mQueue;
    }
    
    public static VolleyConfig getConfig() {
        return getInstance().mConfig;
    }
}
