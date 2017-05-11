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
package cn.ieclipse.af.demo.common.api;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Description
 *
 * @author Jamling
 */

public class BaseInfoTest {
    @Test
    public void testMock() {
        
        // object
        MyBaseInfo info = BaseInfo.mockObject(MyBaseInfo.class);
        Assert.assertTrue(info.id.equals("1"));
        
        // BaseListInfo
        MyBaseListInfo listInfo = new MyBaseListInfo();
        listInfo.mock();
        Assert.assertTrue(listInfo.count >= 0);
        if (listInfo.count > 0) {
            Assert.assertNotNull(listInfo.list.get(0));
        }
    }
    
    @Test
    public void testEquals() {
        MyBaseInfo info1 = new MyBaseInfo();
        MyBaseInfo info2 = new MyBaseInfo();
        // null, null
        Assert.assertFalse(info1.equals(info2));
        info1.id = "1";
        // 1, null
        Assert.assertFalse(info1.equals(info2));
        // 1, 1
        info2.id = "1";
        Assert.assertTrue(info1.equals(info2));
    }
    
    public static class MyBaseListInfo extends BaseListInfo<String> {
        
    }
    
    public static class MyBaseInfo extends BaseInfo {
        public String id;
        
        @Override
        public void mock() {
            id = "1";
        }
        
        @Override
        public boolean equals(Object obj) {
            if (id != null && obj instanceof MyBaseInfo) {
                return id.equals(((MyBaseInfo) obj).id);
            }
            return super.equals(obj);
        }
    }
}
