package com.felix.unbiz.json.rpc.client.ha.selector;

import com.felix.unbiz.json.rpc.client.ha.FailFastStrategy;
import com.felix.unbiz.json.rpc.client.ha.FailOverStrategy;
import com.felix.unbiz.json.rpc.client.ha.FailStrategy;
import com.felix.unbiz.json.rpc.client.ha.LoadBalanceStrategy;
import com.felix.unbiz.json.rpc.client.ha.RRLoadBalanceStrategy;
import com.felix.unbiz.json.rpc.client.ha.RandomLoadBalanceStrategy;

/**
 * ClassName: HAFactory <br>
 * Function:  HA工厂类
 *
 * @author wangxujin
 */
public class HAFactory {

    private HAFactory() {

    }

    /**
     * 构造负载均衡封装
     *
     * @param loadBalanceStrategy
     * @param failStrategy
     *
     * @return
     */
    public static LoadBalanceStrategy build(JsonLoadBalanceStrategy loadBalanceStrategy,
                                            JsonFailStrategy failStrategy) {
        switch (loadBalanceStrategy) {
            case RANDOM:
                return new RandomLoadBalanceStrategy(buildFailStrategy(failStrategy));
            case ROUNDROBIN:
                return new RRLoadBalanceStrategy(buildFailStrategy(failStrategy));
            default:
                return new RandomLoadBalanceStrategy(buildFailStrategy(failStrategy));
        }
    }

    /**
     * 构造容错处理策略
     *
     * @param failStrategy
     *
     * @return
     */
    private static FailStrategy buildFailStrategy(JsonFailStrategy failStrategy) {
        switch (failStrategy) {
            case FAILOVER:
                return new FailOverStrategy();
            case FAILFAST:
                return new FailFastStrategy();
            default:
                return new FailOverStrategy();
        }
    }

}
