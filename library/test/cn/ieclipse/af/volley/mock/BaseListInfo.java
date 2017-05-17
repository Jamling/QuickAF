package cn.ieclipse.af.volley.mock;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

import cn.ieclipse.af.util.RandomUtils;

/**
 * Description
 *
 * @author Jamling
 */

public abstract class BaseListInfo<T> extends BaseInfo {
    public int count;
    public List<T> list;

    @Override
    public void mock() {
        super.mock();
        count = RandomUtils.genInt(15);
        list = new ArrayList<>();
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Class<T> clazz = (Class<T>) type.getActualTypeArguments()[0];
        for (int i = 0; i < count; i++) {
            T t = BaseInfo.mockObject(clazz);
            if (t != null) {
                list.add(t);
            }
        }
    }
}
