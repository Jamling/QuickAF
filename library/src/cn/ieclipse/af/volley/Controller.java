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

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import android.content.Context;
import cn.ieclipse.af.util.StringUtil;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月7日
 *       
 */
public class Controller<Listener> {
    protected Context mContext;
    protected RequestQueue mQueue;
    private List<String> mTaskTags;
    protected Listener mListener;
    public static boolean DEBUG = VolleyLog.DEBUG;
    public static long CACHE_ADAY = 24 * 3600000;
    public static long CACHE_AMONTH = 30 * 24 * 3600000;
    
    public static void log(String msg) {
        VolleyLog.v(msg);
    }
    
    public static void log(String msg, Throwable t) {
        VolleyLog.e(t, msg);
    }
    
    public Controller(Context context) {
        this.mContext = context;
        mTaskTags = new ArrayList<>();
        mQueue = Volley.newRequestQueue(context);
    }
    
    public void setListener(Listener l) {
        this.mListener = l;
    }
    
    public abstract class RequestObjectTask<Input, Output>
            implements Response.ErrorListener, Response.Listener<IBaseResponse> {
        private Class<? extends IBaseResponse> mConcreteBaseResponseClass;
        private Class<Output> mDataClazz;
        private Class<?> mDataItemClass;
        private Gson mGson = new Gson();
        
        protected Input input;
        private long cacheTime;
        private GsonRequest request;
        
        /**
         * 
         *
         * @param cacheTime
         */
        public void setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
        }
        
        public void load(Input input, Class<Output> clazz, boolean needCache) {
            assert mListener != null;
            this.input = input;
            this.mDataClazz = clazz;
            
            String body = getBody(input);
            IUrl url = getUrl();
            if (url.getMethod() == Method.GET) {
                url.setQuery(body);
            }
            Controller.log("request url: " + url.getUrl());
            request = new GsonRequest(url.getMethod(), url.getUrl(), body, this, this);
            request.setOutputClass(mConcreteBaseResponseClass);
            request.setShouldCache(needCache);
            request.setCacheTime(cacheTime);
            if (mTaskTags != null) {
                mTaskTags.add(getClass().getName());
            }
            request.setTag(getClass().getName());
            customRequest(request);
            mQueue.add(request);
        }
        
        public void load2List(Input input, Class<?> itemClass, boolean needCache) {
            this.mDataItemClass = itemClass;
            load(input, null, needCache);
        }
        
        protected void customRequest(final GsonRequest request) {
        
        }
        
        protected String getBody(Input input) {
            // Gson gson = new Gson();
            // String json = gson.toJson(input);
            // json = input.toString();
            // return json;
            String body = null;
            if (input == null) {
                body = null;
            }
            else if (input instanceof Map) {
                Map map = (Map) input;
                StringBuilder sb = new StringBuilder();
                for (Object key : map.keySet()) {
                    sb.append(key);
                    sb.append('=');
                    sb.append(StringUtil.getRequestParamValue(map.get(key), request.getParamsEncoding()));
                    sb.append('&');
                }
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                body = sb.toString();
            }
            else {
                body = StringUtil.getRequestParam(input, request.getParamsEncoding());
            }
            Controller.log("request body: " + body);
            return body;
        }
        
        @Override
        public final void onResponse(IBaseResponse response) {
            Output out = null;
            try {
                out = convertData(response, mDataClazz, mDataItemClass);
                Controller.log("from cache : " + request.intermediate);
                onSuccess(out, request.intermediate);
            } catch (Exception e) {
                Controller.log("error onResponse", e);
                onError(new RestError(new ParseError(e)));
            }
        }
        
        public boolean onInterceptor(IBaseResponse response) {
            return false;
        }
        
        @Override
        public void onErrorResponse(VolleyError error) {
            onError(new RestError(error));
        }
        
        public Output convertData(IBaseResponse response, Class<?> clazz, Class<?> itemClazz) throws Exception {
            Output out = null;
            boolean baseOutput = false;
            boolean listOutput = false;
            if (clazz != null) {
                baseOutput = isIBaseResponse(clazz);
            }
            listOutput = clazz == null && itemClazz != null;
            if (baseOutput) {
                out = ((Output) response);
                return out;
            }
            
            if (!onInterceptor(response)) {
                String data = response.getData();
                if (listOutput) {
                    out = mGson.fromJson(data, type(clazz, itemClazz));
                    if (out == null) {
                        out = (Output) new ArrayList<>(0);
                    }
                }
                else {
                    out = mGson.fromJson(data, mDataClazz);
                }
            }
            return out;
        }
        
        public abstract IUrl getUrl();
        
        public abstract void onSuccess(Output out, boolean fromCache);
        
        public abstract void onError(RestError error);
    }
    
    public static ParameterizedType type(final Class raw, final Type... args) {
        return new ParameterizedType() {
            public Type getRawType() {
                return raw;
            }
            
            public Type[] getActualTypeArguments() {
                return args;
            }
            
            public Type getOwnerType() {
                return null;
            }
        };
    }
    
    public static List<Class<?>> getSuperType(Class<?> clazz) {
        List<Class<?>> set = new ArrayList<>();
        getSuperType(clazz, set);
        return set;
    }
    
    private static void getSuperType(Class<?> clazz, List<Class<?>> set) {
        set.add(clazz);
        if (clazz.getSuperclass() != null) {
            getSuperType(clazz.getSuperclass(), set);
        }
        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> c : interfaces) {
            getSuperType(c, set);
        }
    }
    
    private static boolean isIBaseResponse(Class<?> clazz) {
        List<Class<?>> list = getSuperType(clazz);
        return list.contains(IBaseResponse.class);
    }
    
    public void onDestroy() {
        mQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                if (mTaskTags != null && mTaskTags.contains(request.getTag())) {
                    return true;
                }
                return false;
            }
        });
    }
}