/*
 * Copyright 2014-2018 ieclipse.cn.
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cn.ieclipse.af.common.Logger;

/**
 * Encrypt / Decrypt Utils
 * 
 * @author Jamling
 *         
 */
public abstract class EncryptUtils {
    public static final char[] HEX_CHAR = "0123456789abcdef".toCharArray();
    private static final Logger logger = Logger
            .getLogger(EncryptUtils.class);
            
    private EncryptUtils() {
    
    }
    
    public static MessageDigest get(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            logger.e("No such algorithm " + algorithm);
            return null;
        }
    }
    
    public static String encryptMd5(byte[] bytes) {
        MessageDigest mdTemp = get("MD5");
        mdTemp.update(bytes);
        byte[] md = mdTemp.digest();
        int j = md.length;
        char str[] = new char[j * 2];
        int k = 0;
        for (int i = 0; i < j; i++) {
            byte byte0 = md[i];
            str[k++] = HEX_CHAR[byte0 >>> 4 & 0xf];
            str[k++] = HEX_CHAR[byte0 & 0xf];
        }
        return new String(str);
    }
    
    public static String encryptMd5(String source) {
        if (source == null) {
            return source;
        }
        return EncryptUtils.encryptMd5(source.getBytes());
    }
    
}
