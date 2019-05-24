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
import java.text.DecimalFormat;
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

    public static boolean isEmpty(CharSequence text) {
        return TextUtils.isEmpty(text);
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

    public static String getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double pi = (Math.PI / 180);
        double lat1 = pi * latitude1;
        double lat2 = pi * latitude2;

        double lon1 = pi * longitude1;
        double lon2 = pi * longitude2;

        // 地球半径
        double R = 6371;

        // 两点间距离 km，如果想要米的话，结果*1000就可以了
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1))
            * R;
        DecimalFormat df1 = new DecimalFormat("0.0");// 格式化小数，不足的补0
        return df1.format(d * 1000);
    }
}
