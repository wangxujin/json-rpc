package com.felix.unbiz.json.rpc.server.bo;

import com.felix.unbiz.json.rpc.server.JsonRpcExporter;

/**
 * ClassName: RpcRequest <br/>
 * Function: JsonRpc请求逻辑对象
 *
 * @author wangxujin
 */
public class RpcRequest {

    /**
     * 服务接口以及实现定义
     */
    private JsonRpcExporter exporter;

    /**
     * 请求字节码
     */
    private byte[] request;

    /**
     * Creates a new instance of RpcRequest.
     *
     * @param exporter
     * @param request
     */
    public RpcRequest(JsonRpcExporter exporter, byte[] request) {
        this.exporter = exporter;
        this.request = request;
    }

    public JsonRpcExporter getExporter() {
        return exporter;
    }

    public void setExporter(JsonRpcExporter exporter) {
        this.exporter = exporter;
    }

    public byte[] getRequest() {
        return request;
    }

    public void setRequest(byte[] request) {
        this.request = request;
    }

}