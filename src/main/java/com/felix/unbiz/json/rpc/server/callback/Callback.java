package com.felix.unbiz.json.rpc.server.callback;

/**
 * ClassName: Callback <br/>
 * Function: 在服务端的调用链中扮演响应角色的回调
 *
 * @author wangxujin
 */
public interface Callback<T> {

    /**
     * 处理返回报文
     *
     * @param result
     */
    void handleResult(T result);

}