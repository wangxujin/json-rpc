package com.felix.unbiz.json.rpc.server.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: JsonRpcService <br>
 * Function: JsonRpcService注解
 *
 * @author wangxujin
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonRpcService {

    /**
     * 实现的接口
     *
     * @return
     */
    Class<?> serviceInterface();
}
