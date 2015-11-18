/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.af.demo;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.demo.cview.CustomViewFragment;
import cn.ieclipse.af.demo.recyclerview.RecyclerViewFragment;
import cn.ieclipse.af.demo.volley.VolleyFragment;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2015年11月18日
 *       
 */
public class LeftMenuConfig {
    
    static LeftMenuConfig instance;
    List<Item> menus;
    
    private LeftMenuConfig() {
        menus = new ArrayList<>();
        add("CustomViewDemo", CustomViewFragment.class);
        add("VolleyDemo", VolleyFragment.class);
        // add("FrescoDemo", Fres)
        add("RecyclerViewDemo", RecyclerViewFragment.class);
    }
    
    private void add(String name, Class<? extends BaseFragment> clazz) {
        Item item = new Item(name, clazz);
        menus.add(item);
    }
    
    public String getLabel(int i) {
        return menus.get(i).name;
    }
    
    public String[] getLabels() {
        String[] ret = new String[menus.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = getLabel(i);
        }
        return ret;
    }
    
    public BaseFragment getFragment(int i) {
        try {
            return (BaseFragment) (menus.get(i).clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static LeftMenuConfig getInstance() {
        if (instance == null) {
            instance = new LeftMenuConfig();
        }
        return instance;
    }
    
    private static class Item {
        String name;
        Class<? extends BaseFragment> clazz;
        
        Item(String name, Class<? extends BaseFragment> clazz) {
            this.name = name;
            this.clazz = clazz;
        }
        
        @Override
        public String toString() {
            return name;
        }
    }
}
