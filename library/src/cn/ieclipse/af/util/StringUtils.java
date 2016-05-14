package cn.ieclipse.af.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import android.text.TextUtils;

public class StringUtils {
    
    public static String getRequestParamValue(Object obj, String charset) {
        if (obj == null) {
            return "";
        }
        String value;
        
        if (obj instanceof List) {
            StringBuilder sb = new StringBuilder();
            if (obj != null) {
                for (Object o : (List<?>) obj) {
                    if (o != null) {
                        sb.append(o.toString());
                        sb.append(',');
                    }
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            value = sb.toString();
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

    // TODO fix class extends same fields issue
    public static String getRequestParam(Object object, String encoding) {
        StringBuilder sb = new StringBuilder();
        
        if (null == object) {
        }
        else {
            // 获取此类所有声明的字段
            Field[] field = object.getClass().getFields();
            // 用来拼接所需保存的字符串
            
            // 循环此字段数组，获取属性的值
            for (int i = 0; i < field.length && field.length > 0; i++) {
                // 值为 true 则指示反射的对象在使用时应该取消 Java 语言访问检查.
                field[i].setAccessible(true);
                // field[i].getName() 获取此字段的名称
                // field[i].get(object) 获取指定对象上此字段的值
                String name = field[i].getName();
                Object val;
                try {
                    val = field[i].get(object);
                } catch (Exception e) {
                    continue;
                }
                if (val != null) {
                    if (sb.length() > 0) {
                        sb.append("&" + name + "=" + getRequestParamValue(val, encoding));
                    }
                    else {
                        sb.append(name + "=" + getRequestParamValue(val, encoding));
                    }
                    
                }
            }
            return sb.toString();
        }
        
        return sb.toString();
        
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
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
    /**
     * Get scaled number
     * 
     * @param <T>
     *            number
     * @param value
     *            number valued before scaled
     * @param fraction
     *            fraction
     * @return scaled number
     */
    public static <T extends Number> Number scale(T value, int fraction) {
        return scale(value, fraction, RoundingMode.DOWN);
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static <T extends Number> Number scale(T value, int fraction,
            RoundingMode mode) {
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
}
