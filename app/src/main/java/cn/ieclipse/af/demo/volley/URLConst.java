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
package cn.ieclipse.af.demo.volley;

import com.android.volley.Request.Method;

import android.text.TextUtils;
import cn.ieclipse.af.volley.IUrl;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月16日
 *       
 */
public final class URLConst {
    private URLConst() {
    }
    
    public static final String BASE = "http://apis.baidu.com/apistore/weatherservice/";
    
    public static final IUrl CITY_LIST = new Url("citylist").get();
    
    public static final IUrl CITY_NAME = new Url("cityname").get();
    
    public static class Url implements IUrl {
        protected int method;
        protected String url;
        protected String query;
        
        public Url(String url) {
            this.url = url;
        }
        
        public Url get() {
            this.method = Method.GET;
            return this;
        }
        
        public Url post() {
            this.method = Method.POST;
            return this;
        }
        
        public Url put() {
            this.method = Method.PUT;
            return this;
        }
        
        public Url delete() {
            this.method = Method.DELETE;
            return this;
        }
        
        public String getUrl() {
            return BASE + url + getQuery();
        }
        
        public int getMethod() {
            return method;
        }
        
        protected String getQuery() {
            if (TextUtils.isEmpty(query)) {
                return "";
            }
            else if (url.indexOf("?") >= 0) {
                return "&" + query;
            }
            else {
                return "?" + query;
            }
        }
        
        public void setQuery(String query) {
            this.query = query;
        }
    }
}
