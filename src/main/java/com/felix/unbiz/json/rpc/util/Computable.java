package com.felix.unbiz.json.rpc.util;

import java.util.concurrent.Callable;

/**
 * ClassName: Computable <br>
 * Function: 计算接口
 *
 * @author wangxujin
 */
public interface Computable<K, V> {

    /**
     * 通过关键字key获取
     *
     * @param key
     * @param callable
     *
     * @return
     */
    V get(K key, Callable<V> callable);
}
