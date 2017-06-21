package com.yishuailuo.projects.myframework.helper;

import com.google.common.collect.Maps;
import com.yishuailuo.projects.myframework.util.ReflectionUtil;

import java.util.Map;
import java.util.Set;

/**
 * Created by luoyishuai on 17/6/11.
 */
public final class BeanHelper {

    private static final Map<Class<?>, Object> BEAN_MAP = Maps.newHashMap();

    static {
        Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
        for (Class<?> cls : beanClassSet) {
            Object obj = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls, obj);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<?> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("can not get bean by class:" + cls);
        }
        return (T) BEAN_MAP.get(cls);
    }

    public static void setBean(Class<?> cls, Object obj) {
        BEAN_MAP.put(cls, obj);
    }

}
