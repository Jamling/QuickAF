/*
 * Copyright (C) 2015-2017 QuickAF
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

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * StringUtils
 *
 * @author Jamling
 */
public class StringUtils {

    /**
     * Get request parameter value.
     * List and Array will join by ','
     *
     * @param obj     value object
     * @param charset encoding
     *
     * @return
     */
    public static String getRequestParamValue(Object obj, String charset) {
        if (obj == null) {
            return "";
        }
        String value;
        if (obj instanceof List) {
            value = StringUtils.join(",", (List<?>) obj);
        }
        else if (obj instanceof Object[]) {
            value = StringUtils.join(",", (Object[]) obj);
        }
        else {
            value = obj.toString();
        }
        try {
            return URLEncoder.encode(value, charset);
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /**
     * Get request body from object
     *
     * @param input       map or object
     * @param charset     encoding default is utf-8
     * @param excludeNull whether exclude null default
     *
     * @return request body string
     */
    public static String getRequestBody(Object input, String charset, boolean excludeNull) {
        String encode = TextUtils.isEmpty(charset) ? "UTF-8" : charset;
        if (input == null) {
            return null;
        }
        else if (input instanceof Map) {
            return StringUtils.getMapBody((Map) input, encode, excludeNull);
        }
        // TODO fix class extends same fields issue
        StringBuilder sb = new StringBuilder();
        // 获取此类所有声明的字段
        Field[] field = input.getClass().getFields();
        for (int i = 0; i < field.length && field.length > 0; i++) {
            // 值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查.
            field[i].setAccessible(true);
            // field[i].getName() 获取此字段的名称
            // field[i].get(object) 获取指定对象上此字段的值
            String name = field[i].getName();
            Object val;
            try {
                val = field[i].get(input);
            } catch (Exception e) {
                continue;
            }
            if (val == null && excludeNull) {
                continue;
            }
            sb.append(name);
            sb.append('=');
            sb.append(StringUtils.getRequestParamValue(val, encode));
            sb.append('&');
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * @see #getRequestBody(Object, String, boolean)
     */
    public static String getMapBody(Map map, String charset, boolean excludeNull) {
        StringBuilder sb = new StringBuilder();
        String encode = TextUtils.isEmpty(charset) ? "UTF-8" : charset;
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            if (value == null && excludeNull) {
                continue;
            }
            sb.append(key);
            sb.append('=');
            sb.append(StringUtils.getRequestParamValue(value, encode));
            sb.append('&');
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    @Deprecated
    public static String getRequestParam(Object object, String encoding) {
        return StringUtils.getRequestBody(object, encoding, true);
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][3456789]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * Get scaled number
     *
     * @param <T>      number
     * @param value    number valued before scaled
     * @param fraction fraction
     *
     * @return scaled number
     */
    public static <T extends Number> Number scale(T value, int fraction) {
        return scale(value, fraction, RoundingMode.DOWN);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static <T extends Number> Number scale(T value, int fraction, RoundingMode mode) {
        Number ret;
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(fraction);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            nf.setRoundingMode(mode);
        }
        try {
            ret = nf.parse(nf.format(value));
        } catch (ParseException e) {
            ret = value;
        }
        return ret;
    }

    public static String join(String separator, String... array) {
        if (array == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(TextUtils.isEmpty(array[i]) ? "" : array[i]);
        }
        return sb.toString();
    }

    public static String join(String separator, Object... array) {
        if (array == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            sb.append(array[i] == null ? "" : array[i]);
        }
        return sb.toString();
    }

    public static String join(String separator, List<?> list) {
        if (list == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (i > 0) {
                sb.append(separator);
            }
            Object o = list.get(i);
            sb.append(o == null ? "" : o.toString());
        }
        return sb.toString();
    }
}
