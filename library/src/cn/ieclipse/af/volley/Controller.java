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

import android.util.Log;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.ieclipse.af.BuildConfig;
import cn.ieclipse.af.util.StringUtils;

/**
 * 类/接口描述
 *
 * @author Jamling
 * @date 2015年11月7日
 */
public class Controller<Listener> {
    protected RequestQueue mQueue;
    protected List<String> mTaskTags;
    protected Listener mListener;
    protected static final String TAG = "QuickAF";
    public static boolean DEBUG = Log.isLoggable(TAG, Log.VERBOSE) || BuildConfig.DEBUG;
    public static long CACHE_ADAY = 24 * 3600000;
    public static long CACHE_AMONTH = 30 * 24 * 3600000;
    
    public static void log(String msg) {
        Log.i(TAG, msg);
    }
    
    public static void log(String msg, Throwable t) {
        Log.w(TAG, msg, t);
    }
    
    public Controller() {
        if (VolleyManager.getInstance() == null) {
            throw new NullPointerException(
                    "did you forget initialize the VolleyManager?");
        }
        mTaskTags = new ArrayList<>();
        mQueue = VolleyManager.getInstance().getQueue();
    }
    
    public void setListener(Listener l) {
        this.mListener = l;
    }
    
    public Controller(Listener l) {
        this();
        setListener(l);
    }
    
    protected abstract class RequestObjectTask<Input, Output> implements
            Response.ErrorListener, Response.Listener<IBaseResponse> {
        protected Class<Output> mDataClazz;
        protected Class<?> mDataItemClass;
        protected Gson mGson = new Gson();
        
        protected Input input;
        private long cacheTime;
        protected GsonRequest request;
        
        /**
         * @param cacheTime
         */
        public void setCacheTime(long cacheTime) {
            this.cacheTime = cacheTime;
        }
        
        /**
         * Perform REST request and convert response 'data' json to an object.
         * 
         * @param input
         *            request object
         * @param clazz
         *            response 'data' object class
         * @param needCache
         *            need load from cache or not
         */
        public void load(Input input, Class<Output> clazz, boolean needCache) {
            assert mListener != null;
            this.input = input;
            this.mDataClazz = clazz;
            
            // get body
            String body = getBody(input);
            // get url
            IUrl url = buildUrl(body);
            if (Controller.DEBUG) {
                Controller.log("request url: " + url.getUrl());
            }
            // get request
            request = buildRequest(url, body);
            // set request
            request.setOutputClass(getBaseResponseClass());
            request.setShouldCache(needCache);
            request.setCacheTime(cacheTime);
            if (mTaskTags != null) {
                mTaskTags.add(getClass().getName());
            }
            request.setTag(getClass().getName());
            mQueue.add(request);
        }
        
        /**
         * Perform REST request and convert response 'data' json to
         * {@linkplain java.util.List java.util.List} object.
         * 
         * @param input
         *            request object
         * @param itemClass
         *            entity class of {@linkplain java.util.List java.util.List}
         * @param needCache
         *            need load from cache or not
         */
        public void load2List(Input input, Class<?> itemClass,
                boolean needCache) {
            this.mDataItemClass = itemClass;
            load(input, null, needCache);
        }
        
        /**
         * Build full URL, for HTTP GET, we append the request body to URL
         * typically. if your HTTP GET don't is a full URL, need to override and
         * return {@link #getUrl()}
         * 
         * @param body
         * @return full URL, maybe append query to origin {@link #getUrl()}
         */
        protected IUrl buildUrl(String body) {
            IUrl url = getUrl();
            if (url.getMethod() == Method.GET) {
                url.setQuery(body);
            }
            return url;
        }
        
        /**
         * Create volley request, default is {@link GsonRequest}
         * 
         * @param url
         *            full URL see {@link #buildUrl(String)}
         * @param body
         *            request body see {@link #getBody(Object)}
         * @return volley request instance
         */
        protected GsonRequest buildRequest(IUrl url, String body) {
            return new GsonRequest(url.getMethod(), url.getUrl(), body, this,
                    this);
        }
        
        /**
         * Get parameter encoding
         * 
         * @return request parameter encoding
         * @see com.android.volley.Request#getParamsEncoding
         */
        public String getParamsEncoding() {
            // fix null, request not created.
            // return request.getParamsEncoding();
            return "UTF-8";
        }
        
        /**
         * Get request body.
         * 
         * @param input
         *            request entity
         * @return encoded body string
         * @see #getParamsEncoding()
         */
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
                    sb.append(StringUtils.getRequestParamValue(map.get(key),
                            getParamsEncoding()));
                    sb.append('&');
                }
                if (sb.length() > 1) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                body = sb.toString();
            }
            else {
                body = StringUtils.getRequestParam(input, getParamsEncoding());
            }
            if (Controller.DEBUG) {
                Controller.log("request body: " + body);
            }
            return body;
        }
        
        @Override
        public final void onResponse(IBaseResponse response) {
            Output out = null;
            try {
                if (Controller.DEBUG) {
                    Controller.log("from cache : " + request.intermediate);
                }
                if (response == null) {
                    throw new NullPointerException(
                        "base response is null, please check your http response.");
                }
                out = convertData(response, mDataClazz, mDataItemClass);
            } catch (InterceptorError e) {
                if (Controller.DEBUG) {
                    Controller.log("interceptor the response", e);
                }
                return;
            } catch (VolleyError e) {
                onError(new RestError(e));
                return;
            } catch (RestError e) {
                onError(e);
                return;
            } catch (Exception e) {
                onError(new RestError(new ParseError(e)));
                return;
            }
            try {
                onSuccess(out, request.intermediate);
            } catch (Exception e) {
                onError((e instanceof RestError) ? (RestError) e
                        : new RestError(new ClientError(e)));
            }
        }
        
        @Override
        public void onErrorResponse(VolleyError error) {
            onError(new RestError(error));
        }
        
        /**
         * Intercept 'data' json parser
         * 
         * @param response
         *            the whole response object ({@link IBaseResponse} instance)
         * @return true if you want to skip convert 'data' json to object.
         * @throws Exception
         */
        protected boolean onInterceptor(IBaseResponse response)
                throws Exception {
            return false;
        }
        
        public Class<? extends IBaseResponse> getBaseResponseClass() {
            return VolleyManager.getConfig().getBaseResponseClass();
        }
        
        protected Output convertData(IBaseResponse response, Class<?> clazz,
                Class<?> itemClazz) throws Exception {
            Output out = null;
            if (!onInterceptor(response)) {
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
                String data = response.getData();
                if (listOutput) {
                    out = mGson.fromJson(data, type(List.class, itemClazz));
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