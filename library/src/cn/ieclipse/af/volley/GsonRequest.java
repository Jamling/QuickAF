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

import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class GsonRequest extends JsonRequest<IBaseResponse> {
    protected ParameterizedType mTypes;
    protected Class<?> mClazz;
    protected final Gson mGson = new Gson();
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
        this.intermediate = this.response.intermediate;
        this.response = null;
        super.deliverResponse(response);
    }
    
    @Override
    protected Response<IBaseResponse> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            Response<IBaseResponse> ret = Response.success(getData(json.trim(), response), parseCacheHeaders(response,
                ttl));
            this.response = ret;
            return ret;
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }
    
    public void setOutputClass(Class<?> clazz) {
        this.mClazz = clazz;
    }
    
    public void setCacheTime(long cacheTime) {
        if (cacheTime > 0) {
            this.ttl = cacheTime;
        }
    }
    
    protected IBaseResponse getData(String json, NetworkResponse response) {
        return (IBaseResponse) new Gson().fromJson(json, mClazz);
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
