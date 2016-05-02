package com.felix.unbiz.json.rpc.util;

import java.lang.reflect.Method;

/**
 * ClassName: ProxyUtils <br>
 * Function: 代理工具类
 *
 * @author wangxujin
 */
public final class ProxyUtils {

    private ProxyUtils() {
    }

    public static boolean isHashCode(Method method) {
        return "hashCode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 0;
    }

    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType())
                && method.getParameterTypes().length == 1 && Object.class.equals(method.getParameterTypes()[0]);
    }

    public static boolean isToStringMethod(Method method) {
        try {
            return "toString".equals(method.getName()) && Class.forName("java.lang.String")
                    .equals(method.getReturnType()) && method.getParameterTypes().length == 0;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
