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
package cn.ieclipse.af.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public final class SharedPrefsUtils {
    private SharedPrefsUtils() {
    
    }
    
    private static String FILE_NAME = null;
    private static SharedPreferences sharedPreferences;
    
    private static void init(Context context, String name) {
        if (!TextUtils.isEmpty(name)) {
            FILE_NAME = name;
        }
        sharedPreferences = getSharedPreferences();
    }
    
    private static SharedPreferences getDefaultSharedPreferences(Context context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        }
        return sharedPreferences;
    }
    
    private static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null) {
            // TODO init shared preferences again;
            // throw new NullPointerException();
        }
        return sharedPreferences;
    }
    
    public static void putInt(String key, int value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
    }
    
    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(key, 0);
    }
    
    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }
    
    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }
    
    public static long getLong(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getLong(key, 0);
    }
    
    public static void putLong(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
    }
    
    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }
    
    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, defValue);
    }
    
    public static void remove(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.commit();
    }
    
}
