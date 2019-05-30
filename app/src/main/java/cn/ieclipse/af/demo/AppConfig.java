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
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import cn.ieclipse.af.demo.common.api.BaseResponse;
import cn.ieclipse.af.demo.my.UserInfo;
import cn.ieclipse.af.util.AppUtils;
import cn.ieclipse.af.util.EncodeUtils;
import cn.ieclipse.af.util.EncryptUtils;
import cn.ieclipse.af.util.FileUtils;
import cn.ieclipse.af.util.SDUtils;
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
    public static String uuid;
    public static boolean debug;

    public static void init(Context context) {
        AppConfig.context = context;
        SharedPrefsUtils.init(context, null);
        token = SharedPrefsUtils.getString(AppConstants.Prefs.KEY_USER_TOKEN);
        user = (UserInfo) FileUtils.readObject(SDUtils.getInternal(context), AppConstants.Prefs.KEY_USER_INFO);
        uuid = SharedPrefsUtils.getString(AppConstants.Prefs.KEY_UUID);
        if (TextUtils.isEmpty(uuid)) {
            // need android.permission.READ_PHONE_STATE permission
            uuid = generateUUID(context);
            SharedPrefsUtils.putString(AppConstants.Prefs.KEY_UUID, uuid);
        }
    }

    public static boolean isDebug(){
        return debug || BuildConfig.DEBUG;
    }
    public static String getToken() {
        return token;
    }

    public static boolean isLogin() {
        return !TextUtils.isEmpty(token);
    }

    public static void setToken(String token) {
        AppConfig.token = token;
        if (token == null) {
            SharedPrefsUtils.remove(AppConstants.Prefs.KEY_USER_TOKEN);
        }
        else {
            SharedPrefsUtils.putString(AppConstants.Prefs.KEY_USER_TOKEN, token);
        }
    }

    public static UserInfo getUser() {
        return user;
    }

    public static void setUser(UserInfo user) {
        AppConfig.user = user;
        FileUtils.writeObject(SDUtils.getInternal(context), AppConstants.Prefs.KEY_USER_INFO, user);
    }

    public static void login(String token, UserInfo user) {
        AppConfig.setToken(token);
        AppConfig.setUser(user);
    }

    public static void logout(){
        AppConfig.setToken(null);
        AppConfig.setUser(null);
    }

    public static String getUid() {
        if (getUser() != null) {
            return getUser().userid;
        }
        return "";
    }

    public static boolean showGuide(Context context) {
        String ver1 = AppUtils.getAppVersion(context);
        String ver2 = SharedPrefsUtils.getString(AppConstants.Prefs.KEY_APP_VERSION);
        if (!ver1.equals(ver2)) {
            SharedPrefsUtils.putString(AppConstants.Prefs.KEY_APP_VERSION, ver1);
            return true;
        }
        return false;
    }

    public static String getUUID() {
        return uuid;
    }

    private static String generateUUID(Context context) {
        String uuid = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            uuid = tm.getDeviceId();
        } catch (SecurityException e) {

        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = android.os.Build.SERIAL;
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = String.valueOf(new java.util.Random().nextDouble());
        }
        return EncryptUtils.encryptMd5(uuid);
    }
}
