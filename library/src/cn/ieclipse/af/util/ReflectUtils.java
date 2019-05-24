package cn.ieclipse.af.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public final class ReflectUtils {
    private ReflectUtils() {}

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static List<Field> getClassField(Class<?> clazz) {
        return getClassField(clazz, true, null);
    }

    public static List<Field> getClassField(Class<?> clazz, FieldFilter filter) {
        return getClassField(clazz, true, filter);
    }

    public static List<Field> getClassField(
            Class<?> clazz, boolean searchSuper, FieldFilter filter) {
        return getClassField(clazz, true, true, filter);
    }

    public static List<Field> getClassField(
            Class<?> clazz, boolean declared, boolean searchSuper, FieldFilter filter) {
        List<Field> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        getClassField(clazz, list, names, declared, searchSuper, filter);
        return list;
    }

    private static void getClassField(
            Class<?> clazz,
            List<Field> list,
            List<String> names,
            boolean declared,
            boolean searchSuper,
            FieldFilter filter) {
        if (clazz != null && !clazz.equals(Object.class)) {
            Field[] fields = declared ? clazz.getDeclaredFields() : clazz.getFields();
            if (fields != null) {
                for (Field field : fields) {
                    if (!names.contains(field.getName())) {
                        if (filter == null || filter.accept(field)) {
                            list.add(field);
                            names.add(field.getName());
                        }
                    }
                }
            }
            if (searchSuper) {
                getClassField(clazz.getSuperclass(), list, names, declared, searchSuper, filter);
            }
        }
    }

    public static Object get(Field field, Object obj) {
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public interface FieldFilter {
        boolean accept(Field f);
    }

    public interface MethodFilter {
        boolean accept(Method m);
    }

    public static List<Method> getClassMethod(Class<?> clazz) {
        return getClassMethod(clazz, true, null);
    }

    public static List<Method> getClassMethod(Class<?> clazz, MethodFilter filter) {
        return getClassMethod(clazz, true, filter);
    }

    public static List<Method> getClassMethod(
            Class<?> clazz, boolean searchSuper, MethodFilter filter) {
        return getClassMethod(clazz, true, true, filter);
    }

    public static List<Method> getClassMethod(
            Class<?> clazz, boolean declared, boolean searchSuper, MethodFilter filter) {
        List<Method> list = new ArrayList<>();
        List<String> names = new ArrayList<>();
        getClassMethod(clazz, list, names, declared, searchSuper, filter);
        return list;
    }

    private static void getClassMethod(
            Class<?> clazz,
            List<Method> list,
            List<String> names,
            boolean declared,
            boolean searchSuper,
            MethodFilter filter) {
        if (clazz != null && !clazz.equals(Object.class)) {
            Method[] methods = declared ? clazz.getDeclaredMethods() : clazz.getMethods();
            if (methods != null) {
                for (Method method : methods) {
                    if (!names.contains(method.getName())) {
                        if (filter == null || filter.accept(method)) {
                            list.add(method);
                            names.add(method.getName());
                        }
                    }
                }
            }
            if (searchSuper) {
                getClassMethod(clazz.getSuperclass(), list, names, declared, searchSuper, filter);
            }
        }
    }

    public static Object get(Method m, Object obj, Object... args) {
        m.setAccessible(true);
        try {
            return m.invoke(obj, args);
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
