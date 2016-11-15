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

import android.content.Context;
import android.text.TextUtils;

import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.my.UserInfo;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.SharedPrefsUtils;
import cn.ieclipse.af.volley.IBaseResponse;

/**
 * Description
 *
 * @author Jamling
 */
public class AppConfig {
    public static final int VOLLEY_TIMEOUT = 10000; // 10s
    public static final Class<? extends IBaseResponse> VOLLEY_RESPONSE_CLASS = BaseResponse.class;
    public static Context context;
    public static String token;
    public static UserInfo user;

    public static void init(Context context) {
        AppConfig.context = context;
        SharedPrefsUtils.init(context, null);
        token = SharedPrefsUtils.getString("token");
        user = (UserInfo) SharedPrefsUtils.getObject("user");
    }

    public static boolean isDebug(){
        return true;
    }
    public static String getToken() {
        return token;
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(token);
    }

    public static void setToken(String token) {
        AppConfig.token = token;
    }

    public static UserInfo getUser() {
        return user;
    }

    public static void setUser(UserInfo user) {
        AppConfig.user = user;
    }

    public static void login(Context context, String token, UserInfo user){
        SharedPrefsUtils.putString("token", token);
        AppConfig.setToken(token);
        SharedPrefsUtils.putObject("user", user);
        AppConfig.setUser(user);
    }

    public static void logout(){
        AppConfig.setToken(null);
        SharedPrefsUtils.remove("token");
    }

    public static String getUid() {
        if (getUser() != null) {
            return getUser().userid;
        }
        return "";
    }

    public static boolean showGuide(Context context) {
        String ver1 = AppUtils.getAppVersion(context);
        String ver2 = SharedPrefsUtils.getString("app_version");
        if (!ver1.equals(ver2)) {
            SharedPrefsUtils.putString("app_version", ver1);
            return true;
        }
        return false;
    }
}
