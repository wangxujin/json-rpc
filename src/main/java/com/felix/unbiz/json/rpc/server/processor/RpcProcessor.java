package com.felix.unbiz.json.rpc.server.processor;

import com.felix.unbiz.json.rpc.server.bo.RpcRequest;
import com.felix.unbiz.json.rpc.server.bo.RpcResponse;
import com.felix.unbiz.json.rpc.server.callback.Callback;

/**
 * ClassName: RpcProcessor <br>
 * Function: rpc处理接口
 *
 * @author wangxujin
 */
public interface RpcProcessor {

    /**
     * 处理请求并且响应结果
     *
     * @param request  请求
     * @param callback 结果Callback
     */
    void process(RpcRequest request, Callback<RpcResponse> callback);

}
