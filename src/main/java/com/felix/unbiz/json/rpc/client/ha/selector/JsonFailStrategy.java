package com.felix.unbiz.json.rpc.client.ha.selector;

/**
 * ClassName: JsonFailStrategy <br>
 * Function: 失败重试策略
 *
 * @author wangxujin
 */
public enum JsonFailStrategy {

    /**
     * 失败重试
     */
    FAILOVER(1),

    /**
     * 失败直接退出
     */
    FAILFAST(2);

    private int strategy;

    JsonFailStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getStrategy() {
        return strategy;
    }
}
