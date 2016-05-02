package com.felix.unbiz.json.rpc.client.ha.selector;

/**
 * ClassName: JsonLoadBalanceStrategy <br>
 * Function: 负载均衡策略
 *
 * @author wangxujin
 */
public enum JsonLoadBalanceStrategy {

    /**
     * 随机
     */
    RANDOM(1),

    /**
     * 轮训
     */
    ROUNDROBIN(2);

    private int strategy;

    JsonLoadBalanceStrategy(int strategy) {
        this.strategy = strategy;
    }

    public int getStrategy() {
        return strategy;
    }

}