package com.felix.unbiz.json.rpc.util;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * ClassName: ConcurrentCache <br>
 * Function: 线程安全、延迟加载的缓存实现
 *
 * @author wangxujin
 */
public class ConcurrentCache<K, V> implements Computable<K, V> {

    private final ConcurrentHashMap<K, Future<V>> concurrentMap;

    private ConcurrentCache() {
        concurrentMap = new ConcurrentHashMap<K, Future<V>>();
    }

    /**
     * 静态方法创建实例
     *
     * @param <K>
     * @param <V>
     *
     * @return
     */
    public static <K, V> Computable<K, V> createComputable() {
        return new ConcurrentCache<K, V>();
    }

    @Override
    public V get(K key, Callable<V> callable) {
        Future<V> future = concurrentMap.get(key);
        if (future == null) {
            FutureTask<V> futureTask = new FutureTask<V>(callable);
            future = concurrentMap.putIfAbsent(key, futureTask);
            if (future == null) {
                future = futureTask;
                futureTask.run();
            }
        }
        try {
            return future.get();
        } catch (InterruptedException e) {
            concurrentMap.remove(key);
        } catch (ExecutionException e) {
            concurrentMap.remove(key);
        }
        return null;
    }
}
