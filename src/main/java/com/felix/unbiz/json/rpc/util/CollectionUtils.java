package com.felix.unbiz.json.rpc.util;

import java.util.Collection;

/**
 * ClassName: CollectionUtils <br>
 * Function: 集合工具类
 *
 * @author wangxujin
 */
public final class CollectionUtils {

    private CollectionUtils() {
    }

    /**
     * 判断集合是否为空
     *
     * @param coll 集合对象
     *
     * @return
     */
    public static boolean isEmpty(Collection coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * 判断集合对象不为空
     *
     * @param coll 集合对象
     *
     * @return
     */
    public static boolean isNotEmpty(Collection coll) {
        return !isEmpty(coll);
    }
}
