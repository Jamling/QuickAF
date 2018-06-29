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

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import cn.ieclipse.af.util.EncodeUtils;

public class GsonRequest extends JsonRequest<IBaseResponse> {
    protected Type mClazz;
    protected Gson mGson = new Gson();
    protected boolean intermediate;
    protected long ttl = 365 * 24 * 60 * 60 * 1000;
    protected Map<String, String> mHeaders;
    protected Response<IBaseResponse> response;
    
    public GsonRequest(int method, String url, String body, Listener<IBaseResponse> responseListener,
                       ErrorListener listener) {
        super(method, url, body, responseListener, listener);
    }
    
    @Override
    protected void deliverResponse(IBaseResponse response) {
        if (this.response != null) {
            this.intermediate = this.response.intermediate;
            this.response = null;
        }
        super.deliverResponse(response);
    }
    
    @Override
    protected Response<IBaseResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            IBaseResponse jsonInfoObject = getData(json.trim(), response);
            Cache.Entry cache = parseCacheHeaders(response, ttl);
            Response<IBaseResponse> ret = Response.success(jsonInfoObject, cache);
            this.response = ret;
            return ret;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if (Controller.DEBUG) {
            int code = -1;
            if (volleyError.networkResponse != null) {
                code = volleyError.networkResponse.statusCode;
            }
            Controller.log(String.format("response error HTTP %d", code), volleyError);
        }
        return super.parseNetworkError(volleyError);
    }

    public void setOutputClass(Type clazz) {
        this.mClazz = clazz;
    }
    
    public void setCacheTime(long cacheTime) {
        if (cacheTime > 0) {
            this.ttl = cacheTime;
        }
    }

    public void setGson(Gson gson){
        this.mGson = gson;
    }

    public Gson getGson(){
        return mGson;
    }
    
    protected IBaseResponse getData(String json, NetworkResponse response) {
        if (Controller.DEBUG) {
            Controller.log("response json:" + EncodeUtils.decodeUnicode(json));
        }
        return (IBaseResponse) mGson.fromJson(json, mClazz);
    }
    
    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=" + getParamsEncoding();
    }
    
    @Override
    public String getParamsEncoding() {
        return super.getParamsEncoding();
    }

    @Override
    public Request<?> setRetryPolicy(RetryPolicy retryPolicy) {
        RetryPolicy retry = VolleyManager.getConfig().getRetryPolicy();
        if (retry != null) {
            retryPolicy = retry;
        }
        return super.setRetryPolicy(retryPolicy);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mHeaders == null) {
            mHeaders = super.getHeaders();
        }
        return mHeaders;
    }
    
    public void setHeaders(Map<String, String> headers) {
        if (headers != null) {
            this.mHeaders = headers;
        }
    }
    
    public void addHeader(String key, String value) {

        try {
            if (getHeaders().isEmpty()) {
                setHeaders(new HashMap<String, String>());
            }
            getHeaders().put(key, value);
        } catch (AuthFailureError e) {
            // e.printStackTrace();
        }
    }
    
    /**
     * Extracts a {@link Cache.Entry} from a {@link NetworkResponse}.
     *
     * @param response The network response to parse headers from
     * @param ttl      the cache expired time
     *
     * @return a cache entry for the given response, or null if the response is
     * not cacheable.
     */
    public static Cache.Entry parseCacheHeaders(NetworkResponse response, long ttl) {
        long now = System.currentTimeMillis();
        
        Map<String, String> headers = response.headers;
        
        long serverDate = 0;
        long serverExpires = 0;
        long softExpire = 0;
        long maxAge = 0;
        boolean hasCacheControl = false;
        
        String serverEtag = null;
        String headerValue;
        
        headerValue = headers.get("Date");
        if (headerValue != null) {
            serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
        }
        
        serverEtag = headers.get("ETag");
        softExpire = now + 1000;
        
        Cache.Entry entry = new Cache.Entry();
        entry.data = response.data;
        entry.etag = serverEtag;
        entry.softTtl = softExpire;
        entry.ttl = now + ttl;
        entry.serverDate = serverDate;
        entry.responseHeaders = headers;
        
        return entry;
    }
}
