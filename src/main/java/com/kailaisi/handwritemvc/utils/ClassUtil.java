package com.kailaisi.handwritemvc.utils;

import com.kailaisi.handwritemvc.ioc.util.IOCHelper;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 描述：
 * <p/>作者：wu
 * <br/>创建时间：2019/12/26 23:36
 */
public final class ClassUtil {
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public static Set<Class<?>> getClassSetBySuper(Class<?> superClass) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        Map<String, Object> ioc = IOCHelper.getIoc();
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            if (entry.getValue().getClass().isAssignableFrom(superClass) || entry.getValue().getClass() == superClass) {
                set.add(entry.getValue().getClass());
            }
        }
        return set;
    }

    public static Set<Class<?>> getClassSet(String pkg) {
        Set<Class<?>> set = new HashSet<Class<?>>();
        for (Map.Entry<String, Object> entry : IOCHelper.getIoc().entrySet()) {
            if (entry.getKey().startsWith(pkg)) {
                set.add(entry.getValue().getClass());
            }
        }
        return set;
    }
}
