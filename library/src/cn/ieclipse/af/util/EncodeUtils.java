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
package cn.ieclipse.af.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description
 *
 * @author Jamling
 */
public final class EncodeUtils {
    public static final char[] HEX_CHAR = "0123456789abcdef".toCharArray();
    public static final String UTF8 = "UTF-8";
    public final static String UNICODE_PREFFIX = "\\u";
    
    private EncodeUtils() {
    
    }
    
    public static String encodeXml(String src) {
        if (src == null) {
            return src;
        }
        return src.replace("&", "&amp;").replace("<", "&lt;").replace(">",
                "&gt;");
    }
    
    public static String decodeXml(String src) {
        if (src == null) {
            return src;
        }
        // &apos; -> '
        // &quot; -> "
        return src.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&");
    }
    
    public static String encodeUrl(String src, String charset) {
        if (src == null) {
            return src;
        }
        try {
            return URLEncoder.encode(src,
                    StringUtils.isEmpty(charset) ? UTF8 : charset);
        } catch (UnsupportedEncodingException e) {
            return src;
        }
    }
    
    public static String decodeUrl(String encoded, String charset) {
        if (encoded == null) {
            return null;
        }
        try {
            return URLDecoder.decode(encoded,
                    StringUtils.isEmpty(charset) ? UTF8 : charset);
        } catch (UnsupportedEncodingException e) {
            return encoded;
        }
    }
    
    public static Map<String, Object> encode(Object obj, boolean excludeNull) {
        Map<String, Object> map = new TreeMap<>();
        ReflectUtils.FieldFilter filter = (field) -> {
            return (field.getModifiers() & Modifier.STATIC) == 0;
        };
        List<Field> field = ReflectUtils.getClassField(obj.getClass(), filter);
        for (int i = 0; i < field.size(); i++) {
            Field f = field.get(i);
            String name = f.getName();
            Object val = ReflectUtils.get(f, obj);
            if (!excludeNull || val != null) {
                map.put(name, val);
            }
        }
        return map;
    }
    
    /**
     * Get request parameter value. List and Array will join by ','
     *
     * @param obj
     *            value object
     * @param charset
     *            encoding
     *
     * @return
     */
    public static String encodeRequestParam(Object obj, String charset) {
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
        else if (obj instanceof String[]) {
            value = StringUtils.join(",", (String[]) obj);
        }
        else {
            value = obj.toString();
        }
        return encodeUrl(value, charset);
    }
    
    /**
     * Get request body from object
     *
     * @param input
     *            map or object
     * @param charset
     *            encoding default is utf-8
     * @param excludeNull
     *            whether exclude null default
     *            
     * @return request body string
     */
    public static String encodeRequestBody(Object input, String charset,
            boolean excludeNull) {
        if (input == null) {
            return null;
        }
        else if (input instanceof Map) {
            return encodeRequestBody((Map) input, charset, excludeNull);
        }
        else {
            Map<String, Object> map = encode(input, excludeNull);
            return encodeRequestBody(map, charset, excludeNull);
        }
    }
    
    /**
     * @see #getRequestBody(Object, String, boolean)
     */
    public static String encodeRequestBody(Map map, String charset,
            boolean excludeNull) {
        StringBuilder sb = new StringBuilder();
        for (Object key : map.keySet()) {
            Object value = map.get(key);
            if (value == null && excludeNull) {
                continue;
            }
            sb.append(key);
            sb.append('=');
            sb.append(encodeRequestParam(value, charset));
            sb.append('&');
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
    
    public static String encodeUnicode(String str) {
        StringBuilder sb = new StringBuilder();
        char[] ch = str.toCharArray();
        for (char c : ch) {
            if (c == '\r' || c == '\n' || c == '\t') {
                sb.append(c);
            }
            else if (c < 0x20 || c > 0x7e) {
                sb.append(UNICODE_PREFFIX);
                sb.append(HEX_CHAR[(c >>> 12) & 0xF]);
                sb.append(HEX_CHAR[(c >>> 8) & 0xF]);
                sb.append(HEX_CHAR[(c >>> 4) & 0xF]);
                sb.append(HEX_CHAR[(c) & 0xF]);
            }
            else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
    
    public static String decodeUnicode(String unicode) {
        char[] ch = unicode.toCharArray();
        StringBuilder sb = new StringBuilder(ch.length);
        for (int i = 0; i < ch.length; i++) {
            int j = i + 1;
            if (ch[i] == '\\' && j < ch.length && ch[j] == 'u') {
                int code = 0;
                int i1 = 0;
                if (j + 1 < ch.length
                        && (i1 = getHexCharValue(ch[j + 1])) >= 0) {
                    code += i1 << 12;
                    int i2 = 0;
                    if (j + 2 < ch.length
                            && (i2 = getHexCharValue(ch[j + 2])) >= 0) {
                        code += i2 << 8;
                        int i3 = 0;
                        if (j + 3 < ch.length
                                && (i3 = getHexCharValue(ch[j + 3])) >= 0) {
                            code += i3 << 4;
                            int i4 = 0;
                            if (j + 4 < ch.length
                                    && (i4 = getHexCharValue(ch[j + 4])) >= 0) {
                                code += i4;
                                sb.append((char) code);
                                i += 5;
                            }
                            else {
                                sb.append(UNICODE_PREFFIX);
                                sb.append(ch[j + 1]);
                                sb.append(ch[j + 2]);
                                sb.append(ch[j + 3]);
                                i += 4;
                            }
                        }
                        else {
                            sb.append(UNICODE_PREFFIX);
                            sb.append(ch[j + 1]);
                            sb.append(ch[j + 2]);
                            i += 3;
                        }
                    }
                    else {
                        sb.append(UNICODE_PREFFIX);
                        sb.append(ch[j + 1]);
                        i += 2;
                    }
                }
                else {
                    sb.append(UNICODE_PREFFIX);
                    sb.append(ch[j + 1]);
                    i += 1;
                }
            }
            else {
                sb.append(ch[i]);
            }
        }
        return sb.toString();
    }
    
    private static int getHexCharValue(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        else if (c >= 'a' && c <= 'f') {
            return c - 87;
        }
        else if (c >= 'A' && c <= 'F') {
            return c - 55;
        }
        return -1;
    }
    
}
