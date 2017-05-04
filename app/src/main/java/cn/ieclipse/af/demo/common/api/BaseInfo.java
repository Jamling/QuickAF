package cn.ieclipse.af.demo.common.api;

import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.util.RandomUtils;

/**
 * Description
 *
 * @author Jamling
 */

public class BaseInfo implements java.io.Serializable {
    
    public void mock() {
        
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
