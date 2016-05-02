package com.felix.unbiz.json.rpc.client.ha;

import java.lang.reflect.Method;
import java.util.List;

import com.felix.unbiz.json.rpc.client.RpcClient;
import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;

/**
 * ClassName: LoadBalanceStrategy <br/>
 * Function: 负载均衡器
 *
 * @author wangxujin
 */
public interface LoadBalanceStrategy {

    /**
     * 根据客户端的连接采用负载均衡策略调用发起远程通信
     *
     * @param clientList
     * @param method
     * @param args
     *
     * @return
     *
     * @throws Throwable
     */
    Object transport(List<RpcClient> clientList, Method method, Object[] args) throws RpcException;

    /**
     * 根据客户端的连接采用负载均衡策略调用发起远程通信
     *
     * @param clientList
     * @param method
     * @param args
     * @param headerInfo
     *
     * @return
     *
     * @throws Throwable
     */
    Object transport(List<RpcClient> clientList, Method method, Object[] args,
                     HeaderInfo headerInfo) throws RpcException;

}
