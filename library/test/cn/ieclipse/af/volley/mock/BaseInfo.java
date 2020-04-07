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
package cn.ieclipse.af.volley.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The is the base response info model of network. Override or extend {@link #mock()} to mock object.
 * All the response info must override {@link #equals(Object)} to indicate the same object.
 *
 * @author Jamling
 */

public class BaseInfo implements java.io.Serializable {

    public int id;
    public String name;
    public float weight;
    public int age;

    public void mock() {
        id = 1;
        name = "Jamling";
        weight = 2f;
    }

    @Override
    public String toString() {
        return String.format("id=%d,name=%s,weight=%s,age=%d", id, name, weight, age);
    }

    public static <T> T mockObject(Class<T> clazz) {
        try {
            T output = clazz.newInstance();
            try {
                Method m = clazz.getDeclaredMethod("mock", (Class<?>[]) null);
                m.invoke(output, (Object[]) null);
            } catch (Exception e) {
                //
            }
            return output;
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> mockList(int max, Class<T> clazz) {
        int size = RandomUtils.genInt(max);
        List<T> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            try {
                T t = clazz.newInstance();
                clazz.getDeclaredMethod("mock", (Class<?>[]) null).invoke(t);
                list.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
