/*
 * Copyright 2014-2016 QuickAF
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

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * 将字符串进行DES加密解密
 *
 * @author wangjian
 * @date 2016/5/10 .
 */
public class DESUtils {
    /**
     * 加密KEY
     */
    private static final byte[] KEY = "7;9Ku7;:84VG*B78".getBytes();
    /**
     * 算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * IV
     */
    private static final byte[] IV = "sHjrydLq".getBytes();
    /**
     * TRANSFORMATION
     */
    private static final String TRANSFORMATION = "DES/CBC/PKCS5Padding";

    private int code = 0;

    public DESUtils() {
    }

    /**
     * 构造函数
     *
     * @param code 加密方式：0-“ISO-8859-1”编码，1-base64编码，其它-默认编码（utf-8）
     */
    public DESUtils(int code) {
        this.code = code;
    }

    /**
     * 将字符串进行DES加密
     *
     * @param source 未加密源字符串
     * @return 加密后字符串
     */
    public String encrypt(String source) {
        byte[] retByte = null;

        // Create SecretKey object
        DESKeySpec dks = null;
        try {
            dks = new DESKeySpec(KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Create IvParameterSpec object with initialization vector
            IvParameterSpec spec = new IvParameterSpec(IV);

            // Create Cipter object
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // Initialize Cipher object
            cipher.init(Cipher.ENCRYPT_MODE, securekey, spec);

            // Decrypting data
            retByte = cipher.doFinal(source.getBytes());

            String result = "";
            if (code == 0) {
                result = new String(retByte, "ISO-8859-1");
            }
            else if (code == 1) {
                result = Base64.encodeToString(retByte, Base64.DEFAULT);
            }
            else {
                result = new String(retByte);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 将DES加密的字符串解密
     *
     * @param encrypted 加密过的字符串
     * @return 未加密源字符串
     */
    public String decrypt(String encrypted) {
        byte[] retByte = null;

        // Create SecretKey object
        DESKeySpec dks = null;
        try {
            dks = new DESKeySpec(KEY);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Create IvParameterSpec object with initialization vector
            IvParameterSpec spec = new IvParameterSpec(IV);

            // Create Cipter object
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);

            // Initialize Cipher object
            cipher.init(Cipher.DECRYPT_MODE, securekey, spec);

            if (code == 0) {
                retByte = encrypted.getBytes("ISO-8859-1");
            }
            else if (code == 1) {
                retByte = Base64.decode(encrypted, Base64.DEFAULT);
            }
            else {
                retByte = encrypted.getBytes();
            }

            // Decrypting data
            retByte = cipher.doFinal(retByte);
            return new String(retByte, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
