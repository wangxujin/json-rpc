package com.felix.unbiz.json.rpc.client;

import java.lang.reflect.Method;

import com.felix.unbiz.json.rpc.client.header.HeaderInfo;
import com.felix.unbiz.json.rpc.exception.rpc.RpcException;

/**
 * ClassName: RpcClient <br/>
 * Function: rpc客户端接口
 *
 * @author wangxujin
 */
public interface RpcClient {

    /**
     * 调用发起远程通信
     *
     * @param method
     * @param args
     *
     * @return
     *
     * @throws RpcException
     */
    Object transport(Method method, Object[] args) throws RpcException;

    /**
     * 调用发起远程通信
     *
     * @param method
     * @param args
     * @param headerInfo
     *
     * @return
     *
     * @throws RpcException
     */
    Object transport(Method method, Object[] args, HeaderInfo headerInfo) throws RpcException;

    /**
     * 获取客户端相关信息
     *
     * @return
     */
    String getInfo();

}
