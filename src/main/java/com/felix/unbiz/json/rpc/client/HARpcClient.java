package com.felix.unbiz.json.rpc.client;

import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.felix.unbiz.json.rpc.client.ha.LoadBalanceStrategy;
import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;
import com.felix.unbiz.json.rpc.util.CollectionUtils;

/**
 * ClassName: HARpcClient <br>
 * Function: 负载均衡和容错的json rpc 客户端
 *
 * @author wangxujin
 */
public class HARpcClient implements RpcClient {

    private static final Logger LOG = LoggerFactory.getLogger(HARpcClient.class);

    /**
     * 负载均衡策略
     */
    private LoadBalanceStrategy loadBalanceStrategy;

    /**
     * 客户端list
     */
    private List<RpcClient> clientList;

    /**
     * Creates a new instance of HARpcClient.
     */
    public HARpcClient(List<RpcClient> clientList, LoadBalanceStrategy loadBalanceStrategy) {
        this.clientList = clientList;
        this.loadBalanceStrategy = loadBalanceStrategy;
    }

    @Override
    public Object transport(Method method, Object[] args) throws RpcException {
        LOG.debug(getInfo());
        return loadBalanceStrategy.transport(clientList, method, args);
    }

    @Override
    public Object transport(Method method, Object[] args, HeaderInfo headerInfo) throws RpcException {
        LOG.debug(getInfo());
        return loadBalanceStrategy.transport(clientList, method, args, headerInfo);
    }

    @Override
    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("HA rpc clients=> ");
        if (CollectionUtils.isNotEmpty(clientList)) {
            for (RpcClient client : clientList) {
                sb.append(client.getInfo());
            }
        } else {
            sb.append("empty");
        }
        return sb.toString();
    }
}
