package com.felix.unbiz.json.rpc.client.ha;

import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.felix.unbiz.json.rpc.client.RpcClient;
import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.exception.rpc.HARpcException;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;
import com.felix.unbiz.json.rpc.util.Preconditions;

/**
 * ClassName: RRLoadBalanceStrategy <br/>
 * Function: Round Robin轮训负载均衡策略调用
 *
 * @author wangxujin
 */
public class RRLoadBalanceStrategy implements LoadBalanceStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(RRLoadBalanceStrategy.class);

    /**
     * 线程安全计数器
     */
    private static AtomicInteger counter = new AtomicInteger();

    /**
     * 失败策略
     */
    private FailStrategy failStrategy;

    /**
     * Creates a new instance of RRLoadBalanceStrategy.
     */
    public RRLoadBalanceStrategy() {

    }

    /**
     * Creates a new instance of RRLoadBalanceStrategy.
     */
    public RRLoadBalanceStrategy(FailStrategy failStrategy) {
        this.failStrategy = failStrategy;
    }

    @Override
    public Object transport(List<RpcClient> clientList, Method method, Object[] args)
            throws RpcException {
        return transport(clientList, method, args, null);
    }

    @Override
    public Object transport(List<RpcClient> clientList, Method method, Object[] args,
                            HeaderInfo headerInfo) throws RpcException {
        Preconditions.checkState(failStrategy != null, "fail strategy is not configured");
        int start = counter.incrementAndGet() % clientList.size();
        int clientSize = clientList.size();
        for (int currRetry = 0; currRetry < failStrategy.getMaxRetryTimes()
                && currRetry < clientSize; ) {
            RpcClient client = clientList.get(start);
            start++;
            start %= clientSize;
            try {
                LOG.debug("Call on " + client.getInfo() + " starts...");
                return client.transport(method, args, headerInfo);
            } catch (RpcException e) {
                LOG.error("Call on " + client.getInfo() + " failed due to " + e.getMessage(), e);
                if (failStrategy.isQuitImmediately(currRetry, clientSize)) {
                    throw e;
                }
                LOG.info("Fail over to next if available...");
                continue;
            } finally {
                currRetry++;
            }
        }
        throw new HARpcException("Failed to transport on all clients");
    }

}
