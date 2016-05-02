package com.felix.unbiz.json.rpc.util;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;

/**
 * ClassName: MethodUtils <br>
 * Function: 方法工具类
 *
 * @author wangxujin
 */
public final class MethodUtils {

    private static final Logger LOG = LoggerFactory.getLogger(MethodUtils.class);

    private static Computable<String, Method> cache = ConcurrentCache.createComputable();

    private static Computable<String, Method[]> methodCache = ConcurrentCache.createComputable();

    private MethodUtils() {
    }

    /**
     * 找到bean上对应的方法
     *
     * @param service    查找的接口
     * @param methodName 方法名
     *
     * @return
     */
    public static Method findMethod(final Class<?> service, final String methodName, final JsonArray args) {
        if (service == null || StringUtils.isEmpty(methodName)) {
            return null;
        }
        String key = toCacheKey(service, methodName);
        return cache.get(key, new Callable<Method>() {
            @Override
            public Method call() throws Exception {
                return findMethodCallable(service, methodName, args);
            }
        });
    }

    /**
     * 找到bean上对应的方法
     *
     * @param service
     * @param methodName
     * @param args
     *
     * @return
     */
    public static Method findMethodCallable(final Class<?> service, final String methodName, final JsonArray args) {
        Method invokeMethod = null;
        for (Method method : service.getMethods()) {
            if (!method.getName().equals(methodName)) {
                continue;
            }
            if (method.getParameterTypes().length != args.size()) {
                continue;
            }
            invokeMethod = method;
        }
        return invokeMethod;
    }

    /**
     * 方法cache key生成
     *
     * @param clazz
     * @param methodName
     *
     * @return
     */
    public static String toCacheKey(final Class<?> clazz, final String methodName) {
        final StringBuilder sb = new StringBuilder(clazz.getName()).append('#').append(methodName);
        LOG.debug("cacheKey: {} ", sb);
        return sb.toString();
    }

    public static Method[] getAllCacheMethod(final Class<?> serviceInterface) {
        if (serviceInterface == null) {
            return null;
        }
        LOG.debug("client method cacheKey: {} ", serviceInterface.getName());
        return methodCache.get(serviceInterface.getName(), new Callable<Method[]>() {
            @Override
            public Method[] call() throws Exception {
                return serviceInterface.getMethods();
            }
        });
    }

}
