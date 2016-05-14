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
package cn.ieclipse.af.demo.common.api;

import android.text.TextUtils;

import com.android.volley.Request.Method;

import cn.ieclipse.af.demo.AppConfig;
import cn.ieclipse.af.volley.IUrl;

/**
 * API URL constants
 *
 * @author Jamling
 */
public final class URLConst {

    private URLConst() {
    }

    public static final String BASE_DEBUG = "http://";

    public static final String BASE_RELEASE = "http://";

    public static final String BASE = AppConfig.isDebug() ? BASE_DEBUG : BASE_RELEASE;

    /**
     * 用户相关接口定义
     */
    public interface User {

        // 登录获取验证码
        Url LOGIN_CODE = new Url("sendCode").get();

        //登录
        Url LOGIN_WITH_CODE = new Url("login").post();

        // 登录(使用密码)
        Url LOGIN_WITH_PWD = new Url("login").post();

        Url REG = new Url("addStaff").post();

        Url REG_CODE = new Url("sendCode").get();

        Url INFO = new Url("userInfo").post();

        //退出
        Url LOGOUT = new Url("logout").post();

        Url FORGOT_PWD = new Url("forget").post();

        Url CHANGE_PWD = new Url("changepwd").post();
    }

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
    
    public static class AbsoluteUrl extends Url {

        public AbsoluteUrl(String url) {
            super(url);
        }
        
        @Override
        public String getUrl() {
            return url + getQuery();
        }
    }
}
