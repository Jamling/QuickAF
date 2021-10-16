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
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public final class SharedPrefsUtils {
    private SharedPrefsUtils() {

    }

    private static String FILE_NAME = null;
    private static SharedPreferences sharedPreferences;

    public static void init(Context context, String name) {
        if (!TextUtils.isEmpty(name)) {
            FILE_NAME = name;
        }
        sharedPreferences = getSharedPreferences(context);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
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
        edit.apply();
    }

    public static int getInt(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getInt(key, 0);
    }

    public static void putString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.apply();
    }

    public static String getString(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }

    public static String getString(String key, String value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, value);
    }

    public static long getLong(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getLong(key, 0);
    }

    public static void putLong(String key, long value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putLong(key, value);
        edit.apply();
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.apply();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, defValue);
    }

    public static void remove(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        Editor edit = sharedPreferences.edit();
        edit.remove(key);
        edit.apply();
    }

    /**
     * save object to SharedPreferences and the object must implement Serializable
     *
     * @param saveKey
     * @param object
     * @return
     */
    public static boolean putObject(String saveKey, Object object) {
        if (object != null && object instanceof Serializable) {
            SharedPreferences sharedPreferences = getSharedPreferences();
            // 创建字节输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                // 创建对象输出流，并封装字节流
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                // 将对象写入字节流
                oos.writeObject(object);
                // 将字节流编码成base64的字符窜
                String base64String = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(saveKey, base64String);

                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public static Object getObject(String saveKey) {
        Object object = null;
        SharedPreferences sharedPreferences = getSharedPreferences();
        String base64String = sharedPreferences.getString(saveKey, "");
        // 读取字节
        byte[] base64 = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
        // 封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            // 再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                // 读取对象
                object = bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return object;
    }

    public static void clearAll() {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
