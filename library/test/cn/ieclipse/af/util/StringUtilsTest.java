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

import junit.framework.Assert;

import org.junit.Test;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Description
 *
 * @author Jamling
 */

public class StringUtilsTest {
    @Test
    public void testJoin() {
        String[] array1 = {"1", "2", "3"};
        Assert.assertEquals("1,2,3", StringUtils.join(",", array1));
        Assert.assertEquals("1,2,3", StringUtils.join(",", Arrays.asList(array1)));

        // with null
        String[] array2 = {"1", null, "3"};
        Assert.assertEquals("1,,3", StringUtils.join(",", array2));
        Assert.assertEquals("1,,3", StringUtils.join(",", Arrays.asList(array2)));

        // object
        Object[] array3 = {"1", 5, new MyInfo("3"), true};
        Assert.assertEquals("1-5-3-true", StringUtils.join("-", array3));
        Assert.assertEquals("1-5-3-true", StringUtils.join("-", Arrays.asList(array3)));
    }

    @Test
    public void testGetRequestBody() {
        // map
        Map<String, String> map = new TreeMap<>();
        map.put("id", "id");
        map.put("name", null);
        String body = StringUtils.getRequestBody(map, null, false);
        Assert.assertEquals("id=id&name=", body);
        body = StringUtils.getRequestBody(map, null, true);
        Assert.assertEquals("id=id", body);

        MyInfo info = new MyInfo("1");
        body = StringUtils.getRequestBody(info, null, false);
        Assert.assertEquals("id=1&name=", body);
        body = StringUtils.getRequestBody(info, null, true);
        Assert.assertEquals("id=1", body);
    }

    private static class MyInfo {
        public String id;
        public String name;

        public MyInfo(String id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return id;
        }
    }
}
