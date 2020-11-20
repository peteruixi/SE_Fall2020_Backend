package com.healthMonitor.fall2020.orm;


import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 14-8-17
 * Time: 下午8:26
 * To change this template use File | Settings | File Templates.
 */
public class BaseReflection {

    /**
     * 返回类的所有属性名称，包括父类的
     *
     * @param genericClass
     * @param <T>
     * @return
     */
    public static <T> Set<String> getFieldNames(Class<T> genericClass) {
        Set<String> fieldNames = Sets.newLinkedHashSet();
        Set<Field> fields = getFields(genericClass);
        for (Field field : fields) {
            String name = field.getName();
            if ("serialVersionUID".equals(name)) continue;
            fieldNames.add(name);
        }
        return fieldNames;
    }

    /**
     * 返回类所有属性字段
     *
     * @param genericClass
     * @param <T>
     * @return
     */
    public static <T> Set<Field> getFields(Class<T> genericClass) {
        return new BaseReflection().__GetFields__(genericClass);
    }

    /**
     * 返回类的所有属性
     *
     * @param genericClass
     * @param <T>
     * @return
     */
    private <T> Set<Field> __GetFields__(Class<T> genericClass) {
        Set<Field> allGenericFields = Sets.newLinkedHashSet();
        if (null == genericClass) return allGenericFields;
        return __GetFields__(allGenericFields, genericClass);
    }

    /**
     * 递归调用
     *
     * @param genericClass
     * @param <T>
     * @return
     */
    private <T> Set<Field> __GetFields__(Set<Field> allGenericFields, Class<T> genericClass) {
        Object parent = genericClass.getGenericSuperclass();
        if (null != parent) __GetFields__(allGenericFields, (Class) parent);
        Field[] fields = genericClass.getDeclaredFields();
        if (null != fields) {
            for (Field field : fields) allGenericFields.add(field);
        }
        return allGenericFields;
    }
}
