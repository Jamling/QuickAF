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
package cn.ieclipse.af.demo.common.api;

import android.os.Build;

import androidx.collection.ArrayMap;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Description
 *
 * @author Jamling
 */
public class BaseRequest implements java.io.Serializable {
    public String appKey;
    public String secret;
    public String version;
    public String uuid;

    private Map<String, Object> generateMap() {
        if (Build.VERSION.SDK_INT >= 19) {
            return new android.util.ArrayMap<>();
        }
        else {
            return new ArrayMap<>();
        }
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = generateMap();
        Field[] field = getClass().getFields();

        for (int i = 0; i < field.length && field.length > 0; ++i) {
            field[i].setAccessible(true);
            String name = field[i].getName();
            Object val;
            try {
                val = field[i].get(this);
                if (val != null) {
                    map.put(name, val);
                }
            } catch (Exception var8) {
                continue;
            }
        }
        return map;
    }
}
